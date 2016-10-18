//Function is used to populate the obj variable which retrieves the data by making an ajax call 
//to the end point written in dropwizard. obj is declared globally so that it can be used at other
//places
//var obj={};
var loadMetric={
	Brand:["brandTicker.html","http://10.47.4.24/dashboard/script/l0brand_tech.js"],
	Pla:["plaTicker.html","http://10.47.4.24/dashboard/script/l0pla_tech.js"],
	Fsa:["fsaTicker.html","http://198.143.129.138:3000/dashboard/db/adiquity-data-kafka-metrics-adq-kafka-none"],
	Other:["otherCategory.html","otherCategory.html"]
};
var searchDashboard=[];
var obj;
var objectToLoadUrl={};
function mFun(){
	var dropdown = document.querySelectorAll('.dropdown');
	var dropdownArray = Array.prototype.slice.call(dropdown, 0);
	dropdownArray.forEach(function(el) {
		//alert("onclick1");
		var button = el.querySelector('a[data-toggle="dropdown"]'),
			menu = el.querySelector('.dropdown-menu'),
			arrow = button.querySelector('i.icon-arrow');

		button.onclick = function(event) {
			//alert("onclick");
			if (!menu.hasClass('show')) {
				menu.classList.add('show');
				menu.classList.remove('hide');
				arrow.classList.add('open');
				arrow.classList.remove('close');
				event.preventDefault();
			} else {
				menu.classList.remove('show');
				menu.classList.add('hide');
				arrow.classList.remove('open');
				arrow.classList.add('close');
				event.preventDefault();
			}
		};
	});

	Element.prototype.hasClass = function(className) {
		return this.className && new RegExp("(^|\\s)" + className + "(\\s|$)").test(this.className);
	};
}


makeajax();
function makeajax() {
	$.ajax({
		cache : false,
		dataType : "json",
		url : "http://10.47.3.204/services/dashboard/new",
		asyc : false,
		crossDomain : "false",
		type : 'GET',

		complete : function(xhr, statusText) {
			if (xhr.status === 200) {
				obj = xhr.responseJSON;
				createSideBar(xhr.responseJSON);
				loading(obj);
				$('#sidebar-ul').on('click', '.link', function(e) {
					e.preventDefault();
					var sidebarKey = $(this).attr("href");
					insertHistory(1, xhr.responseJSON, sidebarKey);
				});
			}
		}
	});
}

//check for empty
function isArrayEmpty(array) {
	return array.filter(function(el) {
			return !jQuery.isEmptyObject(el);
		}).length === 0;
}
//creating categorized submenu
function createSideBar(obj) {
	var keys = Object.keys(obj);
	if(keys.length>0){
		var output= " ";
		for(var noOfKey=0;noOfKey<keys.length;noOfKey++){
			if( typeof (obj[keys[noOfKey]])!="object"  ) {

				output += '<li >' + '<a href="#"   data-toggle="dropdown">' + obj[keys[noOfKey]] + '</a>' + '</li>';


			}
			else if(Object.keys(obj[keys[noOfKey]]).length==0){
				output += '<li >' + '<a href="'+'/#'+keys[noOfKey]+'"   data-toggle="dropdown">' +keys[noOfKey]+ '</a>' + '</li>';

			}else{
				//alert("else");
				output += '<li class="dropdown" onclick="mFun()">' + '<a  class="link" href="'+'/#'+keys[noOfKey]+'"   data-toggle="dropdown">' +keys[noOfKey] + '<i class="icon-arrow">'+'</i></a>';

				var subKeys=Object.keys(obj[keys[noOfKey]]);

				if(subKeys.length>0){
					output+='<ul class="dropdown-menu">';
					for(var noOfSubKey=0;noOfSubKey<subKeys.length;noOfSubKey++){
						if(isArrayEmpty(obj[keys[noOfKey]][subKeys[noOfSubKey]])==true){
							//output+='<li><a style="padding-left: 15px;" href="#">'+subKeys[noOfSubKey]+'</a></li>';

						}
						else
						{
							output+='<li class="dropdown" onclick="mFun()"><a style="padding-left: 15px;" href="#" data-toggle="dropdown">'+subKeys[noOfSubKey]+'<i class="icon-arrow" style="color:black;">'+'</i></a>';

							var greatGrandSubKey=obj[keys[noOfKey]][subKeys[noOfSubKey]];
							if(greatGrandSubKey.length>0){
								output+='<ul class="dropdown-menu" >';
								for(var i=0;i<greatGrandSubKey.length;i++)

								{   objectToLoadUrl[greatGrandSubKey[i].key]=greatGrandSubKey[i].url;
									searchDashboard.push({data:'#'+greatGrandSubKey[i].key,value:greatGrandSubKey[i].display});
									output+='<li >'+'<a  class="link searchMe"  style="padding-left: 24px;" href="'+'/#'+greatGrandSubKey[i].key +'"'+' >'+greatGrandSubKey[i].display+'</a></li>';

								}
								output+='</li>';
							}

							output+='</ul>';
						}
						output+='</li>';
					}
					output+='</ul>';
				}

				output+='</li>';
			}


		}
		document.getElementById("sidebar-ul").innerHTML = output;

	}





}

// Function to push the history in the history Stack, replace the current
// history url and also takes care of moving back and forward
var index = 0;
function insertHistory(rel, json_obj, sidebarKey) {
	console.log("insert history");
	// Store the initial content so we can revisit it later
	history.replaceState(index, 'the number is 0', null);
	console.log("replace state"+history.replaceState(index, 'the number is 0', null));
//var sidekey = sidebarKey.split("#");
//if(sidebarKey=="#admin-html"){
//
//	history.pushState(rel, 'rel is ' + rel, '/' + sidebarKey);
//}else{
//	console.log("inside history else");
	history.pushState(rel, 'rel is ' + rel,   sidebarKey);
//}
	var sidekey = sidebarKey.split("#");
	if(loadMetric.hasOwnProperty(sidekey[sidekey.length - 1])){
		loadMultipleRightFrame(loadMetric[sidekey[sidekey.length - 1]]);
	}
	else if(sidekey[sidekey.length - 1]=="homePage"){
		location.href="index.html";
	}
	else if(sidekey[sidekey.length - 1]=="admin-html"){
		console.log("inside else if for admin.html");
		loadRightFrame("admin.html");
	}
	else {
		loadRightFrame(objectToLoadUrl[sidekey[sidekey.length - 1]]);
	}

	// Revert to a previously saved state
	window.addEventListener('popstate', function(event) {
		console.log("back"+event);
		var new_link;
		var currentpage = window.location.href
		var pagekey = currentpage.split("#");
		console.log("key while back"+pagekey);
		// This check is done because the first page which is hosted will not
		// have the hash key

		if (currentpage.indexOf("#") == -1) {

			location.href="index.html";

		}
		else if(loadMetric.hasOwnProperty(pagekey[pagekey.length - 1])){
			//alert("load metrics"+pagekey[pagekey.length - 1]);
			loadMultipleRightFrame(loadMetric[pagekey[pagekey.length - 1]]);

		}
		else if(pagekey[pagekey.length - 1]=="homePage"){
			location.href="index.html";
		}
		else if(pagekey[pagekey.length - 1]=="admin-html"){
			new_link="admin.html";
			loadRightFrame(new_link);
		}
		else {
			new_link = objectToLoadUrl[pagekey[pagekey.length - 1]];
			loadRightFrame(new_link);
		}
		console.log("new link"+new_link);

	});

}

// Function is called on the onload of iframe, to take care of the loading
// the page for the key given in the url
function loading(load_obj) {
	console.log("direct load");
	var loadpage = window.location.href
	var open_page = loadpage.split("#")
	var key_value = (open_page[open_page.length - 1]);
	//alert("#key"+key_value);
	console.log("key: "+key_value);
	if (loadpage.indexOf("#") == -1) {
		$(".frameDiv").remove();
		$("#main iframe").remove();
		console.log("base url"+loadpage);
		var mUrl="ticker.html";
		var div=document.getElementById("main");

		//div.removeChild('main-content');
		var iframe = document.createElement('iframe');
		iframe.frameBorder=0;
		iframe.width="100%";
		iframe.height="50%";
		iframe.id="main-content";
		iframe.setAttribute("src", mUrl);
		div.appendChild(iframe);

	}
	else if(loadMetric.hasOwnProperty(key_value)){
		loadMultipleRightFrame(loadMetric[key_value]);
	}
	else if(key_value=="homePage"){
		location.href="index.html";
	}
	else if(key_value =="admin-html"){
		loadRightFrame("admin.html");
	}
	else{


		open_url = objectToLoadUrl[key_value];
		console.log("loading else");
		//	open_url="http://10.47.3.204/"+open_url;
		console.log("kObject"+open_url);
		loadRightFrame(open_url);
	}

}
//load multiple metrics
function loadMultipleRightFrame(loadframes){
	var mainDiv=document.getElementById("main");
	$("#main .frameDiv").remove();
	$("#main iframe").remove();

	for(var j= 0,h=40;j<loadframes.length;j++){
		//alert(loadframes[j]);
		var div = document.createElement('div');
		div.setAttribute("style","height:"+h+"%");
		div.setAttribute("class","frameDiv");
		//div.width="100%";
		//div.height="50%";
		var iframe = document.createElement('iframe');
		iframe.frameBorder=0;
		iframe.width="100%";
		iframe.height="100%";
		//iframe.id="main-content";
		iframe.setAttribute("src", loadframes[j]);
		div.appendChild(iframe);
		mainDiv.appendChild(div);
		h=h+20;
	}
}
// Function is used to open the page in right frame
function loadRightFrame(url) {
	console.log("frame url: "+url);
	var div=document.getElementById("main");

	$("#main .frameDiv").remove();
	$("#main iframe").remove();
	//div.removeChild('main-content');
	var iframe = document.createElement('iframe');
	iframe.frameBorder=0;
	iframe.width="100%";
	iframe.height="100%";
	iframe.id="main-content";
	iframe.setAttribute("src", url);
	div.appendChild(iframe);
//	//frameObj.src=url;

	//$("#main").html('<object width=100% height=100% data="' + url + '"' + '/>');
}

$(document).ready(function() {
	//$("#searchbox").keyup(function() {

	// setup autocomplete function pulling from currencies[] array
	$('#autocomplete').autocomplete({

		lookup: searchDashboard,
		onSelect: function (suggestion) {
			//var thehtml = suggestion.value;
			//$('#outputcontent').html(thehtml);
			insertHistory(1, obj, suggestion.data);
		}

		//});

		//var filter = $(this).val();
		////("");
		////console.log(filter);
		//$(".dropdown").each(function() {
		////console.log($(this).text());
		//	if ($(this).text().search(new RegExp(filter, "i")) < 0) {
		//		//console.log("reached inside "+ $(this).text());
		//		$(this).fadeOut();
		//	} else {
		//		console.log($(this).text());
		//		$(this).parent().parent().parent().parent().show();
		//		$(this).show();
		//
		//	}
		//});

	});

});



//add delete dashboard
function addOrDelete(){
	insertHistory(1, obj, "/#admin-html");
//{  alert("add or delete");
//	var mIframe=document.getElementById('main-content');
//	mIframe.src='admin.html';

	//$("#main").html('<object width=100% height=100% data="' + 'admin.html' + '"' + '/>');
}



//to load home page
function loadHomePage(){
	console.log("inside load home");
	insertHistory(1, obj, "#homePage");

}

