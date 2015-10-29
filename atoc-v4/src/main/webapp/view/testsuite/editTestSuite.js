jQuery(document)
		.ready(
				function() {

					var userId = sessionStorage.getItem("userId");
					var isAdmin = sessionStorage.getItem("isAdmin");
					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					var data = JSON.parse(localStorage
							.getItem('JsonStringify_TestSuiteData'));
					if (data != null) {
						jQuery("#testSuiteName").val(data.suiteName);
						jQuery("#testSuiteId").val(data.testSuiteId);
						jQuery("#testSuiteUrl").val(data.testSuiteUrl);
						jQuery("#selectedProject").text(data.projectName);
						jQuery("#notes").val(data.notes);
						jQuery("#projectId").val(data.projectId);
						jQuery("#testSuiteXmlPathInJar").val(
								data.testSuiteXmlPathInJar);

					} else {
						localStorage.removeItem("JsonStringify_TestSuiteData");

					}
					jQuery('#editTestSuite_cancel_btn').click(function(event) {

						window.location.href = 'testSuites.html';

					});

					jQuery('.only_alphabets').bind(
							'keyup blur',
							function() {

								jQuery(this).val(
										jQuery(this).val().replace(/\s/g, ''));
							});
					var userId = sessionStorage.getItem("userId");
					jQuery('#editTestSuiteProjectError').hide();

					jQuery('#editTestSuiteNameError').hide();
					jQuery('#editTestSuiteUrlError').hide();
					jQuery('#editTestSuiteXmlError').hide();

					jQuery("#testSuiteName").on("input", function() {
						jQuery('#editTestSuiteNameError').hide();
						jQuery(testSuiteName).css({
							"border" : "",
							"background" : ""
						});
					});
					jQuery("#testSuiteXmlPathInJar").on("input", function() {
						jQuery('#editTestSuiteXmlError').hide();
						jQuery(testSuiteXmlPathInJar).css({
							"border" : "",
							"background" : ""
						});
					});

					jQuery("#testSuiteUrl").on("input", function() {
						jQuery('#editTestSuiteUrlError').hide();
						jQuery(testSuiteUrl).css({
							"border" : "",
							"background" : ""
						});
					});

					var userId = sessionStorage.getItem("userId");
					jQuery("#saveChanges_btn")
							.click(
									function(e) {
										hideAllWarning();
										e.preventDefault();
										var JsonStringifydata = JSON
												.parse(localStorage
														.getItem('JsonStringify_TestSuiteData'));
										var projectId = JsonStringifydata.projectId;
										var testSuiteId = jQuery('#testSuiteId')
												.val();

										var serviceURL = envConfig.serviceBaseURL
												+ '/testSuite/saveTestSuite.action?userId='
												+ userId;
										console.log(serviceURL);

										jQuery('#editTestSuiteProjectError')
												.show();

										jQuery('#editTestSuiteNameError')
												.show();
										jQuery('#editTestSuiteUrlError').show();
										jQuery('#editTestSuiteXmlError').show();
										var isValid = true;
										jQuery(
												'#dropdownselect,#testSuiteName,#testSuiteUrl,#testSuiteXmlPathInJar')
												.each(
														function() {
															if (jQuery
																	.trim(jQuery(
																			this)
																			.val()) == '') {

																isValid = false;
																jQuery('#editTestSuiteNameError').show();
																jQuery('#editTestSuiteUrlError').show();
																jQuery('#editTestSuiteXmlError').show();
																jQuery(this)
																		.css(
																				{
																					"border" : "1px solid red",
																				});
															} else {
																jQuery('#editTestSuiteNameError').show();
																jQuery(this)
																		.css(
																				{

																					"border" : "",
																					"background" : ""
																				});

																jQuery('#editTestSuiteProjectError').hide();
																jQuery('#editTestSuiteNameError').hide();
																jQuery('#editTestSuiteUrlError').hide();
																jQuery('#editTestSuiteXmlError').hide();
															}
														});
										if (isValid == false)
											return;
										jQuery
												.ajax({

													url : serviceURL,
													dataType : "json",
													type : 'POST',
													data : {
														project : projectId,
														suiteName : jQuery('#testSuiteName').val(),
														testSuiteId : testSuiteId,
														notes : jQuery('#notes')
																.val(),
														createdBy : userId,
														updatedBy : userId,
														isActive : 'y',
														testSuiteUrl : jQuery('#testSuiteUrl').val(),
														testSuiteXmlPathInJar : jQuery('#testSuiteXmlPathInJar').val(),
														userId : userId

													},
													success : function(data) {

														if (data.success == true) {
															localStorage.removeItem("JsonStringify_TestSuiteData");
															jQuery("#loading-div-background").hide();
															jQuery('#success_modal').modal('toggle');
															jQuery('#success_modal').modal('view');
														} else {
															localStorage.removeItem("JsonStringify_TestSuiteData");
															jQuery("#loading-div-background").hide();
															jQuery('#failure_modal').modal('toggle');
															jQuery('#failure_modal').modal('view');
														}
													},
													failure : function(data) {
														localStorage.removeItem("JsonStringify_TestSuiteData");
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
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "testSuites.html";
					});
					jQuery('#failure_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "testSuites.html";
					});
				});
function hideAllWarning() {
	jQuery('#editTestSuiteProjectError').hide();
	jQuery('#editTestSuiteNameError').hide();
	jQuery('#editTestSuiteUrlError').hide();
	jQuery('#editTestSuiteXmlError').hide();
}

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}