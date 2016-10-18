#!/usr/bin/env bash

PORT="3306"
USERNAME="fk_analytics_rep"
PASSWORD=""
DATABASE="datareportdb"

currentTime=`date +%Y-%m-%d | tr -d '\n'`
endtime=`date +%Y-%m-%d -d "1 day ago"`

outputFile="/usr/share/fk-ad-thundering-rhino/www/testing/reports/Daily_campaign_consumption_${currentTime}.csv"
`rm /usr/share/fk-ad-thundering-rhino/www/testing/reports/Daily_campaign_consumption_${endtime}.csv`


echo "===========Script $0 Starts at $currentTime==========="

> $outputFile

#echo  "select c.campaign_id, c.name,date(c.start_date) start_date ,c.status,en.delivered_value,s.Completion_date,Date(c.end_date) end_date from  (select entity_id, sum(delivered_value) delivered_value from entity_performance_audit_v3 where created_at between '$starttime' and '$endtime' group by entity_id) en left join campaigns c on en.entity_id= c.campaign_id left join (select campaign_id, date(timestamp_action) Completion_date from campaigns_audit where action_performed='abort' or action_performed='complete') s on c.campaign_id=s.campaign_id;"

mysql -u fk_analytics_rep  adserverdb -h 10.32.237.173 --port 3306 -e "select c.campaign_id, c.name,date(c.start_date) start_date ,c.status,en.delivered_value,s.Completion_date,Date(c.end_date) end_date from  (select entity_id, sum(value) delivered_value from entity_performance_audit_v3 where date(created_at)='$endtime' group by entity_id) en left join campaigns c on en.entity_id= c.campaign_id left join (select campaign_id, date(timestamp_action) Completion_date from campaigns_audit where (target_status='aborted' or target_status='complete')) s on en.entity_id=s.campaign_id where c.status='complete' or c.status='aborted';" >/tmp/Daily_campaign_consumption.txt

cat /tmp/Daily_campaign_consumption.txt | sed 's/\t/,/g' > $outputFile
echo "Data update on $currentTime for the Date $endtime" >> $outputFile

echo "===========Script $0 END==========="