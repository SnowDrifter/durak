FROM jetty:9-jre8

COPY target/durak-1.1.0.war /var/lib/jetty/webapps/root.war

EXPOSE 8080
