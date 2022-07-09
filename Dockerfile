FROM centos:centos7
MAINTAINER zyt<zyt061303130215@163.com>


ADD jdk-8u144-linux-x64.tar.gz /usr/local
ADD zookeeper-3.4.9.tar.gz /usr/local
ADD nacos-server-1.2.1.tar.gz /usr/local

RUN yum -y install vim

ENV MYPATH /usr/local
WORKDIR $MYPATH

ENV JAVA_HOME /usr/local/jdk1.8.0_144
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH $PATH:$JAVA_HOME/bin:

EXPOSE 2181 2888 3888 8080 8848

# 然后就是启动命令nacos 和 zookeeper

ENV ZOOKEEPER_HOME /usr/local/zookeeper-3.4.9
# 环境变量设置
RUN cp $ZOOKEEPER_HOME/conf/zoo_sample.cfg $ZOOKEEPER_HOME/conf/zoo.cfg

CMD $ZOOKEEPER_HOME/bin/zkServer.sh start&&$MYPATH/nacos/bin/startup.sh -m standalone&&/bin/bash
