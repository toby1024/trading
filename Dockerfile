FROM daocloud.io/skio_dep/maven_3.5.3-jdk-8:v5

ENV PROJECT_NAME="trading"
ENV APP_BASE_HOME="/app" 
ENV APP_HOME="${APP_BASE_HOME}/${PROJECT_NAME}"

ADD pom.xml /tmp/build/
ADD src /tmp/build/src

#构建应用
RUN mkdir -p $APP_HOME && \
    mkdir -p $APP_HOME/bin && \
    cd /tmp/build && \
    mv src/main/resources/application.yml.ci src/main/resources/application.yml && \
    # maven package
    mvn clean package -DskipTests=true -q &&\
    #拷贝编译jar到指定目录
    mv /tmp/build/target/*.jar $APP_HOME/app.jar  && \
    #清理编译痕迹
    cd / && rm -rf /tmp/build

ADD *.sh $APP_HOME/bin
RUN chmod 755 $APP_HOME/bin/*.sh

EXPOSE 8080
CMD /bin/bash $APP_HOME/bin/docker_web_run.sh