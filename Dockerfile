FROM bellsoft/liberica-openjdk-alpine:11

RUN apk add --update curl && rm -rf /var/cache/apk/*

COPY target/*.war root.war

EXPOSE 8080

ENTRYPOINT ["java","-jar","/root.war"]