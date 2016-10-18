$(document)
		.ready(
				function() {
					$('#submitButton').click(function() {
						
						storePath = $('#storeId').val();
						tagId = $('#tagId').val();
						facet = $('#facetId').val();
						$('#img').show();
						getListingId(storePath, tagId);
						
						// getSellerInfo('LSTSWDEYSRBZNZAZPXECVZ6M9');
					});
					function getListingId(storePath, tagId) {
						var assArray = {};
						$
								.ajax({
									url : "/services/organicListings?ip="
											+ 'http://10.47.1.159:25290/'
											+ "&storePath=" + storePath
											+ "&tagId=" + tagId +  "&" + facet,
									dataType : "json",
									timeout : "70000",
									async : false,
									success : function(listingData) {
										// alert(listingData['RESPONSE']['products']['ids']);
										// getSellerInfo('LSTSWDEYSRBZNZAZPXECVZ6M9');
										// getSellerInfo(listingData['RESPONSE']['products']['ids']);
										// getSellerInfo(listingData);
										console
												.log(listingData['RESPONSE']['products']['ids']);

										getAdListingId(
												storePath,
												tagId,
												listingData['RESPONSE']['products']['ids']);
									},
									error : function(jqXHR, textStatus,
											errorThrown) {
										alert(" 1 You can not send Cross Domain AJAX requests : "
												+ errorThrown);
									}
								});
					}

					function getAdListingId(storePath, tagId, listingId) {
						$
								.ajax({
									url : "/services/organicListings?ip="
											+ 'http://10.47.1.1/'
											+ "&storePath=" + storePath
											+ "&tagId=" + tagId +  "&" + facet,
									dataType : "json",
									timeout : "70000",
									async : false,
									success : function(adListingData) {
										var asArray = [];
										var productId;
										// alert("length of organic listing id "
										// + listingId.length)
										// alert(adListingData['RESPONSE']['products']['ids']);

										var someFlag = true;
										var found = 0;
										var notFound = 0;
										var adListingId = adListingData['RESPONSE']['products']['ids'];

										for (var i = 0; i < listingId.length; i++) {
											var productId = listingId[i];
											if (adListingId
													.indexOf(listingId[i]) !== -1) {
												asArray[productId] = new Array;
												asArray[productId].push({
													"Found" : "True"
												});
												found++;

											} else {
												asArray[productId] = new Array;
												asArray[productId].push({
													"Found" : "False"
												});
												notFound++;

											}

										}

									

										for (var i = 0; i < listingId.length; i = i + 30) {
											getSellerInfo(asArray, listingId, i);
											// alert("I m called i")
										}

										displayData(asArray);

									}
								});
					}

					function getSellerInfo(asArray, listingId, element) {
						prdArray = [];

						for (var i = element; i < element + 30; i++) {

							if (listingId[i] != null) { //this check if no of elemnt is less than 30
								prdArray.push(listingId[i]);
							}
						}
						$.ajax({
							type : "POST",
							url : "/services/product/npsproduct",
							cache : false,
							dataType : "json",
							timeout : "60000",
							async : false,
							data : JSON.stringify({
								'productIds' : prdArray
							}), // serializes the form's elements.
							contentType : 'application/json; charset=utf-8',
						//	processData : false,
							success : function(data, textStatus, xhr) {
								if(xhr.status == 200) {
								getNPSdata(data, asArray);
								}

								
							}
						

						});
						return;

					}

					function getNPSdata(data, asArray) {

						for (var i = 0; i < data.length; i++) {
							console.log(i);
						

							console
									.log("data is getting printed wait for some time");
							if (data[i]['listingInfo']['listings'][0]	) {
							asArray[data[i]['metaInfo']['id']]
									.push({
										"listing_id" : data[i]['listingInfo']['listings'][0]['listing_id']
									});
							console
									.log(data[i]['listingInfo']['listings'][0]['seller_id']);
							asArray[data[i]['metaInfo']['id']]
									.push({
										"seller_id" : data[i]['listingInfo']['listings'][0]['seller_id']
									});
							}
						}

					}

					function displayData(asArray) {
						 $('#img').hide();
						// alert(asArray[0].length);
						html = "<tr> <th> Listing Id </th> <th> Seller Id </th> <th> Found </th></tr> ";
						$(mainTable).append(html);
						var noOfItems =0;
						for ( var key in asArray) {
							 noOfItems ++;
								
							//for ( var key1 in asArray[key]) {
							//	for ( var key2 in asArray[key][key1]) {
								//	 html = "<td >" + asArray[key][key1][key2]
                                 //    + " </td > </tr>";
							        
							 if( (asArray[key][1]) && (asArray[key][2])) {
							 
									html = "<tr><td >" + asArray[key][1]['listing_id']
											+ " </td ><td> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;" + asArray[key][2]['seller_id']  + "</td><td> &nbsp; &nbsp; &nbsp"
											+ asArray[key][0]['Found'] + "</td></tr>";
							 } else  {
								 
								 html = "<tr><td >" + key
									+ " </td ><td> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;" + "-- "  + "</td><td> &nbsp; &nbsp; &nbsp"
									+ "---" + "</td></tr>";
							 }
							 
									$(mainTable).append(html);
									//console.log(key + " "
										//	+ asArray[key][key1][key2]);

								//}
						//	}

						}
				//		alert(noOfItems);
					}

				});
