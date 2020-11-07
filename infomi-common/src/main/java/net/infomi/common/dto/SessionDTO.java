package net.infomi.common.dto;

import net.infomi.common.consts.SysConsts;

/**
 * 用户登录模块
 *
 * @author hongcq
 * @since 2020/07/18
 */
public class SessionDTO {
    /**
     * 验签串
     */
    private String accessToken;

    /**
     * 来源系统标记
     */
    private String appId;

    /**
     * 版本号
     */
    private String version;

    /**
     * 用户ID
     */
    private String uid;

    public SessionDTO(String uid, String accessToken, String appId, String version) {
        this.uid = uid;
        this.accessToken = accessToken;
        this.version = version;
        this.appId = appId;
    }

    public SessionDTO(String uid, String accessToken, String appId) {
        this.uid = uid;
        this.accessToken = accessToken;
        this.version = SysConsts.version;
        this.appId = appId;
    }

    public SessionDTO(String uid, String accessToken) {
        this.uid = uid;
        this.accessToken = accessToken;
        this.version = SysConsts.version;
        this.appId = "INNER-SYS";
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "SessionDTO{" +
                "accessToken='" + accessToken + '\'' +
                ", uid='" + uid + '\'' +
                ", appId='" + appId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
