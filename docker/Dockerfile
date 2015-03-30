FROM tomcat:8-jre8
MAINTAINER Mariusz Jozala "http://aetas.pl"

ENV VIRTUAL_HOST="user.taskroo.local"

RUN rm -rf webapps/*
ADD taskroo-user.war $CATALINA_HOME/webapps/ROOT.war
ADD local.properties /root/.taskroo/
