
    var countServerStatus={};
$( document ).ready(function() {
var mData; 
$.ajax({
                url : "http://10.47.3.204/services/application/appids",
                dataType : "json",
                timeout : "3000",
                

                success : function(data) {
                createTable(data);
}
});
});

function createTable(data){
        var rotationTableData=data;
	var eachTable=["brandTable","plaTable","fsaTable"];
 	var keys = Object.keys(rotationTableData);
    if(keys.length>0){
    	for(var noOfKey=0;noOfKey<keys.length;noOfKey++){
    		console.log(keys[noOfKey]);
    		if(rotationTableData[keys[noOfKey]].length>0){
    			var reqId='#'+eachTable[noOfKey];
    			for(var i=0;i<rotationTableData[keys[noOfKey]].length;i++){
    				var mId=rotationTableData[keys[noOfKey]][i];
    			var tr = $('<tr/>');
                tr.append("<td align = left style='border-bottom: 4px solid #cfcfcf;color:#0F6CB2;' ><a  href='javascript:void(0);' id='mId' onclick='showDetailStatus(this)'>" + rotationTableData[keys[noOfKey]][i] + "</a></td>");
                tr.append("<td align = left style='border-bottom: 4px solid #cfcfcf;'><button type='button' class='btn btn-success' style='pointer-events: none;' ><span id="+'up'+rotationTableData[keys[noOfKey]][i]+" class='badge'></button></span>"+"<button type='button' class='btn btn-danger' style='pointer-events: none;'> <span id="+'down'+rotationTableData[keys[noOfKey]][i]+" class='badge'></span></button></td>");
                $(reqId).append(tr);
                getApplIDInfo(rotationTableData[keys[noOfKey]][i],1);
                

    			}
    		
    		}    		
    	}
    } 

 }
    

function getApplIDInfo(appname,mNum) {
	

$.ajax({
	url : "http://10.47.3.204/services/application?app_id=" + appname,
	dataType : "json",
	timeout : "3000",
	async:false,
	success : function(appIDdata) {
		getAppInstIaas(appname, appIDdata);

		//console.log("in 1 "+count);

			}
	});
//return mR;

}	

//Main Function which gets the json object for the given appID from the iaas
//server which
//returns the ip address for all the instances having the instance group
//Here we call the end point written in dropizard servers which returns the
//object

function getAppInstIaas(appname, appIDdata) {
	var assArray = {};
	//var appobj = clusters[appname][0];
	$.ajax({
		url : "http://10.47.3.204/services/requestjson?appId="  + appname,
		dataType : "json",
		timeout : "3000",
		async:false,

		success : function(data) {
			var asArray = new Object;
			var instanceGp;
			// var numOfDownServer=0;
			// var numOfUpServer=0;
			for ( var noOfSystems in data) {
				// check if the object has instance group

				if (data[noOfSystems].hasOwnProperty('instance_group')) {
					instanceGp = (data[noOfSystems].instance_group);
					if (instanceGp in asArray) {
						asArray[instanceGp].push({primary_ip: data[noOfSystems].primary_ip,  id:data[noOfSystems].id});
					} else {
						asArray[instanceGp] = new Array;
						asArray[instanceGp].push({primary_ip: data[noOfSystems].primary_ip,  id:data[noOfSystems].id});

					}
				}
			}

			for ( var grp in asArray) {
				
				var cnt = 0;
				if (Object.keys(asArray[grp]).length != 0) {	
				var numOfUpServer=0;
				var numOfDownServer=0;				
				checkhealth(asArray[grp], grp, appname, appIDdata,numOfUpServer,numOfDownServer);
				

			}
               
				//console.log("count"+count);
			

		}

//console.log($('#prod-fk-ad-adserver').html());

		// var test = $('#prod-fk-ad-adserver').val();
		// alert("test :"+test);
		//alert(document.getElementById('prod-fk-ad-adserver').value;
		
		
	},
		error : function(jqXHR, textStatus, errorThrown) {
			alert("You can not send Cross Domain AJAX requests : "
				+ errorThrown);
		}
	});

}

// Function Checks for the health of each server
// If the health port is not defined in the local json file then we
// show it as orange colour, if its unhealthy show it as red and for health
// system show it as green
function checkhealth(instanceIP, instanceGp, appname, appIDdata,up,down) {
	var noOfGrps = Object.keys(appIDdata[appname]);
	 var availGrps = [];
	for (grpno in noOfGrps) {
		availGrps.push(Object.keys(appIDdata[appname][grpno]));
		}

	var configAvailable = false;
	for (grp in availGrps) {
		var extractString = instanceGp.substring(0, String(availGrps[grp]).length)
		if (extractString == availGrps[grp]) {
			configAvailable = true
			var i = 0;
			var instGrpPort = (appIDdata[appname][grp][extractString].port);
			var instGrpUrl = (appIDdata[appname][grp][extractString].url);
			for (i; i < instanceIP.length; i++) {
				var appurl = makeappURL(instanceIP[i].primary_ip, instGrpPort, instGrpUrl);
				makeajax(appurl, instanceIP[i].primary_ip, "#" + appname + instanceGp
					+ "_" + [ i ],appname,up,down,onSuccess);
			}
			
			
			

		}


	}
	


	// Making the colour of cell as orange if the health check port not there in
	// local json
	// if (configAvailable == false) {
	// 	for (var i = 0; i < instanceIP.length; i++) {

	// 		$("#" + appname + instanceGp + "_" + [ i ]).toggleClass('orange',
	// 			true);
	// 		$("#" + appname + instanceGp + "_" + [ i ]).toggleClass('red',
	// 			false);

	// 	}
	// }
	//alert("last" +numOfUpServer);

}

// Function to send a health request
function makeajax(url, item, id,appname,up,down,callBackFun) {
	
	var request = $.ajax({
		cache : false,
		url : url,
		dataType : "script html",
		crossDomain : "false",
		async:false,

		complete : function(xhr, statusText) {
		 if (xhr.status === 200) {
		 	var status=1;
		 	console.log("mUrl:"+url);
		 	callBackFun.call(this,appname,status,url);
		 	// up=up+1;
		 	// console.log("Up"+up);
		 	// var ID="up"+appname;
		 	// document.getElementById(ID).textContent=up;
	    }
		},

		error : function(jqXHR, textStatus, errorThrown) {
			var status=0;
			console.log("mUrl:"+url);
			callBackFun.call(this,appname,status,url);
			// down+=1;
			// var ID="down"+appname;
			// document.getElementById(ID).textContent=down;
		}

	});
	//console.log("k"+numOfUpServer);

}

function makeappURL(app, port, url) {

	return "http://" + app + ":" + port + "/" + url;
}


function onSuccess(appID,status,url) {
	console.log("app id: "+appID);
	console.log("status: "+status);
	
	if(appID=="fk-adiquity-serve"){
		console.log("adserve:" +url);

	}
	if(status==1){
	var ID="up"+appID;
	var mElement=document.getElementById(ID);
	var number=mElement.textContent;
	number++;
	mElement.textContent=number;
}
else{
	var ID="down"+appID;
	var mElement=document.getElementById(ID);
	var number=mElement.textContent;
	number++;
	mElement.textContent=number;
}

}

function showDetailStatus(el){
	//var a=document.getElementById('mId').textContent;
var data= el.parentNode.parentNode.cells[0].textContent;
$("#mainTable").html("");	
console.log("clicked:"+data);
getClickedInfo(data);
$("#detailInfo").css("display", "block");
	//getApplIDInfo()
//	 $("#detailInfo").slideToggle();
  //       return false;

}


function closeDetailDiv(){
console.log("closed");
$("#detailInfo").slideToggle();
  return false;
}
