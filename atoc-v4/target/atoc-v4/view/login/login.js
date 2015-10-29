jQuery.noConflict();
jQuery(function() {

	if (localStorage.chkbx && localStorage.chkbx != '') {
		jQuery('#remember_me').attr('checked', 'checked');
		jQuery('#login_email').val(localStorage.email);
		jQuery('#login_password').val(localStorage.password);
	} else {
		jQuery('#remember_me').removeAttr('checked');
		jQuery('#login_email').val('');
		jQuery('#login_password').val('');
	}

	jQuery('#remember_me').click(function() {
		if (jQuery('#remember_me').is(':checked')) {
			// save username and password
			localStorage.email = jQuery('#login_email').val();

			localStorage.password = jQuery('#login_password').val();
			localStorage.chkbx = jQuery('#remember_me').val();
		} else {
			localStorage.email = '';
			localStorage.password = '';
			localStorage.chkbx = '';
		}
	});

	jQuery.ajaxSetup({
		error : function(jqXHR, exception) {
			if (jqXHR.status === 0) {
				alert('Not connect.\n Verify Network.');
			} else if (jqXHR.status == 404) {
				alert('Requested page not found. [404]');
			} else if (jqXHR.status == 500) {
				alert('Internal Server Error [500].');
			} else if (exception === 'parsererror') {
				alert('Requested JSON parse failed.');
			} else if (exception === 'timeout') {
				alert('Time out error.');
			} else if (exception === 'abort') {
				alert('Ajax request aborted.');
			} else {
				alert('Uncaught Error.\n' + jqXHR.responseText);
			}
		}
	});
});
jQuery(document)
		.ready(
				function() {

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					jQuery("#login_email").focus();
					jQuery('.only_alphabets').bind('keyup blur',function() {
						jQuery(this).val(jQuery(this).val().replace(/[^A-Za-z]/g, ''));
							});
					jQuery('.only_removeSpace').bind('keyup blur',function() {
						jQuery(this).val(jQuery(this).val().replace(/\s/g, ''));
							});
					jQuery('#fieldsNotEmpty').hide();
					jQuery("#login_signin_btn")
							.click(function(e) {
										e.preventDefault();
										var serviceURL = envConfig.serviceBaseURL
												+ '/user/loginVerification.action';
										console.log(serviceURL);
										var loginEmail = jQuery("#login_email").val();
										var atpos = loginEmail.indexOf("@");
										var dotpos = loginEmail.lastIndexOf(".");
										var loginPwd = jQuery("#login_password").val();
										var isValid = false;
										if (loginEmail.length == 0
												&& loginPwd.length == 0) {
											jQuery('#errorMsg').text(
															'Please enter Email Id and Password.');
											jQuery('#errorMsg').show();
											jQuery('#login_email').css({
												"border" : "1px solid #dd4b39",
											});
											jQuery('#login_password').css({
												"border" : "1px solid #dd4b39",
											});
											return false;
										} else if (loginEmail.length > 0
												&& loginPwd.length == 0) {
											var msg = "";
											if (validateEmail(loginEmail)) {
												msg = "Please enter password.";
												jQuery('#login_password')
														.css(
																{
																	"border" : "1px solid #dd4b39",
																});
											} else {
												msg = "Please enter valid Email Id and password.";
												jQuery('#login_email')
														.css(
																{
																	"border" : "1px solid #dd4b39",
																});
											}
											jQuery('#errorMsg').text(msg);
											jQuery('#errorMsg').show();
											return false;
										} else if (loginEmail.length == 0
												&& loginPwd.length > 0) {
											var msg = "";
											if (loginPwd.length >= 8) {
												msg = "Please enter Email Id.";
												jQuery('#login_password')
														.css(
																{
																	"border" : "1px solid #dd4b39",
																});
											} else {
												msg = "Please enter Email Id and Password must contain at least 8 characters.";
												jQuery('#login_email')
														.css(
																{
																	"border" : "1px solid #dd4b39",
																});
											}
											jQuery('#errorMsg').text(msg);
											jQuery('#errorMsg').show();
											return false;
										}
										else if (!validateEmail(loginEmail)
												|| !validatePassword(loginPwd)) {
											var msg = "";
											if (validateEmail(loginEmail)) {
												if (loginPwd.length < 8) {
													msg = "Password must contain at least 8 characters.";
													jQuery('#login_password')
															.css(
																	{
																		"border" : "1px solid #dd4b39",
																	});
												}
											} else {
												if (loginPwd.length >= 8) {
													msg = "Please enter valid Email Id.";
													jQuery('#login_email')
															.css(
																	{
																		"border" : "1px solid #dd4b39",
																	});
												} else {
													msg = "Please enter valid Email Id and Password must contain at least 8 characters.";
													jQuery('#login_email')
															.css(
																	{
																		"border" : "1px solid #dd4b39",
																	});

													jQuery('#login_password')
															.css(
																	{
																		"border" : "1px solid #dd4b39",
																	});

												}

											}
											jQuery('#errorMsg').text(msg);
											jQuery('#errorMsg').show();
											return false;
										} else {
											jQuery('#errorMsg').text('');
											jQuery('#errorMsg').hide();
											jQuery('#loginBtn').attr('disabled', 'disabled');
											jQuery('#hiddenmsg').hide();
											jQuery
													.ajax({
														url : serviceURL,
														type : 'POST',
														data : {
															userEmail : jQuery('#login_email').val(),
															userPassword : jQuery('#login_password').val()
														},
														success : function(response) {
															var responseTextFlag = response.success;
															if (responseTextFlag == true) {
																var userEmail = response.data.userEmail;
																var userId = response.data.userId;
																var userToken = response.data.userToken;
																var isAdmin = response.data_Role.roleName;
																var userName = response.data.firstName;
																var colorSkin = response.data.colorSkin;
																
																//alert("colorSkin is :"+colorSkin);
																sessionStorage.setItem("userEmail",userEmail);
																sessionStorage.setItem("userName",userName);
																sessionStorage.setItem("userId",userId);
																sessionStorage.setItem("userToken",userToken);
																sessionStorage.setItem("isAdmin",isAdmin);
																sessionStorage.setItem("profileImgData",response.data.profileImagData);
																sessionStorage.setItem("colorSkin",colorSkin);
																
																jQuery('#emailnotvalid').hide();
																jQuery('#passwordlength').hide();
																jQuery('#fieldsNotEmpty').hide();
																jQuery('#emailInvalid').hide();
																if (isAdmin == "Master Admin") {
																	window.location.href = "view/dashboard/registrationApprovals.html";
																} else {
																	window.location.href = "view/dashboard/dashboard.html";
																}
															} else {
																jQuery('#errorMsg').text('Sorry this user does not exist.');
																jQuery('#errorMsg').show();
															}
														},
														failure : function(data) {
															window.location.href = "../../";
														}
													});
										}
									});
					jQuery('#login_email').keypress(function(e) {
						jQuery(login_email).css({
							"border" : "",
						});
						jQuery('#fieldsNotEmptyEmail').hide();
						jQuery('#emailInvalid').hide();
					});
					jQuery('#login_password').keypress(function(e) {
						jQuery(login_password).css({
							"border" : "",
						});
						jQuery('#fieldsNotEmptyPassword').hide();
						jQuery('#passwordlength').hide();
						jQuery('#emailInvalid').hide();
					});
				});
function validateEmail(loginEmail) {
	var filter = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
	if (filter.test(loginEmail)) {
		return true;
	} else {
		return false;
	}
}
function validatePassword(loginPwd) {
	if (loginPwd.length < 8) {
		return false;
	} else {
		return true;
	}
}
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}