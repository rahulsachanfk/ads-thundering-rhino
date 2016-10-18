/**
 * Created by suchi.sethi on 21/08/15.
 */

// Function to read the json file for port and Health check for all the gropus
//var appobj = getappinfo(appinput);
//var clusters = appobj.clusters[0];
// Get the Key value from the query string
var currenturl = window.location.href;
var queryStringArray = currenturl.split('?');
var queryStringValue = queryStringArray[1].split("=");
var appname = queryStringValue[1];


//Function to get the json object for the health port and health check url
//from the database

getApplIDInfo(appname);

function getApplIDInfo(appname) {
$.ajax({
	url : "/services/application?app_id=" + appname,
	dataType : "json",
	timeout : "3000",
	success : function(appIDdata) {
		getAppInstIaas(appname, appIDdata)
			}
	});

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
		url : "/services/requestjson?appId="  + appname,
		dataType : "json",
		timeout : "3000",

		success : function(data) {
			var asArray = new Object;
			var instanceGp;
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
			}
			var InsGrpApi = Object.keys(asArray);
			html = "<table border='1' id=" + appname
				+ " style='width:100%'>";

			$(mainTable).append(html);
			html = "<tr  style='width:100%'><th colspan='10'  bgcolor='#333333' align='center'>"
				+ appname + "</th></tr>";
			$(mainTable).append(html);

			for ( var grp in asArray) {
				var cnt = 0;
				if (Object.keys(asArray[grp]).length != 0) {
					html = "<tr id =" + grp
						+ " style='width:100%' ><th >" + grp + ' IP/ID '
						+ "</th></tr>";
					$(mainTable).append(html);
					var oldSvcGPId;
					for (var i = 0; i < Object.keys(asArray[grp]).length; i++) {

						if ((i % 4) == 0) {
							// alert()
							cnt = cnt + 1;
							if (cnt == 1) {
								$("#" + grp).last().after(
									"<tr id=" + grp + "_" + cnt
									+ "></tr>");
								oldSvcGPId = cnt
							} else {

								$("#" + grp + "_" + oldSvcGPId).last()
									.after(
									"<tr id=" + grp + "_"
									+ cnt
									+ "></tr>");
								oldSvcGPId = cnt
							}
						}
						html = "<td id=" + appname + grp + "_" + [ i ]
							+ " style='width:10%' class= 'red'>"
							+ asArray[grp][i].primary_ip + ' / ' + asArray[grp][i].id  + "</td>";
						$("#" + grp + "_" + cnt).append(html)
					}

				}

				checkhealth(asArray[grp], grp, appname, appIDdata);

			}

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
function checkhealth(instanceIP, instanceGp, appname, appIDdata) {
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
			var instGrpPort = (appIDdata[appname][grp][extractString].port);
			var instGrpUrl = (appIDdata[appname][grp][extractString].url);
			for (var i = 0; i < instanceIP.length; i++) {
				var appurl = makeappURL(instanceIP[i].primary_ip, instGrpPort, instGrpUrl);
				makeajax(appurl, instanceIP[i].primary_ip, "#" + appname + instanceGp
					+ "_" + [ i ]);
			}

		}

	}
	// Making the colour of cell as orange if the health check port not there in
	// local json
	if (configAvailable == false) {
		for (var i = 0; i < instanceIP.length; i++) {

			$("#" + appname + instanceGp + "_" + [ i ]).toggleClass('orange',
				true);
			$("#" + appname + instanceGp + "_" + [ i ]).toggleClass('red',
				false);

		}
	}

}

// Function to send a health request
function makeajax(url, item, id) {
	var request = $.ajax({
		cache : false,
		url : url,
		dataType : "script html",
		crossDomain : "false",
		complete : function(xhr, statusText) {
			if (xhr.status === 200) {
				$(id).toggleClass('green', true);
				$(id).toggleClass('red', false);
			}
		},

		error : function(jqXHR, textStatus, errorThrown) {
			html = "<td class='red'>" + item + "</td>";
			$(id).append(html);
		}
	});
}

function makeappURL(app, port, url) {

	return "http://" + app + ":" + port + "/" + url;
}
