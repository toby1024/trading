#!/bin/bash

#如果读不到环境变量RUN_CONTEXT，默认设为dev
if [ -z $RUN_CONTEXT ]; then
    RUN_CONTEXT='pre_prod'
    echo "export RUN_CONTEXT=${RUN_CONTEXT}" >> ${HOME}/.bashrc
fi
#文件的换行模式要选UNIX风格的LF,不然脚本执行会出错!
source ~/.bashrc
#启动sshd
/usr/sbin/sshd
#启动cron
/etc/init.d/cron start
#启动springboot

#预发环境
if [ "$RUN_CONTEXT" = "pre_prod" ]; then
    echo "root:POloXM1980!@&" | chpasswd
    java -jar /app/crms_api/app.jar --spring.profiles.active=pre_prod
#生产环境
elif [ "$RUN_CONTEXT" = "prod" ]; then
    echo "root:POloXM1980!@&" | chpasswd
    java -jar /app/trading/app.jar --spring.profiles.active=prod
fi