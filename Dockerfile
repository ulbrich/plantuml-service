FROM webratio/java:7

# Installs Ant
ENV ANT_VERSION 1.9.4

RUN apt-get -y update && apt-get install -y \
	graphviz

RUN cd && \
    wget -q http://archive.apache.org/dist/ant/binaries/apache-ant-${ANT_VERSION}-bin.tar.gz && \
    tar -xzf apache-ant-${ANT_VERSION}-bin.tar.gz && \
    mv apache-ant-${ANT_VERSION} /opt/ant && \
    rm apache-ant-${ANT_VERSION}-bin.tar.gz
ENV ANT_HOME /opt/ant
ENV PATH ${PATH}:/opt/ant/bin

COPY . /srv

WORKDIR /srv

ENV PORT 8080

CMD ant
