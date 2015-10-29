jQuery(document)
		.ready(
				function() {
					jQuery("#execution").addClass("active");
					jQuery("#executionHistory").addClass("active");

					jQuery('#skippedTest_table tbody').on('click',
							'td.details-control', function() {
								var tr = jQuery(this).closest('tr');
							});

					jQuery('#accordion-2').hide('fast');
					jQuery('#chartContainer').click(function() {
						jQuery('#accordion-2').show('fast');

					});
					var userId = sessionStorage.getItem("userId");
					var isAdmin = sessionStorage.getItem("isAdmin");
					jQuery('#testexecutiondetails_panel').hide();
					jQuery('#testsuitedetails_panel').hide();
					var testSuiteExecId = localStorage
							.getItem('testSuiteExecId');
					var serviceURL = envConfig.serviceBaseURL
							+ '/executionResult/getExecutionResult.action?userId='
							+ userId;
					jQuery("#loading-div-background").show();
					jQuery
							.ajax({
								url : serviceURL,
								dataType : 'json',
								cache : false,
								type : 'POST',
								data : {
									testSuiteExecId : testSuiteExecId
								},
								type : 'Post',
								success : function(data) {
								
									if (data.success == true) {
										
										ajaxSuccess = true;
										jQuery('#executed_Results').show();
										jQuery('#executed_Results_loading').hide();
										jQuery('#testsuitedetails_panel').show();
										jQuery('#testexecutiondetails_panel').show();
									} else if (data.success == false) {
										ajaxSuccess = false;
										jQuery('#testsuitedetails_panel').hide();
										jQuery('#testexecutiondetails_panel').hide();
										jQuery("#loading-div-background").hide();
										jQuery('#nodataExecutionResult_modal').modal('toggle');
										jQuery('#nodataExecutionResult_modal').modal('view');
									}
									var JsonStringify_Data = JSON.stringify(data);
									var obj = jQuery.parseJSON(JsonStringify_Data);
									var arr = [];
									var arr = [];
									jQuery.each(obj, function(i, e) {
										jQuery.each(e, function(key, val) {
											arr.push(val);
										});
									});
									var execReports = arr[0];
									localStorage.setItem("executionResultId",execReports.executionResultId);
									
									var exeResDetArr_Skip = [];
									var exeResDetArr_Fail = [];
									var exeResDetArr_Pass = [];
									var result = jQuery.map(arr,function(val, key) {
														doughnutChart(
																val.passed,
																val.failed,
																val.skipped);
														testExecRes(val);
														var exeResDet = val.executionResultDetails;
														jQuery.each(exeResDet,function(exeResDet_key,exeResDet_val) {
															var status = exeResDet_val.status;
															if (status == "SKIP" || status == "skip") {
																var exeResDetId_Val = exeResDet_val.executionResultDetailId;
																exeResDetArr_Skip.push(exeResDet_val);
																exeResDet_DataTable_Skip(exeResDetArr_Skip,exeResDetId_Val);
															}
															if (status == "FAIL" || status == "fail") {
																var exeResDetId_Val = exeResDet_val.executionResultDetailId;
																exeResDetArr_Fail.push(exeResDet_val);
																exeResDet_DataTable_Fail(exeResDetArr_Fail,exeResDetId_Val);
															}
															if (status == "PASS" || status == "pass") {
																var exeResDetId_Val = exeResDet_val.executionResultDetailId;
																exeResDetArr_Pass.push(exeResDet_val);
																exeResDet_DataTable_Pass(exeResDetArr_Pass,exeResDetId_Val);
															}
														});
														return {
															executionResultId : val.executionResultId
														};
													});
									jQuery("#loading-div-background").hide();
								},
								failure : function(data) {
									jQuery("#loading-div-background").hide();
									window.location.href = "dashboard.html";
								},
								statusCode : {
									403 : function(xhr) {
										alert("Session will be Expired");
										jQuery("#loading-div-background").hide();
										window.location.href = "../../";
									}
								}
							});
					var accordion = jQuery("#accordion-2").accordion({
						collapsible : true,
						heightStyle : "content"
					});
					jQuery('#accordion-2').hide('fast');
					jQuery('#testExec_accordion_modal').hide('fast');
					jQuery('#chartContainer').click(function() {
						jQuery('#accordion-2').show('fast');
					});
					jQuery("#accordion-2").on(
							"accordionactivate",
							function(event, ui) {
								jQuery('#accordion-2').accordion('option',
										'active');
							});
					jQuery(function() {
						jQuery("#accordion-2").accordion();
					});

					jQuery("#unitTestId").click(function(e) {
						e.preventDefault();
						jQuery("#loading-div-background").hide();
						jQuery('#testExec_accordion_modal').modal('toggle');
						jQuery('#testExec_accordion_modal').modal('view');
					});
					jQuery("#sanityTestId").click(function(e) {
						e.preventDefault();
						jQuery("#loading-div-background").hide();
						jQuery('#testExec_accordion_modal').modal('toggle');
						jQuery('#testExec_accordion_modal').modal('view');
					});
					jQuery("#skippedtestId").click(function(e) {
						e.preventDefault();
						jQuery("#loading-div-background").hide();
						jQuery('#testExec_accordion_modal').modal('toggle');
						jQuery('#testExec_accordion_modal').modal('view');
					});
					jQuery('#exeResult_error_btn')
							.click(function(event) {
								window.location.href = "../execution/executionHistory.html";
					});
					
					jQuery('#download_btn').click(function(e) {
					e.preventDefault();
				var downloadFormat = jQuery('#downloadReport').val();
				var exeResId = localStorage.getItem("executionResultId");
				
				//alert("Download is :"+downloadFormat+" :"+exeResId+" VS "+exeResId_Side);
							var report_Download = "#";
										var serviceURL = envConfig.serviceBaseURL
												+'/executionResult/getExecutionDownloadReport.action?userId='
												+ userId+'&downloadFormat='+downloadFormat+ '&exeResId='+ exeResId;
										window.open(serviceURL,'_blank');
										//window.location.href = "" + serviceURL;
									});
					
					

				}); // end of jquery ready function
function testExecRes(data) {
	jQuery('#execution_projectname').text(data.projectName);
	jQuery('#execution_suiteid1').text(data.testSuiteExecName);
	jQuery('#execution_executionname').text(data.testExecutionName);
	jQuery('#execution_starttime').text(data.startTime);
	jQuery('#execution_endtime').text(data.endTime);
	jQuery('#execution_totaltests').text(data.total);
	jQuery('#execution_passed').text(data.passed);
	jQuery('#execution_failed').text(data.failed);
	jQuery('#execution_skipped').text(data.skipped);
}
function doughnutChart(passed, failed, skipped) {
	var data = JSON.parse(localStorage.getItem('JsonStringify_ExecResData'));
	var passed = passed;
	var failed = failed;
	var skipped = skipped;
	var chart = new CanvasJS.Chart("chartContainer", {
		title : {
			text : "Text Execution Result"
		},
		data : [ {
			type : "doughnut",
			startAngle : 60,
			toolTipContent : "{legendText}: {y}",
			showInLegend : true,
			enableAnimations : true,
			padding : {
				left : 5,
				top : 5,
				right : 5,
				bottom : 5
			},
			dataPoints : [ {
				innerRadius : 100,
				radius : 250,
				y : passed,
				color : "#228B22",
				legendText : "Passed"
			}, {
				y : failed,
				color : "#FF4D4D",
				legendText : "Failed"
			}, {
				y : skipped,
				color : "gray",
				legendText : "Skipped"
			}
			]
		} ]
	});
	chart.render();
}


function exeResDet_DataTable_Skip(exeResDet_val, exeResDetId_Val) {
	var userId = sessionStorage.getItem("userId");
	var exeResDet_Json_Obj = jQuery.parseJSON(JSON.stringify(exeResDet_val));
	//alert(JSON.stringify(exeResDet_val));
	var executionResultDetailId = exeResDetId_Val;
	var execList = jQuery("#skippedTest_table")
			.DataTable(
					{
						"aaData" : exeResDet_val,
						"destroy" : true,
						"bPaginate" : false,
						"bLengthChange" : false,
						"bFilter" : false,
						"bSort" : false,
						"bInfo" : false,
						"bAutoWidth" : false,
						"aoColumns" : [
								{
									/* Changes done for execution result exception in UI STARTS HERE */
									"data" : null,
									"bSearchable": false,
									"defaultContent" : "&nbsp;&nbsp;<a href = \"javascript:void(0);\" id="
											+ executionResultDetailId
											+ " class = \"execResDet_Id\" ><span id = "
											+ executionResultDetailId
											+ " class=\"glyphicon glyphicon-info-sign\"></span></a>&nbsp;&nbsp;&nbsp; <br /> &nbsp;&nbsp;"
								}, {
								"data" : null,
								"bSortable" : false,
								"mRender" : function(nRow, aData, iDisplayIndex, iDisplayIndexFull) 
								{
								if(iDisplayIndex.executionResultException.length >= 1){
									return '<a href = "#" id=\"skip_exeResException_aId\">Show Exception</a>';
									} else { return "";} 
								
								}
								},{
									"mData" : "testMethodName",
								}, {
									"mData" : "startedAt"
								}, {
									"mData" : "finishedAt"
								}, {
									"mData" : "signature"
								}
								]
								
					});
	exeResDetId_Val = '';
	exeResDetId_Val = null;
	
	/* Changes done for execution result exception in UI STARTS HERE */
	
	jQuery('#skippedTest_table tbody tr td').on('click','#skip_exeResException_aId',function(e) {
						e.preventDefault();
						jQuery("#execMParam_accordion_modal tbody").html("");
						var data = execList.row(jQuery(this).parents('tr')).data();
						var JsonStringify_Data = JSON.stringify(data);
						//alert(JsonStringify_Data);
						
						 var exeResException = jQuery.parseJSON(JSON.stringify(data));
						var exeResException_Obj = exeResException.executionResultException; 
						//alert(JSON.stringify(exeResException_Obj));
						jQuery("#exeResException_Methodname").text("");
						jQuery("#exeResException_Message").text("");
						jQuery("#exeResException_Methodname").text("No Exception details are avaliable.");
						jQuery.each(exeResException_Obj,function(key,val) {
								//alert(val.executionResultExceptionId +" : "+val.executionResultExceptionMethodName);
								//jQuery("#exeResException_modal p").append(val.executionResultExceptionMethodName);
								jQuery("#exeResException_Methodname").text(val.executionResultExceptionMethodName);
								jQuery("#exeResException_Message").text(val.executionResultExceptionFileContent);
							});
							jQuery('#exeResException_modal').modal('toggle');
							jQuery('#exeResException_modal').modal('view');
						jQuery("#loading-div-background").hide();
						customunblockUI();
					});
	
	/* Changes done for execution result exception in UI ENDS HERE */
	
	jQuery('#skippedTest_table tbody tr td').on('click','.execResDet_Id',function(e) {
		e.preventDefault();
		jQuery("#execMParam_accordion_modal tbody").html("");
		var data = execList.row(jQuery(this).parents('tr')).data();
		var JsonStringify_Data = JSON.stringify(data);
		var exeResDet_Json_Obj1 = jQuery.parseJSON(JSON.stringify(data));
		var exeResDetId_clicked = exeResDet_Json_Obj1.executionResultDetailId;
		var serviceURL = envConfig.serviceBaseURL
								+ '/executionResult/getExecMethodParamList.action?userId='
								+ userId;
						jQuery("#loading-div-background").show();
						jQuery
								.ajax({
									url : serviceURL,
									dataType : 'json',
									cache : false,
									type : 'POST',
									data : {
										executionResultDetailId : exeResDetId_clicked
									},
									type : 'Post',
									success : function(data) {
										jQuery('#executed_Results').show();
										jQuery('#executed_Results_loading').hide();
										jQuery("#loading-dialog").dialog('close');
										var execMParam_obj = jQuery.parseJSON(JSON.stringify(data));
										jQuery("#loading-div-background").hide();
										if (data.total != 0) {
											var execMParam_arr = [];
											jQuery.each(execMParam_obj,function(i, e) {
												jQuery.each(e,function(key,val) {
													var execMPram_skip_addRow = jQuery("<tr class=\"danger\></tr><br />");
													jQuery("#execMParam_accordion_modal tbody").append(execMPram_skip_addRow);
													var execMPram_skip_addRow = jQuery("<tr class=\"danger\"><td>"
																					+ val.parameterIndex + "</td><td>"
																						+ val.parameterValue
																						+ "</td></tr><br />");
													jQuery("#execMParam_accordion_modal tbody").append(execMPram_skip_addRow);
														});
												});
											exeResDetId_clicked = '';
											jQuery("#loading-div-background").hide();
											jQuery('#testExec_accordion_modal').modal('toggle');
											jQuery('#testExec_accordion_modal').modal('view');
											jQuery("#execMParam_accordion_modal tbody").html("");
										} else {
											jQuery("#execMParam_accordion_modal tbody").append(
															"No records found for this test name.");
											exeResDetId_clicked = '';
											jQuery("#loading-div-background").hide();
											jQuery('#testExec_accordion_modal').modal('toggle');
											jQuery('#testExec_accordion_modal').modal('view');
											jQuery("#execMParam_accordion_modal tbody").html("");
										}
									},
									failure : function(data) {
										jQuery("#loading-div-background")
												.hide();
									},
									statusCode : {
										403 : function(xhr) {
											alert("Session will be Expired");
											jQuery("#loading-div-background")
													.hide();
											window.location.href = "../../";
										}
									}
								});
					});
}
function exeResDet_DataTable_Fail(exeResDet_val, exeResDetId_Val) {
	var userId = sessionStorage.getItem("userId");
	jQuery(document).ajaxStart(customblockUI);
	jQuery(document).ajaxStop(customunblockUI);
	var exeResDet_Json_Obj = jQuery.parseJSON(JSON.stringify(exeResDet_val));
	
	/* Chnages done for execution result exception in UI STARTS HERE */
	
	var executionResultDetailId = exeResDetId_Val;
	var execList = jQuery("#failedTest_table")
			.DataTable(
					{
						"aaData" : exeResDet_val,
						"destroy" : true,
						"bPaginate" : false,
						"bLengthChange" : false,
						"bFilter" : false,
						"bSort" : false,
						"bInfo" : false,
						"bAutoWidth" : false,
						"aoColumns" : [
								{
									"data" : null,
									"defaultContent" : "&nbsp;&nbsp;<a href = \"javascript:void(0);\" id="
											+ executionResultDetailId
											+ " class = \"execResDet_Id\" ><span id = "
											+ executionResultDetailId
											+ " class=\"glyphicon glyphicon-info-sign\"></span></a>&nbsp;&nbsp;&nbsp; <br /> &nbsp;&nbsp;"
								},{
								"data" : null,
								"bSortable" : false,
								"mRender" : function(nRow, aData, iDisplayIndex, iDisplayIndexFull) 
								{
								if(iDisplayIndex.executionResultException.length >= 1){
									return '<a href = "#" id=\"fail_exeResException_aId\">Show Exception</a>';
									} else { return "";} 
								}
								},{
									"mData" : "testMethodName"
								}, {
									"mData" : "startedAt"
								}, {
									"mData" : "finishedAt"
								}, {
									"mData" : "signature",
								} ]
					});
	exeResDetId_Val = '';
	exeResDetId_Val = null;
	var userId = sessionStorage.getItem("userId");
	
	/* Chnages done for execution result exception in UI STARTS HERE */ 
	
	jQuery('#failedTest_table tbody tr td').on('click','#fail_exeResException_aId',function(e) {
						e.preventDefault();
						jQuery("#execMParam_accordion_modal tbody").html("");
						var data = execList.row(jQuery(this).parents('tr')).data();
						var JsonStringify_Data = JSON.stringify(data);
						//alert(JsonStringify_Data);
						
						 var exeResException = jQuery.parseJSON(JSON.stringify(data));
						var exeResException_Obj = exeResException.executionResultException; 
						//alert(JSON.stringify(exeResException_Obj));
						jQuery("#exeResException_Methodname").text("");
						jQuery("#exeResException_Message").text("");
						jQuery("#exeResException_Methodname").text("No Exception details are avaliable.");
						jQuery.each(exeResException_Obj,function(key,val) {
								//alert(val.executionResultExceptionId +" : "+val.executionResultExceptionMethodName);
								//jQuery("#exeResException_modal p").append(val.executionResultExceptionMethodName);
								jQuery("#exeResException_Methodname").text(val.executionResultExceptionMethodName);
								jQuery("#exeResException_Message").text(val.executionResultExceptionFileContent);
							});
							jQuery('#exeResException_modal').modal('toggle');
							jQuery('#exeResException_modal').modal('view');
						jQuery("#loading-div-background").hide();
						customunblockUI();
					});
	/* Chnages done for execution result exception in UI ENDS HERE */
	jQuery('#failedTest_table tbody tr td').on('click','.execResDet_Id',function(e) {
						e.preventDefault();
						jQuery("#execMParam_accordion_modal tbody").html("");
						var data = execList.row(jQuery(this).parents('tr')).data();
						var JsonStringify_Data = JSON.stringify(data);
						var exeResDet_Json_Obj1 = jQuery.parseJSON(JSON.stringify(data));
						var exeResDetId_clicked = exeResDet_Json_Obj1.executionResultDetailId;
						var serviceURL = envConfig.serviceBaseURL
								+ '/executionResult/getExecMethodParamList.action?userId='
								+ userId;
						jQuery("#loading-div-background").show();
						jQuery
								.ajax({
									url : serviceURL,
									dataType : 'json',
									cache : false,
									type : 'POST',
									data : {
										executionResultDetailId : exeResDetId_clicked
									},
									type : 'Post',
									success : function(data) {
										jQuery('#executed_Results').show();
										jQuery('#executed_Results_loading').hide();
										jQuery("#loading-dialog").dialog('close');
										var execMParam_obj = jQuery.parseJSON(JSON.stringify(data));
										jQuery("#loading-div-background").hide();
										if (data.total != 0) {
											var execMParam_arr = [];
											jQuery.each(execMParam_obj,function(i, e) {
													jQuery.each(e,function(key,val) {
												var execMPram_skip_addRow = jQuery("<tr class=\"danger\></tr><br />");
												jQuery("#execMParam_accordion_modal tbody").append(execMPram_skip_addRow);
												var execMPram_skip_addRow = jQuery("<tr class=\"danger\"><td>"
																							+ val.parameterIndex
																							+ "</td><td>"
																							+ val.parameterValue
																							+ "</td></tr><br />");
												jQuery("#execMParam_accordion_modal tbody").append(execMPram_skip_addRow);
													});
												});
											exeResDetId_clicked = '';
											jQuery("#loading-div-background").hide();
											jQuery('#testExec_accordion_modal').modal('toggle');
											jQuery('#testExec_accordion_modal').modal('view');
											jQuery("#execMParam_accordion_modal tbody").html("");
										} else {
											jQuery("#execMParam_accordion_modal tbody").append(
															"No records found for this test name.");
											exeResDetId_clicked = '';
											jQuery("#loading-div-background").hide();
											jQuery('#testExec_accordion_modal').modal('toggle');
											jQuery('#testExec_accordion_modal').modal('view');
											jQuery("#execMParam_accordion_modal tbody").html("");
										}
									},
									failure : function(data) {
										jQuery("#loading-div-background").hide();
									},
									statusCode : {
										403 : function(xhr) {
											alert("Session will be Expired");
											jQuery("#loading-div-background").hide();
											window.location.href = "../../";
										}
									}
								});
					});
}
function exeResDet_DataTable_Pass(exeResDet_val, exeResDetId_Val) {
	var userId = sessionStorage.getItem("userId");
	jQuery(document).ajaxStart(customblockUI);
	jQuery(document).ajaxStop(customunblockUI);
	var exeResDet_Json_Obj = jQuery.parseJSON(JSON.stringify(exeResDet_val));
	var executionResultDetailId = exeResDetId_Val;
	var execList = jQuery("#passedTest_table")
			.DataTable(
					{
						"aaData" : exeResDet_val,
						"destroy" : true,
						"bPaginate" : false,
						"bLengthChange" : false,
						"bFilter" : false,
						"bSort" : false,
						"bInfo" : false,
						"bAutoWidth" : false,
						"aoColumns" : [
								{
									"data" : null,
									"defaultContent" : "&nbsp;&nbsp;<a href = \"javascript:void(0);\" id="
											+ executionResultDetailId
											+ " class = \"execResDet_Id\"><span id = "
											+ executionResultDetailId
											+ " class=\"glyphicon glyphicon-info-sign\"></span></a>&nbsp;&nbsp;&nbsp;"
								}, {
									"mData" : "testMethodName"
								}, {
									"mData" : "startedAt"
								}, {
									"mData" : "finishedAt"
								}, {
									"mData" : "signature"
								} ]

					});
	exeResDetId_Val = '';
	exeResDetId_Val = null;
	var userId = sessionStorage.getItem("userId");
	jQuery('#passedTest_table tbody tr td').on('click','.execResDet_Id',function(e) {
						e.preventDefault();
						jQuery("#execMParam_accordion_modal tbody").html("");
						var data = execList.row(jQuery(this).parents('tr')).data();
						var JsonStringify_Data = JSON.stringify(data);
						var exeResDet_Json_Obj1 = jQuery.parseJSON(JSON.stringify(data));
						var exeResDetId_clicked = exeResDet_Json_Obj1.executionResultDetailId;
						var serviceURL = envConfig.serviceBaseURL
								+ '/executionResult/getExecMethodParamList.action?userId='
								+ userId;
						jQuery("#loading-div-background").show();
						jQuery
								.ajax({
									url : serviceURL,
									dataType : 'json',
									cache : false,
									type : 'POST',
									data : {
										executionResultDetailId : exeResDetId_clicked
									},
									type : 'Post',
									success : function(data) {
										jQuery('#executed_Results').show();
										jQuery('#executed_Results_loading').hide();
										jQuery("#loading-dialog").dialog('close');
										var execMParam_obj = jQuery.parseJSON(JSON.stringify(data));
										var execMParam_arr = [];
										jQuery("#loading-div-background").hide();
										if (data.total != 0) {
											jQuery.each(execMParam_obj,function(i, e) {
												jQuery.each(e,function(key,val) {
													var execMPram_skip_addRow = jQuery("<tr class=\"danger\></tr><br />");
													jQuery("#execMParam_accordion_modal tbody").append(execMPram_skip_addRow);
													var execMPram_skip_addRow = jQuery("<tr class=\"danger\"><td>"
																							+ val.parameterIndex
																							+ "</td><td>"
																							+ val.parameterValue
																							+ "</td></tr><br />");
													jQuery("#execMParam_accordion_modal tbody").append(execMPram_skip_addRow);
															});
													});
											exeResDetId_clicked = '';
											jQuery("#loading-div-background").hide();
											jQuery('#testExec_accordion_modal').modal('toggle');
											jQuery('#testExec_accordion_modal').modal('view');
											jQuery("#execMParam_accordion_modal tbody").html("");
										} else {
											jQuery("#execMParam_accordion_modal tbody").append(
															"No records found for this test name.");
											exeResDetId_clicked = '';
											jQuery("#loading-div-background").hide();
											jQuery('#testExec_accordion_modal').modal('toggle');
											jQuery('#testExec_accordion_modal').modal('view');
											jQuery("#execMParam_accordion_modal tbody").html("");
										}
									},
									failure : function(data) {
										jQuery("#loading-div-background")
												.hide();
									},
									statusCode : {
										403 : function(xhr) {
											alert("Session will be Expired");
											jQuery("#loading-div-background").hide();
											window.location.href = "../../";
										}
									}
								});
					});
}
function exeMetParam_Skip(exeMethodParam, exeResDet_val) {
	if (exeMethodParam != null && exeResDet_val != null) {
		var execMPram_skip_addRow = jQuery("<tr class=\"danger\></tr><br />");
		jQuery("#execMParam_accordion_modal tbody").append(
				execMPram_skip_addRow);
		var result = jQuery.map(exeMethodParam, function(val, key) {
			var execMPram_skip_addRow = jQuery("<tr class=\"danger\"><td>"
					+ val.executionParameterId + "</td><td>"
					+ val.parameterIndex + "</td><td>" + val.parameterValue
					+ "</td></tr><br />");
			jQuery("#execMParam_accordion_modal tbody").append(
					execMPram_skip_addRow);
			return {
				parameterIndex : val.parameterIndex
			};
		});
		jQuery(document).on("click", "#skippedTest_table tr a", function(e) {
			e.preventDefault();
		});
	}
}
function exeMetParam_Fail(exeMethodParam_Fail, exeResDet_val) {
	jQuery("#execMParam_accordion_modal tbody").html("");
	if (exeMethodParam_Fail != null && exeResDet_val != null) {
		var execMPram_skip_addRow = jQuery("<tr class=\"danger\></tr><br />");
		jQuery("#execMParam_accordion_modal tbody").append(
				execMPram_skip_addRow);
		var result = jQuery.map(exeMethodParam_Fail, function(val, key) {
			var execMPram_skip_addRow = jQuery("<tr class=\"danger\"><td>"
					+ val.executionParameterId + "</td><td>"
					+ val.parameterIndex + "</td><td>" + val.parameterValue
					+ "</td></tr><br />");
			jQuery("#execMParam_accordion_modal tbody").append(
					execMPram_skip_addRow);
			return {
				parameterIndex : val.parameterIndex
			};
		});
		jQuery(document).on("click", "#failedTest_table tr a", function(e) {
			e.preventDefault();
			jQuery("#loading-div-background").hide();
			jQuery('#testExec_accordion_modal').modal('toggle');
			jQuery('#testExec_accordion_modal').modal('view');
		});
	}
}
function exeMetParam_Pass(exeMethodParam_Pass, exeResDet_val) {
	if (exeMethodParam_Pass != null && exeResDet_val != null) {
		var result = jQuery.map(exeMethodParam_Pass, function(val, key) {
			var execMPram_skip_addRow = jQuery("<tr class=\"danger\"><td>"
					+ val.executionParameterId + "</td><td>"
					+ val.parameterIndex + "</td><td>" + val.parameterValue
					+ "</td></tr><br />");
			jQuery("#execMParam_accordion_modal tbody").append(
					execMPram_skip_addRow);
			return {
				parameterIndex : val.parameterIndex
			};
		});
		jQuery(document).on("click", "#passedTest_table tr a", function(e) {
			e.preventDefault();
			jQuery("#loading-div-background").hide();
			jQuery('#testExec_accordion_modal').modal('toggle');
			jQuery('#testExec_accordion_modal').modal('view');
		});
	}
}
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}