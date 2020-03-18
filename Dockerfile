FROM bellsoft/liberica-openjdk-alpine:11

COPY target/*.war root.war

EXPOSE 8080

ENTRYPOINT ["java","-jar","/root.war"]