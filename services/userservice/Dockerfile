# FROM dekstroza/openjdk9-alpine as packager

# RUN /opt/jdk-9/bin/jlink \
#     --module-path /opt/jdk-9/jmods \
#     --verbose \
#     --add-modules java.base,java.logging,java.xml,jdk.unsupported,java.management \
#     --compress 2 \
#     --no-header-files \
#     --output /opt/jdk-9-minimal

# FROM alpine:3.6
# COPY --from=packager /opt/jdk-9-minimal /opt/jdk-9-minimal
FROM dekstroza/openjdk9-alpine
WORKDIR /usr/app
COPY build/libs/userservice-1.0.jar .

ENV JAVA_HOME=/opt/jdk-9
ENV PATH="$PATH:$JAVA_HOME/bin"
# ENV JAVA_HOME=/opt/jdk-9-minimal
# ENV PATH="$PATH:$JAVA_HOME/bin"

CMD [ "java", "-jar", "/usr/app/userservice-1.0.jar" ]