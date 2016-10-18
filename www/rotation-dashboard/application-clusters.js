var getClusterConfig = function () {
  /** Description of the JSON format for clusters.
   *
   * data is a list of Clusters.
   *
   * Cluster:
   * clusterName   : Human readable and identifiable string
   * healthCheckURL: healthCheckURL component where the component is what 
   *                 to append after hostname to get healthcheck URL.
   * servers       : List of Server objects where
   *        Server:
   *          template: String where '{}' will be replaced by a number
   *          range   : list of numbers to replace {} with. This will be all
   *                    servers
   *          used    : range of servers which is actually in production use
   */
  var data = [
  {
    "clusterName": "revive ads.flipkart.com",
    "healthCheckURL": ":80/delivery/adServerHealthCheck.php",
    "servers": [
    {
      "template": "flap-adserve-bm{}.nm.flipkart.com", 
      "range": [1,2,3,4,5,6,7,8,9,10,11],
      "used": [1,2,3,4,5]
    }
    ]
  },

  {
    "clusterName": "revive native cluster",
    "healthCheckURL": ":80/delivery/adServerHealthCheck.php",
    "servers": [
    {
      "template": "flap-adserve-native-bm{}.nm.flipkart.com", 
      "range": [1,2,3,4,5,6,7,8,9,10,11,12,13]
    }
    ]
  },

  {
    "clusterName": "revive administration",
    "healthCheckURL": ":80/delivery/adServerHealthCheck.php",
    "servers": [
    {
      "template": "w3-ads-adserve{}.nm.flipkart.com", 
      "range": [1,2,3]
    }
    ]
  },

  {
    "clusterName": "revive maintenance",
    "healthCheckURL": ":80/delivery/adServerHealthCheck.php",
    "servers": [
    {
      "template": "w3-ads-adserve{}.nm.flipkart.com", 
      "range": [4,5]
    }
    ]
  },

  {
    "clusterName": "ads external API",
    "healthCheckURL": ":25905/healthcheck",
    "servers": [
    {
      "template": "w3-ads-product-svc{}.nm.flipkart.com", 
      "range": [1,2,3,4,5]
    }
    ]
  },

  {
    "clusterName": "ads internal API",
    "healthCheckURL": ":25907/healthcheck",
    "servers": [
    {
      "template": "flap-ads-product-svc{}.nm.flipkart.com", 
      "range": [1,2,3,4,5]
    }
    ]
  },

  ];

  return data;
}
