FROM registry.access.redhat.com/ubi8/ubi:latest
USER root
LABEL maintainer="Adolfo Benedetti"
# Update the UBI image
RUN yum update --disableplugin=subscription-manager -y && rm -rf /var/cache/yum
RUN yum install --disableplugin=subscription-manager nc -y && rm -rf /var/cache/yum
# we add netcat to _easily_ check for the running instance of elasticsearch