FROM openjdk:21
WORKDIR /app
CMD ["sh", "-c", "java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -cp /app/classes:/app/dependency/* $MAIN_CLASS_TO_DOCKERFILE"]
