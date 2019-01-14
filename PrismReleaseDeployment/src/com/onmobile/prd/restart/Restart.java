package com.onmobile.prd.restart;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


public class Restart {
	static Logger log = Logger.getLogger(Restart.class);
	public String host=null;
    public String hostUserName=null;
    public String hostPassword=null;
    public String sh_Filepath=null;
    public String is_War_Replace="False";
    public String is_Prism_Daemon_Restart="False";
    public String is_Prism_Jar_Replace="False";
    public String old_War_Path=null;
    private static final int port=22;
    public static Session session=null;
    public static ChannelSftp sftpChannel=null;
    
    public void setHost(String host)
    {
    	this.host=host;
    }
    public void setHostuserName(String hostUserName)
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
    public void set_Is_Prism_Jar_Replace(String is_Prism_Jar_Replace){
    	this.is_Prism_Jar_Replace=is_Prism_Jar_Replace;
    }
    public void set_Is_Prism_Daemon_Restart(String is_Prism_Daemon_Restart)
    {
    	this.is_Prism_Daemon_Restart=is_Prism_Daemon_Restart;
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
    	return sh_Filepath;
    }
    public String getIsWarReplace()
    {
    	return is_War_Replace;
    }
    public String get_Is_Prism_Daemon_Restart()
    {
    	return is_Prism_Daemon_Restart;
    }
    public String get_Is_Prism_Jar_Replace(){
    	return is_Prism_Jar_Replace;
    }
    
    public List executeFile(){
	     List result = new ArrayList();
	     
	     
	     log.info("WAR_REPLACE= "+is_War_Replace);
	     log.info("PRISM_JAR_REPLACE= "+is_Prism_Jar_Replace);
	     log.info("PRISM_DAEMON_RESTART= "+is_Prism_Daemon_Restart);
	     try{
	    	 JSch jsch = new JSch();
	    	 System.out.println(hostUserName+hostPassword+host);
	         session = jsch.getSession(hostUserName, host, port);
	         log.info("Host= "+host +"\n"+"Host_UserName= "+hostUserName+"\n"+"Host_Password= "+hostPassword);
	         session.setConfig("StrictHostKeyChecking", "no");
	         session.setPassword(hostPassword);
	         session.connect();
	         log.info("Host Connected");
	         ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
	         InputStream in = channelExec.getInputStream();
	         channelExec.setCommand(sh_Filepath);
	         log.info("Executing SH File");
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
	         			log.info("Tomcat restart done successfully"+"\n"+"Tomcat is up now, but exit status not set!");
	         		}
	         	else if(exitStatus > 0)
	         		{
	         			System.out.println("Done, but with error!");
	         			log.info("Tomcat restart done successfully"+"\n"+"Tomcat is up now, but with error!");
	         		}
	         	else
	         		{
	         			System.out.println("Done!");
	         			log.info("Tomcat restart done successfully"+"\n"+"Tomcat is up now.");
	         		}
	         
	     	}
	     catch(Exception e)
	     	{
	         System.err.println("Error: " + e);
	         log.debug("Error: "+e+"\nUnable to restart Tomcat!!");
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
