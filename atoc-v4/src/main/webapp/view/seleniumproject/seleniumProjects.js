jQuery(document)
		.ready(
				function() {
					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);
					
					function project_jQueryDataTableAjax(serviceURL,
							projectName, createdOn, updatedOn) {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/jarVersion/viewProjectJars.action?userId='
								+ userId;
						console.log(serviceURL);
						/*$(document).ajaxStart($.blockUI({
							message : $('#loading_Message'),
							css : {
								border : '1px solid #30a5ff'
							}
						})).ajaxStop($.unblockUI);*/
						var projectJarsTable = $("#projectJarsTable")
								.DataTable(
										{
											"sAjaxSource" : serviceURL,
											"bProcessing" : false,
											"bServerSide" : true,
											"bPaginate" : true,
											"searching" : false,
											"bFilter" : true,
											"bSort" : true,
											"bJQueryUI" : false,
											"fnServerParams" : function(aoData) {
												aoData.push({
													"name" : "projectName",
													"value" : projectName
												}, {
													"name" : "createdOn",
													"value" : createdOn
												}, {
													"name" : "updatedOn",
													"value" : updatedOn
												});
											},
											"bDestroy" : true,
											"responsive" : true,
											"sPaginationType" : 'simple_numbers',
											"iDisplayLength" : 10,
											columnDefs : [ {
												orderable : false,
												targets : -1
											} ],
											"fnDrawCallback" : function(
													oSettings) {
												if (oSettings.fnRecordsTotal() <= 10) {
													$('.dataTables_length')
															.hide();
													$('.dataTables_paginate')
															.hide();
												} else {
													$('.dataTables_length')
															.show();
													$('.dataTables_paginate')
															.show();
												}
											},
											"aoColumns" : [

													{
														"mData" : "projectName"
													},
													{
														"mData" : "createdOn"
													},
													{
														"mData" : "updatedOn"
													},
													/*
													 * { "mData" : "jarNames" },
													 */
													{
														"data" : null,
														"defaultContent" : "<a href=\"#\" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil btn btn-info\"></span></a>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<a href = \"#\" id=\"delete_btn\"><span class=\"glyphicon glyphicon-trash btn btn-danger\"></span></a>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<a href=\"#\" id=\"action_btn\"><span class=\"glyphicon glyphicon-envelope btn btn-success\"></span></a>"
													} ]
										});
						$('#projectJarsTable tbody')
								.on(
										'click',
										'#edit_btn',
										function() {

											var data = projectJarsTable.row(
													$(this).parents('tr'))
													.data();
											var JsonStringify_ProjectJarData = JSON
													.stringify(data);
											localStorage
													.setItem(
															'JsonStringify_ProjectJarData',
															JsonStringify_ProjectJarData);
											$("#projectJarListId").val(
													data.projectJarListId);
											$("#jarNames").val(data.jarNames);
											$("#projectName").val(
													data.projectName);

											window.location.href = "editProjectByJar.html";

										});

						// Modal Window To Show Sending an email.
						$('#projectJarsTable tbody').on(
								'click',
								'#action_btn',
								function() {
									var data = projectJarsTable.row(
											$(this).parents('tr')).data();
									$('#hidden_modal_projectJarListId').val(
											data.projectJarListId);
									$('#projectJarListId').text(
											data.projectJarListId);
									$('#send_projectjar_email_modal').modal(
											'toggle');
									$('#send_projectjar_email_modal').modal(
											'view');
								});

						// Modal Window Delete

						$('#projectJarsTable').on(
								'click',
								'#delete_btn',
								function(e) {
									var data = projectJarsTable.row(
											$(this).parents('tr')).data();
									$('#hidden_modal_projectJarListId').val(
											data.projectJarListId);
									$('#projectJarListId').text(
											data.projectJarListId);
									$('#delete_project_modal').modal('toggle');
									$('#delete_project_modal').modal('view');
								});
					}

					$("#send_projectjar_email_btn")
							.click(
									function(e) {
										e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										var target = (e.target.id);
										var x = document
												.getElementById("hidden_modal_projectJarListId");
										var projectJarId = x.value;
										var serviceURL = envConfig.serviceBaseURL
												+ '/jarVersion/sendProjectJarEmail.action?userId='
												+ userId;
										console.log(serviceURL);
										$('#send_projectjar_email_modal')
												.modal('toggle');
										/*$.blockUI({
											message : $('#loading_Message'),
											css : {
												border : '1px solid #30a5ff'
											}
										});*/
										$
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														projectJarId : projectJarId,

													},
													type : 'GET',
													success : function(data) {
														// alert("Deleted
														// Successfully");
													//	$.unblockUI();
														window.location.href = "viewProjectByJar.html";
													},
													failure : function(data) {
														//$.unblockUI();
														// alert( "Deleting
														// Failed" );
														window.location.href = "login.html";
													},
													statusCode : {
														403 : function(xhr) {
														//	$.unblockUI();
															alert("Session will be Expired");
															window.location.href = "../";

														}
													}

												});
									});

					$("#delete_projectjar_btn")
							.click(
									function(e) {
										e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										var target = (e.target.id);
										var x = document
												.getElementById("hidden_modal_projectJarListId");
										var projectJarId = x.value;
										var serviceURL = envConfig.serviceBaseURL
												+ '/jarVersion/deleteProjectJarId.action?userId='
												+ userId;
										console.log(serviceURL);
										$('#delete_project_modal').modal(
												'toggle');
									/*	$.blockUI({
											message : $('#loading_Message'),
											css : {
												border : '1px solid #30a5ff'
											}
										});*/
										$
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														projectJarId : projectJarId,

													},
													type : 'GET',
													success : function(data) {
														//$.unblockUI();
														// alert("Deleted
														// Successfully");
														window.location.href = "viewProjectByJar.html";
													},
													failure : function(data) {
													//	$.unblockUI();
														// alert( "Deleting
														// Failed" );
														window.location.href = "dashboard.html";
													},
													statusCode : {
														403 : function(xhr) {
															//$.unblockUI();
															alert("Session will be Expired");
															window.location.href = "../";

														}
													}

												});
									});

					$('#projectCreatedDate_search').datepicker({
						format : "mm/dd/yyyy",
						changeMonth : true,
						changeYear : true
					});
					$('#projectUpdatedDate_search').datepicker({
						format : "mm/dd/yyyy",
						changeMonth : true,
						changeYear : true
					});
					function projectTable_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/jarVersion/viewProjectJars.action?userId='
								+ userId;
						createdOn = $('#projectCreatedDate_search').val();
						updatedOn = $('#projectUpdatedDate_search').val();
						projectName = $('#projectName_search').val();
						/*$.blockUI({
							message : $('#loading_Message'),
							css : {
								border : '1px solid #30a5ff'
							}
						});*/
						project_jQueryDataTableAjax(serviceURL, projectName,
								createdOn, updatedOn);
						//$.unblockUI();

					}

					$("#search_project_btn").click(function(e) {

						e.preventDefault();
						projectTable_Search();

					});
					$("#resetsearch_project_btn").click(function(e) {
						$('#projectCreatedDate_search').val("");
						$('#projectUpdatedDate_search').val("");
						$('#projectName_search').val("");

						projectTable_Search();

					});

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/jarVersion/viewProjectJars.action?userId='
							+ userId;

					project_jQueryDataTableAjax(serviceURL);

					$('#loading_cancel_btn').click(function(event) {
						//$.unblockUI();
					});
				});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}