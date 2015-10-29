jQuery(document)
		.ready(
				function() {
					jQuery("#adminparent").addClass("active");
					jQuery("#generateCssSelector").addClass("active");
					jQuery(document).ajaxStart(customblockUI);
					jQuery(document).ajaxStop(customunblockUI);

					jQuery('#downloadurl').prop("disabled", true);
					jQuery('.only_alphabets').bind(
							'keyup blur',
							function() {
								jQuery(this).val(
										jQuery(this).val().replace(/\s/g, ''));
							});

					jQuery('#submiturl')
							.click(
									function(e) {
										jQuery('#createurl').hide();
										if (/^(http|https|ftp):\/\/[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/i
												.test(jQuery("#urlname").val())) {

											jQuery('#createurl').hide();
											e.preventDefault();
											hideAllErrors();
											var userId = sessionStorage
													.getItem("userId");
											var serviceURL = envConfig.serviceBaseURL
													+ '/user/addUrlProperty.action?userId='
													+ userId;

											var urlproperty = jQuery('#urlname')
													.val();
											jQuery
													.ajax({
														url : serviceURL,
														dataType : "json",
														cache : false,
														type : 'POST',
														data : {
															urlname : jQuery('#urlname').val(),
															createdBy : userId,
															updatedBy : userId,
															isActive : 'yes',
															projectType : 'java',
															userId : userId
														},
														success : function(data) {
															jQuery('#downloadurl').prop("disabled",false);
															jQuery('#createdmessage').show();
															jQuery('#deletecssSelectorMsg').hide();
															jQuery('#deletecssSelectorMsg').hide();
															var cssSelector_path = data.cssSelector_path;
															localStorage.setItem("cssSelectorFileName",data.cssSelectorFileName);
														},
														failure : function(data) {
															alert("Failed");
														},
														statusCode : {
															403 : function(xhr) {
																alert("Session will be Expired");
																window.location.href = "../../";
															}
														}
													});
										}
										else {
											jQuery('#createurl').show();
											jQuery('#urlname').css({
												"border" : "1px solid red",
											});
										}
									});
					jQuery('#downloadurl')
							.click(
									function(e) {
										jQuery('#createdmessage').hide();
										var cssSelectorFileName = localStorage
												.getItem("cssSelectorFileName");
										var userId = sessionStorage
												.getItem("userId");
										var serviceURL = envConfig.serviceBaseURL
												+ '/user/downloadUrlProperty.action?userId='
												+ userId
												+ '&cssSelectorFileName='
												+ cssSelectorFileName;
										//window.location.href = "" + serviceURL+ "";
										window.open(serviceURL,'_blank');
										jQuery('#urlname').val('');
										jQuery('#downloadurl').prop("disabled",
												true);
									});
					jQuery('#urlname').focus();
					jQuery("#urlname").on("input", function() {
						jQuery('#createurl').hide();
						jQuery('#urlname').css({
							"border" : "1px solid #0866c6",
						});
					});
				});
function hideAllErrors() {
	jQuery('#createurl').hide();
	jQuery('#createdmessage').hide();
	jQuery('#deletecssSelectorMsg').hide();
	jQuery('#noCssFileMsg').hide();
	jQuery('#deleteurl').hide();
}
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}