package com.twofish.config.generator;

import com.twofish.gen.DataSourceManager ;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

/**
 * 监听器
 *
 * @author thc
 */
@WebListener
public class RequestListener implements ServletRequestListener {

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		DataSourceManager.closeConnection();
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
	}

}

