#!/bin/bash
PACKAGE=fk-ad-thundering-rhino

# setup apt sources.list.d
echo "10.65.100.196 wzy-mirror.nm.flipkart.com" >> /etc/hosts
echo "10.84.182.21 repo-svc-app-0001.nm.flipkart.com" >> /etc/hosts

echo "deb http://wzy-mirror.nm.flipkart.com/ftp.debian.org/debian wheezy-backports main" > /etc/apt/sources.list.d/wzy-backports.list
echo "deb http://10.47.2.22:80/repos/infra-cli/3 /" > /etc/apt/sources.list.d/infra-cli-svc.list

# install infra-cli (contains repo-service cli)
apt-get update
apt-get install --yes --allow-unauthenticated infra-cli

# include your repo in sources.list.d
reposervice --host repo-svc-app-0001.nm.flipkart.com --port 8080 env --name prod-ads-monitoring-svc --appkey test123 --version 5 > /etc/apt/sources.list.d/$PACKAGE.list;

# install your package
apt-get update;
apt-get install --yes --allow-unauthenticated $PACKAGE;
apt-get install --yes --allow-unauthenticated fk-nsca-wrapper;
apt-get install --yes --allow-unauthenticated fk-nagios-common;

echo "team_name=\"Ads-Monitoring\"" > /etc/default/nsca_wrapper
echo "nagios_server_ip=\"10.47.2.198\"" >> /etc/default/nsca_wrapper

/etc/init.d/nginx start