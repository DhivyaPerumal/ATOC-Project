jQuery(document)
		.ready(
				function() {
					jQuery("#project").addClass("active");
					jQuery("#addproject").addClass("active");
					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					var userId = sessionStorage.getItem("userId");
					var enteredProjectName = "";
					jQuery('#newProject_cancel_btn').click(function(event) {
						jQuery('#proName').val('');
						jQuery('#note').val('');
						jQuery('#jarName').val('');
						window.location.href = 'projects.html';
					});
					jQuery('#proName').val("");
					jQuery('#note').val("");
					jQuery('#jarName').val("");

					jQuery('.only_alphabets').bind(
							'keyup blur',
							function() {

								jQuery(this).val(
										jQuery(this).val().replace(/\s/g, ''));
							});
					var userId = sessionStorage.getItem("userId");

					jQuery('#proName').val('');
					jQuery('#proName').focus();
					jQuery("#addProjectBtn")
							.click(
									function(e) {
										e.preventDefault();
										var userId = sessionStorage
												.getItem("userId");
										var serviceURL = envConfig.serviceBaseURL
												+ '/project/saveProject.action?userId='
												+ userId;
										var dotpos = jQuery('#jarName').val();
										dotpos1 = dotpos.lastIndexOf(".jar");
										var value = dotpos.replace(".jar", "");
										var projectName = jQuery('#proName')
												.val();
										if (jQuery('#proName').val() == '') {
											jQuery('#proName').css({
												"border" : "1px solid red",
											});
											jQuery('#projectNameError').show();
											jQuery('#proName').focus();
											return false;
										} else if (jQuery('#proName').val().length < 5) {
											jQuery('#proName').css({
												"border" : "1px solid red",
											});
											jQuery('#jarName').css({
												"border" : "",
											});
											jQuery('#projectNameErrorValue')
													.show();
											jQuery('#projectJarError').hide();
											jQuery('#projectNameError').hide();
											jQuery('#proName').focus();
											return false;
										} else if (jQuery('#jarName').val() == '') {
											jQuery('#jarName').css({
												"border" : "1px solid red",
											});
											jQuery('#proName').css({
												"border" : "",
											});
											jQuery('#projectJarError').show();
											jQuery('#projectNameError').hide();
											jQuery('#jarName').focus();
											return false;
										} else if (jQuery('#jarName').val().length < 5) {
											jQuery('#jarName').css({
												"border" : "1px solid red",
											});
											jQuery('#proName').css({
												"border" : "",
											});
											jQuery('#projectJarErrorValue')
													.show();
											jQuery('#projectNameErrorValue')
													.hide();
											jQuery('#projectJarError').hide();
											jQuery('#projectNameError').hide();
											jQuery('#jarName').focus();
											return false;
										}
										else if (value != projectName) {
											jQuery('#proName').css({
												"border" : "1px solid red",
											});
											jQuery('#jarName').css({
												"border" : "1px solid red",
											});
											jQuery('#projectJarErrorValuejarFixing').show();
											jQuery('#projectJarErrorValuejar').hide();
											jQuery('#projectJarErrorValue').hide();
											jQuery('#projectJarError').hide();
											jQuery('#jarName').focus();
											return false;
										} else if (dotpos1 == -1) {
											jQuery('#proName').css({
												"border" : "",
											});
											jQuery('#jarName').css({
												"border" : "1px solid red",
											});
											jQuery('#projectJarErrorValuejar').show();
											jQuery('#projectJarErrorValuejarFixing').hide();
											jQuery('#projectJarError').hide();
											jQuery('#projectJarErrorValue')
													.hide();
											jQuery('#jarName').focus();
											return false;
										}
										else {
											console.log(serviceURL);
											hideAllErrors();
											jQuery
													.ajax({
														url : serviceURL,
														cache : false,
														dataType : "json",
														data : {
															projectName : jQuery(
																	'#proName')
																	.val(),
															notes : jQuery(
																	'#note')
																	.val(),
															projectType : 'java',
															createdBy : userId,
															updatedBy : userId,
															isActive : 'yes',
															projectJarName : jQuery(
																	'#jarName')
																	.val(),
															userId : userId
														},
														type : 'Post',
														success : function(data) {
															var responseTextFlag = data.success;

															if (responseTextFlag == true) {

																jQuery('#projectJarErrorValuejar').hide();
																jQuery('#projectJarErrorValuejarFixing').hide();
																jQuery('#projectJarError').hide();
																jQuery('#projectJarErrorValue').hide();
																jQuery('#projectAlreadyExists').hide();
																jQuery("#loading-div-background").hide();
																jQuery('#success_project_modal').modal('toggle');
																jQuery('#success_project_modal').modal('view');
															} else {
																jQuery("#loading-div-background").hide();
																jQuery('#error_modal').modal('toggle');
																jQuery('#error_modal').modal('view');
																jQuery('#projectJarErrorValuejar').hide();
																jQuery('#projectJarErrorValuejarFixing').hide();
																jQuery('#projectJarError').hide();
																jQuery('#projectJarErrorValue').hide();
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
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projects.html";
					});
					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "newProject.html";
					});
					jQuery('#proName').focus();
					jQuery("#proName").on("input", function() {
						jQuery('#projectNameError').hide();
						jQuery('#projectNameErrorValue').hide();
						jQuery('#proName').css({
							"border" : "1px solid #0866c6",
						});
					});
					jQuery("#jarName").on("input", function() {
						jQuery('#projectJarErrorValuejarFixing').hide();
						jQuery('#projectJarErrorValue').hide();
						jQuery('#projectJarErrorValuejar').hide();
						jQuery('#projectJarError').hide();
						jQuery('#jarName').css({
							"border" : "1px solid #0866c6",
						});
						jQuery('#proName').css({
							"border" : "1px solid #0866c6",
						});
					});
					var data = JSON.parse(localStorage
							.getItem('editProject_Data'));
					if (data == null) {
						jQuery('#addProjectBtn_id').show();
						jQuery('#editprojectBtn_id').hide();
					} else {
						jQuery('#addProjectBtn_id').hide();
						jQuery('#editprojectBtn_id').show();
					}
				});
	function hideAllErrors() {
	jQuery('#projectNameError').hide();
	jQuery('#projectNameErrorValue').hide();
	jQuery('#projectAlreadyExists').hide();
	jQuery('#projectJarError').hide();
	jQuery('#projectJarErrorValue').hide();
	jQuery('#projectJarErrorValuejar').hide();
	jQuery('#projectJarErrorValuejarFixing').hide();
}
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}