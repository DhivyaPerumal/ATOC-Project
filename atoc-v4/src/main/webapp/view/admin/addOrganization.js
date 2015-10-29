jQuery(document)
		.ready(
				function() {
					jQuery(".active").removeClass("active");
						jQuery("#addorganization").addClass("active");
						
						jQuery(document).ajaxStart(customblockUI);
						jQuery(document).ajaxStop(customunblockUI);
						
				var userId = sessionStorage.getItem("userId");
					jQuery("#orgorganization").focus();
					jQuery('#fieldNotEmpty').hide();
					jQuery('#emailInvalid').hide();
					jQuery('#registereduseralreadyexists').hide();
					jQuery('#orgNameAlreadyExists').hide();
					
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
					jQuery("#submitBtn")
							.click(
									function(e) {

										e.preventDefault();

										var note = jQuery("#orgnote").val();
										var add = jQuery("#orgaddress").val();
										var city = jQuery("#orgcityname").val();
										var ste = jQuery("#orgstatename").val();
										var cntry = jQuery("#orgcountryname").val();
										var zip = jQuery("#orgzipcode").val();
										var cntmail = jQuery("#orgtactemail").val();

										var atpos = cntmail.indexOf("@");

										var dotpos = cntmail.lastIndexOf(".");

										var cntphn = jQuery("#orgcontactphone")
												.val();

										var orgname = jQuery("#orgorganization")
												.val();

										var orgcontactphone_length = jQuery(
												"#orgcontactphone").val().length;
										var orgzipcode_length = jQuery("#orgzipcode")
												.val().length;

										var userId = sessionStorage
												.getItem("userId");
										var serviceURL = envConfig.serviceBaseURL
												+ '/organization/adminAddOrganization.action?userId='
												+ userId;
										console.log(serviceURL);

										if (isNaN(jQuery("#orgcontactphone").val())) {
											jQuery('#orgcontactphone').css({
												"border" : "1px solid red",
											});
											jQuery('#orgcontactphone_Error').show();
											jQuery('#addOrgErrorEmail').hide();
											jQuery('#emailInvalid').hide();
											jQuery('#addOrgName_Error').hide();
											jQuery('#registereduseralreadyexists')
													.hide();
											jQuery('#orgNameAlreadyExists').hide();
											jQuery("#orgcontactphone").focus();
											return false;
										} else if ((orgcontactphone_length != 0)
												&& (orgcontactphone_length != 10)) {
											// alert(" in Else if ");
											jQuery('#orgcontactphone').css({
												"border" : "1px solid red"
											});
											jQuery('#orgcontactphone_Length_Error')
													.show();
											jQuery('#orgcontactphone_Error').hide();
											jQuery('#orgcontactFax_Error').hide();
											jQuery('#orgcontactZipcode_Error')
													.hide();
											jQuery('#orgcontactphone_Error').hide();
											jQuery('#addOrgErrorEmail').hide();
											jQuery('#emailInvalid').hide();
											jQuery('#addOrgName_Error').hide();
											jQuery('#registereduseralreadyexists')
													.hide();
											jQuery('#orgNameAlreadyExists').hide();
											jQuery("#orgcontactphone").focus();
											return false;
										} else {
										}

										if (isNaN(jQuery("#orgzipcode").val())) {
											jQuery('#orgzipcode').css({
												"border" : "1px solid red"
											});
											jQuery('#orgcontactZipcode_Error')
													.show();
											jQuery('#orgcontactFax_Error').hide();
											jQuery('#orgcontactphone_Error').hide();
											jQuery('#addOrgErrorEmail').hide();
											jQuery('#emailInvalid').hide();
											jQuery('#addOrgName_Error').hide();
											jQuery('#registereduseralreadyexists')
													.hide();
											jQuery('#orgNameAlreadyExists').hide();
											jQuery("#orgzipcode").focus();
											return false;
										} else if ((orgzipcode_length != 0)
												&& (orgzipcode_length != 6)) {
											// alert(" in Else if ");
											jQuery('#orgzipcode').css({
												"border" : "1px solid red"
											});
											jQuery('#orgcontactZip_Length_Error')
													.show();
											jQuery('#orgcontactFax_Error').hide();
											jQuery('#orgcontactZipcode_Error')
													.hide();
											jQuery('#orgcontactphone_Error').hide();
											jQuery('#addOrgErrorEmail').hide();
											jQuery('#emailInvalid').hide();
											jQuery('#addOrgName_Error').hide();
											jQuery('#registereduseralreadyexists')
													.hide();
											jQuery('#orgNameAlreadyExists').hide();
											jQuery("#orgzipcode").focus();
											return false;
										} else {
										}

										if (jQuery("#orgorganization").val() == '') {
											// addOrgName_Error emailInvalid
											// registereduseralreadyexists
											jQuery('#orgorganization').css({
												"border" : "1px solid red",
											// "background": "#FFCECE"
											});
											jQuery('#addOrgName_Error').show();
											jQuery('#emailInvalid').hide();
											jQuery('#registereduseralreadyexists')
													.hide();
											jQuery('#orgNameAlreadyExists').hide();
											jQuery("#orgorganization").focus();
											return false;
										} else if (jQuery("#orgtactemail").val() == '') {
											jQuery('#orgtactemail').css({
												"border" : "1px solid red",
											// "background": "#FFCECE"
											});
											jQuery('#addOrgErrorEmail').show();
											jQuery('#emailInvalid').hide();
											jQuery('#addOrgName_Error').hide();
											jQuery('#registereduseralreadyexists')
													.hide();
											jQuery('#orgNameAlreadyExists').hide();
											jQuery("#orgtactemail").focus();
											return false;
										}

										else if (atpos < 2
												|| dotpos < atpos + 3
												|| dotpos + 2 >= cntmail.length) {
											jQuery('#fieldNotEmpty').hide();
											jQuery('#addOrgErrorEmail').hide();
											jQuery('#orgtactemail').css({
												"border" : "1px solid red",
											// "background": "#FFCECE"
											});
											jQuery('#emailInvalid').show();
											jQuery('#registereduseralreadyexists')
													.hide();
											jQuery('#orgNameAlreadyExists').hide();
											jQuery("#orgtactemail").focus();
											return false;
										} else if (isNaN(jQuery("#orgfax").val())) {
											jQuery('#orgfax').css({
												"border" : "1px solid red"
											});
											jQuery('#orgcontactFax_Error').show();
											jQuery('#orgcontactZipcode_Error')
													.hide();
											jQuery('#orgcontactphone_Error').hide();
											jQuery('#addOrgErrorEmail').hide();
											jQuery('#emailInvalid').hide();
											jQuery('#addOrgName_Error').hide();
											jQuery('#registereduseralreadyexists')
													.hide();
											jQuery('#orgNameAlreadyExists').hide();
											jQuery("#orgfax").focus();
											return false;
										} else {
											/*jQuery
													.blockUI({
														message : jQuery('#loading_Message'),
														css : {
															border : '1px solid #30a5ff'
														}
													});*/
											jQuery
													.ajax({
														type : 'Post',
														url : serviceURL,
														dataType : "json",
														data : {
															organizationName : jQuery(
																	'#orgorganization')
																	.val(),
															phone : jQuery(
																	'#orgcontactphone')
																	.val(),
															email : jQuery(
																	'#orgtactemail')
																	.val(),
															fax : jQuery('#orgfax')
																	.val(),
															notes : jQuery(
																	'#orgnote')
																	.val(),
															address : jQuery(
																	'#orgaddress')
																	.val(),
															city : jQuery(
																	'#orgcityname')
																	.val(),
															state : jQuery(
																	'#orgstatename')
																	.val(),
															country : jQuery(
																	'#orgcountryname')
																	.val(),
															zipCode : jQuery(
																	'#orgzipcode')
																	.val(),
															isActive : 'y',
															createdBy : userId,
															updatedBy : userId
														},

														success : function(data) {
															var responseTextFlag = data.success;

															if (responseTextFlag == false) {
																var addOrgFlag = data.addOrg;
																if (addOrgFlag == "orgNameExist") {
																	/*jQuery
																			.unblockUI();*/
																	jQuery(
																			'#orgNameAlreadyExists')
																			.show();
																	jQuery(
																			"#orgorganization")
																			.focus();
																} else if (addOrgFlag == "orgEmailExist") {
																	/*jQuery
																			.unblockUI();*/
																	jQuery(
																			'#registereduseralreadyexists')
																			.show();
																	jQuery(
																			"#orgtactemail")
																			.focus();
																}
															} else {
																//jQuery.unblockUI();
																jQuery(
																		'#success_modal')
																		.modal(
																				'toggle');
																jQuery(
																		'#success_modal')
																		.modal(
																				'view');

																// window.location.href = "organizations.html";
															}

														},
														failure : function(data) {
															//jQuery.unblockUI();
															//alert( "Failed" );
															window.location.href = "registrationApprovals.html";
														},
														statusCode : {
															403 : function(xhr) {
																//jQuery.unblockUI();
																alert("Session will be Expired");
																window.location.href = "../../";

															}
														}
													});

										}
									});

					jQuery('#loading_cancel_btn').click(function(event) {
						//jQuery.unblockUI();
					});

					jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "../admin/organizations.html";
					});

					jQuery('#cancelbtn').click(function(e) {
						e.preventDefault();
						window.location.href = "../dashboard/registrationApprovals.html";
					});

					jQuery("#orgorganization").val('');
					jQuery("#orgtactemail").val('');
					jQuery("#orgcontactphone").val('');
					jQuery("#orgfax").val('');
					jQuery('#orgnote').val('');
					jQuery('#orgaddress').val('');
					jQuery('#orgcityname').val('');
					jQuery('#orgstatename').val('');
					jQuery('#orgcountryname').val('');
					jQuery('#orgzipcode').val('');

					jQuery("#orgorganization").on("input", function() {
						jQuery('#addOrgName_Error').hide();
						jQuery('#fieldNotEmpty').hide();
						jQuery('#addOrgName_Error').hide();
						jQuery('#emailInvalid').hide();
						jQuery('#registereduseralreadyexists').hide();
						jQuery('#orgNameAlreadyExists').hide();
						jQuery('#orgorganization').css({
							"border" : "1px solid #0866c6",
						//"background" : "#FFCECE"
						});
					});
					jQuery("#orgtactemail").on("input", function() {
						jQuery('#emailInvalid').hide();
						jQuery('#addOrgName_Error').hide();
						jQuery('#addOrgErrorEmail').hide();
						jQuery('#registereduseralreadyexists').hide();
						jQuery('#fieldNotEmpty').hide();
						jQuery('#emailInvalid').hide();
						jQuery('#orgNameAlreadyExists').hide();
						jQuery('#orgtactemail').css({
							"border" : "1px solid #0866c6",
						});
					});
					jQuery("#orgcontactphone").on("input", function() {
						jQuery('#orgcontactphone_Error').hide();
						jQuery('#orgcontactZip_Length_Error').hide();
						jQuery('#orgcontactZipcode_Error').hide();
						jQuery('#orgcontactphone_Length_Error').hide();
						jQuery('#orgcontactphone_Error').hide();
						jQuery('#orgcontactFax_Error').hide();
						jQuery('#emailInvalid').hide();
						jQuery('#addOrgName_Error').hide();
						jQuery('#addOrgErrorEmail').hide();
						jQuery('#registereduseralreadyexists').hide();
						jQuery('#fieldNotEmpty').hide();
						jQuery('#emailInvalid').hide();
						jQuery('#orgNameAlreadyExists').hide();
						jQuery('#orgcontactphone').css({
							"border" : "1px solid #0866c6",
						});
					});

					jQuery("#orgzipcode").on("input", function() {
						jQuery('#orgcontactZipcode_Error').hide();
						jQuery('#orgcontactZip_Length_Error').hide();
						jQuery('#orgcontactphone_Length_Error').hide();
						jQuery('#orgcontactphone_Error').hide();
						jQuery('#orgcontactFax_Error').hide();
						jQuery('#emailInvalid').hide();
						jQuery('#addOrgName_Error').hide();
						jQuery('#addOrgErrorEmail').hide();
						jQuery('#registereduseralreadyexists').hide();
						jQuery('#fieldNotEmpty').hide();
						jQuery('#emailInvalid').hide();
						jQuery('#orgNameAlreadyExists').hide();
						jQuery('#orgzipcode').css({
							"border" : "1px solid #0866c6",
						});
					});

					jQuery("#orgfax").on("input", function() {
						jQuery('#orgcontactFax_Error').hide();
						jQuery('#orgcontactZip_Length_Error').hide();
						jQuery('#orgcontactphone_Length_Error').hide();
						jQuery('#orgcontactZipcode_Error').hide();
						jQuery('#orgcontactphone_Error').hide();
						jQuery('#emailInvalid').hide();
						jQuery('#addOrgName_Error').hide();
						jQuery('#addOrgErrorEmail').hide();
						jQuery('#registereduseralreadyexists').hide();
						jQuery('#fieldNotEmpty').hide();
						jQuery('#emailInvalid').hide();
						jQuery('#orgNameAlreadyExists').hide();
						jQuery('#orgfax').css({
							"border" : "1px solid #0866c6",
						});
					});

				});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}