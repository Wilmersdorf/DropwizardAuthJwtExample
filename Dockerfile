FROM java:8-jre
COPY daedocker.yml /opt/dropwizard/
COPY build/libs/DropwizardAuthJwtExample-all.jar /opt/dropwizard/
EXPOSE 80
EXPOSE 8081
WORKDIR /opt/dropwizard
CMD ["java", "-Xms128m", "-Xmx1500m", "-Dfile.encoding=UTF-8", "-jar", "DropwizardAuthJwtExample-all.jar", "server", "daedocker.yml"]
