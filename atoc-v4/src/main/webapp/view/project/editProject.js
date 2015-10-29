jQuery(document)
		.ready(
				function() {
					jQuery("#project").addClass("active");
					jQuery("#projectlist").addClass("active");

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					var userId = sessionStorage.getItem("userId");

					jQuery('.only_alphabets').bind(
							'keyup blur',
							function() {

								jQuery(this).val(
										jQuery(this).val().replace(/\s/g, ''));
							});
					var userId = sessionStorage.getItem("userId");
					var data = JSON.parse(localStorage
							.getItem('editProject_Data'));
					if (data !== null) {

						jQuery('#projectId').val(data.projectId);
						localStorage.setItem('projectId_edit', data.projectId);
						jQuery("#projectName").val(data.projectName);
						jQuery("#notes").val(data.notes);
						jQuery("#startDatePicker").val(data.startDate);
						jQuery("#endDatePicker").val(data.endDate);
						jQuery("#jarName1").val(data.projectPath);
						jQuery('#newProject_div').hide('fast');
						jQuery('#editProject_div').show('fast');

					} else {
						jQuery('#editProject_div').hide('fast');
						jQuery('#newProject_div').show('fast');
					}
					var enteredProjectName = "";

					jQuery('.only_alphabets').bind(
							'keyup blur',
							function() {

								jQuery(this).val(
										jQuery(this).val().replace(/\s/g, ''));
							});
					jQuery("#projectName").on("input", function() {
						jQuery('#editProjectNameError').hide();
						jQuery('#editProjectNameErrorValue').hide();
						jQuery(projectName).css({
							"border" : "",
						});
					});

					jQuery("#jarName1").on("input", function() {
						jQuery('#editProjectJarError').hide();
						jQuery('#editProjectJarErrorValue').hide();
						jQuery(projectName).css({
							"border" : "",
						});
					});

					jQuery('#editProject_cancel_btn').click(function(event) {
						jQuery('#projectName').val('');
						jQuery('#notes').val('');
						jQuery('#jarName1').val('');
						window.location.href = 'projects.html';
					});
					jQuery("#saveChanges_btn")
							.click(
									function(e) {
										e.preventDefault();

										var userId = sessionStorage
												.getItem("userId");
										var serviceURL = envConfig.serviceBaseURL
												+ '/project/saveProject.action?userId='
												+ userId;
										var dotpos1 = jQuery('#jarName1').val();
										dotpos2 = dotpos1.lastIndexOf(".jar");
										var value1 = dotpos1
												.replace(".jar", "");
										var projectNameJarFile = jQuery(
												'#projectName').val();
										if (jQuery('#projectName').val() == '') {
											jQuery('#projectName').css({
												"border" : "1px solid red",
											});
											jQuery('#editProjectNameError')
													.show();
											jQuery('#projectName').focus();
											return false;
										} else if (jQuery('#projectName').val().length < 5) {
											jQuery('#projectName').css({
												"border" : "1px solid red",
											});
											jQuery('#jarName1').css({
												"border" : "",
											});
											jQuery('#editProjectNameErrorValue')
													.show();
											jQuery('#editProjectNameError')
													.hide();
											jQuery('#editProjectJarErrorValue')
													.hide();
											jQuery('#editProjectJarError')
													.hide();
											jQuery('#projectName').focus();
											return false;
										}

										else if (jQuery('#jarName1').val() == '') {
											jQuery('#jarName1').css({
												"border" : "1px solid red",
											});
											jQuery('#projectName').css({
												"border" : "",
											});
											jQuery('#editProjectJarError')
													.show();
											jQuery('#editProjectNameError')
													.hide();
											jQuery('#editProjectNameErrorValue')
													.hide();
											jQuery('#editProjectJarErrorValue')
													.hide();
											jQuery('#jarName1').focus();
											return false;
										} else if (jQuery('#jarName1').val().length < 5) {
											jQuery('#jarName1').css({
												"border" : "1px solid red",
											});
											jQuery('#projectName ').css({
												"border" : "",
											});
											jQuery('#editProjectJarErrorValue')
													.show();
											jQuery('#editProjectNameErrorValue')
													.hide();
											jQuery('#editProjectJarError')
													.hide();
											jQuery('#editProjectNameError')
													.hide();
											jQuery('#jarName1').focus();
											return false;
										} else if (value1 != projectNameJarFile) {

											jQuery('#projectName').css({
												"border" : "1px solid red",
											});
											jQuery('#jarName1').css({
												"border" : "1px solid red",
											});
											jQuery(
													'#editProjectJarErrorValueDisplay')
													.show();
											jQuery('#editProjectJarError')
													.hide();
											jQuery('#editProjectJarErrorValue')
													.hide();
											jQuery('#editProjectNameErrorValue')
													.hide();
											jQuery('#editProjectNameErro')
													.hide();
											jQuery('#jarName1').focus();
											return false;
										}

										else if (dotpos2 == -1) {
											jQuery('#projectName').css({
												"border" : "",
											});
											jQuery('#jarName1').css({
												"border" : "1px solid red",
											});
											jQuery(
													'#editProjectJarErrorValueDisplayJar')
													.show();
											jQuery(
													'#editProjectJarErrorValueDisplay')
													.hide();
											jQuery('#editProjectJarError')
													.hide();
											jQuery('#editProjectNameError')
													.hide();
											jQuery('#editProjectNameErrorValue')
													.hide();
											jQuery('#jarName1').focus();
											return false;
										} else {
											console.log(serviceURL);
											hideAllErrors();
											var projectId_edit = localStorage
													.getItem('projectId_edit');
											jQuery
													.ajax({
														url : serviceURL,
														dataType : "json",
														cache : false,
														type : 'POST',
														data : {

															projectId : projectId_edit,
															projectName : jQuery(
																	'#projectName')
																	.val(),
															notes : jQuery(
																	'#notes')
																	.val(),
															createdBy : userId,
															updatedBy : userId,
															isActive : 'yes',
															projectType : 'java',
															projectJarName : jQuery(
																	'#jarName1')
																	.val(),
															userId : userId
														},
														success : function(data) {

															var responseTextFlag = data.success;

															if (responseTextFlag == true) {

																jQuery('#editProjectJarErrorValueDisplayJar').hide();
																jQuery('#editProjectJarErrorValueDisplay').hide();
																jQuery('#editProjectJarError').hide();
																jQuery('#editProjectNameError').hide();
																jQuery('#editProjectNameErrorValue').hide();
																jQuery('#editProjectNameErrorValue').hide();
																jQuery("#loading-div-background").hide();
																jQuery('#success_project_modal').modal('toggle');
																jQuery('#success_project_modal').modal('view');
															} else {
																jQuery("#loading-div-background").hide();
																jQuery('#error_modal').modal('toggle');
																jQuery('#error_modal').modal('view');
																jQuery('#editProjectJarErrorValueDisplayJar').hide();
																jQuery('#editProjectJarErrorValueDisplay').hide();
																jQuery('#editProjectJarError').hide();
																jQuery('#editProjectNameError').hide();
																jQuery('#editProjectNameErrorValue').hide();
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

					jQuery('#projectName').focus();
					jQuery("#projectName").on("input", function() {
						jQuery('#editProjectNameError').hide();
						jQuery('#editProjectNameErrorValue').hide();
						jQuery('#projectName').css({
							"border" : "1px solid #0866c6",
						});
					});

					jQuery("#jarName1").on("input", function() {
						jQuery('#editProjectJarErrorValueDisplay').hide();
						jQuery('#editProjectNameErrorValue').hide();
						jQuery('#editProjectJarErrorValueDisplayJar').hide();
						jQuery('#editProjectJarError').hide();
						jQuery('#jarName1').css({
							"border" : "1px solid #0866c6",
						});
						jQuery('#projectName').css({
							"border" : "1px solid #0866c6",
						});
					});

					function hideAllErrors() {
						jQuery('#editProjectNameError').hide();
						jQuery('#editProjectNameErrorValue').hide();
						jQuery('#editProjectJarError').hide();
						jQuery('#editProjectJarErrorValueDisplayJar').hide();
						jQuery('#editProjectJarErrorValueDisplay').hide();
					}
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "projects.html";
					});

					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "editProject.html";
					});

				});
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}