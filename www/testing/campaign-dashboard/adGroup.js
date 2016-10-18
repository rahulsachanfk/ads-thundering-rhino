var byName;
var total;
(function($) {
	function getAdGroupDetails(campaignId, adGroupID, fn) {
		var URL = '/services/campaigns/' + campaignId + '/adgroups/'
				+ adGroupID;
		$.getJSON(URL, fn);
	}

	function updateAdGroupDetails(data) {
		$('#adgroup-id').text(data['id']);
		$('#adgroup-name').text(data['name']);
		$('#adgroup-status').text(data['status']);
		$('#adgroup-budget-min').text(data['budget']['min_budget']);
		$('#adgroup-minbudget-currency').text(data['budget']['currency']);
		$('#adgroup-budget-max').text(data['budget']['max_budget']);
		$('#adgroup-maxbudget-currency').text(data['budget']['currency']);
		$('#adgroup-listing-count').text(data['listings']['count']);
		$('#adgroup-creation').text(data['created_time']);
		$('#adgroup-modified').text(data['last_modified_time']);

	}
	function getListingIdDetails(campaignId, adGroupID, fn) {
		var URL = '/services/campaigns/' + campaignId + '/adgroups/'
				+ adGroupID + '/listings';

		$.getJSON(URL, fn);

	}

	function displayAdGroupsListingIds(data) {

		var space = '            ';
		var listingInStock = 0;
		var listingOutStock = 0;

		for (var i = 0; i < data['count']; i++) {
			var bannerId = data['listingids']['listings'][i]['openXBannerId'];
			var bannerValue;
			if (bannerId == null) {
				bannerValue = "No";
			} else {
				bannerValue = "Yes";
			}

			if (data['listingids']['listings'][i]['inStock'] == true) {
				listingInStock++;
			} else {
				listingOutStock++;
			}

			html = "<tr > <td >"
					+ data['listingids']['listings'][i]['listingId'] + "<td >"
					+ '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp'
					+ " </td >" + +"</td>" + "<td >"
					+ data['listingids']['listings'][i]['inStock'] + "<td >"
					+ space + " </td >" + +"</td>" + "<td >" + '&nbsp'
					+ '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp'
					+ '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp'
					+ '&nbsp' + '&nbsp' + " </td >" + "<td >" + bannerValue
					+ " </td > </tr>";
			$(listingid).append(html);

		}

		$('#adgroup-instock-count').text(listingInStock);
		$('#adgroup-outstock-count').text(listingOutStock);

		byName = data['listingids']['listings'].slice(0);
		total = data['count'];

	}

	function sortedList() {
		var space = '            ';
		byName.sort(function(a, b) {
			var x = a.inStock;
			var y = b.inStock;
			return y - x;
		});
		$("#listingid tr").remove();
		for (var i = 0; i < total; i++) {
			var bannerId = byName[i]['openXBannerId'];
			var bannerValue;
			if (bannerId == null) {
				bannerValue = "No";
			} else {
				bannerValue = "Yes";
			}
			html = "<tr > <td >" + byName[i]['listingId'] + "<td >" + '&nbsp'
					+ '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp' + " </td >"
					+ +"</td>" + "<td >" + byName[i]['inStock'] + "<td >"
					+ space + " </td >" + +"</td>" + "<td >" + '&nbsp' + '&nbsp'
					+ '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp'
					+ '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp' + '&nbsp'
					+ '&nbsp' + " </td >" + "<td >" + bannerValue
					+ " </td > </tr>";

			$(listingid).append(html);

		}
	}

	// Register event handlers
	$(document).ready(
			function() {
				var sort = "no";
				var currenturl = window.location.href;
				var queryStringArray = currenturl.split('?');
				var queryStringParms = queryStringArray[1].split("&");
				var queryStringval1 = queryStringParms[0].split("=");
				var queryStringval2 = queryStringParms[1].split("=");
				var campaignId = queryStringval1[1];
				var adGroupID = queryStringval2[1];
				var details = getAdGroupDetails(campaignId, adGroupID,
						updateAdGroupDetails);
				var listing = getListingIdDetails(campaignId, adGroupID,
						displayAdGroupsListingIds);

				$('#instock').click(function() {
					sortedList();
				});

			});

})($);
