jQuery(document)
		.ready(
				function() {
				
				jQuery("#testSuite").addClass("active");
				jQuery("#testSuitelist").addClass("active");
				
				jQuery(document).ajaxStart(customblockUI);
				jQuery(document).ajaxStop(customunblockUI);
				
					var userId = sessionStorage.getItem("userId");
					jQuery("#Add_test_suite_btn")
							.click(
									function() {
										var serviceURL = envConfig.serviceBaseURL
												+ '/testSuite/viewTestSuiteList.action?userId='
												+ userId;
										console.log(serviceURL);
										/*jQuery.blockUI({
											message : jQuery('#loading_Message'),
											css : {
												border : '1px solid #30a5ff'
											}
										});*/
										jQuery
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														project : jQuery(
																'#dropDown_ProjectName')
																.children(
																		":selected")
																.attr("id"),
														suiteName : jQuery(
																'#testSuiteName')
																.val(),
														testSuiteXmlPathInJar : jQuery(
																'#testSuiteXmlPathInJar')
																.val(),
														organization : "2"
													},
													type : 'Post',
													success : function(data) {
														// alert("success"+data);
													//	jQuery.unblockUI();
														window.location.href = "dashboard.html";

													},
													failure : function() {
														// alert( "Failed" );
														//jQuery.unblockUI();
													},
													statusCode : {
														403 : function(xhr) {
															//jQuery.unblockUI();
															alert("Session will be Expired");
															window.location.href = "../../";

														}
													}
												});

									});

					function project_jQueryDataTableAjax(serviceURL,
							projectName, suiteName, createdOn) {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/testSuite/viewTestSuiteListPagination.action?userId='
								+ userId;
						console.log(serviceURL);

						var testSuiteTable = jQuery("#testSuiteTable")
								.DataTable(
										{
											"sAjaxSource" : serviceURL,
											"bProcessing" : false,
											"bServerSide" : true,
											"bPaginate" : true,
											"searching" : false,
											"bFilter" : true,
											"bSort" : false,
											"bJQueryUI" : false,
											"bDestroy" : true,
											"responsive" : true,
											"fnServerParams" : function(aoData) {
												aoData.push({
													"name" : "projectName",
													"value" : projectName
												}, {
													"name" : "suiteName",
													"value" : suiteName
												}, {
													"name" : "createdOn",
													"value" : createdOn
												});
											},

											"sPaginationType" : 'simple_numbers',
											"iDisplayLength" : 10,
											columnDefs : [ {
												orderable : false,
												targets : -1
											} ],
											"fnDrawCallback" : function(
													oSettings) {
												if (oSettings.fnRecordsTotal() <= 10) {
													jQuery('.dataTables_length')
															.hide();
													jQuery('.dataTables_paginate')
															.hide();
												} else {
													jQuery('.dataTables_length')
															.show();
													jQuery('.dataTables_paginate')
															.show();
												}
											},
											"aoColumns" : [

													{
														"mData" : "projectName"
													},
													{
														"mData" : "suiteName"
													},
													{
														"mData" : "createdOn"
													},
													/*{
														"data" : null,
														"defaultContent" : "<a href=\"#\" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil btn btn-info\"></span></a>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<a href = \"#\" id=\"delete_btn\"><span class=\"glyphicon glyphicon-trash btn btn-danger\"></span></a>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<a href=\"#\" id=\"action_btn\"><span class=\"glyphicon glyphicon-play-circle btn btn-success\"></span></a>"
													}*/
													{ 
														  "mData": "testSuiteId",
														  "bSortable": false,
														  "mRender": function (testSuiteId) 
															 {
															 
															 /* return '<input id="btnDispose" type="button" onclick="Dispose(' 
																   + sourceData +')" value="Dispose" />'; */
																return '<a href = "#" onClick = "editTestSuite(' 
																   + testSuiteId +');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil btn btn-info\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteTestSuite(' 
																   + testSuiteId +');"  id=\"delete_btn\"><span class=\"glyphicon glyphicon-trash btn btn-danger\"></span></a>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<a href=\"#\" onClick = "runTestSuite(' 
																   + testSuiteId +');"id=\"action_btn\"><span class=\"glyphicon glyphicon-play-circle btn btn-success\"></span></a>';
															  }
													} 
													]
										});
						/*jQuery('#testSuiteTable tbody')
								.on(
										'click',
										'#edit_btn',
										function() {

											var data = testSuiteTable.row(
													jQuery(this).parents('tr'))
													.data();
											var JsonStringify_TestSuiteData = JSON
													.stringify(data);
											localStorage
													.setItem(
															'JsonStringify_TestSuiteData',
															JsonStringify_TestSuiteData);
											jQuery("#projectName").val(
													data.projectName);
											jQuery("#suiteName").val(data.suiteName);
											jQuery("#notes").val(data.notes);
											jQuery("#isActive").val(data.isActive);
											jQuery("#testSuiteId").val(
													data.testSuiteId);
											jQuery("#projectId").val(data.projectId);
											jQuery("#testSuiteUrl").val(
													data.testSuiteUrl);
											jQuery("#testSuiteXmlPathInJar").val(
													data.testSuiteXmlPathInJar);

											localStorage.setItem("projectName",
													data.projectName);
											localStorage.setItem("testSuiteId",
													data.testSuiteId);
											localStorage.setItem("Notes",
													data.notes);
											localStorage.setItem("suiteName",
													data.suiteName);
											localStorage.setItem("projectId",
													data.projectId);
											localStorage.setItem(
													"testSuiteUrl",
													data.testSuiteUrl);

											window.location.href = "editTestSuite.html";

										});*/
						/* jQuery('#testSuiteTable tbody')
								.on(
										'click',
										'#action_btn',
										function() {

											var data = testSuiteTable.row(
													jQuery(this).parents('tr'))
													.data();
											var JsonStringify_Data = JSON
													.stringify(data);
											localStorage.setItem(
													'JsonStringify_Data',
													JsonStringify_Data);
											jQuery("#dropdown_project").val(
													data.projectName);
											jQuery("#dropdown_suite").val(
													data.suiteName);

											localStorage.setItem("projectName",
													data.projectName);
											localStorage.setItem("suiteName",
													data.suiteName);
											localStorage.setItem("projectId",
													data.projectId);
											localStorage.setItem("testSuiteId",
													data.testSuiteId);
											localStorage.setItem("fromPage",
													"exec");

											window.location.href = "testExecution.html";

										});
										*/

						// Modal Window Delete

						/*jQuery('#testSuiteTable').on(
								'click',
								'#delete_btn',
								function(e) {
									var data = testSuiteTable.row(
											jQuery(this).parents('tr')).data();
									jQuery('#hidden_modal_projectId').val(
											data.testSuiteId);
									jQuery('#testSuiteId').text(data.testSuiteId);
									jQuery('#delete_testSuite_modal').modal('toggle');
									jQuery('#delete_testSuite_modal').modal('view');
								});
								*/

					}
					
					 jQuery("#delete_testSuite_btn")
							.click(
									function(e) {
										e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										/* var target = (e.target.id);
										var x = document
												.getElementById("hidden_modal_projectId"); */
												
										var testSuiteId = localStorage.getItem('testSuiteId');;
										var serviceURL = envConfig.serviceBaseURL
												+ '/testSuite/deleteTestSuite.action?userId='
												+ userId;
										console.log(serviceURL);
										jQuery('#delete_testSuite_modal').modal(
												'toggle');
										jQuery('#delete_testSuite_modal')
												.modal('hide');
										/*jQuery.blockUI({
											message : jQuery('#loading_Message'),
											css : {
												border : '1px solid #30a5ff'
											}
										});*/
										jQuery
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														testSuiteId : testSuiteId,

													},
													type : 'GET',
													success : function(data) {
														//jQuery.unblockUI();
														// alert("Deleted
														// Successfully");
														window.location.href = "testSuites.html";
													},
													failure : function(data) {
														//jQuery.unblockUI();
														// alert( "Deleting
														// Failed" );
														window.location.href = "login.html";
													},
													statusCode : {
														403 : function(xhr) {
															//jQuery.unblockUI();
															alert("Session will be Expired");
															window.location.href = "../../";

														}
													}

												});
									});
									

					var userId = sessionStorage.getItem("userId");

					var serviceURL = envConfig.serviceBaseURL
							+ '/testSuite/viewTestSuiteListPagination.action?userId='
							+ userId;
					createdOn = "";
					suiteName = "";
					projectName = "";
					/*jQuery(document).ajaxStart
							(jQuery.blockUI({ message: jQuery('#loading_Message'), css: { border: '1px solid #30a5ff' } }))
							.ajaxStop(jQuery.unblockUI);*/
					project_jQueryDataTableAjax(serviceURL, projectName,
							suiteName, createdOn);

					jQuery('#testSuiteCreatedDate').datepicker({
						format : "mm/dd/yyyy",
						changeMonth : true,
						changeYear : true
					});
					// var dialog, form = jQuery(" #searchForm_project ");

					function projectTable_Search() {
						var userId = sessionStorage.getItem("userId");

						var serviceURL = envConfig.serviceBaseURL
								+ '/testSuite/viewTestSuiteListPagination.action?userId='
								+ userId;
						createdOn = jQuery('#testSuiteCreatedDate').val();
						suiteName = jQuery('#testSuiteNameValue').val();
						projectName = jQuery('#projectName_search').val();
						/*jQuery(document).ajaxStart
							(jQuery.blockUI({ message: jQuery('#loading_Message'), css: { border: '1px solid #30a5ff' } }))
							.ajaxStop(jQuery.unblockUI);*/

						project_jQueryDataTableAjax(serviceURL, projectName,
								suiteName, createdOn);
						//jQuery.unblockUI();
						// dialog.dialog("close");
					}
					jQuery("#search_testsuit_btn").click(function(e) {
						e.preventDefault();
						projectTable_Search();
					});

					jQuery("#resetsearch_testsuit_btn").click(function(e) {
						jQuery('#testSuiteCreatedDate').val('');
						jQuery('#testSuiteNameValue').val('');
						jQuery('#projectName_search').val('');

						projectTable_Search();

					});
					jQuery('#loading_cancel_btn').click(function(event) {
						//jQuery.unblockUI();
					});

				});

				function editTestSuite(testSuiteId)
{
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
					+ '/testSuite/getTestSuiteInfo.action?userId='+userId+'&testSuiteId=' + testSuiteId;


jQuery.ajax({
				url : serviceURL,
				dataType : "json",
				data : 
				{
					testSuiteId : testSuiteId
				},
				type : 'Post',
				success : function(response) {
					var JsonStringify_Data = JSON.stringify(response.data);
					var editProject_Data = response.data;

					localStorage.setItem('JsonStringify_TestSuiteData', JsonStringify_Data);
					//jQuery("#projectName").val(response.data.projectName);
					window.location.href = "editTestSuite.html";
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

}

function runTestSuite(testSuiteId)
{
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
					+ '/testSuite/getTestSuiteInfo.action?userId='+userId+'&testSuiteId=' + testSuiteId;


jQuery.ajax({
				url : serviceURL,
				dataType : "json",
				data : 
				{
					testSuiteId : testSuiteId
				},
				type : 'Post',
				success : function(response) {
					var JsonStringify_Data = JSON.stringify(response.data);
					var editProject_Data = response.data;

					localStorage.setItem('JsonStringify_Data_TestSuiteInfo', JsonStringify_Data);
					jQuery("#dropdown_project").val(editProject_Data.projectName);
					jQuery("#dropdown_suite").val(editProject_Data.suiteName);

					localStorage.setItem("projectName", editProject_Data.projectName);
					localStorage.setItem("suiteName", editProject_Data.suiteName);
					localStorage.setItem("projectId", editProject_Data.projectId);
					localStorage.setItem("testSuiteId", editProject_Data.testSuiteId);
					localStorage.setItem("fromPage", "exec");

					window.location.href = "../execution/testExecution.html";
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

}

function deleteTestSuite(testSuiteId)
{
	localStorage.setItem('testSuiteId', testSuiteId);
	jQuery('#delete_testSuite_modal').modal('toggle');
	jQuery('#delete_testSuite_modal').modal('view');
	
}
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}