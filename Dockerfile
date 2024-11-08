# FROM_Nexus

FROM openjdk:17-jdk-alpine
ARG NEXUS_URL
ARG GROUP_ID
ARG ARTIFACT_ID
ARG VERSION
RUN apk add --no-cache curl && \
    curl -o app.jar "$NEXUS_URL/repository/KaddemUniversite_SalmaMEJRI_5Arctic4/$(echo $GROUP_ID | tr . /)/$ARTIFACT_ID/$VERSION/$ARTIFACT_ID-$VERSION.jar"
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "kaddem-0.0.1-SNAPSHOT.jar"]

#From target
# FROM openjdk:17
# WORKDIR /app
# COPY "target/kaddem-0.0.1-SNAPSHOT.jar" "kaddem-0.0.1-SNAPSHOT.jar"
# EXPOSE 8089
# CMD ["java", "-jar", "kaddem-0.0.1-SNAPSHOT.jar"]

