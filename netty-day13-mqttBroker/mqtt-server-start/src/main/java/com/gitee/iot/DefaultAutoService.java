package com.gitee.iot;

import com.gitee.iot.bootstrap.BaseAuthService;
import org.springframework.stereotype.Service;

/**
 * <p>权限校验
 * @author jie
 */
@Service
public class DefaultAutoService implements BaseAuthService {

    @Override
    public boolean authorized(String username, String password) {
        return true;
    }
}
