#!/bin/bash

TOMCAT_PATH="/PRISM/TOMCAT/apache-tomcat-7.0.54/webapps"
TOMCAT_PROCESS_NAME="tomcat"
PRISM_PATH="/PRISM/PRISMD/PRISMD_RELEASE"
PRISM_PROCES_NAME="prismD"

/etc/init.d/prismD stop

/bin/sleep 2

/etc/init.d/prismD start