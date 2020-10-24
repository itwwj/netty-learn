package com.gitee.iot.bootstrap;

/**
 * <p>权限校验
 * @author jie
 */
public interface BaseAuthService {

    boolean  authorized(String username, String password);
}
