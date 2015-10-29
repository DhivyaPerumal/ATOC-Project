jQuery(document)
		.ready(
				function() {

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					jQuery("#currentpassword").focus();
					jQuery('#currentpassword').keypress(function(e) {
						jQuery('#currentpassword').css({
							"border" : "",
						});
						jQuery('#currentpasswordid').hide();
						jQuery('#currentnewid').hide();
						jQuery('#currentconfirmid').hide();
						jQuery('#invalidcurrentpwd').hide();

					});
					jQuery('#password').keypress(function(e) {
						jQuery('#password').css({
							"border" : "",
						});
						jQuery('#currentpasswordid').hide();
						jQuery('#currentnewid').hide();
						jQuery('#currentconfirmid').hide();
						jQuery('#Cntnewpwdnotsame').hide();
						jQuery('#currentpassword').css({
							"border" : "",
						});
					});

					jQuery('#confirmpassword').keypress(function(e) {
						jQuery('#confirmpassword').css({
							"border" : "",
						});
						jQuery('#currentpasswordid').hide();
						jQuery('#currentnewid').hide();
						jQuery('#currentconfirmid').hide();

					});
					var userId = sessionStorage.getItem("userId");

					var isAdmin = sessionStorage.getItem("isAdmin");
					jQuery('#fieldsNotEmpty').hide();
					jQuery('#passwordmismatch').hide();
					jQuery('#invalidcurrentpwd').hide();
					jQuery('#passwordLengthValid').hide();
					jQuery('#Cntnewpwdnotsame').hide();
					jQuery("#submitBtn")
							.click(
									function(e) {
										e.preventDefault();
										var serviceURL = envConfig.serviceBaseURL
												+ '/user/changePassword.action?userId='
												+ userId;
										console.log(serviceURL);
										hideAllErrors();
										if (jQuery('#currentpassword').val() == '') {

											jQuery('#currentpasswordid').show();
											jQuery('#currentnewid').hide();
											jQuery('#currentconfirmid').hide();
											jQuery('#passwordLengthValid')
													.hide();
											jQuery('#currentpassword ').css({
												"border" : "1px solid red",
											});
											jQuery('#currentpassword').focus();
										}

										else if (jQuery('#password').val() == '') {
											jQuery('#currentpasswordid').hide();
											jQuery('#currentnewid').show();
											jQuery('#currentconfirmid').hide();
											jQuery('#passwordLengthValid')
													.hide();
											jQuery('#password').css({
												"border" : "1px solid red",
											});
											jQuery('#password').focus();

										} else if (jQuery('#confirmpassword')
												.val() == '') {
											jQuery('#currentpasswordid').hide();
											jQuery('#currentnewid').hide();
											jQuery('#currentconfirmid').show();
											jQuery('#passwordLengthValid')
													.hide();
											jQuery('#confirmpassword').css({
												"border" : "1px solid red",
											});
											jQuery('#confirmpassword').focus();
										}

										else if (jQuery('#currentpassword')
												.val() == (jQuery('#password')
												.val())) {
											jQuery('#Cntnewpwdnotsame').show();
											jQuery('#fieldsNotEmpty').hide();
											jQuery('#passwordmismatch').hide();
											jQuery('#invalidcurrentpwd').hide();
											jQuery('#passwordLengthValid')
													.hide();
											jQuery('#currentpassword').css({
												"border" : "1px solid red",
											});
											jQuery('#password').css({
												"border" : "1px solid red",
											});
											jQuery('#currentpassword').focus();
										} else if (jQuery('#confirmpassword')
												.val().length < 8
												|| jQuery('#confirmpassword')
														.val().length > 15) {

											jQuery('#fieldsNotEmpty').hide();
											jQuery('#passwordmismatch').hide();
											jQuery('#invalidcurrentpwd').hide();
											jQuery('#Cntnewpwdnotsame').hide();
											jQuery('#passwordLengthValid')
													.show();
											jQuery('#password').css({
												"border" : "1px solid red",
											});

											jQuery('#confirmpassword').css({
												"border" : "1px solid red",
											});
											jQuery('#confirmpassword').focus();
										} else if (jQuery('#password').val() != jQuery(
												'#confirmpassword').val()) {
											jQuery('#fieldsNotEmpty').hide();

											jQuery('#invalidcurrentpwd').hide();
											jQuery('#passwordLengthValid')
													.hide();
											jQuery('#Cntnewpwdnotsame').hide();
											jQuery('#passwordmismatch').show();
											jQuery('#password').css({
												"border" : "1px solid red",
											});
											jQuery('#confirmpassword').css({
												"border" : "1px solid red",
											});
											jQuery('#password').focus();
										}

										else if (jQuery('#password').val() == jQuery(
												'#confirmpassword').val()) {

											var userEmail = sessionStorage
													.getItem("userEmail");
											jQuery
													.ajax({
														type : 'POST',
														dataType : 'json',
														url : serviceURL,
														data : {
															userId : sessionStorage
																	.getItem("userId"),
															userEmail : userEmail,
															userPassword : jQuery('#currentpassword').val(),
															newPassword : jQuery('#password').val()
														},

														success : function(data) {
															var responseTextFlag = data.success;

															if (responseTextFlag == true)

															{
																jQuery('#fieldsNotEmpty').hide();
																jQuery('#Cntnewpwdnotsame').hide();
																jQuery('#passwordmismatch').hide();
																jQuery('#invalidcurrentpwd').hide();
																var isAdmin = sessionStorage.getItem("isAdmin");
																if (isAdmin == "Master Admin") {
																	window.location.href = "../dashboard/registrationApprovals.html";
																} else {
																	window.location.href = "../dashboard/dashboard.html";
																}
															} else {
																jQuery('#invalidcurrentpwd').show();
																jQuery('#Cntnewpwdnotsame').hide();
																jQuery('#fieldsNotEmpty').hide();
																jQuery('#passwordmismatch').hide();
																jQuery('#passwordLengthValid').hide();
																jQuery('#currentpassword').css(
																				{
																					"border" : "1px solid red",
																				});
																jQuery('#password').css(
																				{
																					"border" : "",
																				});
																jQuery('#confirmpassword').css(
																				{
																					"border" : "",
																				});
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
									});
					jQuery('#currentpassword').focus();
					jQuery("#currentpassword").on("input", function() {
						jQuery('#currentpasswordid').hide();
						jQuery('#currentnewid').hide();
						jQuery('#currentconfirmid').hide();
						jQuery('#Cntnewpwdnotsame').hide();
						jQuery('#currentpassword').css({
							"border" : "1px solid #0866c6",
						});
						jQuery('#password').css({
							"border" : "",
						});
					});
					jQuery("#password").on("input", function() {
						jQuery('#currentconfirmid').hide();
						jQuery('#currentpasswordid').hide();
						jQuery('#currentnewid').hide();
						jQuery('#Cntnewpwdnotsame').hide();
						jQuery('#passwordmismatch').hide();
						jQuery('#password').css({
							"border" : "1px solid #0866c6",
						});
						jQuery('#currentpassword').css({
							"border" : "1px solid #0866c6",
						});
						jQuery('#confirmpassword').css({
							"border" : "",
						});
					});
					jQuery("#confirmpassword").on("input", function() {
						jQuery('#currentpasswordid').hide();
						jQuery('#currentnewid').hide();
						jQuery('#currentconfirmid').hide();
						jQuery('#passwordmismatch').hide();
						jQuery('#confirmpassword').css({
							"border" : "1px solid #0866c6",
						});
						jQuery('#password').css({
							"border" : "1px solid #0866c6",
						});
					});
				});
function hideAllErrors() {
	jQuery('#currentpasswordid').hide();
	jQuery('#Cntnewpwdnotsame').hide();
	jQuery('#invalidcurrentpwd').hide();
	jQuery('#currentnewid').hide();
	jQuery('#passwordmismatch').hide();
	jQuery('#passwordLengthValid').hide();
	jQuery('#currentconfirmid').hide();
}
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}