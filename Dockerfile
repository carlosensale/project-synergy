FROM ubuntu:latest
LABEL authors="carlosensale"

ENTRYPOINT ["top", "-b"]