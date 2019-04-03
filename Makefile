.PHONY: build jar run
.DEFAULT_GOAL := run

build:
	gradle build

jar: build
	gradle jar
	cp ./build/libs/tictactoe.jar ./tictactoe.jar

run:
	gradle run
