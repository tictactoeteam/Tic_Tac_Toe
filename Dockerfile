FROM java:openjdk-8
LABEL maintainer="david@typokign.com"

RUN apt-get update && \
    apt-get install maven

EXPOSE 6969

ADD . /srv
WORKDIR /srv

RUN mvn compile
RUN mvn package

# workaround cuz I don't want to set up a build system


CMD ["java", "-jar", "target/tictactoe*.jar", "--server"]
