var currentdate = new Date();
var etime = currentdate.getFullYear() + "-" + (currentdate.getMonth()+1)  + "-" + currentdate.getDate() + " " + currentdate.getHours() + ":00:00";
currentdate.setDate(currentdate.getDate()-1);
var stime = currentdate.getFullYear() + "-" + (currentdate.getMonth()+1)  + "-" +  currentdate.getDate() + " " + currentdate.getHours() + ":00:00";
var chartIds=["chartContainer1","chartContainer2","chartContainer3"];
var url = ["plareqrateinterval","plafillrateinterval","plaactionrateinterval"];

function parsedata(data){
        var keys = Object.keys(data);  
        var accounting = [];
        for (var i in keys)
        {
	 var datetime = keys[i];
         var res = datetime.split(" ");
         var date = res[0].split("-");
         var time = res[1].split(":");
         accounting.push({x :new Date(date[0],date[1]-1,date[2],time[0],time[1],time[2]), y: data[keys[i]] });
         //accounting.push({x :new Date(keys[i]), y: data[keys[i]] });
        }
 return accounting;
}

 for(var i=0;i <chartIds.length;i++){ 
(function(i) {
 var url1 = "http://10.47.3.204/services/ticker/"+url[i]+"?strTime="+stime+"&enTime="+etime;
 console.log(url1);
$.ajax({
  dataType: "json",
  url: url1,
  type: 'GET',
  success: function(data){
   renderChart(data,i);
}
});
})(i);
}


function renderChart(data,i){
  var accounting = parsedata(data);
  console.log(accounting);
    var chart = new CanvasJS.Chart(chartIds[i],
    {
        backgroundColor: "#1F1F1F",
        axisX: {
        gridThickness: 1,
        interval:2,
        lineThickness: 0,
        gridColor: "#585858" , 
        intervalType: "hour",        
        valueFormatString: "hh TT DD-MM-YY", 
        labelAngle: -20
      },
      axisY:{
        gridColor: "#585858" ,
        gridThickness: 1,
        lineThickness: 0
      },
       data:[
      {
        type: "line",
        lineThickness: 1,
        xValueType: "dateTime",
        dataPoints: accounting
	} 
	]
});

    chart.render();
}

