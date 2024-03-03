FROM openjdk:17-oracle

COPY root.crt root.crt

RUN echo yes | keytool -importcert -alias mongo_db -keystore \
    $JAVA_HOME/lib/security/cacerts -storepass changeit -file root.crt
