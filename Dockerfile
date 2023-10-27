FROM ubuntu:latest
LABEL authors="fede"

ENTRYPOINT ["top", "-b"]