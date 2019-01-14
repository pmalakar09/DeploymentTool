package com.onmobile.ps.input;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.onmobile.ps.deploy.Deployment;
import com.onmobile.ps.execution.Execution;
import com.onmobile.ps.restart.Restart;
import com.onmobile.ps.scripts.Script;

public class Loading {
	static Logger log = Logger.getLogger(Loading.class);
	public void readInputs(Script objScript, Deployment objDeploy, Restart objRestart)
	{
		 FileInputStream fileInput = null;
		 try{
			 File file = new File("Input.properties");
			 fileInput = new FileInputStream(file);
			 Properties properties = new Properties();
			 log.info("Input.properties is loading......");
			 properties.load(fileInput);
			 log.info("Input.properties loaded succesfully");
			 
			 objScript.setDbUrl(properties.getProperty("DBURL"));
			 objScript.setDbUser(properties.getProperty("DBUSER"));
			 objScript.setDbPassword(properties.getProperty("DBPASSWORD"));
			 
			 objDeploy.setHost(properties.getProperty("HOST"));
			 objDeploy.setHostUserName(properties.getProperty("HOST_USERNAME"));
			 objDeploy.setHostPassword(properties.getProperty("HOST_PASSWORD"));
			 objDeploy.setIsWarReplace(properties.getProperty("WAR_REPLACE"));
			 objDeploy.setIsTomcatRestart(properties.getProperty("TOMCAT_RESTART"));
			 objDeploy.setOldWarPath(properties.getProperty("OLD_WAR_PATH"));
			 objDeploy.setShFilePath(properties.getProperty("SH_FILEPATH"));
			 
			 objRestart.setHost(properties.getProperty("HOST"));
			 objRestart.setHostuserName(properties.getProperty("HOST_USERNAME"));
			 objRestart.setHostPassword(properties.getProperty("HOST_PASSWORD"));
			 objRestart.setIsWarReplace(properties.getProperty("WAR_REPLACE"));
			 objRestart.setIsTomcatRestart(properties.getProperty("TOMCAT_RESTART"));
			 objRestart.setShFilePath(properties.getProperty("SH_FILEPATH"));
		 }
		 catch(Exception e)
		 {
			 log.debug("ERROR during loading the Input.properties!!!! "+e);
		 }
		 finally{
			 
		 }
	}

}
