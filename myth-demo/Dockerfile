FROM openjdk:11.0.14-jre-buster

RUN mkdir -p /opt/projects/myth-demo

WORKDIR /opt/projects/myth-demo

ADD ./target/myth-demo*.jar app.jar

EXPOSE 8888

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar