package com.onmobile.prd.scripts;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;


public class Script {
	static Logger log = Logger.getLogger(Script.class.getName());
	private Connection connection;
	private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "SQL_QUERY,EXECUTION_START_TIME,EXECUTION_END_TIME,OUTPUT";
    public boolean status=false;
    private String dbURL=null;
    private String dbUSER=null;
    private String dbPASSWORD=null;
	public long queryCount=0;
	private FileWriter fileWriter=null;
	private String[] token=null;
	private int i=0;
	private String reportCSV = "REPORT-"+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss'.csv'").format(new Date());
	private java.sql.Statement pstmt=null;
	private String dBDriver=null;
	
	
	public void setDbUrl(String dbURL)
	{
	this.dbURL=dbURL;	
	}
	public void setDbUser(String dbUSER)
	{
	this.dbUSER=dbUSER;	
	}
	public void setDbPassword(String dbPASSWORD)
	{
	this.dbPASSWORD=dbPASSWORD;	
	}
	public void setDBDriver(String dBDriver){
		this.dBDriver=dBDriver;
	}
	public String getDbUrl()
	{
		return this.dbURL;
	}
	public String getDbUser()
	{
		return this.dbUSER;
	}
	public String getDbPassword()
	{
		return this.dbPASSWORD;
	}
	
	 public Connection getConnection() {
		 try
	      {
			System.out.println(dbURL+dbPASSWORD+dbUSER);
		    if (this.connection!= null||dbURL==null||dbUSER==null||dbPASSWORD==null) 
		    {
		    	log.debug("In Input.properties file mandatory fields are empty ==> "+"DBURL= "+dbURL+";"+"DBUSER= "+dbUSER+";"+"DBPASSWORD= "+dbPASSWORD+"\n"+"Please fill the Input.properties properly!!");
		    }
		      
		    else
		    {
		    	Class.forName(dBDriver);
		    	log.info("Driver Loading Is Done Successfully");
		        this.connection = DriverManager.getConnection(this.dbURL, this.dbUSER, this.dbPASSWORD);
		        log.info("Connecting To The DB : "+dbURL);
		        log.info("DB connection done successfully");
		     
		    return this.connection;
		    }
	      }
		    catch (Exception e)
		    {
		    	log.debug(e);
		    }
		return connection;
	
	
}
	 
	 public boolean scriptExecution()
	 {
		 try
		 {
	     log.info("SQL execution begin.....");
		 //fileWriter=new FileWriter(conf\\reportCSV);
		 fileWriter=new FileWriter("D:\\Eclipse Workspace\\PrismReleaseDeployment\\conf\\"+reportCSV);
		 fileWriter.append(FILE_HEADER.toString());
         fileWriter.append(NEW_LINE_SEPARATOR);
         connection.setAutoCommit(false);
         pstmt=connection.createStatement();
         ArrayList al= new ArrayList();
			StringBuilder currentSql= new StringBuilder();
			System.out.println("1"+currentSql);
			System.out.println("al:"+al);
			log.info("INPUT SCRIPT : "+al);
			token=(new Script().readFile()).split(";");
			if (new Script().readFile().isEmpty())
			{
				queryCount=0;
			}
			else
			{
				queryCount=token.length;
			}
			log.info("Total query count: "+queryCount);
			for(i=0;i<token.length;i++){
				System.out.println("SQL Query : "+token[i]);
				log.info("SQL Query : "+token[i]);
				currentSql.append((token[i]));
				System.out.println("al:"+al);
				if(token[i].contains("dist_systemconfiguration"))
				{
					fileWriter.append(token[i].replaceAll(",", " {COMMA} "));
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(new SimpleDateFormat("HH-mm-ss").format(new Date()));
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(new SimpleDateFormat("HH-mm-ss").format(new Date()));
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append("Not executed because of dist_systemconfiguration script");
					fileWriter.append(NEW_LINE_SEPARATOR);
					log.info("Not executed SQL because of dist_systemconfiguration script found");
					System.out.println("Not executed because of dist_systemconfiguration script");
				}
				else if(token[i].startsWith("-"))
				{	
					fileWriter.append(token[i].replaceAll(",", " {COMMA} "));
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(new SimpleDateFormat("HH-mm-ss").format(new Date()));
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(new SimpleDateFormat("HH-mm-ss").format(new Date()));
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append("Not executed because SQL query is COMMENTED");
					fileWriter.append(NEW_LINE_SEPARATOR);
					log.info("Not executed because SQL query is COMMENTED");
					System.out.println("Not executed because SQL query is COMMENTED");
				}
				else
				{
				log.info("Executing Query : "+token[i]);
				fileWriter.append(token[i].replaceAll(",", " {COMMA} "));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(new SimpleDateFormat("HH:mm:ss").format(new Date()));
				pstmt.execute(token[i]);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(new SimpleDateFormat("HH:mm:ss").format(new Date()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append("EXECUTED");
				fileWriter.append(NEW_LINE_SEPARATOR);
				log.info("Successfully Executed Query : "+token[i]);
				}
			}
			System.out.println(al);
		connection.commit();
		connection.close();
		status=true;
	 }
	 catch (Exception e) 
    {
		 try
		 {
		e.printStackTrace();
     	fileWriter.append(COMMA_DELIMITER);
		fileWriter.append(new SimpleDateFormat("HH:mm:ss").format(new Date()));
		log.debug("NOT EXECUTED SQL : "+token[i]);
		fileWriter.append(COMMA_DELIMITER);
		String exceptionText=e.toString();
		exceptionText=exceptionText.replace("\n"," ");
		System.out.println("EXCEPTIONTEXT : "+exceptionText);
		log.debug(e);
		fileWriter.append(exceptionText);
		fileWriter.append(NEW_LINE_SEPARATOR);
		e.printStackTrace();
		 }
		 catch(Exception e1)
		 {
			 log.debug("Error while writing Report.csv file !!!"+e1);
		 }
    }
	 finally
	 {
		 try{
				fileWriter.flush();
				fileWriter.close();
			}
			catch(Exception e){
				System.out.println("Error while flushing/closing fileWriter !!!");
		        e.printStackTrace();
		        log.debug("Error while flushing/closing fileWriter !!!"+e);
		    	}
	 }
		return status;
	 
}
	 String readFile() throws IOException {
		    //String filename="conf\\SCRIPT.sql";
			String filename="D:\\Eclipse Workspace\\PrismReleaseDeployment\\conf\\SCRIPT.sql";
			BufferedReader br = new BufferedReader(new FileReader(filename));
		    try {
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null) {
		            sb.append(line);
		            line = br.readLine();
		        }
		        System.out.println("sb.length: "+sb.length());
		        System.out.println("sb.toString: "+sb.toString());
		        return sb.toString();
		    } finally {
		        br.close();
		    }
		}
	 
}
