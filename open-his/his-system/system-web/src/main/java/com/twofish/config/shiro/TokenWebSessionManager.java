package com.twofish.config.shiro;

import com.twofish.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.UUID;

/**
 * 用于生成token
 * 如果head里有就返回
 * 没用就构建一个返回
 * @author ccy
 */
@Configuration
public class TokenWebSessionManager extends DefaultWebSessionManager {

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {

        //从头里面得到请求TOKEN 如果不存在就生成一个
        String token = WebUtils.toHttp(request).getHeader(Constants.TOKEN);
        if(StringUtils.isNotBlank(token)){
            return token;
        }
        String uuid = UUID.randomUUID().toString();
        return uuid;

    }


}
