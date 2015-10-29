var JsonStringify_Data ;
jQuery(document).ready(function() {
			jQuery(document).ajaxStart(customblockUI);
			jQuery(document).ajaxStop(customunblockUI);
			jQuery("#notification_panel").hide();
});
function myfun(s)
{
		var notificationId_view = s.getAttribute("id");
		jQuery("#notification_panel").show();
	
		var userId = sessionStorage.getItem("userId");
				var serviceURL = envConfig.serviceBaseURL
								+ '/notificationView/saveNotificationView.action?userId='
								+ userId;
				console.log(serviceURL);
				jQuery.ajax({
								url : serviceURL,
								cache : false,
								dataType : "json",
								data : {
										notificationId : notificationId_view,
										notificationStatus : "VIEWED",
										userId : userId,
										createdBy : userId,
										updatedBy : userId
										},
								type : 'Post',
								success : function(data) {
									
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
					
				var obj_view = jQuery.parseJSON(JsonStringify_Data);
				var arr = [];
					jQuery.each(obj_view, function(key1, val1) {
					jQuery.each(val1, function(i1, e1) {
						if(e1.notificationId == notificationId_view){
						jQuery("#title").text(""+e1.notificationTitle);
						jQuery("#message").text(""+e1.notificationMessage);
						}
						
					});
					});
				
}
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}