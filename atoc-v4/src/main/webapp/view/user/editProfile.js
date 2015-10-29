jQuery(document)
		.ready(
				function() {

					var userId = sessionStorage.getItem("userId");

					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					jQuery('.only_alphabets').bind('keyup blur',function() {
						jQuery(this).val(jQuery(this).val().replace(/[^A-Za-z0-9]/g, ""));
							});
					jQuery('.only_numbers').bind('keyup blur',function() {
							jQuery(this).val(jQuery(this).val().replace(/[^0-9]/g, ""));
							});
					jQuery('.only_alphabet').bind('keyup blur',function() {
							jQuery(this).val(jQuery(this).val().replace(/\s/g, ''));
							});
					var serviceURL = envConfig.serviceBaseURL
							+ '/user/userId.action?userId=' + userId;
					console.log(serviceURL);
					// load profile data
					loadProfileData(serviceURL, userId);
					var profileImgError = false;
					jQuery('#file').bind('change',function() {
										profileImgError = false;
										var fileName = document
												.getElementById("file").value
										if (fileName.split(".")[1]
												.toUpperCase() == "PNG") {
											var img = new Image;
											img.src = URL.createObjectURL(this.files[0]);
											img.onload = function() {
												var picWidth = this.width;
												var picHeight = this.height;
												if (Number(picWidth) > 100
														&& Number(picHeight) > 100) {
													jQuery('#profileImgError').show();
													profileImgError = true;
													jQuery('#profileImgError').text('Upload image size should be 100 x 100');
												}
											}
										} else {
											profileImgError = true;
											jQuery('#profileImgError').text('Select only png images.');
										}
										if (profileImgError == false) {
											jQuery('#profileImgError').hide();
										} else {
											jQuery('#profileImgError').show();
										}
									});
					// To update user details
					jQuery("form").submit(function(e) {
										e.preventDefault();
										var userId = sessionStorage.getItem("userId");
										var firstName = jQuery('#firstName').val();
										var lastName = jQuery('#lastName').val();
										var serviceURL = envConfig.serviceBaseURL
												+ '/user/saveUser.action?userId='
												+ userId
										var formData = new FormData(jQuery(this)[0]);
										if (jQuery('#firstName').val() == ''
												|| jQuery('#firstName').val() == 'undefined') {
											jQuery('#firstname').css({
												"border" : "1px solid red",
											});
											jQuery('#firstNameError').show();
											jQuery('#firstName').focus();
											return false;
										} else if (jQuery('#lastName').val() == ''
												|| jQuery('#lastName').val() == 'undefined') {
											jQuery('#lastName').css({
												"border" : "1px solid red",
											});
											jQuery('#lastName').css({
												"border" : "",
											});
											jQuery('#lastNameError').show();
											jQuery('#firstNameError').hide();
											jQuery('#lastNameError').focus();
											return false;
										} else if (profileImgError) {
											return false;
										} else {
											jQuery
													.ajax({
														url : serviceURL,
														cache : false,
														dataType : 'text',
														data : formData,
														type : 'POST',
														enctype : 'multipart/form-data',
														processData : false,
														contentType : false,
														success : function(data) {
															var userName = firstName;
															sessionStorage.setItem("userName",userName);
															var responseTextFlag = data.success;
															jQuery("#loading-div-background").hide();
															jQuery('#success_modal').modal('toggle');
															jQuery('#success_modal').modal('view');
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
						window.location.href = "../user/editProfile.html";
					});
				});
function loadProfileData(serviceURL, userId) {
	setUserTokenToRequestHeader(sessionStorage.getItem("userToken"));
	setProfileImage(sessionStorage.getItem("profileImgData"));
	jQuery
			.ajax({
				url : serviceURL,
				dataType : "json",
				data : {
					userId : userId
				},
				type : 'Post',
				success : function(response) {
					var JsonStringify_Data = JSON.stringify(response);
					jQuery('#firstName').val(response.data.firstName);
					jQuery('#lastName').val(response.data.lastName);
					jQuery('#userEmail').val(response.data.userEmail);
					jQuery('#orgName').val(response.data.organizationName);
					if (response.data.profileImagData != ""
							&& response.data.profileImagData != "null"
							&& response.data.profileImagData != "undefined") {
						document.getElementById("profileImg").src = "data:image/png;base64,"
								+ response.data.profileImagData;
					}
					sessionStorage.setItem("profileImgData",
							response.data.profileImagData)
					var userName = response.data.firstName;
					sessionStorage.setItem("userName", userName);
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
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}