jQuery(document)
		.ready(
				function() {
					jQuery("#adminparent").addClass("active");
					jQuery("#viewProjectByJar").addClass("active");
					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					function project_jQueryDataTableAjax(serviceURL,
							projectName, createdOn, updatedOn) {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/jarVersion/viewProjectJars.action?userId='
								+ userId;
						console.log(serviceURL);
						var projectJarsTable = jQuery("#projectJarsTable")
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
													jQuery('.dataTables_length').hide();
													jQuery('.dataTables_paginate').hide();
												} else {
													jQuery('.dataTables_length').show();
													jQuery('.dataTables_paginate').show();
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
													{
														"mData" : "projectJarListId",
														"bSortable" : false,
														"mRender" : function(
																projectJarListId) {
															return '<a href = "#" onClick = "editSeleniumProject('
																	+ projectJarListId
																	+ ');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil btn btn-info\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteSeleniumProject('
																	+ projectJarListId
																	+ ');"  id=\"delete_btn\"><span class=\"glyphicon glyphicon-trash btn btn-danger\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Delete\"></span></a>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<a href=\"#\" onClick = "sendEmailSeleniumProject('
																	+ projectJarListId
																	+ ');" id=\"action_btn\"><span class=\"glyphicon glyphicon-envelope btn btn-success\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Mail\"></span></a>';
														}
													} ]
										});
					}
					jQuery('#projectCreatedDate_search').datepicker({
						format : "mm/dd/yyyy",
						changeMonth : true,
						changeYear : true
					});
					jQuery('#projectUpdatedDate_search').datepicker({
						format : "mm/dd/yyyy",
						changeMonth : true,
						changeYear : true
					});
					function projectTable_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/jarVersion/viewProjectJars.action?userId='
								+ userId;
						createdOn = jQuery('#projectCreatedDate_search').val();
						updatedOn = jQuery('#projectUpdatedDate_search').val();
						projectName = jQuery('#projectName_search').val();
						project_jQueryDataTableAjax(serviceURL, projectName,
								createdOn, updatedOn);
					}
					jQuery("#send_projectjar_email_btn")
							.click(
									function(e) {
										e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										var projectJarId = localStorage
												.getItem('hidden_modal_projectJarListId');
										var serviceURL = envConfig.serviceBaseURL
												+ '/jarVersion/sendProjectJarEmail.action?userId='
												+ userId;
										console.log(serviceURL);
										jQuery('#send_projectjar_email_modal')
												.modal('toggle');
										jQuery('#send_projectjar_email_modal')
												.modal('hide');
										jQuery
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														projectJarId : projectJarId,
													},
													type : 'GET',
													success : function(data) {
														window.location.href = "viewProjectByJar.html";
													},
													failure : function(data) {
														jQuery.unblockUI();
														window.location.href = "../dashboard/dashboard.html";
													},
													statusCode : {
														403 : function(xhr) {
															jQuery.unblockUI();
															alert("Session will be Expired");
															window.location.href = "../../";
														}
													}
												});
									});
					jQuery("#delete_projectjar_btn")
							.click(
									function(e) {
										e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										var projectJarId = localStorage
												.getItem('hidden_modal_projectJarListId');
										var serviceURL = envConfig.serviceBaseURL
												+ '/jarVersion/deleteProjectJarId.action?userId='
												+ userId;
										console.log(serviceURL);
										jQuery('#delete_project_modal').modal(
												'toggle');
										jQuery('#delete_project_modal').modal(
												'hide');
										jQuery
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														projectJarId : projectJarId,

													},
													type : 'GET',
													success : function(data) {

														window.location.href = "viewProjectByJar.html";
													},
													failure : function(data) {
														jQuery.unblockUI();
														window.location.href = "../dashboard/dashboard.html";
													},
													statusCode : {
														403 : function(xhr) {
															jQuery.unblockUI();
															alert("Session will be Expired");
															window.location.href = "../../";
														}
													}
												});
									});
					jQuery("#search_project_btn").click(function(e) {
						e.preventDefault();
						projectTable_Search();
					});
					jQuery("#resetsearch_project_btn").click(function(e) {
						jQuery('#projectCreatedDate_search').val("");
						jQuery('#projectUpdatedDate_search').val("");
						jQuery('#projectName_search').val("");
						projectTable_Search();
					});
					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/jarVersion/viewProjectJars.action?userId='
							+ userId;
					project_jQueryDataTableAjax(serviceURL);
				});
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
function editSeleniumProject(projectJarListId) {
	var userId = sessionStorage.getItem("userId");
	var serviceURL = envConfig.serviceBaseURL
			+ '/selenium/getSeleniumProjectInfo.action?userId=' + userId
			+ '&projectJarId=' + projectJarListId;
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			projectJarId : projectJarListId
		},
		type : 'Post',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			localStorage.setItem('JsonStringify_ProjectJarData',
					JsonStringify_Data);
			window.location.href = "../seleniumproject/editProjectByJar.html";
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
function deleteSeleniumProject(projectJarListId) {
	localStorage.setItem('hidden_modal_projectJarListId', projectJarListId);
	localStorage.setItem('projectJarListId', projectJarListId);
	jQuery('#delete_project_modal').modal('toggle');
	jQuery('#delete_project_modal').modal('view');
}
function sendEmailSeleniumProject(projectJarListId) {
	localStorage.setItem('hidden_modal_projectJarListId', projectJarListId);
	localStorage.setItem('projectJarListId', projectJarListId);
	jQuery('#send_projectjar_email_modal').modal('toggle');
	jQuery('#send_projectjar_email_modal').modal('view');
}