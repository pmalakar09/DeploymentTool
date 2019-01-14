#!/bin/bash

Tom="atlantis-Intl-Tomcat7.0.50"

PID="`/bin/ps aux |/bin/grep $Tom |/bin/grep -v grep |/usr/bin/tr -s " " |/usr/bin/cut -d" " -f2`"

kill -9 $PID

/bin/ls /home/atlantis/$Tom/logs/ | /bin/gzip -c > /home/atlantis/$Tom/logs/backup/oldlog-`/bin/date +"%m-%d-%Y-%H-%M-%S"`.gz

/bin/rm -rvf /home/atlantis/$Tom/logs/*.*

/bin/sleep 4
/bin/mv /home/atlantis/$Tom/webapps/atlantis.war /home/atlantis/$Tom/webapps/atlantis.war_`/bin/date +"%m-%d-%Y-%H-%M-%S"`

/bin/sleep 4

/bin/mv /tmp/atlantis.war /home/atlantis/$Tom/webapps/atlantis.war
/bin/sleep 2
/bin/rm -rvf /home/atlantis/$Tom/lib/onmobile /home/atlantis/$Tom/lib/pojo.config /home/atlantis/$Tom/lib/PojoGeneration.errors /home/atlantis/$Tom/work/Catalina /home/atlantis/$Tom/conf/Catalina /home/atlantis/$Tom/webapps/atlantis

/bin/sleep 2

/bin/su - root -p password  -c /home/atlantis/$Tom/bin/startup.sh