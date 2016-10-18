/*This function is called on submit of insert, check if all the feilds are provided n if not will 
 * pop up the alert. Upon submit it also makes an ajax call for inserting the data  in DB which 
 * call the end point written in dropwizard
 *
 *
 */
var bussinessData;
var moduleData;
$( document ).ready(function() {
	console.log("in admin page");
	$.ajax({
		cache: false,
		dataType: "json",
		url: "/services/dashboard/products",
		asyc: false,
		crossDomain: "false",
		type: 'GET',

		complete: function (xhr, statusText) {
			if (xhr.status === 200) {
				obj = xhr.responseJSON;
				bussinessData=obj;
				var opt = document.createElement('option');
				opt.value = 0;
				opt.innerHTML = "select bussiness";
				document.getElementById('business').appendChild(opt);
				//console.log(obj);
				for (key in obj) {
					if (obj.hasOwnProperty(key)) {
						var opt = document.createElement('option');
						opt.value = key;
						opt.innerHTML = obj[key];
						document.getElementById('business').appendChild(opt);
						//console.log(key + " = " + obj[key]);
					}
				}


			}
		}
	});
	$.ajax({
		cache: false,
		dataType: "json",
		url: "/services/dashboard/modules",
		asyc: false,
		crossDomain: "false",
		type: 'GET',

		complete: function (xhr, statusText) {
			if (xhr.status === 200) {
				obj = xhr.responseJSON;
				moduleData=obj;
				//	var moduleArray=obj[]
				console.log(obj);


			}
		}
	});

	$('#newModule').hide();
	$('#removeAddBox').hide();
	$('#btn_s').click(function(e){
		e.preventDefault();
		var moduleValue;
		if($('#module').is(':disabled')===true){
			moduleValue=$("#newModule").val()

		}
		else
		{
			moduleValue=$('#module').val();
		}
		//alert(moduleValue);
		//alert($('#module').val());

		var Key = $('#Key'), URL = $('#URL'), Display = $('#Display');
		radioval = $('#Insert:checked').val();


		// check if all fields is not empty
		if (radioval == "Insert") {
			if ($('#business').val()===null|| $('#business').val()===0||moduleValue===null || Key.val() === '' || URL.val() === ''
				|| Display.val() === '') {
				// ev.preventDefault(); // prevent form submit
				alert('All fields are required.'); // alert
				return false;
				// message
			} else {
				//inserturl = "/services/dashboard/insert?key="
				//	+ Key.val()
				//	+ "&url="
				//	+ encodeURIComponent(URL.val())
				//	+ "&display=" + Display.val();
				inserturl="/services/dashboard/new/insert";
				newDashboard={};
				newDashboard["productid"]=$('#business').val();
				if($('#module').is(':disabled')===true){
					newDashboard["moduleid"]="-";
					newDashboard["newModuleName"]=moduleValue;
				}
				else{
					newDashboard["newModuleName"]="-";
					newDashboard["moduleid"]=moduleValue;
				}


				newDashboard["dash_key"]=Key.val();
				newDashboard["url"]=URL.val();
				newDashboard["display"]=Display.val();
				//alert(newDashboard);

				makeajax(inserturl,newDashboard);
			}
		}

	})
});





/*This function is an ajax call for updating and inserting, "jsonp" is taken as datatype to avoid 
 * the cross domain errors
 */
function makeajax(url,newDashboard) {
	console.log(newDashboard);
	var a = $.ajax({
		cache : false,
                dataType : "json",
                url : url,
                type : "POST",
                data : JSON.stringify(newDashboard),
                contentType: 'application/json',
                complete : function(xhr, statusText) {
                console.log(xhr.responseText);
                alert(xhr.responseText);
		}
	});
}

/*This function is to dispaly the curent dashborads on the delete page for the user to view what
 * all pages are available in thundering rhino dashborad. It makes an Read ajax call to the endpoint 
 * written in dropwizard 
 */
function getResults(elem) {
	if (elem.checked && elem.value == "Delete") {

		$("#Key").hide();
		$(".ins").hide();
		$("#innerDiv").hide();
		$(".helpText").hide();
		$.ajax({
			cache : false,
			dataType : "json",
			url : "/services/dashboard/new/dashboardurl",
			asyc : false,
			crossDomain : "false",
			type : 'GET',
			complete : function(xhr, statusText) {
				if (xhr.status === 200) {
					obj = xhr.responseJSON;
					$("#table td").parent().remove();
					var th = $('<tr/>');
					th.append("<td align = left style='color: red;border-bottom: 4px solid #cfcfcf;'> KEY </td>");
					th.append("<td align = left style='color: red;border-bottom: 4px solid #cfcfcf;' > URL</td>");
					th.append("<td align = left style='color: red;border-bottom: 4px solid #cfcfcf;'> DISPLAY </td>");
					$("#table").append(th);
					for ( var prop in obj) {
						var tr = $('<tr/>');
						tr.append("<td align = left style='border-bottom: 6px solid #cfcfcf;'>" + prop + "</td>");
						tr.append("<td align = left style='border-bottom: 6px solid #cfcfcf;'>" + obj[prop].url
							+ "</td>");
						tr.append("<td align = left style='border-bottom: 6px solid #cfcfcf;'>"
							+ obj[prop].display + "</td>");
						tr.append("<td style='border-bottom: 6px solid #cfcfcf;'> <input type=button  style='background: red;color: white;border: none;border-radius: 1px' class=button value= DELETE > </td>");

						$("#table").append(tr);

					}

				}
			}

		});

	} else {
		$("#innerDiv").show();
		$(".ins").show();
		$(".helpText").show();
		$("#table tr").parent().remove();
	}

}

/*when the delete button is clickecd on the delete page this is called for updating the Db to
 * update exist feild as False
 * 
 */
$("table").on("click", '.button', function() {
	console.log("delete");
	var passPara={};
	key = $(this).closest("tr").find('td:eq(0)').text();
	if (confirm("Are you sure you want to delete this?")) {
		updateurl = "/services/dashboard/new/delete?key=" + key;
		makeajax(updateurl,passPara);
	}
	radobj = document.getElementById('Delete');
	getResults(radobj);
});

function addModule(){
//alert("hii");
	$("#add_module").hide();
	$('#spanForAdd').hide();
	$("#newModule").show();
	$('#removeAddBox').show();
	$("#module").prop("disabled", true);
}


// drop down option clicked
function createModuleDropDown(){
	var productKey = document.getElementById("business").value;
	console.log("hi"+productKey);
	console.log("mData"+moduleData);
	$('#module').find('option').remove();
	if (moduleData.hasOwnProperty(productKey)) {
		for(var i=0;i<moduleData[productKey].length;i++){
			var opt = document.createElement('option');
			opt.value = moduleData[productKey][i]['moduleid'];
			opt.innerHTML = moduleData[productKey][i]['modulename'];
			document.getElementById('module').appendChild(opt);

		}


	}

}

function removeAddModule(){
	$("#newModule").hide();
	$('#removeAddBox').hide();
	$('#spanForAdd').show();
	$('#add_module').show();
	$("#module").prop("disabled", false);
	$('#add_module').attr('checked', false);
}
