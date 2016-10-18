var valueOfcmpId;
var valueOfafforExch;
var valueOfdate;


var config = {
'host':'108.178.60.18',
 'port':'25916'
};

$(document).ready(function() {

 $('#nodeContent').hide();
 $('#divForUU').hide();
 var d = new Date();
 var currentYear = d.getFullYear();
 $("#dateId").datepicker({
  dateFormat: 'yy-mm-dd',
  maxDate: new Date,
  yearRange: "1930:" + currentYear,
  changeMonth: true,
  changeYear: true,
  defaultDate: new Date()
 });

 $.ajax({
  url: "http://"+config.host+":"+config.port+"/services/funnel/aff_list",
  type: "GET",
  cache: false,
  dataType: 'json',
  success: function(data, textStatus, xhr) {
   if (xhr.status === 204) {
   } else {
    // Create a new <option> element.
    var dataList = document.getElementById('ids');
    for (var i = 0; i < data.length; i++) {
     var option = document.createElement('option');
     option.value = data[i];
     dataList.appendChild(option);
    }
   }
  },
  error: function(status, exception) {
   console.log("Internal Error in  drop down API for affiliate ids");
  }
});
});







function inputValidation() {
  $("#alertMessage").html('');
  $("#myChart").hide();
  $('#nodeContent').hide();
  $('#divForUU').hide();

 //var validator = RegExp('^([0-9]+\.$)');
 var validator = /^\d+(\.\d{1,2})?$/i;
 var dateformat = /^(\d{4})(\/|-)(\d{1,2})(\/|-)(\d{1,2})$/;
 valueOfcmpId = document.getElementById("cmpId").value;
 valueOfafforExch = document.getElementById("affIdOrExchId").value;
 valueOfdate = document.getElementById("dateId").value;
 if (valueOfafforExch) {
  var index = valueOfafforExch.indexOf("-");
  if (index > -1) {
   valueOfafforExch = valueOfafforExch.substring(0, index);
  }
 }

 //if (valueOfcmpId.match(validator) == null || !valueOfcmpId) {
 // document.getElementById("cmpId").style.border = "1px solid red";
 // $('#cmpId').next('span').remove();
 // $('#cmpId').val('');
 // $("#cmpId").after(' <span style="color:red;">Enter correct value</span>');
 //
 //
 //} else {
 // $('#cmpId').next('span').remove();
 // document.getElementById("cmpId").style.border = "1px solid blue";
 //}

 if ((valueOfafforExch.match(validator) == null || !valueOfafforExch) && (valueOfcmpId.match(validator) == null || !valueOfcmpId)) {
  document.getElementById("affIdOrExchId").style.border = "1px solid red";
  document.getElementById("cmpId").style.border = "1px solid red";
  $('#affIdOrExchId').next('span').remove();
  $('#affIdOrExchId').val('');
  $('#affIdOrExchId').after(' <span style="color:red;">Enter Cmpid/Affid value</span>');
 } else {
  $('#affIdOrExchId').next('span').remove();
  document.getElementById("affIdOrExchId").style.border = "1px solid blue";
  document.getElementById("cmpId").style.border = "1px solid blue";
 }
 if (valueOfdate.match(dateformat) == null || !valueOfdate) {
  document.getElementById("dateId").style.border = "1px solid red";
  $('#dateId').next('span').remove();
  $('#dateId').val('');
  $('#dateId').after(' <span style="color:red;">Enter correct value</span>');
 } else {
  $('#dateId').next('span').remove();
  document.getElementById("dateId").style.border = "1px solid blue";
 }

 if ((valueOfcmpId.match(validator) != null) && (valueOfafforExch.match(validator) != null) && (valueOfdate.match(dateformat) != null)) {

  //validating API
  $.ajax({
   url: "http://"+config.host+":"+config.port+"/services/funnel/validate?" + "cmpid=" + valueOfcmpId + "&affid=" + valueOfafforExch,
   type: "GET",
   cache: false,
   dataType: 'json',
   success: function(data, textStatus, xhr) {
    var jsonData = jQuery.parseJSON(JSON.stringify(data));
    console.log("data:"+jsonData['cmpid']);
    console.log(xhr.status);
    if (xhr.status === 204) {
     console.log("204 status for validating API");

    } else {
     if (jsonData['cmpid'] == 0 && jsonData['affid'] == 0) {
      $("#alertMessage").html('<h4 >Both Affiliate id and Campaign id are not valid...</h4>');
     } else if (jsonData['cmpid'] == 0) {
 
      $("#alertMessage").html('<h4>Campaign  id is not valid...</h4>');
     } else if (jsonData['affid'] == 0) {
 
      $("#alertMessage").html('<h4 >Affiliate  id is not valid...</h4>');
     } else {
     $("#alertMessage").html('');
      $.ajax({
    url: "http://"+config.host+":"+config.port+"/services/funnel/dspflag?" + "cmpid=" + valueOfcmpId + "&affid=" + valueOfafforExch + "&date=" + valueOfdate,
    type: "GET",
    cache: false,
    dataType: 'json',
    success: function(result, textStatus, xhr) {

     if (xhr.status === 204) {
      $("#alertMessage").html('<h4 >No content for above inputs</h4>');
      $("#myChart").hide();
      $('#divForUU').hide();
      $('#nodeContent').hide();
     } else {
      $("#alertMessage").html(" ");
      renderInChart(result);
     }
    },
    error: function(status, exception) {
     
     console.log("getdata error "+exception);
     $('#nodeContent').hide();
     $('#divForUU').hide();
     $('#myChart').hide();
     if(status.status==400){
      $("#alertMessage").html('<h4> Campaign id and Affiliate id mismatch</h4>');
     }
     else{
       $("#alertMessage").html('<h4>Network error in fetching data for above inputs</h4>');
     }
    
    }
   });
     
     }
    }
   },
   error: function(status, exception) {
    console.log("Error  for validating API");


   }


  });


 }
 else if((valueOfcmpId.match(validator) != null) && (valueOfdate.match(dateformat) != null)){
  $("#alertMessage").html('');
  $.ajax({
   url: "http://"+config.host+":"+config.port+"/services/funnel/databycmpid?" + "cmpid=" + valueOfcmpId + "&date=" + valueOfdate,
   type: "GET",
   cache: false,
   dataType: 'json',
   success: function(result, textStatus, xhr) {

    if (xhr.status === 204) {
     $("#alertMessage").html('<h4 >No content for above inputs</h4>');
     $("#myChart").hide();
     $('#divForUU').hide();
     $('#nodeContent').hide();
    } else {
     $("#alertMessage").html(" ");
     renderInChart(result);
    }
   }
  });
 }
 else if((valueOfafforExch.match(validator) != null) && (valueOfdate.match(dateformat) != null)){
  $.ajax({
   url: "http://"+config.host+":"+config.port+"/services/funnel/databyaffid?" + "affid=" + valueOfafforExch + "&date=" + valueOfdate,
   type: "GET",
   cache: false,
   dataType: 'json',
   success: function(result, textStatus, xhr) {
    if (xhr.status === 204) {
     $("#alertMessage").html('<h4 >No content for above inputs</h4>');
     $("#myChart").hide();
     $('#divForUU').hide();
     $('#nodeContent').hide();
    } else {
     $("#alertMessage").html(" ");
     renderInChart(result);
    }
   }
  });
 }
 else {
  $("#myChart").hide();
  $('#nodeContent').hide();
  $('#divForUU').hide();
 }
}



function showDivOnNodeClick(data) {
 console.log(data);
 var jsonData = jQuery.parseJSON(JSON.stringify(data));
 var popUpData = '';
 for (var key in data) {
  if (data.hasOwnProperty(key)) {
   var val = data[key];
   popUpData += key + " : " + val + "<br>";
   //console.log(key +"  "+ val);
  }
 }
 console.log("req" + popUpData);
 $('#nodeContent').show();
 document.getElementById("nodeContent").innerHTML = popUpData;

}


function renderInChart(result) {
 $('#myChart').show();
 var levels;
 if (result['dspFlag'] == "true") {
  levels = 8;
 } else if (result['dspFlag'] == "false") {
  levels = 6;
 }

 var uniqueMatchedUser;
 var uniquePassedUser;
 uniqueMatchedUser = result['uuMatch'];
 uniquePassedUser = result['uuP'];
 if (uniqueMatchedUser | uniquePassedUser) {
  $('#divForUU').show();
  document.getElementById("divForUU").innerHTML = "Matched Unique Users: " + uniqueMatchedUser + "<br>" + "Qualified Unique Users :" + uniquePassedUser + "<br>";
 }

 var dataInput = [];
 var reqValues = [];
 var headingSet = [];
 var colorSet = ['#0e5a7e', '#166f99', '#2185b4', '#319fd2', '#3eaee2', '#75c8f0', '#a3dbf5', '#bae4f7'];
 if (levels == 6) {
  reqValues = [result['tReq'], result['mReq'], result['pReq'], result['imp'], result['cli'], result['cnv']];
  console.log("val:" + reqValues);
  headingSet = ['Total Requests', 'Matched Requests', 'Qualified Requests', 'Impressions', 'Clicks', 'Conversions'];

 } else {
  reqValues = [result['tReq'], result['mReq'], result['pReq'], result['bPlacedReq'], result['bWinReq'], result['imp'], result['cli'], result['cnv']];
  headingSet = ['Total Requests', 'Matched Requests', 'Qualified Requests', 'Bids Placed ', 'Bids Won', 'Impressions', 'Clicks', 'Conversions'];

 }
 for (var i = 0; i < levels; i++) {

  if (i == 2) {
   dataInput.push({
    "values": [reqValues[i]],
    "text": headingSet[i],
    "background-color": colorSet[i],
    "entry": {
     "values": [1],
     "labels": ["Click to get details of failed request"],
     "padding": 5,
     "font-size": 13
    }
   });
  } else {
   dataInput.push({
    "values": [reqValues[i]],
    "text": headingSet[i],
    "background-color": colorSet[i]
   });
  }

 }
 console.log("series:" + dataInput[2]['entry']);


 var myConfig = {
  "type": "funnel",



  "plot": {
   "max-entry": "20%",
   "min-exit": "12%",
   "value-box": {
    "text": "%v",
    "placement": "in",
    "font-color": "#fff",
    "font-family": "Georgia",
    "font-size": 16,
    "font-weight": "normal"

   },


   "tooltip": {
    "text": " %t :%v<br>",
    "font-family": "Georgia",
    "font-size": 12
   } //To adjust the appearance of your funnel tip.
  },

  "scale-y": {
   "placement": "opposite",
   "labels": headingSet,
   "item": {
    "font-color": "#999999",
    "font-family": "Georgia"
   },

  },
  "series": dataInput
 };

 zingchart.render({
  id: 'myChart',
  data: myConfig,
  height: 400,
  width: "100%"
 });
 zingchart.node_click = function(p) {
  console.log(p.plotindex);
  console.log(valueOfafforExch);
  console.log(valueOfcmpId);
  console.log(valueOfdate);
  if (p.plotindex == 2) {

   $.ajax({
    url: "http://"+config.host+":"+config.port+"/services/funnel/error?" + "cmpid=" + valueOfcmpId + "&affid=" + valueOfafforExch + "&date=" + valueOfdate,
    type: "GET",
    cache: false,
    dataType: 'json',
    success: function(data, textStatus, xhr) {
     if (xhr.status === 204) {
      $('#nodeContent').show();
      document.getElementById("nodeContent").innerHTML = "No content";

     } else {
      showDivOnNodeClick(data);
     }
    },
    error: function(status, exception) {
     $('#nodeContent').show();
     document.getElementById("nodeContent").innerHTML = "Network Error";

    }


   });
  }
 }
}