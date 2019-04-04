FROM gradle:jdk11
LABEL maintainer="david@typokign.com"

USER root

EXPOSE 6969

COPY . /srv/
WORKDIR /srv/

RUN gradle build --no-daemon && \
    gradle jar --no-daemon && \
    cp ./build/libs/tictactoe.jar tictactoe.jar

CMD ["java", "-jar", "tictactoe.jar", "--server", "--migrate"]
