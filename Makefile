.PHONY: build jar run
.DEFAULT_GOAL := run

build:
	gradle build

jar: build
	gradle shadowJar

run:
	gradle run
