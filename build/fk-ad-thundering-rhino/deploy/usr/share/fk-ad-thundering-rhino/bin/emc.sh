#!/usr/bin/env bash

PORT="3306"
USERNAME="fk_analytics_rep"
PASSWORD=""
DATABASE="datareportdb"

currentTime=`date +%Y-%m-%d | tr -d '\n'`
startdate=`date +%Y-%m-01 -d "-$(date +%d) days -0 month"`
enddate=`date +%Y-%m-%d -d "-$(date +%d) days -0 month"`
month=`date +"%B"`
lmonth=`date +"%B" -d"-1 month"`

outputfile="/usr/share/fk-ad-thundering-rhino/www/testing/reports/MEC_${month}_report.csv"
`rm /usr/share/fk-ad-thundering-rhino/www/testing/reports/MEC_${lmonth}_report.csv`
echo "===========Script $0 Starts at $currentTime==========="

> $outputfile

echo "select entity_id as campaign_id, sum(delivered_value) as budget_consumed, date(created_at) as sent_to_accounting_on from entity_performance_audit_v3 where Date(created_at) >= '$startdate' and Date(created_at) <= '$enddate'  group by campaign_id;"
mysql -u fk_analytics_rep  adserverdb -h 10.32.237.173 --port 3306 -e "select entity_id as campaign_id, sum(delivered_value) as budget_consumed, date(created_at) as sent_to_accounting_on from entity_performance_audit_v3 where Date(created_at) >= '$startdate' and Date(created_at) <= '$enddate'  group by campaign_id;" > /tmp/emc.txt
cat /tmp/emc.txt | sed 's/\t/,/g' > $outputfile
echo "Data update on $currentTime for the period of $startdate to $enddate" >> $outputfile
echo "===========Script $0 END==========="