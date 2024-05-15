FROM jenkins/jenkins:2.426.3
USER root
RUN apt-get update && apt-get install -y \
bzip2 \
rsync \
&& rm -rf /var/lib/apt/lists/*

