jQuery(document)
		.ready(
				function() {
                   
				  
					jQuery("#reports").addClass("active");
					jQuery("#comparisionReport").addClass("active");

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					var userId = sessionStorage.getItem("userId");

					jQueryselectProject = jQuery('#dropdown_project');

					jQueryselectSuite = jQuery('#dropdown_suite');

					jQueryselectSuiteExecution = jQuery('#dropdown_suiteExecution_side');
					jQueryselectSuiteExecutionSide = jQuery('#dropdown_suiteExecution');

					jQueryselectProject.html('');

					jQueryselectSuite.html('');

					jQueryselectSuiteExecution.html('');
					jQueryselectSuiteExecutionSide.html('');

					jQueryselectProject
							.append('<option selected="selected" value="">Select Project</option>');

					jQueryselectSuite
							.append('<option selected="selected" value="">Select Test Suite</option>');

					jQueryselectSuiteExecution
							.append('<option selected="selected" value="">Select Test Execution</option>');
					jQueryselectSuiteExecutionSide
							.append('<option selected="selected" value="">Select Test Execution</option>');
                      /*Code for Dropdown Option li select*/
					  
					
					  /*End of Code*/
					var serviceURL = envConfig.serviceBaseURL
							+ '/project/viewProjectList.action?userId='
							+ userId;
					console.log(serviceURL);
					hideAllErrors();
					jQuery.ajax({
						url : serviceURL,
						dataType : 'json',
						type : 'GET',
						cache : 'false',
						success : function(data) {

							var JsonStringify_Data = JSON.stringify(data);
							obj = jQuery.parseJSON(JsonStringify_Data);
							var arr = [];
							jQuery.each(obj, function(i, e) {
								jQuery.each(e, function(key1, val_0) {
									arr.push(val_0);
									jQuery.each(val_0.testSuiteSet, function(key2, val_l) {
										jQuery.each(val_l,function(key3, val_2) {
												});
									});
								});
							});
							var result = jQuery.map(arr, function(val, key) {
								return {
									projectId : val.projectId,
									projectName : val.projectName
								};
							});
							jQuery.each(result, function(key, val) {
								jQueryselectProject.append('<option id="'
										+ val.projectId + '">'
										+ val.projectName + '</option>');
							})
							jQuery('#dropdown_project').focus();
						},
						error : function() {
						},
						statusCode : {
							403 : function(xhr) {
								alert("Session will be Expired");
								window.location.href = "../../";
							}
						}
					});
					jQuery("#dropdown_project").change(function() {
						jQuery('#selectProjectError').hide();
						jQuery('#comparison_error').hide();
						jQueryselectSuite.html('');
						jQueryselectSuite.append('<option selected="selected" value="">Select Test Suite</option>');
						var id = jQuery(this).children(":selected").attr("id");
						projectId = id;
						var projectName = '';
						var dropDownprojectName = jQuery('#dropdown_project').val();
						var suiteId = '';
						jQuery.each(obj,function(i, e) {
							jQuery.each(e,function(key1,val_0) {
							projectName = val_0.projectName;
							jQuery.each(val_0.testSuiteSet,function(key2,val_l) {
									jQuery.each(val_l,function(key3,val_2) {
										if (projectName == dropDownprojectName) {
											if (key3 == 'testSuiteId') {													suiteId = val_2;
											}
											if (key3 == 'suiteName') {
											jQueryselectSuite.append('<option id="' + suiteId + '">' + val_2 + '</option>');
											}
										}
									});
								});
							});
						});
					});
					jQuery("#dropdown_suite").change(function() {
						jQuery('#selectSuiteError').hide();
						jQuery('#executionNameError').hide();
						jQuery('#comparison_error').hide();
						var serviceURL = envConfig.serviceBaseURL
								+ '/testSuiteExecution/viewtestSuiteExecutionList.action?userId='
								+ userId;
						console.log(serviceURL);
						hideAllErrors();
						jQueryselectSuiteExecution.append('<option selected="selected" value="">Select execution-1</option>');
						jQueryselectSuiteExecutionSide.append('<option selected="selected" value="">Select execution-2</option>');
						var id = jQuery(this).children(":selected").attr("id");
						testSuiteId = id;
						jQuery.ajax({
										url : serviceURL,
										dataType : "json",
										cache : 'true',
										data : {
												testSuiteId : id,
												},
										type : 'GET',
										success : function(data) {
													jQueryselectSuiteExecution.html('');
													jQueryselectSuiteExecutionSide.html('');
													var TestExecList_JsonStringify_Data = JSON.stringify(data);
													var TestSuiteExecList_obj = jQuery.parseJSON(TestExecList_JsonStringify_Data);
													var testArr = [];
													jQuery.each(TestSuiteExecList_obj,function(i,e) {
															jQuery.each(e,function(key,val) {
																		testArr.push(val);
																		});
															});
													var result = jQuery.map(testArr,function(val,key) {
														return {
																testSuiteExecId : val.testSuiteExecId,
																testSuiteExecName : val.testSuiteExecName
																};
													});
													jQuery.each(result,function(key,val) {
													jQueryselectSuiteExecution.append('<option id="' + val.testSuiteExecId
																							+ '">'
																							+ val.testSuiteExecName
																							+ '</option>');
													jQueryselectSuiteExecutionSide.append('<option id="' + val.testSuiteExecId
																							+ '">'
																							+ val.testSuiteExecName
																							+ '</option>');
														});
													},
													error : function() {
													},
													statusCode : {
														403 : function(xhr) {
															alert("Session will be Expired");
															window.location.href = "../../";
														}
													}
												});
									});
					var isAdmin = sessionStorage.getItem("isAdmin");
					jQuery('#selectProjectError').hide();
					jQuery('#selectProjectErrorSide').hide();
					jQuery('#executionNameError').hide();
					jQuery('#executionNameErrorSide').hide();
					jQuery('#compare_id').hide();
					jQuery('#comparison_error').hide();
					jQuery('#download_btn').hide();
					jQuery('#downloadReport').hide();
					jQuery("#submitBtn").click(function(e) {
										jQuery('#comparison_error').hide();
										e.preventDefault();
										var testSuiteId = jQuery('#dropdown_suite').children(":selected").attr("id");
										var testSuite_SideId = jQuery('#dropdown_suite').children(":selected").attr("id");
										var testSuiteExecName = jQuery('#dropdown_suiteExecution_side').val();
										var testSuiteExecName_side = jQuery('#dropdown_suiteExecution').val();
										if (jQuery('#dropdown_project option:selected').val() == '') {
											jQuery('#dropdown_project').css({
												"border" : "1px solid red",
											});
											jQuery('#selectProjectError').show();
											jQuery("#dropdown_project").focus();
											return false;
										} else if (jQuery('#dropdown_suite option:selected').val() == '') {
											jQuery('#dropdown_suite').css({
												"border" : "1px solid red",
											});
											jQuery('#selectSuiteError').show();
											return false;
										} else if (jQuery('#dropdown_suiteExecution_side option:selected').val() == '') {
											jQuery('#dropdown_suiteExecution_side').css(
															{
																"border" : "1px solid red",
															});
											jQuery('#executionNameErrorSide').show();
											return false;
										} else if (jQuery('#dropdown_suiteExecution option:selected').val() == '') {
											jQuery('#dropdown_suiteExecution').css(
															{
																"border" : "1px solid red",
															});
											jQuery('#executionNameError').show();
											return false;
										}
										else if (testSuiteExecName == testSuiteExecName_side) {
											jQuery('#comparison_error').show();
											jQuery('#compare_id').hide();
											return false;
										}
										else {
											var serviceURL = envConfig.serviceBaseURL
													+ '/executionResult/getReportList.action?userId='
													+ userId;
											console.log(serviceURL);
											jQuery.ajax({
														url : serviceURL,
														dataType : "json",
														type : 'Post',
														data : {
															testSuiteId : testSuiteId,
															testSuite_SideId : testSuite_SideId,
															testSuiteExecName : testSuiteExecName,
															testSuiteExecName_side : testSuiteExecName_side,
														},
														success : function(data) {
															jQuery('#compare_id').show();
															var exeResDet;
															jQueryleft_Table = jQuery('#left_table');
															if (data != null) {
																jQuery("#left_table tbody").html("");
																var JsonStringify_Data = JSON.stringify(data);
																var obj = jQuery.parseJSON(JsonStringify_Data);
																var arr = [];
																var arr1 = [];
																jQuery.each(obj,function(i,e) {
																	jQuery.each(e,function(key,val) {
																	exeResDet = val.executionResultDetails;
																	arr1.push(exeResDet);
																	arr.push(val);
																	});
																});
																var execReports_left = arr[0];
																var execReports_right = arr[1];
																jQuery('#testSuiteExecName1').text("" + execReports_left.testSuiteExecName);
																
																/*New Code*/
																jQuery('#download_btn').show();
																jQuery('#downloadReport').show();
																/*Code ends*/
																
																localStorage.setItem("executionResultId_left",execReports_left.executionResultId);
																localStorage.setItem("executionResultId_right",execReports_right.executionResultId);
																jQuery('#totalresult1').text(""
																						+ execReports_left.total);
																jQuery('#passedresult1').text(""
																						+ execReports_left.passed);
																jQuery('#failedresult1').text(""
																						+ execReports_left.failed);
																jQuery('#skippedresult1').text(""
																						+ execReports_left.skipped);
																jQuery('#testSuiteExecName_side').text(""
																						+ execReports_right.testSuiteExecName);
																jQuery('#totalresult2').text(""
																						+ execReports_right.total);
																jQuery('#passedresult2').text(""
																						+ execReports_right.passed);
																jQuery('#failedresult2').text(""
																						+ execReports_right.failed);
																jQuery('#skippedresult2').text(""
																						+ execReports_right.skipped);
																var exeResult_Left_Array = arr1[0];
																var exeResult_Right_Array = arr1[1];
																//alert(arr1[0]);
																//alert(arr1[1]);
																//alert(JSON.stringify(exeResult_Right_Array));
																//alert(exeResult_Right_Array.length);
																var exeRes_Right_Arr = [];
																var exeRes_Right_Method_arr = [];
																var exeRes_Right_Sig_Arr = [];
																var na = '';
																var i = 0; var j = 0;
																var result = jQuery.map(exeResult_Right_Array,
																				function(exeResDet_val_Right,exeResDet_key_Right) {
																				
																				//alert(exeResDet_val_Right.status);
																				exeRes_Right_Method_arr[i] = 
																				exeResDet_val_Right.testMethodName;
																				exeRes_Right_Arr[i] = exeResDet_val_Right.status;
																				exeRes_Right_Sig_Arr[i] = exeResDet_val_Right.signature;
																				i++;
																				});
																				// alert(exeRes_Right_Arr.length);
																				// alert(exeRes_Right_Sig_Arr.length);
																				
																var result = jQuery.map(exeResult_Left_Array,
																				function(exeResDet_val,exeResDet_key) {
																				//alert(exeRes_Right_Arr[sta]);
																					var executionResultDetailId = exeResDet_val.executionResultDetailId;
																					
																					/* alert(jQuery.inArray(exeResDet_val.signature, exeRes_Right_Sig_Arr)); */
										var isSign = jQuery.inArray(exeResDet_val.signature, exeRes_Right_Sig_Arr);
																// alert("Equals"+isSign);
																// alert(exeResDet_val.signature+ " == "+exeRes_Right_Sig_Arr[isSign]);
																// alert(JSON.stringify(exeRes_Right_Sig_Arr));
																if(!(isNaN(isSign)) && isSign != "-1")
																		{
																					//alert("Equals");
																					var leftTable_addRow = jQuery("<tr class = \"exeResDet_tr\"><td id ="
																							+ executionResultDetailId
																							+ ">"
																							+ exeResDet_val.testMethodName
																							+ "</td><td>"
																							+ exeResDet_val.signature
																							+ "</td><td>"
																							+ exeResDet_val.status
																							+ "</td><td>"
																							+ exeRes_Right_Arr[isSign]
																							+ "</td></tr><br />");
																					jQuery("#left_table tbody")
																							.append(
																									leftTable_addRow);
																					executionResultDetailId = '';
																					exeResDet_val = '';
																		}
																else{
																
																var leftTable_addRow = jQuery("<tr class = \"exeResDet_tr\"><td id ="
																							+ executionResultDetailId
																							+ ">"
																							+ exeResDet_val.testMethodName
																							+ "</td><td>"
																							+ exeResDet_val.signature
																							+ "</td><td>"
																							+ exeResDet_val.status
																							+ "</td><td>"
																							+ na
																							+ "</td></tr><br />");
																jQuery("#left_table tbody")
																							.append(
																									leftTable_addRow);
																
																var leftTable_addRow1 = jQuery("<tr class = \"exeResDet_tr1\"><td id ="
																							+ executionResultDetailId
																							+ ">"
																					+ exeRes_Right_Method_arr[j]
																							+ "</td><td>"
																							+ exeRes_Right_Sig_Arr[j]
																							+ "</td><td>"
																							+ na
																							+ "</td><td>"
																							+ exeRes_Right_Arr[j]
																							+ "</td></tr><br />");
																jQuery("#left_table tbody")
																							.append(
																									leftTable_addRow1);
																
																	j++;
																	}				
																				});
																	//alert(JSON.stringify(exeResult_Right_Array));
																}
															else {
																jQuery('#totalresult1').text("");
																jQuery('#passedresult1').text("");
																jQuery('#failedresult1').text("");
																jQuery('#skippedresult1').text("");
																jQuery('#testmtdname1').text("");
																jQuery('#signature1').text("");
																jQuery('#totalresult2').text("");
																jQuery('#passedresult2').text("");
																jQuery('#failedresult2').text("");
																jQuery('#skippedresult2').text("");
																jQuery('#testmtdname2').text("");
																jQuery('#signature2').text("");
															}
														},
														failure : function(data) {
															window.location.href = "dashboard.html";
														},
														statusCode : {
															403 : function(xhr) {
																alert("Session will be Expired");
																window.location.href = "../../";
															}
														}
													});
										}
									});
									
									
					jQuery("#dropdown_project").change(function() {
						jQuery('#selectProjectError').hide();
						jQuery('#comparison_error').hide();
						jQuery('#dropdown_project').css({
							"border" : "1px solid #0866c6",
						});
					});
					jQuery("#dropdown_suite").change(function() {
						jQuery('#selectSuiteError').hide();
						jQuery('#executionNameErrorSide').hide();
						jQuery('#executionNameError').hide();
						jQuery('#comparison_error').hide();
						jQuery('#dropdown_suite').css({
							"border" : "1px solid #0866c6",
						});
						jQuery('#dropdown_suiteExecution_side').css({
							"border" : "1px solid #0866c6",
						});
						jQuery('#dropdown_suiteExecution').css({
							"border" : "1px solid #0866c6",
						});
					});

					jQuery("#dropdown_suiteExecution_side").change(function() {
						jQuery('#executionNameErrorSide').hide();
						jQuery('#comparison_error').hide();
						jQuery('#dropdown_suiteExecution_side').css({
							"border" : "1px solid #0866c6",
						});
					});

					jQuery("#dropdown_suiteExecution").change(function() {
						jQuery('#comparison_error').hide();
						jQuery('#executionNameError').hide();
						jQuery('#dropdown_suiteExecution').css({
							"border" : "1px solid #0866c6",
						});
					});

					jQuery('#comparison_error').hide();
					
					jQuery('#download_btn').click(function(e) {
					e.preventDefault();
				var downloadFormat = jQuery('#downloadReport').val();
				var exeResId = localStorage.getItem("executionResultId_left");
				var exeResId_Side=localStorage.getItem("executionResultId_right");
				//alert("Download is :"+downloadFormat+" :"+exeResId+" VS "+exeResId_Side);
							var report_Download = "#";
										var serviceURL = envConfig.serviceBaseURL
												+'/executionResult/getDownloadReport.action?userId='
												+ userId+'&downloadFormat='+downloadFormat+ '&exeResId='+ exeResId
												+'&exeResId_Side='+ exeResId_Side;
										window.open(serviceURL,'_blank');
										//window.location.href = "" + serviceURL;
									});
				});

function hideAllErrors() {
	jQuery('#selectProjectError').hide();
	jQuery('#selectSuiteError').hide();
	jQuery('#executionNameError').hide();
	jQuery('#comparison_error').hide();
}
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}