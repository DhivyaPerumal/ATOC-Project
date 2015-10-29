jQuery(document).ready(
		function() {
			jQuery(".active").removeClass("active");
			jQuery("#dashboard").addClass("active");
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			var userId = sessionStorage.getItem("userId");
			var obj = '';
			var serviceURL = envConfig.serviceBaseURL
					+ '/dashboardview/getTotalCountExecution.action?userId='
					+ userId;
			console.log(serviceURL);
			jQuery.ajax({
				type : 'Post',
				url : serviceURL,
				dataType : "json",
				data : {
					userId : userId
				},
				success : function(data) {
					var JsonStringify_Data = JSON.stringify(data);
					var obj = jQuery.parseJSON(JsonStringify_Data);
					var list = data.list;
					var testName;
					var testKey = "";
					var re1;
					var testvalue = "";
					var dataPoints = [];
					var arr = [];
					if (list.length > 0) {
						jQuery.each(list, function(i, e) {
							jQuery.each(e, function(key, val) {
								testName = val;
								if (key == 0) {
									testKey = testKey.concat("&" + val);
								} else if (key == 1) {
									testvalue = testvalue.concat("&" + val);
								}
								jQuery('#bargraphproj').hide();

							});

						});
					} else
					{
						jQuery('#testExeCountChart').hide();

						jQuery('#bargraphproj').show();
					}
					var num = testKey.toString();
					var num1 = testvalue.toString();
					var res1 = num1.split('&');
					var res = num.split('&');
					if (res.length == res1.length) {
						for (var k = 1; k < res.length; k++) {
							if (res[k] != null || res1[k] != null) {
								dataPoints.push({
									y : parseInt(res[k]),
									label : res1[k]
								});
							}
						}
					}
					var chart = new CanvasJS.Chart("testExeCountChart", {
						axisY : {
							gridThickness : 0,
							title : "No.Of Executions",
							minimum : 0
						},
						axisX : {
							labelFontSize : 15,
							labelMaxWidth : 50,
							axisLabelUseCanvas : true
						},
						legend : {
							verticalAlign : "bottom",
							horizontalAlign : "center"
						},
						data : [// array of dataSeries
						{ // dataSeries object
							type : "column",
							dataPoints : dataPoints
						} ]
					});
					chart.render();
				},
				failure : function(data) {
					window.location.href = "../../";
				},
				statusCode : {
					403 : function(xhr) {
						alert("Session will be Expired");
						window.location.href = "../../";

					}
				}
			});
			var userId = sessionStorage.getItem("userId");
			var serviceURL = envConfig.serviceBaseURL
					+ '/dashboardview/getLastExecutionResult.action?userId='
					+ userId;
			console.log(serviceURL);
			jQuery.ajax({
				type : 'Post',
				url : serviceURL,
				dataType : "json",
				data : {
					userId : userId
				},
				success : function(data) {
					var JsonStringify_Data = JSON.stringify(data);
					var obj = jQuery.parseJSON(JsonStringify_Data);
					var passedName;
					if (data.success == true) {
						if (passedName == undefined) {
						}
					} else if (data.success == false) {
						jQuery('#doughnutgraphproj').show();
						jQuery('#testResultCountChart').hide();
					}
					var arr = [];
					jQuery.each(obj, function(i, e) {
						jQuery.each(e, function(key, val) {
							jQuery.each(val, function(key1, val1) {
								if (key1 == "passed") {
									passedName = val1;
								}
							});
							localStorage.getItem('passed', passedName);

						});
					});
					if (passedName == null) {
						jQuery('#doughnutgraphproj').show();
						jQuery('#testResultCountChart').hide();
					}
					else {
						jQuery('#dashboardproj').hide();
					}
					/* failed data loop */
					var failedName;
					var arr = [];
					jQuery.each(obj, function(i, e) {
						jQuery.each(e, function(key, val) {
							jQuery.each(val, function(key1, val1) {
								if (key1 == "failed") {
									failedName = val1;
								}
							});
							localStorage.getItem('failed', failedName);

						});
					});
					/* Skipped loop Function */
					var skippedName;
					var arr = [];
					jQuery.each(obj, function(i, e) {
						jQuery.each(e, function(key, val) {
							jQuery.each(val, function(key1, val1) {
								if (key1 == "skipped") {
									skippedName = val1;

								}
							});
							localStorage.getItem('failed', skippedName);

						});
					});
					if (data != null) {
						jQuery("#passed").val(passedName);
						jQuery("#failed").val(failedName);
						jQuery("#skipped").val(skippedName);
						var testResultChart = new CanvasJS.Chart(
								"testResultCountChart", {
									animationEnabled : true,
									data : [ {
										type : "doughnut",
										startAngle : 60,
										toolTipContent : "{legendText}: {y}",
										showInLegend : true,
										dataPoints : [ {
											y : passedName,
											color : "#228B22",
											legendText : "Passed"
										}, {
											y : failedName,
											color : "#FF4D4D",
											legendText : "Failed"
										}, {
											y : skippedName,
											color : "gray",
											legendText : "Skipped"
										} ]
									} ]
								});
						testResultChart.render();

					}
				},
				failure : function(data) {
					window.location.href = "../../";
				},
				statusCode : {
					403 : function(xhr) {
						alert("Session will be Expired");
						window.location.href = "../../";

					}
				}
			});

			// date picker
			jQuery('#datepicker').datepicker();

			// tabbed widget
			jQuery('.tabbedwidget').tabs();


	/* jQuery('.skin_color a').click(function(){
	jQuery('#color_skin  a').hover(function(){
	
		
		jQuery('#color_skin li a').click(function(e){
		
		
		
	alert("Hai");
	var userId = sessionStorage.getItem("userId");
										e.preventDefault();
										var serviceURL = envConfig.serviceBaseURL
					+ '/user/saveUser.action?userId='
					+ userId;
					
			console.log(serviceURL);
										jQuery.ajax({
														//url : serviceURL,
														type : 'Post',
														dataType : "json",
														data : {
														userId : userId,
														colorSkin: jQuery('.skin_color').val()
														},
														success : function(data) {
															
														
														},
														failure : function(data) {
															window.location.href = "../../";
														}
													});
	

		
	
		});
		});
		}); */
		
		
		});
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
