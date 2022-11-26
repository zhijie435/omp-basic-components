FROM harbor01.yiako.com/tools/java-8u111-jre-alpine:latest
ARG PACKAGE_NAME
RUN  mkdir /usr/local/apps
COPY  ./target/${PACKAGE_NAME} /tmp/${PACKAGE_NAME}
RUN  tar -xvzf /tmp/${PACKAGE_NAME} -C /usr/local/apps

COPY  docker-entrypoint.sh /
RUN  chmod u+x docker-entrypoint.sh
WORKDIR /usr/local/apps

ENTRYPOINT ["/docker-entrypoint.sh"]
