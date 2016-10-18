#!/usr/bin/env bash
HOST="10.32.141.156"
PORT="3306"
USERNAME="data_report_ro"
PASSWORD="5fBPpyi0"
DATABASE="datareportdb"

todayDate=`date +"%Y-%m-%d 00:00:00"`
echo $todayDate

engagements=$(mysql -u $USERNAME -p$PASSWORD -h $HOST --port $PORT -se "select sum(engagements) as engagements from datareportdb.data_stats_summary_half_hourly_v3 where interval_start <='$todayDate'")

echo "SET daily ${engagements}"|redis-cli