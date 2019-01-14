package com.onmobile.prd.deploy;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Deployment {
	static Logger log = Logger.getLogger(Deployment.class);
	private String host=null;
	private String hostUserName=null;
	private String hostPassword=null;
	private String sh_Filepath=null;
	private String is_War_Replace="False";
	private String is_Prism_Daemon_Restart="False";
	public String is_Prism_Jar_Replace="False";
	private String old_War_Path=null;
    private static final int port=22;
    private static Session session=null;
    private static ChannelSftp sftpChannel=null;
    private String old_prism_jar_path=null;
    
    public void setHost(String host)
    {
    	this.host=host;
    }
    public void setHostUserName(String hostUserName)
    {
    	this.hostUserName=hostUserName;
    }
    public void setHostPassword(String hostPassword)
    {
    	this.hostPassword=hostPassword;
    }
    public void setShFilePath(String sh_Filepath)
    {
    	this.sh_Filepath=sh_Filepath;
    }
    public void setIsWarReplace(String is_War_Replace)
    {
    	this.is_War_Replace=is_War_Replace;
    }
    public void set_Is_Prism_Jar_Replace(String is_Prism_Jar_Replace)
    {
    	this.is_Prism_Jar_Replace=is_Prism_Jar_Replace;
    }
    public void set_Is_Prism_Daemon_Restart(String is_Prism_Daemon_Restart)
    {
    	this.is_Prism_Daemon_Restart=is_Prism_Daemon_Restart;
    }
    public void setOldWarPath(String old_War_Path)
    {
    	this.old_War_Path=old_War_Path;
    }
    public void setOld_Prism_Jar_Path(String old_prism_jar_path)
    {
    	this.old_prism_jar_path=old_prism_jar_path;
    }
    public String getHost()
    {
    	return this.host;
    }
    public String getHostUserName()
    {
    	return this.hostUserName;
    }
    public String getHostPassword()
    {
    	return this.hostPassword;
    }
    public String getShFilePath()
    {
    	return this.sh_Filepath;
    }
    public String getIsWarReplace()
    {
    	return this.is_War_Replace;
    }
    public String get_Is_Prism_Daemon_Restart()
    {
    	return this.is_Prism_Daemon_Restart;
    }
    public String getOldWarPath()
    {
    	return this.old_War_Path;
    }
    public String getOld_Prism_Jar_Path()
    {
    	return this.old_prism_jar_path;
    }
    public String get_Is_Prism_Jar_Replace()
    {
    	return this.is_Prism_Jar_Replace;
    }


    
    public List executeWarFile(){
	     List result = new ArrayList();
	     
	     
	     log.info("WAR_REPLACE= "+is_War_Replace);
	     log.info("PRISM_JAR_REPLACE= "+is_Prism_Jar_Replace);
	     log.info("PRISM_DAEMON_RESTART= "+is_Prism_Daemon_Restart);
	     if (is_War_Replace.equalsIgnoreCase("true")){
	     try{
	    	 JSch jsch = new JSch();
	    	 System.out.println(hostUserName+hostPassword+host);
	         session = jsch.getSession(hostUserName, host, port);
	         log.info("Host= "+host +"\n"+"Host_UserName= "+hostUserName+"\n"+"Host_Password= "+hostPassword);
	         session.setConfig("StrictHostKeyChecking", "no");
	         session.setPassword(hostPassword);
	         session.connect();
	         log.info("Host Connected");
	         sftpChannel = (ChannelSftp) session.openChannel("sftp");
	         sftpChannel.connect();
	         log.info("Uploading subscription.war in to the remote server...............");
	         sftpChannel.put("D:\\Eclipse Workspace\\PrismReleaseDeployment\\upload\\subscription.war", "/tmp/subscription.war");
	         //sftpChannel.put("upload\\subscription.war", "/tmp/subscription.war");
	         log.info("subscription.war uploaded succesfully in to the remote server.");
	         	         	         
	         if(old_War_Path.trim()==null || old_War_Path.trim().isEmpty())
	         {
	        	 log.info("OLD_WAR_PATH=NULL\nUnable to download the old subscription.war");
	         }
	         else
	         {	 
	        	 String dlFileName="subscription_"+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss'.war'").format(new Date());
	         	 log.info("Downloading old subscription.war from remote server..............");
	        	 //sftpChannel.get(old_War_Path+"/webapps/subscription.war", "download\\"+dlFileName);
	        	 sftpChannel.get(old_War_Path+"/webapps/subscription.war", "D:\\Eclipse Workspace\\PrismReleaseDeployment\\download\\"+dlFileName);
	        	 log.info("Old subscription.war download succesfully from remote server.");
	         }
	           
	         ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
	         InputStream in = channelExec.getInputStream();
	         channelExec.setCommand(sh_Filepath);
	         log.info("Executing SH File");
	         //channelExec.setCommand("/root/bin/deployment.sh");
	         channelExec.connect();
	         BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	         String line;
	         	while ((line = reader.readLine()) != null)
	         		{
	         			result.add(line);
	         		}
	         int exitStatus = channelExec.getExitStatus();
	         channelExec.disconnect();
	         session.disconnect();
	         	if(exitStatus < 0)
	         		{
	         			System.out.println("Done, but exit status not set!");
	         			log.info("subscription.war Replacement/Log File backup completed successfully."+"\n"+"Deployment completed successfully"+"\n"+"Tomcat is up now with the new subscription.war, but exit status not set!");
	         		}
	         	else if(exitStatus > 0)
	         		{
	         			System.out.println("Done, but with error!");
	         			log.info("subscription.war Replacement/Log File backup completed successfully."+"\n"+"Deployment completed successfully"+"\n"+"Tomcat is up now with the new subscription.war, but with error!");
	         		}
	         	else
	         		{
	         			System.out.println("Done!");
	         			log.info("subscription.war Replacement/Log File backup completed successfully."+"\n"+"Deployment completed successfully"+"\n"+"Tomcat is up now with the new subscription.war.");
	         		}
	         
	     	}
	     catch(Exception e)
	     	{
	         System.err.println("Error: " + e);
	         log.debug("Error: "+e+"\nUnable to replace atlantis.war!!");
	     	}
	     finally
	     	{
	    	 if(session!=null)
	    	 {
	    		 System.out.println(session);
	    		 session.disconnect();
	    		 session=null;
	    		 System.out.println(session);
	    	 }
	     	}
	     
	
    return result; 
	     }
	     else
	     {
	     	log.info("WAR_REPLACE= "+is_War_Replace);	
	     	return result; 
	     }
    }
    public List executeJarFile(){

	     List result = new ArrayList();
	     
	     log.info("PRISM_JAR_REPLACE= "+is_Prism_Jar_Replace);
	     log.info("PRISM_DAEMON_RESTART= "+is_Prism_Daemon_Restart);
	     if (is_Prism_Jar_Replace.equalsIgnoreCase("true")){
	     try{
	    	 JSch jsch = new JSch();
	    	 System.out.println(hostUserName+hostPassword+host);
	         session = jsch.getSession(hostUserName, host, port);
	         log.info("Host= "+host +"\n"+"Host_UserName= "+hostUserName+"\n"+"Host_Password= "+hostPassword);
	         session.setConfig("StrictHostKeyChecking", "no");
	         session.setPassword(hostPassword);
	         session.connect();
	         log.info("Host Connected");
	         sftpChannel = (ChannelSftp) session.openChannel("sftp");
	         sftpChannel.connect();	         
	         log.info("Uploading prism.jar in to the remote server...............");
	         sftpChannel.put("D:\\Eclipse Workspace\\PrismReleaseDeployment\\upload\\prism.jar", "/tmp/prism.jar");
	         //sftpChannel.put("upload\\prism.jar", "/tmp/prism.jar");
	         log.info("prism.jar uploaded succesfully in to the remote server.");
	          
	         if(old_prism_jar_path.trim()==null || old_prism_jar_path.trim().isEmpty())
	         {
	        	 log.info("OLD_PRISM_JAR_PATH=NULL\nUnable to download the old prism.jar");
	         }
	         else
	         {	 
	        	 String dlFileName="prism_"+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss'.jar'").format(new Date());
	         	 log.info("Downloading old prism.jar from remote server..............");
	        	 //sftpChannel.get(old_prism_jar_path+"/prism.jar", "download\\"+dlFileName);
	        	 sftpChannel.get(old_prism_jar_path+"/prism.jar", "D:\\Eclipse Workspace\\PrismReleaseDeployment\\download\\"+dlFileName);
	        	 log.info("Old prism download succesfully from remote server.");
	         }
	         
	         ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
	         InputStream in = channelExec.getInputStream();
	         channelExec.setCommand(sh_Filepath);
	         log.info("Executing SH File");
	         //channelExec.setCommand("/root/bin/deployment.sh");
	         channelExec.connect();
	         BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	         String line;
	         	while ((line = reader.readLine()) != null)
	         		{
	         			result.add(line);
	         		}
	         int exitStatus = channelExec.getExitStatus();
	         channelExec.disconnect();
	         session.disconnect();
	         	if(exitStatus < 0)
	         		{
	         			System.out.println("Done, but exit status not set!");
	         			log.info("prism.jar Replacement/Log File backup completed successfully."+"\n"+"Deployment completed successfully"+"\n"+"Tomcat is up now with the new prism.jar, but exit status not set!");
	         		}
	         	else if(exitStatus > 0)
	         		{
	         			System.out.println("Done, but with error!");
	         			log.info("prism.jar Replacement/Pojo Deletion/Log File backup completed successfully."+"\n"+"Deployment completed successfully"+"\n"+"Tomcat is up now with the new prism.jar, but with error!");
	         		}
	         	else
	         		{
	         			System.out.println("Done!");
	         			log.info("prism.jar Replacement/Log File backup completed successfully."+"\n"+"Deployment completed successfully"+"\n"+"Tomcat is up now with the new prism.jar.");
	         		}
	         
	     	}
	     catch(Exception e)
	     	{
	         System.err.println("Error: " + e);
	         log.debug("Error: "+e+"\nUnable to replace atlantis.war!!");
	     	}
	     finally
	     	{
	    	 if(session!=null)
	    	 {
	    		 System.out.println(session);
	    		 session.disconnect();
	    		 session=null;
	    		 System.out.println(session);
	    	 }
	     	}
	     
	
   return result; 
   
    }
	     else
	     {
	     	log.info("PRISM_JAR_REPLACE= "+is_Prism_Jar_Replace);	
	     	return result; 
	     }
	     }
}
