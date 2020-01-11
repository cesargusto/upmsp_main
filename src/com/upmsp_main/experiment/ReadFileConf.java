package com.upmsp_main.experiment;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadFileConf {
	
	public static Properties getProp() throws IOException{
		Properties props = new Properties();
		//FileInputStream file = new FileInputStream("../Conf/conf.properties");
		FileInputStream file = new FileInputStream("Conf/conf.properties");
		props.load(file);
		return props;
	}

}
