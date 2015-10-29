jQuery(document)
		.ready(
				function() {
					jQuery("#testSuite").addClass("active");
					jQuery("#addtestsuite").addClass("active");

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					var userId = sessionStorage.getItem("userId");
					var isAdmin = sessionStorage.getItem("isAdmin");

					jQueryselect = jQuery('#newTestSuite_dropdownselect');
					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/project/viewProjectList.action?userId='
							+ userId;
					console.log(serviceURL);
					hideAllWarning();
					jQuery.ajax({
						url : serviceURL,
						dataType : 'json',
						type : 'GET',
						success : function(data) {
							var JsonStringify_Data = JSON.stringify(data);
							var obj = jQuery.parseJSON(JsonStringify_Data);

							var arr = [];
							jQuery.each(obj, function(i, e) {
								jQuery.each(e, function(key, val) {
									arr.push(val);
								});
							});

							var result = jQuery.map(arr, function(val, key) {
								return {
									projectId : val.projectId,
									projectName : val.projectName
								};
							});

							// iterate over the data and append a select option
							jQuery.each(result, function(key, val) {
								jQueryselect.append('<option id="'
										+ val.projectId + '">'
										+ val.projectName + '</option>');
							})
							jQuery("#newTestSuite_dropdownselect").focus();
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

					jQuery('#newTestSuite_cancel_btn').click(function(event) {
						window.location.href = 'testSuites.html';
					});

					jQuery('.only_alphabets').bind(
							'keyup blur',
							function() {

								jQuery(this).val(
										jQuery(this).val().replace(/\s/g, ''));
							});
					var userId = sessionStorage.getItem("userId");
					jQuery('#upload_jar_error').hide();

					jQuery("#newTestSuite_dropdownselect").focus();
					jQuery("#addTestSuiteBtn")
							.click(
									function(e) {

										e.preventDefault();

										var projectId = jQuery(
												'#newTestSuite_dropdownselect')
												.children(":selected").attr(
														"id");
										var serviceURL = envConfig.serviceBaseURL
												+ '/testSuite/saveTestSuite.action?userId='
												+ userId;
										console.log(serviceURL);
										var testSuiteURl = jQuery(
												'#newTestSuite_testSuiteUrl')
												.val();
										var testSuiteXML_Path = jQuery(
												'#newTestSuite_testSuiteXmlPathInJar')
												.val();
										testSuiteURl = testSuiteURl
												.toUpperCase();
										testSuiteURl = testSuiteURl.split(".")[1];
										// alert(testSuiteURl);
										testSuiteXML_Path = testSuiteXML_Path
												.toUpperCase();
										testSuiteXML_Path = testSuiteXML_Path
												.split(".")[1];

										if (jQuery(
												'#newTestSuite_dropdownselect option:selected')
												.val() == '') {
											jQuery(
													'#newTestSuite_dropdownselect')
													.css(
															{
																"border" : "1px solid red",
															});
											jQuery('#select_project_error').show();
											jQuery("#newTestSuite_dropdownselect").focus();
											return false;
										} else if (jQuery('#newTestSuite_testSuiteName')
												.val() == '') {
											jQuery('#newTestSuite_testSuiteName')
													.css(
															{
																"border" : "1px solid red",
															});
											jQuery('#test_suite_error').show();
											jQuery('#newTestSuite_testSuiteName').focus();
											return false;
										} else if (jQuery(
												'#newTestSuite_testSuiteUrl')
												.val() == '') {
											jQuery('#newTestSuite_testSuiteUrl')
													.css(
															{
																"border" : "1px solid red",
															});
											jQuery('#newTestSuite_testSuiteName')
													.css({
														"border" : "",
													});
											jQuery('#test_suite_url').show();
											jQuery('#test_suite_error').hide();
											jQuery('#newTestSuite_testSuiteUrl').focus();
											return false;
										} else if (testSuiteURl != "XML") {
											jQuery('#newTestSuite_testSuiteUrl')
													.css(
															{
																"border" : "1px solid red",
															});
											jQuery('#newTestSuite_testSuiteXmlPathInJar')
													.css({
														"border" : "",
													});
											jQuery('#test_suite_url_invalid').show();
											jQuery('#test_suite_xml_invalid').hide();
											jQuery('#test_suite_xml').hide();
											jQuery('#test_suite_url').hide();
											jQuery('#newTestSuite_testSuiteUrl').focus();
											return false;
										} else if (jQuery('#newTestSuite_testSuiteXmlPathInJar')
												.val() == '') {
											jQuery('#newTestSuite_testSuiteXmlPathInJar')
													.css(
															{
																"border" : "1px solid red",
															});
											jQuery('#newTestSuite_testSuiteUrl')
													.css({
														"border" : "",
													});
											jQuery('#test_suite_xml').show();
											jQuery('#test_suite_url').hide();
											jQuery('#newTestSuite_testSuiteXmlPathInJar').focus();
											return false;
										}
										else if (testSuiteXML_Path != "XML") {
											jQuery('#newTestSuite_testSuiteXmlPathInJar')
													.css(
															{
																"border" : "1px solid red",
															});
											jQuery('#newTestSuite_testSuiteUrl')
													.css({
														"border" : "",
													});

											jQuery('#test_suite_xml_invalid').show();
											jQuery('#test_suite_url_invalid').hide();
											jQuery('#test_suite_xml').hide();
											jQuery('#test_suite_url').hide();
											jQuery('#newTestSuite_testSuiteXmlPathInJar').focus();
											return false;
										} else {
											hideAllWarning();
											jQuery
													.ajax({
														url : serviceURL,
														dataType : "json",
														type : 'POST',
														data : {

															suiteName : jQuery(
																	'#newTestSuite_testSuiteName')
																	.val(),
															projectName : jQuery(
																	'#newTestSuite_dropdownselect')
																	.val(),
															notes : jQuery(
																	'#newTestSuite_notes')
																	.val(),
															isActive : 'y',
															testSuiteUrl : jQuery(
																	'#newTestSuite_testSuiteUrl')
																	.val(),
															testSuiteXmlPathInJar : jQuery(
																	'#newTestSuite_testSuiteXmlPathInJar')
																	.val(),
															project : jQuery(
																	'#newTestSuite_dropdownselect')
																	.children(
																			":selected")
																	.attr("id"),
															userId : userId
														},
														success : function(data) {
															var responseTextFlag = data.success;
															if (responseTextFlag == true) {

																jQuery('#upload_jar_error').hide();
																jQuery('#select_project_error').hide();
																jQuery('#test_suite_error').hide();
																jQuery('#test_suite_url').hide();
																jQuery('#test_suite_xml').hide();
																jQuery("#loading-div-background").hide();
																jQuery('#success_modal').modal('toggle');
																jQuery('#success_modal').modal('view');
															} else {
																jQuery("#loading-div-background").hide();
																jQuery('#error_modal').modal('toggle');
																jQuery('#error_modal').modal('view');
																jQuery('#select_project_error').hide();
																jQuery('#test_suite_error').hide();
																jQuery('#test_suite_url').hide();
																jQuery('#test_suite_xml').hide();
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

					jQuery('#newTestSuite_testSuiteName').focus();
					jQuery("#newTestSuite_testSuiteName").on("input",
							function() {
								jQuery('#test_suite_error').hide();
								jQuery('#newTestSuite_testSuiteName').css({
									"border" : "1px solid #0866c6",
								});
							});
					jQuery("#newTestSuite_testSuiteUrl").on("input",
							function() {
								jQuery('#test_suite_url_invalid').hide();
								jQuery('#test_suite_xml').hide();
								jQuery('#test_suite_url').hide();
								jQuery('#test_suite_xml_invalid').hide();
								jQuery('#newTestSuite_testSuiteUrl').css({
									"border" : "1px solid #0866c6",
								});
							});

					jQuery("#newTestSuite_testSuiteXmlPathInJar").on(
							"input",
							function() {
								jQuery('#test_suite_url_invalid').hide();
								jQuery('#upload_jar_error').hide();
								jQuery('#test_suite_xml').hide();
								jQuery('#test_suite_xml_invalid').hide();
								jQuery('#newTestSuite_testSuiteXmlPathInJar')
										.css({
											"border" : "1px solid #0866c6",
										});
							});
					jQuery("#newTestSuite_dropdownselect").change(function() {
						jQuery('#select_project_error').hide();
						jQuery('#newTestSuite_dropdownselect').css({
							"border" : "1px solid #0866c6",
						});
					});

					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "testSuites.html";
					});
					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "newTestSuite.html";
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