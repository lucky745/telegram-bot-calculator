FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
VOLUME /tmp
COPY target/*.jar app.jar
ENV JAVA_OPTS="-Xmx384m -Xms128m -XX:MaxRAM=400m -XX:+UseSerialGC -XX:MaxMetaspaceSize=64m"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]
