FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
VOLUME /tmp
COPY target/*.jar app.jar
ENV JAVA_OPTS="-Xmx256m -Xms64m -XX:MaxRAM=280m -XX:+UseSerialGC -XX:MaxMetaspaceSize=32m -XX:+UseCompressedOops -XX:+TieredCompilation -XX:TieredStopAtLevel=1 -XX:MinHeapFreeRatio=20 -XX:MaxHeapFreeRatio=40"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]
