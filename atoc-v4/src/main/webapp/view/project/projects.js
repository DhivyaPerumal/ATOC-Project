jQuery(document)
		.ready(
				function() {
					jQuery("#project").addClass("active");
					jQuery("#projectlist").addClass("active");

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					function project_jQueryDataTableAjax(serviceURL,
							projectName, projectPath, createdOn) {

						var userId = sessionStorage.getItem("userId");
						console.log(serviceURL);
						var projectListTable = jQuery("#projectList_table")
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
											"fnServerParams" : function(aoData) {
												aoData.push({
													"name" : "projectName",
													"value" : projectName
												}, {
													"name" : "projectPath",
													"value" : projectPath
												}, {
													"name" : "createdOn",
													"value" : createdOn
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
													jQuery('.dataTables_length')
															.hide();
													jQuery(
															'.dataTables_paginate')
															.hide();
												} else {
													jQuery('.dataTables_length')
															.show();
													jQuery(
															'.dataTables_paginate')
															.show();
												}
											},
											"aoColumns" : [
													{
														"mData" : "projectName"
													},
													{
														"mData" : "projectPath"
													},
													{
														"mData" : "createdOn"
													},
													{
														"mData" : "organization"
													},
													{
														"mData" : "projectId",
														"bSortable" : false,
														"mRender" : function(
																projectId) {
															return '<a href = "#" onClick = "editProject('
																	+ projectId
																	+ ');" id=\"edit_btn\"><span class=\"glyphicon glyphicon-pencil btn btn-info\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Edit\"></span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href = \"#\" onClick = "deleteProject('
																	+ projectId
																	+ ');"  id=\"delete_btn\"><span class=\"glyphicon glyphicon-trash btn btn-danger\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"Delete\"></span></a>';
														}
													} ],

										});
					}

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/project/viewProjectListPagination.action?userId='
							+ userId;
					createdOn = "";
					projectPath = "";
					projectName = "";
					project_jQueryDataTableAjax(serviceURL, projectName,
							projectPath, createdOn);
					var userId = sessionStorage.getItem("userId");
					jQuery("#submitChanges_btn")
							.click(
									function(e) {
										e.preventDefault();
										var target = (event.target.id);
										var x = document
												.getElementById("projectId");
										var projectId = x.innerHTML;
										var userId = sessionStorage
												.getItem("userId");
										var serviceURL = envConfig.serviceBaseURL
												+ '/project/viewProjectList.action?userId='
												+ userId;
										console.log(serviceURL);
										jQuery
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														projectId : projectId,
														projectName : jQuery(
																'#projectName')
																.val(),
														notes : jQuery('#notes')
																.val(),
														startDate : jQuery(
																'#startDate')
																.val(),
														endDate : jQuery(
																'#endDate')
																.val(),
														isActive : jQuery(
																'#isActive')
																.val(),
														projectType : jQuery(
																'#projectType')
																.val(),
														projectPath : jQuery(
																'#projectPath')
																.val()
													},
													type : 'Post',
													success : function(data) {
														window.location.href = "dashboard.html";
													},
													failure : function(data) {
														window.location.href = "login.html";
													},
													statusCode : {
														403 : function(xhr) {
															alert("Session will be Expired");
															window.location.href = "../../";

														}
													}

												});
									});

					jQuery('#projectCreatedDate_search').datepicker({
						format : "mm/dd/yyyy",
						changeMonth : true,
						changeYear : true
					});
					function projectTable_Search() {
						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/project/viewProjectListPagination.action?userId='
								+ userId;
						createdOn = jQuery('#projectCreatedDate_search').val();
						projectPath = jQuery('#projectPath_search').val();
						projectName = jQuery('#projectName_search').val();
						project_jQueryDataTableAjax(serviceURL, projectName,
								projectPath, createdOn);

					}
					jQuery("#search_project_btn").click(function(e) {

						e.preventDefault();
						projectTable_Search();

					});
					jQuery("#resetsearch_project_btn").click(function(e) {
						jQuery('#projectCreatedDate_search').val("");
						jQuery('#projectPath_search').val("");
						jQuery('#projectName_search').val("");

						projectTable_Search();

					});

					jQuery("#delete_project_btn")
							.click(
									function(e) {

										e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										var projectId = localStorage
												.getItem('projectId');
										var serviceURL = envConfig.serviceBaseURL
												+ '/project/deleteProject.action?userId='
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
														projectId : projectId
													},
													type : 'GET',
													success : function(data) {
														localStorage
																.removeItem(projectId);
														window.location.href = "projects.html";
													},
													failure : function(data) {
														localStorage
																.removeItem(projectId);
														window.location.href = "login.html";
													},
													statusCode : {
														403 : function(xhr) {
															alert("Session will be Expired");
															window.location.href = "../../";

														}
													}

												});
									});
				});
function editProject(projectId) {
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/project/getProjectInfo.action?userId=' + userId + '&projectId='
			+ projectId;

	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			projectId : projectId
		},
		type : 'Post',
		success : function(response) {
			var JsonStringify_Data = JSON.stringify(response.data);
			var editProject_Data = response.data;

			localStorage.setItem('editProject_Data', JsonStringify_Data);
			window.location.href = "editProject.html";
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
function deleteProject(projectId) {
	localStorage.setItem('projectId', projectId);
	jQuery('#delete_project_modal').modal('toggle');
	jQuery('#delete_project_modal').modal('view');

}
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}