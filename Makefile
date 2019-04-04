.PHONY: build jar run
.DEFAULT_GOAL := run

build:
	./gradlew build

jar: build
	./gradlew jar
	cp ./build/libs/tictactoe.jar ./tictactoe.jar

run:
	./gradlew run

build-docker:
	docker build .

up: build-docker
	docker-compose up
