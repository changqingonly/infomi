package net.infomi.common.services;

import net.infomi.common.dto.ResultDTO;
import net.infomi.common.dto.SessionDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Passport服务类
 *
 * @author hongcq
 * @since 2020/07/18
 */
public class SessionService {

    private static Logger logger = LogManager.getLogger(SessionService.class);

    /**
     * 登录验证
     *
     * @param dto
     * @return
     */
    public ResultDTO checkLogin(SessionDTO dto) {
        logger.info("msg[do check login by session starter] info[{}]", dto);
        return ResultDTO.instance(dto);
    }

}