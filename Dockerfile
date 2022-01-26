FROM openjdk:7-jre-slim
MAINTAINER wangzhaoxian

ENV PARAMS=""

ADD target/auto-test-*.jar /app.jar

#ENTRYPOINT ["java", "-jar", "$PARAMS /app.jar"]
ENTRYPOINT ["sh","-c","java -jar /app.jar $PARAMS"]