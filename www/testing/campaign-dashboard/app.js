(function ($) {
    function getCampaignDetails(campaignId, fn) {
        var URL = '/services/campaigns/' + campaignId;
        $.getJSON(URL, fn);
    }
    
 
    

    function updateCampaignDetails(data) {
        $('#campaign-id').text(data['campaignId']);
        $('#campaign-title').text(data['campaignTitle']);
        $('#seller-id').text(data['sellerId']);
        $('#campaign-budget').text(data['goals']['budget']['value']);
        $('#campaign-budget-currency').text(data['goals']['budget']['currency']);
        $('#campaign-start').text(data['goals']['startDate']);
        $('#campaign-end').text(data['goals']['endDate']);
        $('#campaign-status').text(data['status']);

    }
  
    function getCampaignImp(campaignId, fn) {
        var URL = '/services/campaigns/' + campaignId + '/performance';
        $.getJSON(URL, fn);
        }
           
    function updateCampaignImpClicks(data) {
 
       $('#actions').text(data['clickImpressions']['response'][0]['actions']);
      $('#views').text(data['clickImpressions']['response'][0]['views']);
      $('#conversions').text(data['clickImpressions']['response'][0]['conversions']);
      $('#budget').text(data['clickImpressions']['response'][0]['budget']);
      
  

    }
    
    
    function getCampaignAdGroup(campaignId, fn) {
        var URL = '/services/campaigns/' + campaignId + '/adgroups';
        $.getJSON(URL, fn);
        }
    
    function displayAdGroups(data) {
    	$(mainTable).empty();

    	for (var i = 0; i < data['count']; i++) {
    	  	html = "<tr"  
		+ " style='width:100%' > <td >" + "<a + target='_blank' href=" + 'adGroupInfo.html?campaignId=' + data['campaign_id'] + '&adgroupID='+ data['gropuIDs']['adgroups'][i]['id']  + ">" + data['gropuIDs']['adgroups'][i]['id']  + "</a>" + 
		 "</td><td>" + data['gropuIDs']['adgroups'][i]['name'] + "</td></tr>";
    	$(mainTable).append(html);
         
    	}

      }

    // Register event handlers
    $(document).ready(function () {
        $('#campaign-submit').click(function () {
            var campaignId = $('#campaign-id-input').val();
            var details = getCampaignDetails(campaignId,
                updateCampaignDetails);
            var impClick = getCampaignImp(campaignId,
            		updateCampaignImpClicks);
            
            var impClick = getCampaignAdGroup(campaignId,
            		displayAdGroups);
            
            
            
        });
    });
})($);
