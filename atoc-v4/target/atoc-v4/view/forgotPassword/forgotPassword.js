jQuery(document).ready(
		function() {

			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);

			jQuery("#home").click(function(e) {
				e.preventDefault();
				window.location.href = "../../";
			});

			jQuery("#forgot_email").focus();
			jQuery('.only_alphabets').bind('keyup blur', function() {
				jQuery(this).val(jQuery(this).val().replace(/[^A-Za-z]/g, ''));
			});
			jQuery('.only_removeSpace').bind('keyup blur', function() {

				jQuery(this).val(jQuery(this).val().replace(/\s/g, ''));
			});

			jQuery('#forgot_email').keypress(function(e) {

				jQuery(forgot_email).css({
					"border" : "",
				});
				jQuery('#forgotfieldNotEmpty').hide();
				jQuery('#emailInvalid').hide();
			});

			jQuery("#forgot_send_btn").click(
					function(e) {
						e.preventDefault();
						var serviceURL = envConfig.serviceBaseURL
								+ '/user/forgotPassword.action';
						console.log(serviceURL);

						var email = jQuery("#forgot_email").val();
						var atpos = email.indexOf("@");
						var dotpos = email.lastIndexOf(".");
						if (email == '') {
							jQuery('#forgotfieldNotEmpty').show();
							jQuery('#emailInvalid').hide();
							jQuery('#forgotemailnotvalid').hide();
							jQuery('#forgot_email').css({
								"border" : "1px solid #dd4b39"
							});
							return false;
						} else if (atpos < 2 || dotpos < atpos + 3
								|| dotpos + 2 >= email.length) {
							jQuery('#forgotfieldNotEmpty').hide();
							jQuery('#emailInvalid').show();
							jQuery('#forgotemailnotvalid').hide();
							jQuery('#forgot_email').css({
								"border" : "1px solid #dd4b39"
							});
						} else {
							jQuery.ajax({
								url : serviceURL,
								type : 'POST',
								data : {
									userEmail : jQuery('#forgot_email').val(),

								},
								success : function(data) {
									var responseTextFlag = data.success;
									if (responseTextFlag == true) {
										jQuery('#forgotemailnotvalid').hide();
										jQuery('#forgotfieldNotEmpty').hide();
										jQuery('#emailInvalid').hide();
										window.location.href = "../../";
									} else {
										jQuery('#forgotemailnotvalid').show();
										jQuery('#forgotfieldNotEmpty').hide();
										jQuery('#emailInvalid').hide();
									}
								},
								failure : function(data) {
									alert("failed");
									window.location.href = "../../";
								}
							});
						}
					});
		});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}
