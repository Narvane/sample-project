FROM alpine:latest
RUN apk add --no-cache gettext
WORKDIR /app
CMD export $(grep -v '^#' ./env.template | xargs) && \
    mkdir -p /app/output && \
    for template in /app/templates/*; do \
      filename=$(basename "$template" .template); \
      envsubst < "$template" > "/app/output/${filename}"; \
      echo "Processed $template -> /app/output/${filename}"; \
    done && \
    cp ./env.template /app/output/.env && \
    echo ".env copied to /app/output/.env" && \
    echo "All substitutions complete." && \
    ls -la /app/output