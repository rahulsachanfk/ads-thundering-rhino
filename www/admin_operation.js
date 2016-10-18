/*This function is called on submit of insert, check if all the feilds are provided n if not will 
 * pop up the alert. Upon submit it also makes an ajax call for inserting the data  in DB which 
 * call the end point written in dropwizard
 */






	$( document ).ready(function() {
		$('#btn_s').click(function(e){
			e.preventDefault();

			var Key = $('#Key'), URL = $('#URL'), Display = $('#Display');
			radioval = $('#Insert:checked').val();


					// check if all fields is not empty
					if (radioval == "Insert") {
						if (Key.val() === '' || URL.val() === ''
							|| Display.val() === '') {
							// ev.preventDefault(); // prevent form submit
							alert('All fields are required.'); // alert
							return false;
							// message
						} else {
							inserturl = "/services/dashboard/insert?key="
								+ Key.val()
								+ "&url="
								+ encodeURIComponent(URL.val())
								+ "&display=" + Display.val();
							makeajax(inserturl);
						}
					}

		})
	});





/*This function is an ajax call for updating and inserting, "jsonp" is taken as datatype to avoid 
 * the cross domain errors
 */
function makeajax(url) {
	var a = $.ajax({
		crossDomain : "false",
		type : 'POST',
		dataType : "jsonp",
		url : url,
		complete : function(xhr, statusText) {
			    alert("Dashboard list modified");
				 location.href="/index.html";
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
        $(".helpText").hide();
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
							$("#table td").parent().remove();
							var th = $('<tr/>');
							th.append("<td align = left> KEY </td>");
							th.append("<td align = left> URL</td>");
							th.append("<td align = left> DISPLAY </td>");
							$("#table").append(th);
							for ( var prop in obj) {
								var tr = $('<tr/>');
								tr.append("<td align = left>" + prop + "</td>");
								tr.append("<td align = left>" + obj[prop].url
										+ "</td>");
								tr.append("<td align = left>"
										+ obj[prop].display + "</td>");
								tr
										.append("<td> <input type=button class=button value= DELETE > </td>");

								$("#table").append(tr);

							}

						}
					}

				});

	} else {
		$(".ins").show();
		$("#table tr").parent().remove();
	}

}

/*when the delete button is clickecd on the delete page this is called for updating the Db to
 * update exist feild as False
 * 
 */
$("table").on("click", '.button', function() {
	key = $(this).closest("tr").find('td:eq(0)').text();
	if (confirm("Are you sure you want to delete this?")) {
		updateurl = "/services/dashboard/delete?key=" + key;
		makeajax(updateurl);
	}
	radobj = document.getElementById('Delete');
	getResults(radobj);
});
