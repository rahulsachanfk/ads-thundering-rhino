//Function is used to populate the obj variable which retrieves the data by making an ajax call 
//to the end point written in dropwizard. obj is declared globally so that it can be used at other
//places
var obj;
makeajax();
function makeajax() {
	$.ajax({
		cache : false,
		dataType : "json",
		url : "/services/dashboard/",
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

// Function to create the sidebar dynamically. it will read the json file in
// order and create the side bar.
function createSideBar(obj) {
	var keys = Object.keys(obj);
	var out = "";
	for ( var prop in obj) {

		out += '<li>' + '<a class="link" href="' + '#' + prop + '"' + 'target='
				+ '"main"' + '>' + obj[prop].display + '</a></li>';
	}
	document.getElementById("sidebar-ul").innerHTML = out;

}

// Function to push the history in the history Stack, replace the current
// history url and also takes care of moving back and forward
var index = 0;
function insertHistory(rel, json_obj, sidebarKey) {
	// Store the initial content so we can revisit it later
	history.replaceState(index, 'the number is 0', null);

	history.pushState(rel, 'rel is ' + rel, '/' + sidebarKey);
	var sidekey = sidebarKey.split("#");
	loadRightFrame(json_obj[sidekey[sidekey.length - 1]].url);

	// Revert to a previously saved state
	window.addEventListener('popstate', function(event) {
		var new_link;
		var currentpage = window.location.href
		var pagekey = currentpage.split("#");
		// This check is done because the first page which is hosted will not
		// have the hash key

		if (currentpage.indexOf("#") == -1) {
			new_link = json_obj["home"].url;
		} else {
			new_link = json_obj[pagekey[pagekey.length - 1]].url;
		}

		loadRightFrame(new_link);
	});

}

// Function is called on the onload of iframe, to take care of the loading
// the page for the key given in the url
function loading(load_obj) {
	var loadpage = window.location.href
	var open_page = loadpage.split("#")
	var key_value = (open_page[open_page.length - 1]);
	if (loadpage.indexOf("#") == -1) {
		open_url = load_obj["home"].url;
	} else {
		open_url = load_obj[key_value].url;
	}
	loadRightFrame(open_url);
}

// Function is used to open the page in right frame
function loadRightFrame(url) {

	$("#main").html('<object width=100% height=100% data="' + url + '"' + '/>');
}

$(document).ready(function() {
	$("#searchbox").keyup(function() {
		var filter = $(this).val();
		$(".sidebar-menu li").each(function() {
			if ($(this).text().search(new RegExp(filter, "i")) < 0) {
				$(this).fadeOut();
			} else {
				$(this).show();
			}
		});

	});

});
