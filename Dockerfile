FROM fedora:41
WORKDIR /app
COPY target/oda-max-service /app

CMD ["./oda-max-service"]
