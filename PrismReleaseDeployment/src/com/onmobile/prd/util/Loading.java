package com.onmobile.prd.util;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.onmobile.prd.deploy.Deployment;
import com.onmobile.prd.restart.Restart;
import com.onmobile.prd.scripts.Script;


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
			 
			 objScript.setDbUrl(properties.getProperty("DBURL", "org.mariadb.jdbc.Driver"));
			 objScript.setDbUser(properties.getProperty("DBUSER"));
			 objScript.setDbPassword(properties.getProperty("DBPASSWORD"));
			 objScript.setDBDriver(properties.getProperty("DB_DRIVER"));
			 
			 objDeploy.setHost(properties.getProperty("HOST"));
			 objDeploy.setHostUserName(properties.getProperty("HOST_USERNAME"));
			 objDeploy.setHostPassword(properties.getProperty("HOST_PASSWORD"));
			 objDeploy.setIsWarReplace(properties.getProperty("WAR_REPLACE", "FALSE"));
			 objDeploy.set_Is_Prism_Daemon_Restart(properties.getProperty("PRISM_DAEMON_RESTART", "FALSE"));
			 objDeploy.setOldWarPath(properties.getProperty("OLD_WAR_PATH"));
			 objDeploy.setShFilePath(properties.getProperty("SH_FILEPATH"));
			 objDeploy.set_Is_Prism_Jar_Replace(properties.getProperty("PRISM_JAR_REPLACE", "FALSE"));
			 objDeploy.setOld_Prism_Jar_Path(properties.getProperty("OLD_PRISM_JAR_PATH", "/PRISM/PRISMD/PRISMD_RELEASE/lib"));
			 
			 objRestart.setHost(properties.getProperty("HOST"));
			 objRestart.setHostuserName(properties.getProperty("HOST_USERNAME"));
			 objRestart.setHostPassword(properties.getProperty("HOST_PASSWORD"));
			 objRestart.setIsWarReplace(properties.getProperty("WAR_REPLACE", "FALSE"));
			 objRestart.set_Is_Prism_Daemon_Restart(properties.getProperty("PRISM_DAEMON_RESTART", "FALSE"));
			 objRestart.setShFilePath(properties.getProperty("SH_FILEPATH"));
			 objRestart.set_Is_Prism_Jar_Replace(properties.getProperty("PRISM_JAR_REPLACE", "FALSE"));
		 }
		 catch(Exception e)
		 {
			 log.debug("ERROR during loading the Input.properties!!!! "+e);
		 }
		 finally{
			 
		 }
	}

}
