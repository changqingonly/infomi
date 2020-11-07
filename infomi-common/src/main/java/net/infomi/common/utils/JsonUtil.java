package net.infomi.common.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.infomi.common.services.SessionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Json编解码工具
 *
 * @Auther: hongcq
 * @Date: 2020/06/19/上午9:43
 * @Description:
 */
public class JsonUtil {

    private static Logger logger = LogManager.getLogger(SessionService.class);

    /**
     * 对象转JSON串
     *
     * @param object
     * @return String
     */
    public static String object2json(Object object) {
        String json = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error("msg[parse json error]", e);
        }
        return json;
    }

    /**
     * JSON串转对象
     *
     * @param clazz
     * @return String
     */
    public static <T> T json2object(String json, Class<T> clazz) {
        T retObj = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            retObj = mapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("msg[parse json error]", e);
        }
        return retObj;
    }

    /**
     * json数组转换
     *
     * @param json
     * @param collectionClass
     * @param elementClasses
     * @param <T>
     * @return T
     */
    public static  <T> T toCollectionBean(String json,Class<?> collectionClass, Class<?>... elementClasses) {
        T retObj = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
            retObj = mapper.readValue(json, javaType);
        } catch (Exception e) {
            logger.error("msg[parse json error]", e);
        }
        return retObj;
    }
}
