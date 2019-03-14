FROM java:openjdk-8
LABEL maintainer="david@typokign.com"

RUN apt-get update && \
    apt-get -y upgrade && \
    apt-get -y install openjfx

EXPOSE 6969

RUN mkdir /srv/classes

WORKDIR /srv/
ADD . .

# workaround cuz I don't want to set up a build system
RUN find -name "*.java"  > src.txt && \
    javac @src.txt -d ./classes && \
    jar cfe tictactoe.jar edu.saddleback.tictactoe.Main -C classes . && \
    rm src.txt && \   
    rm -rf classes

CMD ["java", "-jar", "tictactoe.jar", "--server"]
