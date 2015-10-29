jQuery(document)
		.ready(
				function() {
					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);
					jQuery("#home").click(function(e) {
						e.preventDefault();
						window.location.href = "../../";
					});
					jQuery('#registration_cancel_btn').click(function(event) {
						window.location.href = "../../";
					});

					jQuery("#registerorganization").focus();
					jQuery('#fieldNotEmpty').hide();
					jQuery('#emailInvalid').hide();
					jQuery('#registereduseralreadyexists').hide();

					jQuery('.only_alphabets').bind(
							'keyup blur',
							function() {
								jQuery(this).val(
										jQuery(this).val().replace(
												/[^A-Za-z0-9]/g, ""));
							});
					jQuery('.only_numbers').bind(
							'keyup blur',
							function() {
								jQuery(this).val(
										jQuery(this).val().replace(/[^0-9]/g,
												""));
							});
					jQuery('.only_characters').bind(
							'keyup blur',
							function() {
								jQuery(this).val(
										jQuery(this).val().replace(
												/[^A-Za-z]/g, ""));
							});
					jQuery('.only_removeSpace').bind(
							'keyup blur',
							function() {

								jQuery(this).val(
										jQuery(this).val().replace(/\s/g, ''));
							});

					jQuery("#registrationSubmit")
							.click(
									function(e) {
										e.preventDefault();
										var firstname = jQuery(
												'#registrationfirstname').val();
										var lastname = jQuery(
												'#registrationlastname').val();
										var note = jQuery("#registernote")
												.val();
										var add = jQuery(
												"#registerenteraddress").val();
										var city = jQuery("#registercityname")
												.val();
										var ste = jQuery("#registerstatename")
												.val();
										var cntry = jQuery(
												"#registercountryname").val();
										var zip = jQuery("#registerzipcode")
												.val();
										var cntname = jQuery(
												"#registercontactname").val();
										var cntmail = jQuery(
												"#registercontactemail").val();
										var atpos = cntmail.indexOf("@");
										var dotpos = cntmail.lastIndexOf(".");
										var cntphn = jQuery(
												"#registercontactphone").val();

										var orgname = jQuery(
												"#registerorganization").val();
										var web = jQuery("#registrationwebsite")
												.val();

										var serviceURL = envConfig.serviceBaseURL
												+ '/customer/saveCustomer.action';
										console.log(serviceURL);
										if (orgname == '') {
											jQuery('#orgname_error').show();
											jQuery('#orgname_error')
													.text(
															"Please enter organization name.");
											jQuery('#registerorganization')
													.css(
															{
																"border" : "1px solid red"
															});
											jQuery("#registerorganization")
													.focus();
											return false;
										}

										else if (firstname == '') {
											jQuery('#firstname_error').show();
											jQuery('#firstname_error').text(
													"Please enter first name.");
											jQuery('#registrationfirstname')
													.css(
															{
																"border" : "1px solid red"
															});
											jQuery("#registrationfirstname")
													.focus();
											return false;
										} else if (lastname == '') {
											jQuery('#lastname_error').show();
											jQuery('#lastname_error').text(
													"Please enter last name.");
											jQuery('#registrationlastname')
													.css(
															{
																"border" : "1px solid red"
															});
											jQuery("#registrationlastname")
													.focus();
											return false;
										}

										else if (isNaN(zip)) {
											jQuery('#registerzipcode').css({
												"border" : "1px solid red"
											});
											jQuery('#zipcode_error').show();
											jQuery('#zipcode_error')
													.text(
															"Please enter valid zip code.");
											jQuery("#registerzipcode").focus();
											return false;
										} else if ((zip.length != 0)
												&& (zip.length != 6)) {
											jQuery('#registerzipcode').css({
												"border" : "1px solid red"
											});
											jQuery('#zipcode_error').show();
											jQuery('#zipcode_error')
													.text(
															"Zip Code must contain 6 digits.");
											jQuery("#registerzipcode").focus();
											return false;
										} else if (isNaN(jQuery(
												"#registercontactphone").val())) {
											jQuery('#registercontactphone')
													.css(
															{
																"border" : "1px solid red"
															});
											jQuery('#contactnumber_error')
													.show();
											jQuery('#contactnumber_error')
													.text(
															"Please enter valid contact number.");
											jQuery("#registercontactphone")
													.focus();
											return false;
										} else if ((cntphn.length != 0)
												&& (cntphn.length != 10)) {
											jQuery('#registercontactphone')
													.css(
															{
																"border" : "1px solid red"
															});
											jQuery('#contactnumber_error')
													.show();
											jQuery('#contactnumber_error')
													.text(
															"Contact Number must contain 10 digits.");
											jQuery("#registercontactphone")
													.focus();
											return false;
										} else if (cntmail == '') {
											jQuery('#contactemail_error')
													.show();
											jQuery('#contactemail_error').text(
													"Please enter email id.");
											jQuery('#registercontactemail')
													.css(
															{
																"border" : "1px solid red"
															});
											jQuery("#registercontactemail")
													.focus();
											return false;
										} else if (atpos < 2
												|| dotpos < atpos + 3
												|| dotpos + 2 >= cntmail.length) {
											jQuery('#contactemail_error')
													.show();
											jQuery('#contactemail_error').text(
													"Invalid email id.");
											jQuery('#registercontactemail')
													.css(
															{
																"border" : "1px solid red"
															});
											jQuery("#registercontactemail")
													.focus();
										} else {
											jQuery
													.ajax({
														type : 'Post',
														url : serviceURL,
														dataType : "json",
														data : {
															firstname : jQuery(
																	'#registrationfirstname')
																	.val(),
															lastname : jQuery(
																	'#registrationlastname')
																	.val(),
															notes : jQuery(
																	'#registernote')
																	.val(),
															address : jQuery(
																	'#registerenteraddress')
																	.val(),
															city : jQuery(
																	'#registercityname')
																	.val(),
															state : jQuery(
																	'#registerstatename')
																	.val(),
															country : jQuery(
																	'#registercountryname')
																	.val(),
															zipCode : jQuery(
																	'#registerzipcode')
																	.val(),
															contactName : jQuery(
																	'#registercontactname')
																	.val(),
															contactEmail : jQuery(
																	'#registercontactemail')
																	.val(),
															contactPhone : jQuery(
																	'#registercontactphone')
																	.val(),
															organizationName : jQuery(
																	'#registerorganization')
																	.val(),
															customerWebsite : jQuery(
																	'#registrationwebsite')
																	.val(),
															isAdmin : jQuery(
																	'input[name=isAdminRadio1]:checked')
																	.val(),
															approvalStatus : 'PENDING'
														},

														success : function(data) {
															var responseTextFlag = data.success;
															var isEmailExist = data.customerRegistration;
															if (isEmailExist == "userEmailExist") {
																jQuery(
																		'#contactemail_error')
																		.show();
																jQuery(
																		'#contactemail_error')
																		.text(
																				"Sorry this email id already exists.");
															} else if (responseTextFlag == false) {

																alert("Failed");
															} else {

																window.location.href = "successful.html";
															}
														},
														failure : function(data) {
															window.location.href = "../../";
														}
													});

										}
									});

					jQuery('#registerorganization').keypress(function(e) {
						jQuery('#orgname_error').hide();
						jQuery('#registerorganization').css({
							"border" : "1px solid #0866c6"
						});
					});

					jQuery('#registrationfirstname').keypress(function(e) {
						jQuery('#firstname_error').hide();
						jQuery('#registrationfirstname').css({
							"border" : "1px solid #0866c6"
						});
					});

					jQuery('#registrationlastname').keypress(function(e) {
						jQuery('#lastname_error').hide();
						jQuery('#registrationlastname').css({
							"border" : "1px solid #0866c6"
						});
					});

					jQuery('#registercontactemail').keypress(function(e) {
						jQuery('#contactemail_error').hide();
						jQuery('#registercontactemail').css({
							"border" : "1px solid #0866c6"
						});
					});

					jQuery('#registerzipcode').keypress(function(e) {
						jQuery('#zipcode_error').hide();
						jQuery('#registerzipcode').css({
							"border" : "1px solid #0866c6"
						});
					});

					jQuery('#registercontactphone').keypress(function(e) {
						jQuery('#contactnumber_error').hide();
						jQuery('#registercontactphone').css({
							"border" : "1px solid #0866c6"
						});
					});

				});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}