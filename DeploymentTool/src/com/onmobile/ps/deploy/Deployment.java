package com.onmobile.ps.deploy;
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
	private String is_Tomcat_Restart="False";
	private String old_War_Path=null;
    private static final int port=22;
    private static Session session=null;
    private static ChannelSftp sftpChannel=null;
    
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
    public void setIsTomcatRestart(String is_Tomcat_Restart)
    {
    	this.is_Tomcat_Restart=is_Tomcat_Restart;
    }
    public void setOldWarPath(String old_War_Path)
    {
    	this.old_War_Path=old_War_Path;
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
    public String getIsTomcatRestart()
    {
    	return this.is_Tomcat_Restart;
    }
    public String getOldWarPath()
    {
    	return this.old_War_Path;
    }


    
    public List executeFile(){
	     List result = new ArrayList();
	     
	     
	     log.info("WAR_REPLACE= "+is_War_Replace);
	     log.info("TOMCAT_RESTART= "+is_Tomcat_Restart);
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
	         log.info("Uploading atlantis.war in to the remote server...............");
	         sftpChannel.put("D:\\My Workspace\\Python-Workspace\\AtlantisWar\\atlantis.war", "/tmp/atlantis.war");
	         //sftpChannel.put("AtlantisWar\\atlantis.war", "/tmp/atlantis.war");
	         log.info("atlantis.war uploaded succesfully in to the remote server.");
	         if(old_War_Path==null)
	         {
	        	 log.info("OLD_WAR_PATH=NULL\nUnable to download the old atlantis.war");
	         }
	         else
	         {	 
	        	 String dlFileName="atlantis_"+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss'.war'").format(new Date());
	         	 log.info("Downloading old atlantis.war from remote server..............");
	        	 //sftpChannel.get(old_War_Path+"/webapps/atlantis.war", "OLD-WAR\\"+dlFileName);
	        	 sftpChannel.get(old_War_Path+"/webapps/atlantis.war", "D:\\My Workspace\\Python-Workspace\\OLD-WAR\\"+dlFileName);
	        	 log.info("Old atlantis.war download succesfully from remote server.");
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
	         			log.info("War Replacement/Pojo Deletion/Log File backup completed successfully."+"\n"+"Deployment completed successfully"+"\n"+"Tomcat is up now with the new WAR, but exit status not set!");
	         		}
	         	else if(exitStatus > 0)
	         		{
	         			System.out.println("Done, but with error!");
	         			log.info("War Replacement/Pojo Deletion/Log File backup completed successfully."+"\n"+"Deployment completed successfully"+"\n"+"Tomcat is up now with the new WAR, but with error!");
	         		}
	         	else
	         		{
	         			System.out.println("Done!");
	         			log.info("War Replacement/Pojo Deletion/Log File backup completed successfully."+"\n"+"Deployment completed successfully"+"\n"+"Tomcat is up now with the new WAR.");
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
}
