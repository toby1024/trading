#!/bin/bash

#文件的换行模式要选UNIX风格的LF,不然脚本执行会出错!
source ~/.bashrc
#启动sshd
/usr/sbin/sshd
#启动cron
/etc/init.d/cron start
#启动springboot

echo "root:POloXM1980!@&" | chpasswd
java -jar /app/crms_api/app.jar --spring.profiles.active=prod

