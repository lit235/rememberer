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

dev.stop-env:
	docker-compose -f docker/docker-compose.dev.yml down
