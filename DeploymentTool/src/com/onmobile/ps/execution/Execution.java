package com.onmobile.ps.execution;
import java.sql.Connection;
import org.apache.log4j.Logger;
import com.onmobile.ps.deploy.Deployment;
import com.onmobile.ps.input.Loading;
import com.onmobile.ps.restart.Restart;
import com.onmobile.ps.scripts.Script;

public class Execution {
	static Logger log = Logger.getLogger(Execution.class);
	
	public static void main(String[] args) 
	{
		log.info("Deployment Tool Start..");
		Script objScript=new Script();
		Deployment objDeployment=new Deployment();
		Restart objRestart=new Restart();
		Loading objLoading=new Loading();
		Connection connection=null;
		boolean status=false;
		objLoading.readInputs(objScript, objDeployment, objRestart);
		connection=objScript.getConnection();
	    System.out.println(connection);
	    status=objScript.scriptExecution();
	    System.out.println(status);
	    if(status==true&&objDeployment.getIsWarReplace().equalsIgnoreCase("true")&&objDeployment.getIsTomcatRestart().equalsIgnoreCase("false"))
	    {
	    	objDeployment.executeFile();
	    }
	    else if(status==true&&objRestart.getIsWarReplace().equalsIgnoreCase("false")&&objRestart.getIsTomcatRestart().equalsIgnoreCase("true"))
	    {
	    	objRestart.executeFile();
	    }
	    else
	     {
	    	 if(status==true && objDeployment.getIsWarReplace().equalsIgnoreCase("false")&&objDeployment.getIsTomcatRestart().equalsIgnoreCase("false"))
	    	 {
	    	 log.info("SQL Query deployed successfully.\nWAR not replaced & Tomcat not restarted, \"WAR_REPLACE=False & TOMCAT_RESTART=False\" ");
	    	 }
	    	 else if(status==true&&objRestart.getIsWarReplace().equalsIgnoreCase("true")&&objRestart.getIsTomcatRestart().equalsIgnoreCase("true"))
	    	 {
	    	 log.info("In Input.Properties File \"WAR_REPLACE=True & TOMCAT_RESTART=True\" both parameters are TRUE!! This is a wrong configuaration. Any one of it should be FALSE!!");
	    	 }
	    	 else
	    	 {
	    	 log.debug("Exception occures during SQL Script execution!!\n Please check the REPORT.CSV file for details!!");	 
	    	 }
	     }
	}
}
