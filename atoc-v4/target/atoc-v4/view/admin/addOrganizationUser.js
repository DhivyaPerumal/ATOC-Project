jQuery(document)
		.ready(
				function() {
				
			    var isAdmin = sessionStorage.getItem("isAdmin");
				if (isAdmin == "Org Admin") {

					jQuery("#adminparent").addClass("active");
					jQuery("#adminaddOrgUser").addClass("active");

				}
				else
				{

					jQuery(".active").removeClass("active");
					jQuery("#addorguser").addClass("active");
				}

						jQuery(document).ajaxStart(customblockUI);
						jQuery(document).ajaxStop(customunblockUI);
						
				var userId = sessionStorage.getItem("userId");
					jQuery('#fieldsNotEmpty').hide();
					jQuery('#emailInvalid').hide();
					jQuery('#useralreadyexists').hide();

					jQuery('.only_alphabets').bind('keyup blur',function(){ 
	jQuery(this).val( jQuery(this).val().replace(/[^A-Za-z0-9]/g,"") ); }
	);
	jQuery('.only_numbers').bind('keyup blur',function(){ 
	jQuery(this).val( jQuery(this).val().replace(/[^0-9]/g,"") ); }
	);
	jQuery('.only_removeSpace').bind('keyup blur',function(){ 

	    jQuery(this).val( jQuery(this).val().replace( /\s/g,'') ); }
	);
					jQueryselectOrgName = jQuery('#orgName_dropdownselect');

					var serviceURL = envConfig.serviceBaseURL
							+ '/organization/viewAllOrganizationList.action?userId='
							+ userId;
					console.log(serviceURL);
					// request the JSON data and parse into the select element
					jQuery.ajax({
						url : serviceURL,
						dataType : 'json',
						type : 'GET',
						cache : 'false',
						success : function(data) {
							var JsonStringify_Data = JSON.stringify(data);
							obj = jQuery.parseJSON(JsonStringify_Data);
							var arr = [];

							jQuery.each(obj, function(i, e) {
								jQuery.each(e, function(key1, val_0) {
									arr.push(val_0);

								});
							});

							var result = jQuery.map(arr, function(val, key) {
								return {
									organizationId : val.organizationId,
									organizationName : val.organizationName
								};
							});

							jQuery.each(result, function(key, val) {
								jQueryselectOrgName.append('<option id="'
										+ val.organizationId + '">'
										+ val.organizationName + '</option>');

							});
							jQuery('#firstname').focus();
				
						},
						failure : function(data) {
							window.location.href = "../";
						},
						statusCode : {
							403 : function(xhr) {
								alert("Session will be Expired");
								window.location.href = "../../";

							}
						}
					});

					jQuery('#submitBtn').click(function(event) {

						if (jQuery('#id_java').val() == 'java') {
							var isValid = true;
							jQuery('#firstname').each(function() {
								if (jQuery.trim(jQuery(this).val()) == '') {
									isValid = false;
									jQuery(this).css({
										"border" : "1px solid red",
										"background" : "#FFCECE"
									});
								} else {
									jQuery(this).css({
										"border" : "",
										"background" : ""
									});
								}
							});
							if (isValid == false)
								return;
						}
					});
					jQuery("#submitBtn")
							.click(
									function(e) {
										e.preventDefault();
										var firstName = jQuery('#firstname').val();
										var lastName = jQuery('#lastname').val();
										var cntmail = jQuery("#cntmail").val();
										var atpos = cntmail.indexOf("@");
										var dotpos = cntmail.lastIndexOf(".");
										var isOrgAdmin = jQuery(
												'input[name=isOrgAdmin]:checked')
												.val();
										var orgName = jQuery(
												'#orgName_dropdownselect')
												.val();

										var userId = sessionStorage
												.getItem("userId");
										var serviceURL = envConfig.serviceBaseURL
												+ '/user/addOrgUser.action?userId='
												+ userId;
										if (jQuery(
												'#orgName_dropdownselect option:selected')
												.val() == '') {
											jQuery('#orgName_dropdownselect').css({
												"border" : "1px solid red",
											});
											jQuery('#select_org_error').show();
											jQuery("#orgName_dropdownselect")
													.focus();
											return false;
										} else if (jQuery('#firstname').val() == '') {
											jQuery('#firstname').css({
												"border" : "1px solid red",
											});
											jQuery('#addOrgUserError').show();
											jQuery('#firstname').focus();
											return false;
										} else if (jQuery('#lastname').val() == '') {
											jQuery('#lastname').css({
												"border" : "1px solid red",
											});
											jQuery('#firstname').css({
												"border" : "",
											});
											jQuery('#addOrgUserErrorLast').show();
											jQuery('#addOrgUserError').hide();
											jQuery('#lastname').focus();
											return false;
										}

										else if (jQuery('#cntmail').val() == '') {
											jQuery('#cntmail').css({
												"border" : "1px solid red",
											});
											jQuery('#lastname').css({
												"border" : "",
											});
											jQuery('#addOrgUserErrorEmail').show();
											jQuery('#addOrgUserErrorLast').hide();
											jQuery('#cntmail').focus();
											return false;
										}

										else if (atpos < 2
												|| dotpos < atpos + 3
												|| dotpos + 2 >= cntmail.length) {
											jQuery('#addOrgUserErrorEmail').hide();
											jQuery('#useralreadyexists').hide();
											jQuery('#emailInvalid').show();
											jQuery('#cntmail').focus();
											return false;
										}
										else {
											console.log(serviceURL);
											jQuery.ajax({
														url : serviceURL,
														dataType : 'json',
														type : 'POST',
														cache : 'false',
														data : {
															firstname : firstName,
															lastname : lastName,
															contactEmail : cntmail,
															isOrgAdmin : isOrgAdmin,
															orgName : orgName,
															isActive : 'Y',
															createdBy : userId,
															updatedBy : userId
														},
														success : function(data) {
															var responseTextFlag = data.success;
															if (responseTextFlag == false) {
																jQuery(
																		'#useralreadyexists')
																		.show();
																jQuery(
																		'#addOrgUserError')
																		.hide();
																jQuery(
																		'#emailInvalid')
																		.hide();
																jQuery(
																		'#addOrgUserErrorLast')
																		.hide();

															} else {
																jQuery("#loading-div-background").hide();
																jQuery(
																		'#useralreadyexists')
																		.hide();
																jQuery(
																		'#success_modal')
																		.modal(
																				'toggle');
																jQuery(
																		'#success_modal')
																		.modal(
																				'view');

															}
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
					jQuery('#firstname').focus();
					jQuery("#firstname").on("input", function() {
						jQuery('#addOrgUserError').hide();
						jQuery('#firstname').css({
							"border" : "1px solid #0866c6",
						});
					});
					jQuery("#lastname").on("input", function() {
						jQuery('#addOrgUserErrorLast').hide();
						jQuery('#lastname').css({
							"border" : "1px solid #0866c6",
						});
					});
					jQuery("#cntmail").on("input", function() {
						jQuery('#useralreadyexists').hide();
						jQuery('#emailInvalid').hide();
						jQuery('#addOrgUserErrorEmail').hide();
						jQuery('#emailInvalid').hide();
						jQuery('#cntmail').css({
							"border" : "1px solid #0866c6",
						});
					});
					jQuery("#orgName_dropdownselect").change(function() {
						jQuery('#select_org_error').hide();
						jQuery('#orgName_dropdownselect').css({
							"border" : "1px solid #0866c6",
						});
					});
					jQuery('#loading_cancel_btn').click(function(event) {
					});
					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						 var isAdmin = sessionStorage.getItem("isAdmin");
				if (isAdmin == "Org Admin") {
				window.location.href = "../dashboard/dashboard.html";
				}
				else
				{
					window.location.href = "../dashboard/registrationApprovals.html";
				}
					});
					jQuery('#addorguser_breadcrumb').click(function(e) {
						e.preventDefault();
						 var isAdmin = sessionStorage.getItem("isAdmin");
				if (isAdmin == "Org Admin") {
				window.location.href = "../dashboard/dashboard.html";
				}
				else if (isAdmin == "Master Admin")
				{
					window.location.href = "../dashboard/registrationApprovals.html";
				}	
					});
				jQuery('#addOrgUser_logo')
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

				});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}