#!/usr/bin/env bash

PORT="3306"
USERNAME="fk_analytics_rep"
PASSWORD=""
DATABASE="datareportdb"

currentTime=`date +%Y-%m-%d-%H-%M | tr -d '\n'`
echo "===========Script $0 Starts at $currentTime==========="

> /usr/share/fk-ad-thundering-rhino/www/testing/reports/consumed_campaign_greterthen_80.csv

mysql -u fk_analytics_rep  adserverdb -h 10.32.237.173 --port 3306 -e "select s.seller_id,campaign_id,status,budget_value, en.delivered_value from campaigns c left join (select entity_id, sum(delivered_value) delivered_value from entity_performance_audit_v3 group by entity_id) en on c.campaign_id=en.entity_id left join sellers s on c.advertiser_id=s.id where c.status='Ready' and c.budget_value* .79 < en.delivered_value; " > /tmp/consumed_campaign.txt

cat /tmp/consumed_campaign.txt | sed 's/\t/,/g' > /usr/share/fk-ad-thundering-rhino/www/testing/reports/consumed_campaign_greterthen_80.csv
echo "Data update on $currentTime" >> /usr/share/fk-ad-thundering-rhino/www/testing/reports/consumed_campaign_greterthen_80.csv

echo "===========Script $0 END==========="
