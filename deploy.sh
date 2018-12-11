#!/bin/bash
# @Author: fxq
# @Date:   2018-02-22

mvn clean
mvn clean install
mvn clean package -P prod
sleep 1
rm -rf /home/control/cloud-admin-1.0.0.2-prod.jar
cp cloud-admin/target/cloud-admin-1.0.0.2-prod.jar  /home/control

killall -15 cloud-admin
sleep 1
nohup java -jar /home/control/cloud-admin-1.0.0.2-prod.jar 2>&1 >> info.log 2>&1 /dev/null &
echo "服务已启动..."

