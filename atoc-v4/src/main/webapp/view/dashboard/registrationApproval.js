jQuery(document)
		.ready(
				function() {

					jQuery(".active").removeClass("active");
					jQuery("#regapproval").addClass("active");

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					jQuery('#approve_btn').prop('disabled', false);
					jQuery('.only_alphabets').bind(
							'keyup blur',
							function() {
								jQuery(this).val(
										jQuery(this).val().replace(/\s/g, ''));
							});

					jQuery("#close").click(function(event) {
						jQuery('#reApprovalNotes').hide();
						jQuery('#notes').css({
							"border" : "",
						});
						jQuery("#loading-div-background").hide();
					});
					jQuery("#winclose").click(function(event) {
						jQuery('#reApprovalNotes').hide();
						jQuery('#notes').css({
							"border" : "",
						});
						jQuery("#loading-div-background").hide();
					});

					jQuery('#notes').val('');
					jQuery("#notes").on("input", function() {
						jQuery('#reApprovalNotes').hide();
						jQuery('#notes').focus();

						jQuery('#notes').css({
							"border" : "1px solid #0866c6",
						// "background" : "#FFCECE"
						});
					});

					jQuery('#loading_cancel_btn').click(function(event) {
						// jQuery.unblockUI();
					});

					jQuery("#approve_btn")
							.click(
									function(event) {
										// ATC-92 issue:start
										jQuery('#approve_btn').prop('disabled',
												true);
										jQuery('#regApproval_Modal').modal(
												'toggle');
										jQuery('#regApproval_Modal').modal(
												'hide');
										// End issue
										var userId = sessionStorage
												.getItem("userId");
										var target = (event.target.id);
										/*
										 * jQuery .blockUI({ message :
										 * jQuery('#loading_Message'), css : {
										 * border : '1px solid #30a5ff' } });
										 */
										var serviceURL = envConfig.serviceBaseURL
												+ '/customer/updateCustomer.action?userId='
												+ userId;
										console.log(serviceURL);

										/*
										 * var x = document
										 * .getElementById("id"); var
										 * customerRegistrationId = x.innerHTML;
										 */
										var customerRegId = localStorage
												.getItem('customerRegId');
										// jQuery.blockUI({ message:
										// jQuery('#loading_Message'), css: {
										// border: '1px solid #30a5ff' } });
										jQuery("#loading-div-background")
												.show();
										jQuery
												.ajax({
													url : serviceURL,
													dataType : "json",
													data : {
														customerRegistrationId : customerRegId,
														notes : jQuery('#notes')
																.val(),
														approvalStatus : 'APPROVED'
													},
													type : 'Post',
													success : function(data) {
														jQuery(
																"#loading-div-background")
																.hide();
														// jQuery.unblockUI();
														// alert("APPROVED
														// Successfully");
														window.location.href = "../dashboard/registrationApprovals.html";
													},
													failure : function(data) {
														// jQuery.unblockUI();
														// alert( "Failed" );
														window.location.href = "../../";
													},
													statusCode : {
														403 : function(xhr) {
															// jQuery.unblockUI();
															alert("Session will be Expired");
															window.location.href = "../../";

														}
													}

												});
									});

					jQuery("#reject_btn")
							.click(
									function(event) {

										if (jQuery('#notes').val() == ''
												|| jQuery('#notes').val() == 'undefined') {
											jQuery('#notes').css({
												"border" : "1px solid red",
											// "background": "#FFCECE"
											});
											jQuery('#reApprovalNotes').show();
											jQuery('#notes').focus();
											return false;
										} else {
											jQuery('#reApprovalNotes').hide();
											var userId = sessionStorage
													.getItem("userId");
											var target = (event.target.id);
											var customerRegId = localStorage
													.getItem('customerRegId');
											// alert(customerRegId);
											// var x =
											// document.getElementById("id");
											// var customerRegistrationId =
											// x.innerHTML;
											var serviceURL = envConfig.serviceBaseURL
													+ '/customer/updateCustomer.action?userId='
													+ userId;
											console.log(serviceURL);
											jQuery('#regApproval_Modal').modal(
													'toggle');
											// jQuery.blockUI({ message:
											// jQuery('#loading_Message'), css:
											// { border: '1px solid #30a5ff' }
											// });
											jQuery
													.ajax({
														url : serviceURL,
														dataType : "json",
														data : {
															customerRegistrationId : customerRegId,
															notes : jQuery(
																	'#notes')
																	.val(),
															approvalStatus : 'REJECTED'
														},
														type : 'Post',
														success : function(data) {
															// jQuery.unblockUI();
															window.location.href = "../dashboard/registrationApprovals.html";
														},
														statusCode : {
															403 : function(xhr) {
																/*
																 * jQuery
																 * .unblockUI();
																 */
																alert("Session will be Expired");
																window.location.href = "../../";

															}
														}

													});
										}
									});
					var isAdmin = sessionStorage.getItem("isAdmin");
					if (isAdmin == "Master Admin") {

						var userId = sessionStorage.getItem("userId");
						var serviceURL = envConfig.serviceBaseURL
								+ '/customer/viewCustomerListPagination.action?userId='
								+ userId;
						console.log(serviceURL);
						// jQuery(document).ajaxStart(jQuery.blockUI({ message:
						// jQuery('#loading_Message'), css: { border: '1px solid
						// #30a5ff' } }))
						// .ajaxStop(jQuery.unblockUI);
						var table1 = jQuery("#CustomerDataTable")
								.DataTable(
										{
											"bFilter" : false,
											"bSort" : false,
											"bProcessing" : false,
											"sAjaxSource" : serviceURL,
											"bServerSide" : true,
											columnDefs : [ {
												orderable : false,
												targets : -1
											} ],
											"fnDrawCallback" : function(
													oSettings) {
												jQuery.uniform.update();
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
														"mData" : "organizationName"
													},
													{
														"mData" : "firstname"
													},
													{
														"mData" : "lastname"
													},
													{
														"mData" : "contactEmail"
													},
													{
														"mData" : "contactPhone"
													},

													{
														"mData" : "customerRegistrationId",
														"bSortable" : false,
														"mRender" : function(
																customerRegistrationId) {

															return '<a href = "#" onClick = "viewRegDetail('
																	+ customerRegistrationId
																	+ ');" id=\"customerReg_view\"><span class=\"glyphicon glyphicon-eye-open btn btn-info\"></span></a>';
														}
													} ]
										});

					} else if (isAdmin == "Org Admin") {
					} else if (isAdmin == "Org User") {
					}

				});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}

function viewRegDetail(customerRegistrationId) {
	var userId = sessionStorage.getItem("userId");

	var serviceURL = envConfig.serviceBaseURL
			+ '/customer/getCustomerInfo.action?userId=' + userId
			+ '&customerRegistrationId=' + customerRegistrationId;

	jQuery("#loading-div-background").show();
	jQuery.ajax({
		url : serviceURL,
		dataType : "json",
		data : {
			customerRegistrationId : customerRegistrationId
		},
		type : 'Post',
		success : function(response) {
			/*
			 * var JsonStringify_Data = JSON.stringify(response.data); var
			 * editProject_Data = response.data;
			 * tag.html(editProject_Data.html).dialog({modal:
			 * '#regApproval_Modal'}).dialog('open');
			 */
			var data = response.data;
			// jQuery('#regApproval_Modal').html(response).modal();
			jQuery("#loading-div-background").hide();

			jQuery('#id').text(data.customerRegistrationId);
			localStorage.setItem('customerRegId', data.customerRegistrationId);
			sessionStorage.setItem("customerRegistrationId",
					data.customerRegistrationId);

			jQuery('#name').text(data.firstname);
			jQuery('#lastName').text(data.lastname);
			jQuery('#address').text(data.address);
			jQuery('#city').text(data.city);

			jQuery('#cphone').text(data.contactPhone);
			jQuery('#cweb').text(data.customerWebsite);

			jQuery('#state').text(data.state);
			jQuery('#country').text(data.country);
			jQuery('#zip').text(data.zipCode);
			jQuery('#cname').text(data.contactName);
			jQuery('#cemail').text(data.contactEmail);
			jQuery('#organizationName').text(data.organizationName);
			// ATC-92 issue
			/*
			 * jQuery('#approve_btn').bind('click',function() {
			 * submit_click(jQuery(this)) });
			 */
			jQuery('#regApproval_Modal').modal('toggle');
			jQuery('#regApproval_Modal').modal('view');

			jQuery('#regApproval_Modal').on('show.bs.modal', function() {
				// alert("In Modal shown");
				jQuery('#notes').focus();
			});

		},
		failure : function(data) {
			window.location.href = "../";

		},
		statusCode : {
			403 : function(xhr) {
				alert("Session will be Expired");
				window.location.href = "../";

			}
		}

	});

}