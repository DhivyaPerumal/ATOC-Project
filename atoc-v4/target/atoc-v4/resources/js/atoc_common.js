jQuery(document).ready(function() {
		var userId = sessionStorage.getItem("userId");
		var isAdmin = sessionStorage.getItem("isAdmin");
		if (isAdmin == "Master Admin") {
			jQuery('#masteradmin_menu_id').show();
			jQuery('#orgoradminuser_menu_id').hide();
			jQuery('#masterUserList').show();
		} else if (isAdmin == "Org Admin") {
			jQuery('#masteradmin_menu_id').hide();
			jQuery('#orgoradminuser_menu_id').show();
			jQuery('#adminaddOrgUser').show();
			jQuery('#masterUserList').show();
		} else if (isAdmin == "Org User") {
			jQuery('#masteradmin_menu_id').hide();
			jQuery('#orgoradminuser_menu_id').show();
			jQuery('#adminaddOrgUser').hide();
			jQuery('#masterUserList').hide();
			
		} else {
			jQuery('#masteradmin_menu_id').hide();
			jQuery('#orgoradminuser_menu_id').hide();
		}


var userEmail = sessionStorage.getItem("userEmail");
		var userName = sessionStorage.getItem("userName");
		var userId = sessionStorage.getItem("userId");
		jQuery("#userEmail_id").append(userEmail);
		jQuery("#userName_id").append(userName);
		jQuery('#logout_link')
										.click(
												function() {

													var userId = sessionStorage
															.getItem("userId");
													var userToken = sessionStorage
															.getItem("userToken");
													var serviceURL = envConfig.serviceBaseURL
															+ '/user/logoutUser.action?userId='
															+ userId;
													console.log(serviceURL);
													jQuery
															.ajax({
																type : 'Post',
																url : serviceURL,
																dataType : "json",
																data : {
																	userToken : userToken
																},
																success : function(
																		data) {
																	window.location.href = "../../";
																},
																failure : function() {
																	window.location.href = "dashboard.html";
																}
															});
												});
												
							jQuery(".active").removeClass("active");
						
					//side bar accordion menu
						jQuery('.leftmenu li').each(function() {
							if (jQuery(this).children('ul').length > 0) {
								jQuery(this).addClass('parent');
							}
						});

						jQuery('.leftmenu li.parent > a')
								.click(
										function(event) {
											jQuery(this).parent().siblings().find(
													'ul').slideUp('fast');

											if (jQuery(this).next('ul')
													.is(':hidden')) {
												event.preventDefault();
												jQuery(this).parent().children('ul')
														.slideDown('fast');
												jQuery(this).parent().children
														.siblings('ul')
														.slideUp('fast');
											}
										});
										
		jQuery('#default,#navyblue,#palegreen,#red,#green,#brown').click(function(e) {
		
		var colorSkin_Slected = jQuery(this).attr('id');
		// alert(colorSkin_Slected);
	var userId = sessionStorage.getItem("userId");
										e.preventDefault();
										var serviceURL = envConfig.serviceBaseURL
					+ '/user/saveChangeSkin.action?userId='
					+ userId;
					
			console.log(serviceURL);
										jQuery.ajax({
														url : serviceURL,
														type : 'Post',
														dataType : "json",
														data : {
														userId : userId,
														colorSkin: colorSkin_Slected
														},
														success : function(res) {
														var colorSkin = res.data.colorSkin;
														//alert(res.data.colorSkin);
														sessionStorage.setItem("colorSkin",colorSkin);
														if(colorSkin != null){
														jQuery('head').append('<link id="skinstyle" rel="stylesheet" href="../../resources/css/style.'+colorSkin+'.css" type="text/css" />');
														jQuery.cookie("skin-color", colorSkin, { path: '/' });
														}
														
														},
														failure : function(res) {
															window.location.href = "../../";
														}
													});
		
		}); 
		
		 var colorSkin = sessionStorage.getItem("colorSkin");
		//alert(colorSkin);
		
		jQuery('head').append('<link id="skinstyle" rel="stylesheet" href="../../resources/css/style.'+colorSkin+'.css" type="text/css" />');
		jQuery.cookie("skin-color", colorSkin, { path: '/' });
		
		//Get the Notifications
		jQuery("#viewMore").hide();
		var userId = sessionStorage.getItem("userId");
			var serviceURL = envConfig.serviceBaseURL
					+ '/notificationView/getNotificationsOfUser.action?userId='
					+ userId;
			console.log(serviceURL);
			jQuery.ajax({
				type : 'GET',
				url : serviceURL,
				dataType : "json",
				success : function(data) {
					JsonStringify_Data = JSON.stringify(data);
					var obj = jQuery.parseJSON(JsonStringify_Data);
					var noOfNotifications = data.total;
					if(noOfNotifications > 0) {  
					jQuery('#notification_Count').text(noOfNotifications);  } else {
					jQuery('#notification_Count').text("");	
					}
					var arr = [];
					jQuery.each(obj, function(key, val) {
					jQuery.each(val, function(i, e) {
						arr.push(e);
						var count = jQuery("#notification_dropdown").children().length;
						if(count > 3){
							jQuery('#viewMore').show();
							if(count <=3){
								jQuery('#notification_dropdown').append( '<a href='+e.notificationId+' id = '+e.notificationId+' onClick="myfun(this);"><span class="fa fa-bell-o"></span> <strong>'+ e.notificationTitle+'</strong></a>');
								jQuery('#viewMore').show();
							}
							jQuery('#viewMore').click(function(event){
							event.preventDefault();
							for(i = 4;i <= count;i++){
								jQuery('#notification_dropdown').append( '<a href='+e.notificationId+' id = '+e.notificationId+' onClick="myfun(this);"><span class="fa fa-bell-o"></span> <strong>'+ e.notificationTitle+'</strong></a>');
            				}
							jQuery('#viewMore').hide();
							});
						}
						else{
							jQuery('#notification_dropdown').append( '<a href='+e.notificationId+' id = '+e.notificationId+' onClick="myfun(this);"><span class="fa fa-bell-o"></span> <strong>'+ e.notificationTitle+'</strong></a>');
							jQuery('#viewMore').hide();
						}
						jQuery('#notification_dropdown').click(function(event){
							event.preventDefault();
						});
					});
					});
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
});