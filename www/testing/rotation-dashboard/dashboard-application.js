// This is a version of app.js that doesn't suck. It is 
// modular and self-contained. It doesn't rely on HTML 
// to have anything declared.

function initDashboard(data, $, divId) {
  // data: Cluster data as defined in application-clusters.js
  // $: jquery, passed in here just for local scoping
  // divName: string HTML id attribute of div to add this to..

  function createCard(clusterConfig) {
    // Create a rotation health card for the passed in cluster 
    // config object

    function getHostList(servers) {
      // returns a list of hosts given a list of server config as 
      // defined in application-clusters.js
      var rtn = [];

      $.map(servers, function (serverRange, i) {
        var consideredRange = serverRange.used === undefined ? serverRange.range : serverRange.used;

        var b = $.map(consideredRange, function (token, i) {
          return serverRange.template.replace('{}', token);
        });

        rtn = rtn.concat(b);
      });

      return rtn;
    };

    
    var card = $('<div class="card"></div>')
      , consideredHosts = getHostList(clusterConfig.servers);

    card.append('<div class="heading">' + clusterConfig.clusterName + '</div>');

    $.map(consideredHosts, function (host, i) {
      card.append('<div id="' + host + '">' + host + '</div>');
    });

    return card;
  };

  var dashDiv = $('#' + divId);

  for (var i=0; i<data.length; i++) {
    // iterate through all clusters and create a card for each.
    $(dashDiv).append(createCard(data[i]));
  }
}
