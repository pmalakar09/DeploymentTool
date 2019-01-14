# DeploymentTool
**Automatic Deployment Tool**

1)	Overview
This tool is designed for the deployment of the {Project} Release. The tool will automatically deploy the SQL Scripts in the {Project} DB and post that it will deploy the {Project}.war file into the Linux server. The tool is also capable to restart the Apache Tomcat(As per the configuration in the Input.properties)

2)	Application Requirement
The requirement of this tool is to automate the release deployment.

3)	Design Description
3.1	Control  Flow
*	1st the tool will deploy the SQL Queries in the DB Server.
*	All the SQL Queries should be present in the SQL Bucket (SCRIPT.sql).
*	After SQL Queries deployment, a report file will be generated. e.g: REPORT-2018-03-14-15-48-44.csv
*	During SQL file deployment if any exception occurs then the whole process will be reverted into the initial stage.
*	Then only after successfully executing the SQL queries, the tool will go into the next phase, i.e. {Project}.war deployment or Apache Tomcat restart (As per the configuration in the Input.properties)
*	Case 1: {Project}.war Deployment: In this case, the tool will stop the running tomcat, then deploy the {Project}.war file (Which should be present in the {Project}War directry) into the Linux server under the apache tomcat. e.g. /home/{Project}/{Project}-Intl-Tomcat7.0.50/webapps/
*	The tool also delete the Pojo ’s, webapps/{Project} directory, logs (Old logs also kept as a backup under /logs/backup/)
*	The tool also takes the backup of the old running {Project}.war file inside the webapps by renaming it e.g. {Project}.war_03-09-2018-13-26-53
*	If the user wants the tool also download the old running {Project}.war file into the local system(under OLD-WAR directory)
*	At the last, the code will start the apache tomcat.
*	Case 2: Apache Tomcat Restart (No {Project}.war deployment): In this case after the successfully SQL deployment the tool will stop the running Apache Tomcat and post that it will delete all the old pojo’s and delete all the old logs (Old logs also kept as a backup under /logs/backup/). Post that the tool will start the apache tomcat.
*	Case 3: No SQL deployment, only {Project}.war deployment: In this case do not keep any sql queries in the SQL Bucket (SCRIPT.sql), and do the necessary configuarations under the Input.properties file for war deployment.
*	Case 4: Only SQL deployment: If no war deployment of tomcat restart is needed then do the necessary configuarations under the Input.properties file for the same. Then the tool will only deploy the SQL scripts into the DB server.
Input.properties File:

4)	Prerequisites:
*	Java 1.8 should be installed on the local system.
*	The deployment.sh and restart.sh file should be present into the Linux Server, and the past should be mentioned in the Input.properties file under the key SH_FILEPATH
*	Do change the correct SH_FILEPATH accordingly to the requirement(deployment or resart).
*	Inside both the deployment.sh and restart.sh file mentions the target apache tomcat details. e.g. Tom="{Project}-Intl-Tomcat7.0.50" (This is a one-time activity because the tool was developed under this assumption that we deploy the {Project}.war or restart the tomcat in both the cases the target tomcat is constant, its getting changed in a rare basis.)

