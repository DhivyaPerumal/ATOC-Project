jQuery(document).ready(
				function() {
					jQuery(".active").removeClass("active");
					jQuery("#addmasteruser").addClass("active");
				var userId = sessionStorage.getItem("userId");
				jQuery(document).ajaxStart(customblockUI);
				jQuery(document).ajaxStop(customunblockUI);
				jQuery('.only_alphabets').bind('keyup blur',function(){ 
	jQuery(this).val( jQuery(this).val().replace(/[^A-Za-z0-9]/g,"") ); }
	);
	jQuery('.only_characters').bind('keyup blur',function(){ 
	jQuery(this).val( jQuery(this).val().replace(/[^A-Za-z]/g,"") ); }
	);
	jQuery('.only_numbers').bind('keyup blur',function(){ 
	jQuery(this).val( jQuery(this).val().replace(/[^0-9]/g,"") ); }
	);
	jQuery('.only_removeSpace').bind('keyup blur',function(){ 
	    jQuery(this).val( jQuery(this).val().replace( /\s/g,'') ); }
	);
					jQuery('#firstname').val('');
					jQuery('#lastname').val('');
					jQuery("#cntmail").val('');
					jQuery('#firstname').focus();
					jQuery("#submitBtn").click(
									function(e) {
										e.preventDefault();
										var userId = sessionStorage.getItem("userId");
										var firstName = jQuery('#firstname').val();
										var lastName = jQuery('#lastname').val();
										var cntmail = jQuery("#cntmail").val();
										var orgName = jQuery('#mastername').val();
										var atpos = cntmail.indexOf("@");
										var dotpos = cntmail.lastIndexOf(".");
										var serviceURL = envConfig.serviceBaseURL+ '/user/addmasterUser.action?userId='+ userId;
										if (jQuery('#firstname').val() == ''
												|| jQuery('#firstname').val() == 'undefined') {
											jQuery('#firstname').css({
												"border" : "1px solid red",
												});
											jQuery('#addMasterUserError').show();
											jQuery('#firstname').focus();
											return false;
										} else if (jQuery('#lastname').val() == ''
												|| jQuery('#firstname').val() == 'undefined') {
											jQuery('#lastname').css({
												"border" : "1px solid red",
												});
											jQuery('#firstname').css({
												"border" : "",
												});
											jQuery('#addMasterUserErrorLast').show();
											jQuery('#addMasterUserError').hide();
											jQuery('#lastname').focus();
											return false;
										} else if (jQuery('#cntmail').val() == ''
												|| jQuery('#firstname').val() == 'undefined') {
											jQuery('#cntmail').css({
												"border" : "1px solid red",
															});
											jQuery('#lastname').css({
												"border" : "",
												});
											jQuery('#addMasterUserErrorEmail')
													.show();
											jQuery('#addMasterUserErrorLast').hide();
											jQuery('#cntmail').focus();
											return false;
										} else if (atpos < 2
												|| dotpos < atpos + 3
												|| dotpos + 2 >= cntmail.length) {
											jQuery('#addMasterUserErrorEmail').hide();
											jQuery('#useralreadyexists').hide();
											jQuery('#addMasterUserErrorLast').hide();
											jQuery('#emailInvalid').show();
											jQuery('#cntmail').focus();
											return false;
										} else {
											console.log(serviceURL);
											hideAllErrors();
											jQuery
													.ajax({
														url : serviceURL,
														dataType : 'json',
														type : 'POST',
														cache : 'false',
														data : {
															firstname : firstName,
															lastname : lastName,
															contactEmail : cntmail,
															orgName : orgName,
															isActive : 'Y',
															createdBy : userId,
															updatedBy : userId,
															ismasteradmin : 'Master Admin'
														},

														success : function(data) {
														var responseTextFlag = data.success;
															if (responseTextFlag == false) {
																	var isuserExist = (data.userExist) ? emailExists(): "";
															} else {
																		jQuery('#useralreadyexists').hide();
																		jQuery("#loading-div-background").hide();
																jQuery('#success_modal').modal('toggle');
																jQuery('#success_modal').modal('view');
																
															}
														},
														failure : function(data) {
															window.location.href = "registrationApprovals.html";
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
					function emailExists() {
						jQuery('#useralreadyexists').show();
						jQuery('#addMasterUserError').hide();
						jQuery('#emailInvalid').hide();
						jQuery('#addMasterUserErrorLast').hide();
					}
					jQuery("#firstname").on("input", function() {
						jQuery('#addMasterUserError').hide();
						jQuery('#firstname').css({
							"border" : "1px solid #0866c6",
							});
					});
					jQuery("#lastname").on("input", function() {
						jQuery('#addMasterUserErrorLast').hide();
						jQuery('#lastname').css({
							"border" : "1px solid #0866c6",
									});
					});

					jQuery("#cntmail").on("input", function() {
						jQuery('#addMasterUserErrorEmail').hide();
						jQuery('#emailInvalid').hide();
						jQuery('#useralreadyexists').hide();
						jQuery('#cntmail').css({
							"border" : "1px solid #0866c6",
							});
					});
					function hideAllErrors() {
						jQuery('#addMasterUserError').hide();
						jQuery('#addMasterUserErrorEmail').hide();
						jQuery('#addMasterUserErrorLast').hide();
						jQuery('#emailInvalid').hide();
					}
					jQuery('#ok_btn').click(function(e) {
								e.preventDefault();
								window.location.href = "../dashboard/registrationApprovals.html";
						});
				});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}