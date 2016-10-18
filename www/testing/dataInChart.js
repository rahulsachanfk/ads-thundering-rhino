var currentdate = new Date();
var etime = currentdate.getFullYear() + "-" + (currentdate.getMonth()+1)  + "-" + currentdate.getDate() + " " + currentdate.getHours() + ":00:00";
var stime = currentdate.getFullYear() + "-" + (currentdate.getMonth()+1)  + "-" + (currentdate.getDate()-1) + " " + currentdate.getHours() + ":00:00";
var chartIds=["chartContainer1","chartContainer2","chartContainer3"];
var url1 = ["desktopintervalrequests","desktopintervalfillrate","desktopintervalactionrate"];
var url2 = ["appintervalrequests","appintervalfillrate","appintervalactionrate"];
var baseurl = "http://10.33.10.193:25916/services/ticker/";
var period ="?starttime="+stime+"&endtime="+etime;

$(document).ready(function() {
    $.ajax({
        dataType: "json",
        url: "http://10.33.10.193:25916/services/ticker/livecamp",
        type: 'GET',
        success: function(data){
            document.getElementById("modalContent").innerHTML="App Campaign : "+data['appCamp']+"<br>"+"Desktop Campaign : "+data['desktopCamp']+"<br>";


    }});

});

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
$.ajax({
  dataType: "json",
  url: baseurl+url1[i]+period,
  type: 'GET',
  success: function(data){
  	$.ajax({
  	dataType: "json",
  	url: baseurl+url2[i]+period,
  	type: 'GET',
  	success:function(data1){
	renderChart(data,data1,i);
	}
	});
}
});
})(i);
}


function renderChart(data,data1,i){
  var accounting = parsedata(data);
  var accounting1 = parsedata(data1);
 
    var chart = new CanvasJS.Chart(chartIds[i],
    {
        backgroundColor: "#1F1F1F",
        axisX: {
labelFontColor: "#ffffff",
        gridThickness: 1,
        interval:2,
        lineThickness: 0,
        gridColor: "#585858" , 
        intervalType: "hour",        
        valueFormatString: "hh TT DD-MM-YY", 
        labelAngle: -20
      },
      axisY:{
labelFontColor: "#ffffff",
        gridColor: "#585858" ,
        gridThickness: 1,
        lineThickness: 0
      },
toolTip: {
        shared: true
      },
       data:[
      {
        type: "line",
        lineThickness: 1,
        xValueType: "dateTime",
toolTipContent: "<span style='\"'color:#659EBF;'\"'> Desktop- {x}: {y}</span> ",        
dataPoints: accounting
	},
        {
        type: "line",
        lineThickness: 1,
        xValueType: "dateTime",
toolTipContent: "<span style='\"'color:#A73B4F;'\"'>App- {x}: {y}</span> ",
        dataPoints: accounting1
        }
	]
});

    chart.render();
}

