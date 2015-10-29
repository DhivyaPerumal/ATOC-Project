jQuery(document)
		.ready(
				function() {
					jQuery("#masterUserList").addClass("active");
					var userId = sessionStorage.getItem("userId");
					var isAdmin = sessionStorage.getItem("isAdmin");
					if (isAdmin == "Master Admin") {
						jQuery("#masterUserListMasterAdmin").addClass("active");
						jQuery("#masterUserOrganization")
								.removeAttr('disabled');
					} else if (isAdmin == "Org Admin") {
						jQuery("#masterUserList").addClass("active");
					}
					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);
					var serviceURL = envConfig.serviceBaseURL
							+ '/user/userId.action?userId=' + userId;
					console.log(serviceURL);

					// load Organization data
					loadOrgData(serviceURL, userId);
					function masterUser_jQueryDataTableAjax(serviceURL,firstName, lastName, organization) {
var userId =  sessionStorage.getItem("userId");
var masterUser_table = jQuery("#masterUser_table").DataTable({
											"sAjaxSource" : serviceURL,
											"bProcessing" : false,
											"bServerSide" : true,
											"bPaginate" : true,
											"bFilter" : true,
											"searching" : false,
											"bSort" : false,
											"bDestroy" : true,
											"bJQueryUI" : false,
											"sPaginationType" : 'simple_numbers',
											"iDisplayStart" : 0,
											"iDisplayLength" : 10,
											"fnServerParams" : function(aoData) {
												aoData.push({
													"name" : "firstName",
													"value" : firstName
												}, {
													"name" : "lastName",
													"value" : lastName
												}, {
													"name" : "organization",
													"value" : organization
												});
											},
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

											"aoColumns" : [ {
												"mData" : "firstName"
											}, {
												"mData" : "lastName"
											}, {
												"mData" : "userEmail"
											}, {
												"mData" : "organization"
											}, {
												"mData" : "isActive"
											}, {
												"mData" : "phone"
											}, ]
										});

					}

					function masterUserTable_Search() {
						var userId = sessionStorage.getItem("userId");
						var isAdmin = sessionStorage.getItem("isAdmin");
						var serviceURL = envConfig.serviceBaseURL
								+ '/user/viewMasterUser.action?userId='
								+ userId;
						if (isAdmin == "Master Admin") {
							lastName = jQuery('#masterUserLastName').val();
							firstName = jQuery('#masterUserFirstName').val();
							organization = jQuery('#masterUserOrganization')
									.val();
						} else {
							lastName = jQuery('#masterUserLastName').val();
							firstName = jQuery('#masterUserFirstName').val();
						}
						masterUser_jQueryDataTableAjax(serviceURL, firstName,
								lastName, organization);
					}
					jQuery("#resetsearch_masteruser_btn").click(
							function(e) {
								e.preventDefault();
								var serviceURL = envConfig.serviceBaseURL
										+ '/user/viewMasterUser.action?userId='
										+ userId;
								lastName = jQuery('#masterUserLastName')
										.val("");
								firstName = jQuery('#masterUserFirstName').val(
										"");
								organization = jQuery('#masterUserOrganization').val(
								"");
								firstName = "";
								lastName = "";
								organization = "";
								masterUser_jQueryDataTableAjax(serviceURL,
										firstName, lastName, organization);
							});
					jQuery("#search_masteruser_btn").click(function(e) {
						masterUserTable_Search();

					});
					jQuery('#addMasteruser_breadcrumb')
							.click(
									function(e) {
										e.preventDefault();
										var isAdmin = sessionStorage
												.getItem("isAdmin");
										if (isAdmin == "Org Admin") {
											window.location.href = "../dashboard/dashboard.html";
										} else if (isAdmin == "Master Admin") {
											window.location.href = "../dashboard/registrationApprovals.html";
										}
									});

					jQuery('#addMasteruser_logo')
							.click(
									function(e) {
										e.preventDefault();
										var isAdmin = sessionStorage
												.getItem("isAdmin");
										if (isAdmin == "Org Admin") {
											window.location.href = "../dashboard/dashboard.html";
										} else if (isAdmin == "Master Admin") {
											window.location.href = "../dashboard/registrationApprovals.html";
										}
									});

					var serviceURL = envConfig.serviceBaseURL
							+ '/user/viewMasterUser.action?userId=' + userId;
					firstName = "";
					lastName = "";
					organization = "";
					masterUser_jQueryDataTableAjax(serviceURL, firstName,
							lastName, organization);
					function loadOrgData(serviceURL, userId) {
						var userId = sessionStorage.getItem("userId");
						jQuery
								.ajax({

									url : serviceURL,
									dataType : "json",
									data : {
										userId : userId
									},
									type : 'Post',
									success : function(response) {
										var JsonStringify_Data = JSON
												.stringify(response);
										var isAdmin = sessionStorage
												.getItem("isAdmin");
										if (isAdmin == "Master Admin") {
											jQuery('#masterUserOrganization')
													.val("");
										} else {
											jQuery('#masterUserOrganization')
													.val(
															response.data.organizationName);

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
					}
					function customblockUI() {
						jQuery("#loading-div-background").show();
					}
					function customunblockUI() {
						jQuery("#loading-div-background").hide();
					}
				});