FROM docker-prod.artifactory-edge.kroger.com/library/java-openjdk:21-latest

EXPOSE 8080

ADD build/libs /app

CMD $JAVA_HOME/bin/java/ -jar /app/ankitexampleskeleton-*.jar
