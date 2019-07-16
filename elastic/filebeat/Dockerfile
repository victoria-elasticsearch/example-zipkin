ARG ES_VERSION

FROM docker.elastic.co/beats/filebeat:${ES_VERSION}


COPY config/filebeat.yml /usr/share/filebeat/filebeat.yml
USER root
RUN chown root:filebeat /usr/share/filebeat/filebeat.yml && chmod go-w /usr/share/filebeat/filebeat.yml