FROM gcr.io/distroless/java21-debian12:nonroot

WORKDIR /app
COPY target/*.jar /app/app.jar

ENV JAVA_TOOL_OPTIONS="\
-XX:MaxRAMPercentage=65 \
-XX:InitialRAMPercentage=20 \
-XX:+UseSerialGC \
-XX:+ExitOnOutOfMemoryError \
"

ENTRYPOINT ["java","-jar","/app/app.jar"]
