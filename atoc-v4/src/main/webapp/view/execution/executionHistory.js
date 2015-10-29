jQuery(document)
		.ready(
				function() {
					jQuery("#execution").addClass("active");
					jQuery("#executionHistory").addClass("active");
					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);
					var userId = sessionStorage.getItem("userId");
					var isAdmin = sessionStorage.getItem("isAdmin");
					jQueryselectProject = jQuery('#dropdown_project');
					jQueryselectSuite = jQuery('#dropdown_suite');
					jQueryselectProject.html('');
					jQueryselectSuite.html('');
					var userId = sessionStorage.getItem("userId");
					var obj;
					var projectName;
					var suiteName;
					var testSuiteId;
					var selected_testSuiteId;
					var projectId;
					jQueryselectProject = jQuery('#dropdown_project');
					jQueryselectSuite = jQuery('#dropdown_suite');
					var serviceURL = envConfig.serviceBaseURL
							+ '/project/viewProjectList.action?userId='
							+ userId;
					console.log(serviceURL);
					jQuery
							.ajax({
								url : serviceURL,
								dataType : 'json',
								type : 'GET',
								cache : 'false',
								success : function(data) {
									jQueryselectProject.html('');
									jQueryselectSuite.html('');
									jQueryselectProject
											.append('<option selected="selected" value="">Select Project</option>');
									jQueryselectSuite
											.append('<option selected="selected" value="">Select Test Suite</option>');
									var JsonStringify_Data = JSON
											.stringify(data);
									obj = jQuery.parseJSON(JsonStringify_Data);
									var arr = [];
									jQuery.each(obj, function(i, e) {
										jQuery.each(e, function(key1, val_0) {
											arr.push(val_0);
											jQuery.each(val_0.testSuiteSet,
													function(key2, val_l) {
														jQuery.each(val_l,
																function(key3,
																		val_2) {

																});
													});
										});
									});
									var result = jQuery.map(arr, function(val,
											key) {
										return {
											projectId : val.projectId,
											projectName : val.projectName
										};
									});
									jQuery.each(result, function(key, val) {
										jQueryselectProject
												.append('<option id="'
														+ val.projectId + '">'
														+ val.projectName
														+ '</option>');
									})
									jQuery("#dropdown_project")
											.val(projectName);
									jQuery('#dropdown_project').focus();
									jQuery("#dropdown_suite").val(suiteName);
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
					jQuery("#dropdown_project")
							.change(
									function() {
										jQueryselectSuite.html('');
										var id = jQuery(this).children(
												":selected").attr("id");
										projectId = id;
										var projectName = '';
										var dropDownprojectName = jQuery(
												'#dropdown_project').val();
										var suiteId = '';
										jQuery.each(obj,function(i, e) {
												jQuery.each(e,function(key1,val_0) {
													projectName = val_0.projectName;
														jQuery.each(val_0.testSuiteSet,function(key2,val_l) {
															jQuery.each(val_l,function(key3,val_2) {
																if (projectName == dropDownprojectName) {
																	if (key3 == 'testSuiteId') {
																		suiteId = val_2;
																		}
																	if (key3 == 'suiteName') {
																		jQueryselectSuite.append('<option id="'+ suiteId+ '">'
																								+ val_2 + '</option>');
																	}
															}
														});
													});
												});
											});
									});
					jQuery('#selectProjectError').hide();
					jQuery('#selectSuiteError').hide();
					jQuery('#execution_table').hide();
					jQuery("#submitBtn")
							.click(function(e) {
										jQuery('#execution_table').hide();
										var userId = sessionStorage
												.getItem("userId");
										e.preventDefault();
										var serviceURL = envConfig.serviceBaseURL
												+ '/testSuiteExecution/viewtestSuiteExecutionPaginationList.action?userId='
												+ userId;
										console.log(serviceURL);
										var id = jQuery('#dropdown_suite')
												.children(":selected").attr(
														"id");
										testSuiteId = id;
										testSuiteExecName = "";
										browser = "";
										browserVersion = "";
										operatingSystem = "";
										execCompleteOn = "";
										jQuery('#selectProjectError').hide();
										jQuery('#selectSuiteError').hide();
										jQuery('#execution_table').hide();
										jQuery('#table_info').show();
										jQuery('#dropdown_project').focus();
										if (jQuery(
												'#dropdown_project option:selected')
												.val() == '') {

											jQuery('#dropdown_project').css({
												"border" : "1px solid red",
											});
											jQuery('#selectProjectError').show();
											jQuery('#table_info').hide();
											jQuery('#execution_table').hide();
											return false;
										} else if (jQuery('#dropdown_suite option:selected').length == 0) {
											jQuery('#dropdown_suite').css({
												"border" : "1px solid red",
											});
											jQuery('#selectSuiteError').show();
											jQuery('#execution_table').hide();
											jQuery('#table_info').hide();
											return false;
										}
										else {
											jQuery('#table_info').show();
											var execList = jQuery("#execution_table")
													.DataTable(
															{
																"sAjaxSource" : serviceURL,
																"bProcessing" : false,
																"bServerSide" : true,
																"bPaginate" : true,
																"bFilter" : true,
																"searching" : false,
																"bSort" : false,
																"bDestroy" : true,
																"bJQueryUI" : false,
																"fnServerParams" : function(
																		aoData) {
																	aoData
																			.push(
																					{
																						"name" : "testSuiteId",
																						"value" : testSuiteId
																					},
																					{
																						"name" : "testSuiteExecName",
																						"value" : testSuiteExecName
																					},
																					{
																						"name" : "browser",
																						"value" : browser
																					},
																					{
																						"name" : "browserVersion",
																						"value" : browserVersion
																					},
																					{
																						"name" : "operatingSystem",
																						"value" : operatingSystem
																					},
																					{
																						"name" : "execCompleteOn",
																						"value" : execCompleteOn
																					});
																},
																"sPaginationType" : 'simple_numbers',
																"iDisplayStart" : 0,
																"iDisplayLength" : 10,
																columnDefs : [ {
																	orderable : false,
																	targets : -1
																} ],
																"fnDrawCallback" : function(
																		oSettings) {
																	if (oSettings.fnRecordsTotal() <= 10) {
																		jQuery('.dataTables_length').hide();
																		jQuery('.dataTables_paginate').hide();
																	} else {
																		jQuery('.dataTables_length').show();
																		jQuery('.dataTables_paginate').show();
																	}
																},
																"aoColumns" : [
																		{
																			"mData" : "testSuiteExecName"
																		},
																		{
																			"mData" : "browser"
																		},
																		{
																			"mData" : "browserVersion"
																		},
																		{
																			"mData" : "operatingSystem"
																		},
																		{
																			"mData" : "execCompleteOn"
																		},
																		{
																			"mData" : "testSuiteExecId",
																			"bSortable" : false,
																			"mRender" : function(
																					testSuiteExecId) {
																				return '<a href = "#" onClick = "showExecutionResult('
																						+ testSuiteExecId
																						+ ');" id=\"testSuiteExecId\"><span class=\"glyphicon glyphicon-eye-open btn btn-success\" data-toggle=\"tooltip\" title=\"\" data-original-title=\"View\"></span></a>';
																			}
																		} ]
															});
											jQuery('#execution_table').hide();
											jQuery('#execution_table tbody').on('click','#testSuiteExecId',function(e) {
												var data = execList.row(jQuery(this).parents('tr')).data();
												var JsonStringify_Data = JSON.stringify(data);
												localStorage.setItem('JsonStringify_Data',JsonStringify_Data);
												var execHistory_Data = JSON.stringify(data);
												localStorage.setItem("testSuiteExecId",data.testSuiteExecId);
												localStorage.setItem("requestPage","executionHistory");
											});
											jQuery('#execution_table').show();
										}
									});
					jQuery("#dropdown_project").change(function() {
						jQuery('#selectProjectError').hide();
						jQuery('#execution_table').hide();
						jQuery('#selectSuiteError').hide();
						jQuery('#table_info').hide();
						jQuery('#dropdown_project').css({
							"border" : "1px solid #0866c6",
						});
						jQuery('#dropdown_suite').css({
							"border" : "1px solid #0866c6",
						});
					});
					jQuery("#dropdown_suite").change(function() {
						jQuery('#selectSuiteError').hide();
						jQuery('#execution_table').hide();
						jQuery('#table_info').hide();
						jQuery('#dropdown_suite').css({
							"border" : "1px solid #0866c6",
						});
					});
					jQuery('#exeon').datepicker({
						format : "mm/dd/yyyy",
						changeMonth : true,
						changeYear : true
					});
				});
function showExecutionResult(testSuiteExecId) {
	var userId = sessionStorage.getItem("userId");
	localStorage.setItem('testSuiteExecId', testSuiteExecId);
	window.location.href = "executionResult.html";
}
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}