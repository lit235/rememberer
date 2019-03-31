#!/usr/bin/make

SHELL = /bin/sh

# --- [ Tests ] -------------------------------------------------------------------------------------------------------

test.run-env:
	docker-compose -f docker/docker-compose.test.yml up -d
	sh waitformysql.sh 33060
	mvn flyway\:clean -Dflyway.configFile=flyway.test.properties
	mvn flyway\:migrate -Dflyway.configFile=flyway.test.properties

test.stop-env:
	docker-compose -f docker/docker-compose.test.yml down

test.do-tests:
	mvn test

test: test.run-env test.do-tests test.stop-env

# --- [ Development ] -------------------------------------------------------------------------------------------------

dev.clean-db:
	mvn flyway\:clean -Dflyway.configFile=flyway.properties
	mvn flyway\:migrate -Dflyway.configFile=flyway.properties

dev.run-env:
	docker-compose -f docker/docker-compose.dev.yml up -d
	sh waitformysql.sh 3306

dev.stop-env:
	docker-compose -f docker/docker-compose.dev.yml down

dev.build:
	mvn war:war

dev.deploy:
	sh ~/tomcat/bin/catalina.sh stop
	cp ./target/rememberer-0.0.1-SNAPSHOT.war ~/tomcat/webapps/rememberer.war
	sh ~/tomcat/bin/catalina.sh run

dev.stop-server:
	sh ~/tomcat/bin/catalina.sh stop

dev.start-app: dev.run-env dev.clean-db dev.build dev.deploy

dev.stop-app: dev.stop-server dev.stop-env
