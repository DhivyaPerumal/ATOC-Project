jQuery('#checkAll_mail').change(function() {
	jQuery('.chk_mail').prop('checked', this.checked);
});
jQuery('#checkAll_apache').change(function() {
	jQuery('.chk_apache').prop('checked', this.checked);
});
jQuery('#checkAll_others').change(function() {
	jQuery('.chk_others').prop('checked', this.checked);
});

jQuery(".chk_mail").change(function() {
	if (jQuery(".chk_mail:checked").length == jQuery(".chk_mail").length) {
		jQuery('#checkAll_mail').prop('checked', 'checked');
	} else {
		jQuery('#checkAll_mail').prop('checked', false);
	}
});
jQuery(".chk_apache").change(function() {
	if (jQuery(".chk_apache:checked").length == jQuery(".chk_apache").length) {
		jQuery('#checkAll_apache').prop('checked', 'checked');
	} else {
		jQuery('#checkAll_apache').prop('checked', false);
	}
});
jQuery(".chk_others").change(function() {
	if (jQuery(".chk_others:checked").length == jQuery(".chk_others").length) {
		jQuery('#checkAll_others').prop('checked', 'checked');
	} else {
		jQuery('#checkAll_others').prop('checked', false);
	}
});

jQuery(document)
		.ready(
				function() {

					jQuery("#adminparent").addClass("active");
					jQuery("#createSeleniumProject").addClass("active");
					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/jarVersion/viewJarVersionList.action?userId='
							+ userId;
					jQuery
							.ajax({
								url : serviceURL,
								dataType : 'json',
								type : 'GET',
								cache : 'false',
								success : function(data) {
									jarList = "";
									var JsonStringify_Data = JSON
											.stringify(data);
									obj = jQuery.parseJSON(JsonStringify_Data);

									var arr = [];

									jQuery.each(obj, function(i, e) {
										jQuery.each(e, function(key1, val_0) {
											arr.push(val_0);
										});
									});
									var result = jQuery.map(arr, function(val,
											key) {

										return {
											jarId : val.jarId,
											jarName : val.jarName,
											category : val.category

										};

									});
									var i = 0;
									jQuery
											.each(
													result,
													function(key, val) {
														var jar_Name = val.jarName;
														var category = val.category;
														//alert("category"+category);
														if(category == "Selenium Java")
														 {
															var x = document
																	.getElementById("#selenium_java");
															sname = "<input type=\"checkbox\" name=\"ocCustomerServiceId1\" class=\"chk\" value=\""
																	+ val.jarId
																	+ "\" id=\""
																	+ val.jarId
																	+ "\">&nbsp; &nbsp;"
																	+ val.jarName
																	+ ""
																	+ "</br>";
															jQuery(
																	"input:checkbox")
																	.change(
																			function() {
																				var group = ":checkbox[name='"
																						+ jQuery(
																								this)
																								.attr(
																										"name")
																						+ "']";
																				if (jQuery(
																						this)
																						.is(
																								'.chk:checked')) {
																					jQuery(
																							group)
																							.not(
																									jQuery(this))
																							.attr(
																									"checked",
																									false);
																				}
																			});
															jQuery(
																	"#selenium_java")
																	.append(
																			sname);
														} else if (category == "Selenium Server Stand Alone") {
															var x = document
																	.getElementById("#selenium_server");
															sname = "<input type=\"checkbox\" name=\"ocCustomerServiceId2\" class=\"chk\" value=\""
																	+ val.jarId
																	+ "\" id=\""
																	+ val.jarId
																	+ "\">&nbsp; &nbsp;"
																	+ val.jarName
																	+ ""
																	+ "</br>";
															jQuery(
																	"input:checkbox")
																	.change(
																			function() {
																				var group = ":checkbox[name='"
																						+ jQuery(
																								this)
																								.attr(
																										"name")
																						+ "']";
																				if (jQuery(
																						this)
																						.is(
																								'.chk:checked')) {
																					jQuery(
																							group)
																							.not(
																									jQuery(this))
																							.attr(
																									"checked",
																									false);
																				}
																			});
															jQuery(
																	"#selenium_server")
																	.append(
																			sname);
														} else if (category == "Mail") {
															var x = document
																	.getElementById("#checkAll");
															sname = " <input type=\"checkbox\" name=\"ocCustomerServiceId3\" class=\"chk_mail\" value=\""
																	+ val.jarId
																	+ "\" id=\""
																	+ val.jarId
																	+ "\">&nbsp; &nbsp;"
																	+ val.jarName
																	+ ""
																	+ "</br>";
															jQuery(
																	"input:checkbox")
																	.change(
																			function() {
																				var group = ":checkbox[name='"
																						+ jQuery(
																								this)
																								.attr(
																										"name")
																						+ "']";
																				if (jQuery(
																						this)
																						.is(
																								'.chk:checked')) {
																					jQuery(
																							group)
																							.not(
																									jQuery(this))
																							.attr(
																									"checked",
																									false);
																				}
																			});
															jQuery("#mailed")
																	.append(
																			sname);
														} else if (category == "Apache Poi") {
															var x = document
																	.getElementById("#apache_poi");
															sname = " <input type=\"checkbox\" name=\"ocCustomerServiceId4\" class=\"chk_apache\" value=\""
																	+ val.jarId
																	+ "\" id=\""
																	+ val.jarId
																	+ "\">&nbsp; &nbsp;"
																	+ val.jarName
																	+ ""
																	+ "</br>";
															jQuery(
																	"#apache_poi")
																	.append(
																			sname);
														} else {
															var x = document
																	.getElementById("#createProject_othersJar");

															sname = "<td><input type=\"checkbox\" name=\"ocCustomerServiceId5\" class=\"chk_others\" value=\""
																	+ val.jarId
																	+ "\" id=\""
																	+ val.jarId
																	+ "\">&nbsp; &nbsp;"
																	+ val.jarName
																	+ "</td>";
															if (i != 3) {
																jQuery(
																		"#createProject_othersJar tbody")
																		.append(
																				sname);
																i++;
															} else {
																i = 0;
																jQuery(
																		"#createProject_othersJar tbody")
																		.append(
																				"<tr> <tr />");
															}
														}
													});
								}
							});
					jQuery('.only_alphabets').bind(
							'keyup blur',
							function() {

								jQuery(this).val(
										jQuery(this).val().replace(/\s/g, ''));
							});
					var userId = sessionStorage.getItem("userId");
					jQuery('#projectNameError').hide();
					jQuery('#jarErrorValue').hide();
					jQuery('#projectAlreadyExists').hide();
					jQuery('#projectName').val('');
					jQuery('#projectName').focus();
					jQuery("#submitBtn")
							.click(
									function(e) {
										e.preventDefault();
										var val = [];
										var isJarChecked = false;
										jQuery(':checkbox:checked')
												.each(
														function(i) {
															val[i] = jQuery(
																	this).val();
															isJarChecked = true;
														});

										if (jQuery('#projectName').val() == '') {
											jQuery('#projectName').css({
												"border" : "1px solid red",
											});
											jQuery("#projectName").focus();
											jQuery('#projectNameError').show();
											return false;
										}

										if (!isJarChecked) {
											jQuery('#projectName').css({
												"border" : "",
											});
											jQuery('#projectNameError').hide();
											jQuery('#jarErrorValue').show();

											return false;
										}

										var userId = sessionStorage
												.getItem("userId");

										var val = [];
										jQuery(':checkbox:checked')
												.each(
														function(i) {
															val[i] = jQuery(
																	this).val();

														});
										var serviceURL = envConfig.serviceBaseURL
												+ '/jarVersion/createProjectByJars.action?userId='
												+ userId
												+ '&selected_jar='
												+ val + '';

										jQuery
												.ajax({
													url : serviceURL,
													dataType : 'json',
													cache : false,
													type : 'POST',
													data : {
														projectJarId : jQuery(
																'#hiddenProjectJarId')
																.val(),
														projectName : jQuery(
																'#projectName')
																.val()
													},
													type : 'Post',
													success : function(data) {
														var responseTextFlag = data.success;
														if (responseTextFlag == true) {
															jQuery('#projectAlreadyExists').hide();
															jQuery("#loading-div-background").hide();
															jQuery('#success_modal').modal('toggle');
															jQuery('#success_modal').modal('view');
														} else {
															jQuery('#projectAlreadyExists').show();
															jQuery('#projectNameError').hide();
															jQuery('#jarErrorValue').hide();
															jQuery("#loading-div-background").hide();
															jQuery('#success_modal').modal('toggle');
															jQuery('#success_modal').modal('view');
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
									});
					jQuery("#projectName").on("input", function() {
						jQuery('#projectNameError').hide();
						jQuery('#projectName').css({
							"border" : "1px solid #0866c6",
						});
					});
					jQuery('#ok_btn')
							.click(
									function(e) {
										e.preventDefault();
										window.location.href = "../seleniumproject/viewProjectByJar.html";
									});

				});

function loadAllCheckBoxes() {
	var data = JSON.parse(localStorage.getItem('JsonStringify_ProjectJarData'));

	if (data != null) {
		jQuery("#hiddenProjectJarId").val(data.projectJarListId);
		jQuery("#projectName").val(data.projectName);

		var projectJarIds = data.jarIds.split(",");
		jQuery.each(projectJarIds, function(i) {
			jQuery('#' + projectJarIds[i]).prop('checked', true);
		});

	}

	// Remove the project jar data from html local storage.
	localStorage.removeItem("JsonStringify_ProjectJarData");

}

function hideAllErrors() {
	jQuery('#projectNameError').hide();
	jQuery('#projectAlreadyExists').hide();
	jQuery('#jarErrorValue').hide();
}
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}