function getClusters(json){
    return json;	      	
  }
var input = {

 "clusters" : [

        {

            "oogway" : [

                {

                  "externalapi": [

                   {

                     "carray" : [1,2,3,4,5],

                     "port":"25905",

                     "host":"w3-ads-product-svc.nm.flipkart.com",
                     
                     "url":"healthcheck"

                     }

                   ],

                  "internalapi": [

                   {

                    "carray" : [1,2,3,4,5],

                     "port":"25907",

                     "host":"flap-ads-product-svc.nm.flipkart.com",
                     
                     "url":"healthcheck"

                    }

                   ]

                 }],

            "revive" : [

                {

                  "admin": [

                  {

                    "carray" : [1,2,3],

                     "port":"25947",
                     
                     "host":"w3-ads-adserve.nm.flipkart.com",

                     "url":"healthcheck"

                    }

                    ],



                  "preprod": [

                    {

                    "carray" : [ ],

                     "port":"",

                     "host":"",

                     "url":""

                    }

                  ],
                  "maintanence": [

                        {

                            "carray" : [4,5],

                            "port":"80",

                            "host":"w3-ads-adserve.nm.flipkart.com",

                            "url":"delivery/adServerHealthCheck.php"

                        }

                    ],

                  "IdentityService":[
                      {
                          "carray":[1,2,3],
                          "port":"25929",
                          "host":"flap-identity-service-00.nm.flipkart.com",
                          "url":"healthcheck"
                      }
                  ],
                    "IdentityServiceZK":[
                        {
                            "carray":[1,2,3],
                            "port":"25929",
                            "host":"flap-identity-zk-00.nm.flipkart.com",
                            "url":"healthcheck"
                        }
                    ],

                    "adtracker":[
                        {
                            "carray":[1,2,3,4,5,6,7,8,9,10],
                            "port":"25941",
                            "host":"flap-ad-tracker-00.nm.flipkart.com",
                            "url":"healthcheck"
                        }
                    ],

                    "reportsAPI":[
                        {
                            "carray":[1,2,3],
                            "port":"25935",
                            "host":"flap-data-report-00.nm.flipkart.com",
                            "url":"healthcheck"
                        }
                    ],

                    "repoSchema":[
                        {
                            "carray":[2,3],
                            "port":"25932",
                            "host":"flap-data-srepo-00.nm.flipkart.com",
                            "url":"healthcheck"
                        }
                    ],
                    "adserver":[
                        {
                            "carray":[16,17,18,19,20,21,22,23,24,25],
                            "port":"25938",
                            "host":"flap-ad-adserver-00.nm.flipkart.com",
                            "url":"healthcheck"
                        }
                    ],

                    "adserver_tracker":[
                        {
                            "carray":[11,12,13,14,15,16,17,18,19,20],
                            "port":"25938",
                            "host":"flap-ad-tracker-00.nm.flipkart.com",
                            "url":"healthcheck"
                        }
                    ],
                    "ad_tracker_server":[
                        {
                            "carray":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15],
                            "port":"25941",
                            "host":"flap-ad-adserver-00.nm.flipkart.com",
                            "url":"healthcheck"
                        }
                    ]

                 }     

            ],
            "redis":[
                {
                    "rediscache": [{
                        "carray":[1,2,3,4],
                        "host":"flap-data-redis-cache-00.nm.flipkart.com",
                        "port":"25921"

                    }

                    ],
                    "redisstore": [{
                        "carray":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],
                        "host":"flap-data-redis-store-00.nm.flipkart.com",
                        "port":"25922"
                    }

                    ]
                }
            ]

        }

    ]

 };
