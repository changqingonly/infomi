package net.infomi.common.starters.filters;

import net.infomi.common.consts.SysConsts;
import net.infomi.common.starters.properties.TracerProperties;
import net.infomi.common.utils.IpUtil;
import net.infomi.common.utils.JsonUtil;
import net.infomi.common.utils.LogUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求统计过滤器
 * 1. 请求耗时
 * 2. url
 * 3. 请求体
 * 4. 响应体
 * 5. 客户端IP
 *
 * @author hongcq
 * @since 2020/06/29
 */
public class TracerFilter extends OncePerRequestFilter {

    private static Logger logger = LogManager.getLogger(TracerFilter.class);

    @Resource
    private TracerProperties properties;

    /** 日志格式 */
    private static String logFormat = "msg[REQUEST-PROCESS-STS] headerId[{}] ip[{}] uri[{}] method[{}] userAgent[{}] responseCode[{}] requestParams[{}] responseBody[{}] totalTime[{}]";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        LogUtil.setDefaultLogContext();
        long requestTime = System.currentTimeMillis();
        long costTime = 0L;
        String ip = IpUtil.getClientIp(request);
        String ua = request.getHeader(HttpHeaders.USER_AGENT);
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String lowerCaseUri = uri.toLowerCase();
        String headerId = request.getHeader(SysConsts.HTTP_HEADER_TRACE_ID);
        if (StringUtils.isEmpty(headerId)) {
            headerId = "-";
        }
        // 静态资源后缀(jpg/png/html/pdf/xml/properties) 跳过
        if (lowerCaseUri.endsWith(".jpg") || lowerCaseUri.endsWith(".png")
                || lowerCaseUri.endsWith(".html") || lowerCaseUri.endsWith(".pdf")
                || lowerCaseUri.endsWith(".xml") || lowerCaseUri.endsWith(".properties")) {
            filterChain.doFilter(request, response);
            costTime = System.currentTimeMillis() - requestTime;
            logger.info(logFormat, headerId, ip, uri, method, ua, '-', '-', '-', costTime);
            LogUtil.removeDefaultLogContext();
            return;
        }

        // 输出请求体
        String requestBody = "-";
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);

        if (properties.isEnabled() && properties.getTraceRequest()) {
            if (StringUtils.isNotEmpty(contentType)) {
                // xml/json
                if (contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)
                        || contentType.startsWith(MediaType.APPLICATION_XML_VALUE)) {
                    requestBody = getJsonOrXmlBody(request);
                    final ByteArrayInputStream inputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
                    request = new HttpServletRequestWrapper(request) {
                        @Override
                        public ServletInputStream getInputStream() throws IOException {
                            return new TongBaoByteArrayServletInputStream(inputStream);
                        }
                    };
                    // 普通表单提交
                } else if (contentType.startsWith(MediaType.APPLICATION_FORM_URLENCODED_VALUE)){
                    requestBody = JsonUtil.object2json(request.getParameterMap());

                    // 文件表单提交
                } else if (contentType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)){
                    requestBody = getMultipartParams(request);
                }
            } else if ("GET".equals(method)) {
                requestBody = request.getQueryString();
            } else if ("POST".equals(method)) {
                requestBody = JsonUtil.object2json(request.getParameterMap());
            }
        }

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        response = new HttpServletResponseWrapper(response) {
            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return new TongBaoServletOutputStream(super.getOutputStream(), outputStream);
            }
        };

        // --------------------------------------------------------
        // 执行下一个过滤器
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 异常情况下，确保打印入参信息
            costTime = System.currentTimeMillis() - requestTime;
            logger.error(logFormat, headerId, ip, uri, method, ua, '-', requestBody, "-", costTime);
            throw e;
        }
        // --------------------------------------------------------

        String responseBody = "-";
        // 暂定只有json/xml/plain输出响应体，其他的输出场景不做参数收集
        contentType = response.getHeader(HttpHeaders.CONTENT_TYPE);
        if (properties.isEnabled() && properties.getTraceResponse()) {
            if (StringUtils.isEmpty(contentType)
                    || contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)
                    || contentType.startsWith(MediaType.TEXT_PLAIN_VALUE)
                    || contentType.startsWith(MediaType.TEXT_HTML_VALUE)
                    || contentType.startsWith(MediaType.TEXT_XML_VALUE)) {
                responseBody = outputStream.toString();
            }
        }

        // 日志打印
        costTime = System.currentTimeMillis() - requestTime;
        if (response.getStatus() >= 200 && response.getStatus() < 300) {
            logger.info(logFormat, headerId, ip, uri, method, ua, response.getStatus(), requestBody, responseBody, costTime);
        } else {
            logger.warn(logFormat, headerId, ip, uri, method, ua, response.getStatus(), requestBody, responseBody, costTime);
        }
        LogUtil.removeDefaultLogContext();
    }

    /**
     * 请求头指定传输json或xml的情况下，获取入参列表
     *
     * @param request HttpServletRequest入参
     * @return String
     */
    private String getJsonOrXmlBody(HttpServletRequest request) {
        int contentLength = request.getContentLength();
        String body = "";
        if(contentLength <= 0) {
            return body;
        }
        try {
            body = IOUtils.toString(request.getReader());
        } catch (IOException e) {
            logger.error("msg[获取请求体失败]", e);
        }
        return body;
    }

    /**
     * 文件上传方式下，获取文件信息和参数信息
     *
     * @param request
     * @return String
     */
    private static String getMultipartParams(HttpServletRequest request) {
        MultipartResolver resolver = new StandardServletMultipartResolver();
        MultipartHttpServletRequest mRequest = resolver.resolveMultipart(request);

        final Map<String, Object> param = new HashMap<>();
        Map<String, String[]> parameterMap = mRequest.getParameterMap();
        if (!parameterMap.isEmpty()) {
            param.putAll(parameterMap);
        }
        Map<String, MultipartFile> fileMap = mRequest.getFileMap();
        if(!fileMap.isEmpty()) {
            for (Map.Entry<String, MultipartFile> fileEntry : fileMap.entrySet()) {
                MultipartFile file = fileEntry.getValue();
                param.put(fileEntry.getKey(), file.getOriginalFilename() + "(" + file.getSize() + " byte)");
            }
        }
        return JsonUtil.object2json(param);
    }

    static class TongBaoByteArrayServletInputStream extends ServletInputStream {

        private ByteArrayInputStream byteArrayInputStream;

        public TongBaoByteArrayServletInputStream(ByteArrayInputStream byteArrayInputStream) {
            this.byteArrayInputStream = byteArrayInputStream;
        }

        @Override
        public int read() throws IOException {
            return byteArrayInputStream.read();
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener listener) {}
    }

    static class TongBaoServletOutputStream extends ServletOutputStream {

        /**
         * 利用Apache commons-io包里的复制输出流来实现
         */
        private final TeeOutputStream targetStream;

        public TongBaoServletOutputStream(final OutputStream out, final OutputStream branch) {
            this.targetStream = new TeeOutputStream(out, branch);
        }

        @Override
        public void write(byte[] b) throws IOException {
            this.targetStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            this.targetStream.write(b, off, len);
        }

        @Override
        public void write(int b) throws IOException {
            this.targetStream.write(b);
        }

        @Override
        public void flush() throws IOException {
            super.flush();
            this.targetStream.flush();
        }

        @Override
        public void close() throws IOException {
            super.close();
            this.targetStream.close();
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {}
    }
}