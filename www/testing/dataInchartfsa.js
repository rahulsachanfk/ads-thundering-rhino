var currentdate = new Date();
var etime = currentdate.getFullYear() + "-" + (currentdate.getMonth()+1)  + "-" + currentdate.getDate();
currentdate.setDate(currentdate.getDate()-30);
var stime = currentdate.getFullYear() + "-" + (currentdate.getMonth()+1)  + "-" + (currentdate.getDate());
var chartIds=["chartContainer1","chartContainer2","chartContainer3"];
var url = ["fsadailyrequest","fsadailyrevenue","fsadailyprofitmargin"];

function parsedata(data){
        var keys = Object.keys(data);  
        var accounting = [];
        for (var i in keys)
        {
	 var datetime = keys[i];
         //var res = datetime.split(" ");
         //var date = res[0].split("-");
         //var time = res[1].split(":");
         //accounting.push({x :new Date(date[0],date[1],date[2]), y: data[keys[i]] });
         accounting.push({x :new Date(keys[i]), y: data[keys[i]] });
        }
 return accounting;
}

 for(var i=0;i <chartIds.length;i++){ 
(function(i) {
$.ajax({
  dataType: "json",
  url: "http://10.33.10.193:25916/service/resource/"+url[i]+"?starttime="+stime+"&endtime="+etime,
  type: 'GET',
  success: function(data){
	console.log(data);
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
        lineThickness: 0,
        gridColor: "#585858" ,         
        valueFormatString: "YY-MM-DD", 
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
        xValueType: "date",
        dataPoints: accounting
	} 
	]
});

    chart.render();
}

