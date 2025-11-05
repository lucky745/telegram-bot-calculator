FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
VOLUME /tmp
COPY target/*.jar app.jar
ENV JAVA_OPTS="-Xmx356m -Xms164m -XX:MaxRAM=380m -XX:+UseSerialGC -XX:+UseCompressedOops -XX:+TieredCompilation -XX:TieredStopAtLevel=1"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]
