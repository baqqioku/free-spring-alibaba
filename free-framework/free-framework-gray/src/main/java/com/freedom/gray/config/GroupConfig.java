package com.freedom.gray.config;

import com.free.common.util.TraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class GroupConfig {
	public static Logger logger = LoggerFactory.getLogger(GroupConfig.class);
	
	private static final String GROUP_NAME = "tag";


	private static Properties config;
	
	static{
		try {
			config = getPro(ConfigConstant.groupConfigPath);
		}catch (Exception e){
			logger.warn("不存在文件：{}", ConfigConstant.groupConfigPath);
			config = new Properties();
		}

	}
	
	/**
	 * 这个方法属于框架底层方法，业务层不要调用
	 */
	public static String getDefaultGroupName() {
		String value = getValue(TraceUtil.TAG);
        return value;
    }


	private static String getValue(String key) {

        String value = "";

        if (null != config) {
            value = config.getProperty(key, value);
        }

        return value;
    }

	public static Properties getPro(String resourcesPath){
		Properties pro = null;
		try {
			InputStream in = new FileInputStream(resourcesPath);
			pro = new Properties();
			pro.load(in);

			in.close();
		} catch (Exception e) {
			logger.error("读取配置异常" + resourcesPath);
		}

		return pro;
	}
}
