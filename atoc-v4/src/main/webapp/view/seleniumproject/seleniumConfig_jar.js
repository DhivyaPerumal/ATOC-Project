jQuery(document).ready(function() {
	jQuery("#adminparent").addClass("active");
	jQuery("#configJar").addClass("active");
	
	jQuery(document).ajaxStart(customblockUI);
	jQuery(document).ajaxStop(customunblockUI);
	
	var userId = sessionStorage.getItem("userId");
	var isAdmin = sessionStorage.getItem("isAdmin");
	
	var userId = sessionStorage.getItem("userId");
					var serviceURL = envConfig.serviceBaseURL
							+ '/jarVersion/viewJarVersionList.action?userId='
							+ userId;
					jQuery.ajax({
								url : serviceURL,
								dataType : 'json',
								type : 'GET',
								cache : 'false',
								success : function(data) {
									jarList = "";
									var JsonStringify_Data = JSON
											.stringify(data);
									obj = jQuery.parseJSON(JsonStringify_Data);
									var arr = [];
									jQuery.each(obj, function(i, e) {
										jQuery.each(e, function(key1, val_0) {
											arr.push(val_0);
										});
									});
									var result = jQuery.map(arr, function(val,
											key) {
										return {
											jarId : val.jarId,
											jarName : val.jarName,
											category : val.category

										};
									});
									jQuery("#Slenium_Slide").hide();
									jQuery("#Slenium_serverSlide").hide();
									jQuery("#mailSlide").hide();
									jQuery("#apcheSlide").hide();
									jQuery("#othersSlide").hide();
									var i = 0;
									jQuery.each(result,function(key, val) {
														var jar_Name = val.jarName;
														var category = val.category;
														var jarId = val.jarId;
														if(category == "Selenium Java")
														 {
															var x = document
																	.getElementById("#selenium_java");
															sname = "<input type=\"checkbox\" name=\"ocCustomerServiceId1\" class=\"chk\" value=\""
																	+ val.jarId
																	+ "\" id=\""
																	+ val.jarId
																	+ "\">&nbsp; &nbsp;"
																	+ val.jarName
																	+ "</br>";
															jQuery("input:checkbox").change(function() {
																var group = ":checkbox[name='" + jQuery(this)
																								.attr("name")
																								+ "']";
																
																if (jQuery(this).is('.chk:checked')) 
																{
																	jQuery(group).not(jQuery(this)).attr("checked",false);
																}
															});
															jQuery("#selenium_java").append(sname);
														} else if (category == "Selenium Server Stand Alone") {
															var x = document
																	.getElementById("#selenium_server");
															sname = "<input type=\"checkbox\" name=\"ocCustomerServiceId2\" class=\"chk\" value=\""
																	+ val.jarId
																	+ "\" id=\""
																	+ val.jarId
																	+ "\">&nbsp; &nbsp;"
																	+ val.jarName
																	+ "</br>";
															jQuery("input:checkbox").change(function() {
																var group = ":checkbox[name='" + jQuery(this)
																								.attr("name")
																						+ "']";
																if (jQuery(this).is('.chk:checked')) {
																		jQuery(group).not(jQuery(this))
																				.attr("checked",false);
																				}
															});
															jQuery("#selenium_server").append(sname);
														} else if (category == "Mail") {
															var x = document
																	.getElementById("#checkAll");
															sname = "<input type=\"checkbox\" name=\"ocCustomerServiceId3\" class=\"chk\" value=\""
																	+ val.jarId
																	+ "\" id=\""
																	+ val.jarId
																	+ "\">&nbsp; &nbsp;"
																	+ val.jarName
																	+ "</br>";
															jQuery("input:checkbox").change(function() {
																var group = ":checkbox[name='"
																						+ jQuery(this).attr("name")
																						+ "']";
															if (jQuery(this).is('.chk:checked')) {
																jQuery(group).not(jQuery(this))
																					.attr("checked",false);
																				}
															});
															jQuery("#mailed").append(sname);
														} else if (category == "Apache Poi") {
															var x = document
																	.getElementById("#apache_poi");
															sname = "<input type=\"checkbox\" name=\"ocCustomerServiceId4\" class=\"chk\" value=\""
																	+ val.jarId
																	+ "\" id=\""
																	+ val.jarId
																	+ "\">&nbsp; &nbsp;"
																	+ val.jarName
																	+ "</br>";
																jQuery("input:checkbox").change(function() {
																var group = ":checkbox[name='" + jQuery(this)
																								.attr("name")
																						+ "']";
																if (jQuery(this).is('.chk:checked')) {
																		jQuery(group).not(jQuery(this))
																				.attr("checked",false);
																				}
															});
															jQuery("#apache_poi").append(sname);
														} else {
															var x = document
																	.getElementById("#createProject_othersJar");
															sname = "<td><input type=\"checkbox\" name=\"ocCustomerServiceId5\" class=\"chk\" value=\""
																	+ val.jarId
																	+ "\" id=\""
																	+ val.jarId
																	+ "\">&nbsp; &nbsp;"
																	+ val.jarName
																	+ "</td>";
																jQuery("input:checkbox").change(function() {
																var group = ":checkbox[name='" + jQuery(this)
																								.attr("name")
																						+ "']";
																if (jQuery(this).is('.chk:checked')) {
																		jQuery(group).not(jQuery(this))
																				.attr("checked",false);
																				}
															});
															if (i != 3) {
																jQuery("#createProject_othersJar tbody").append(sname);
																i++;
															} else {
																i = 0;
																jQuery("#createProject_othersJar tbody").append(
																				"<tr><tr/>");
															}
														}
													});
								}
							});
	
		var addvalue="";
	 jQuery("#addSeleinumJar,#addServerJar,#addMailJar,#addApacheJar,#addOthersJar").click(function(){
		 var category = this.id;
		 jQuery('#uploadpanel').remove();
		 if (category == 'addSeleinumJar')
		 {
			jQuery("#Slenium_Slide").append('<div id="uploadpanel" class="panel panel-default"><div class="form-group"><label for="uploadJar">Upload Jar:</label><input type="hidden" name="category" value="Selenium Java"/><input type="file" name="file" id="file" class="form-group" /><input type="hidden" id = "jarId" name="jarId"/><p id="configJarError" style="color: red; display: none;">Please select .jar files only </p><p id="configJarError_exceeds" style="color: red; display: none;">Please upload below 20mb .jar files only </p></div><div class="form-group"><button type="submit" class="btn btn-primary" id="seleinumJarSubmit">Submit</button><button type="reset" class="btn btn-primary" id="seleinumJar_cancel_btn">Cancel</button> </div> </div>');
			addvalue="add";
		 jQuery('#jarId').val('');
		 jQuery("#Slenium_Slide").slideToggle("slow");
									jQuery("#Slenium_serverSlide").hide();
									jQuery("#mailSlide").hide();
									jQuery("#apcheSlide").hide();
									jQuery("#othersSlide").hide();
									jQuery("#editSeleniumJarError").hide();
									jQuery("#editServerJarError").hide();
									jQuery("#editMailJarError").hide();
									jQuery("#editApacheJarError").hide();
									jQuery("#editOthersJarError").hide();
			jQuery("#seleinumJar_cancel_btn").click(function(e){
				e.preventDefault();
				window.location.href = "seleniumConfig_jar.html";
			});
		 }
		 if (category == 'addServerJar')
		 {
			jQuery("#Slenium_serverSlide").append('<div id="uploadpanel" class="panel panel-default"><div class="form-group"><label for="uploadJar">Upload Jar:</label><input type="hidden" name="category" value="Selenium Server Stand Alone"/><input type="file" name="file" id="file" class="form-group" /><input type="hidden" id = "jarId" name="jarId"/><p id="configJarError" style="color: red; display: none;">Please select .jar files only </p><p id="configJarError_exceeds" style="color: red; display: none;">Please upload below 20mb .jar files only </p></div><div class="form-group"><button type="submit" class="btn btn-primary" id="serverJarSubmit">Submit</button><button type="reset" class="btn btn-primary" id="severJar_cancel_btn">Cancel</button> </div> </div>');
			addvalue="add";
			jQuery('#jarId').val('');
		 
									jQuery("#Slenium_serverSlide").slideToggle("slow");
									jQuery("#Slenium_Slide").hide();
									jQuery("#mailSlide").hide();
									jQuery("#apcheSlide").hide();
									jQuery("#othersSlide").hide();
									jQuery("#editSeleniumJarError").hide();
									jQuery("#editServerJarError").hide();
									jQuery("#editMailJarError").hide();
									jQuery("#editApacheJarError").hide();
									jQuery("#editOthersJarError").hide();
			jQuery("#severJar_cancel_btn").click(function(e){
				e.preventDefault();
			window.location.href = "seleniumConfig_jar.html";
			});
		 }
		 if (category == 'addMailJar')
		 {
			jQuery("#mailSlide").append('<div id="uploadpanel" class="panel panel-default"><div class="form-group"><label for="uploadJar">Upload Jar:</label><input type="hidden" name="category" value="Mail"/><input type="file" name="file" id="file" class="form-group" /><input type="hidden" id = "jarId" name="jarId"/><p id="configJarError" style="color: red; display: none;">Please select .jar files only </p><p id="configJarError_exceeds" style="color: red; display: none;">Please upload below 20mb .jar files only </p></div><div class="form-group"><button type="submit" class="btn btn-primary" id="mailJarSubmit">Submit</button><button type="reset" class="btn btn-primary" id="mail_cancel_btn">Cancel</button> </div> </div>');
			addvalue="add";
			jQuery('#jarId').val('');
		 
									jQuery("#mailSlide").slideToggle("slow");
									jQuery("#Slenium_Slide").hide();
									jQuery("#Slenium_serverSlide").hide();
									jQuery("#apcheSlide").hide();
									jQuery("#othersSlide").hide();
									jQuery("#editSeleniumJarError").hide();
									jQuery("#editServerJarError").hide();
									jQuery("#editMailJarError").hide();
									jQuery("#editApacheJarError").hide();
									jQuery("#editOthersJarError").hide();
									
			jQuery("#mail_cancel_btn").click(function(e){
				e.preventDefault();
				window.location.href = "seleniumConfig_jar.html";
			});
		 }
		 if (category == 'addApacheJar')
		 {
			jQuery("#apcheSlide").append('<div id="uploadpanel" class="panel panel-default"><div class="form-group"><label for="uploadJar">Upload Jar:</label><input type="hidden" name="category" value="Apache Poi"/><input type="file" name="file" id="file" class="form-group" /><input type="hidden" id = "jarId" name="jarId"/><p id="configJarError" style="color: red; display: none;">Please select .jar files only </p><p id="configJarError_exceeds" style="color: red; display: none;">Please upload below 20mb .jar files only </p></div><div class="form-group"><button type="submit" class="btn btn-primary" id="apacheJarSubmit">Submit</button><button type="reset" class="btn btn-primary" id="apache_cancel_btn">Cancel</button> </div> </div>');
			addvalue="add";
			jQuery('#jarId').val('');
		 
									jQuery("#apcheSlide").slideToggle("slow");
									jQuery("#Slenium_Slide").hide();
									jQuery("#mailSlide").hide();
									jQuery("#Slenium_serverSlide").hide();
									jQuery("#othersSlide").hide();
									jQuery("#editSeleniumJarError").hide();
									jQuery("#editServerJarError").hide();
									jQuery("#editMailJarError").hide();
									jQuery("#editApacheJarError").hide();
									jQuery("#editOthersJarError").hide();
			jQuery("#apache_cancel_btn").click(function(e){
				e.preventDefault();
				window.location.href = "seleniumConfig_jar.html";
			});
		 }
		 if (category == 'addOthersJar')
		 {
			jQuery("#othersSlide").append('<div id="uploadpanel" class="panel panel-default"><div class="form-group"><label for="uploadJar">Upload Jar:</label><input type="hidden" name="category" value="Others"/><input type="file" name="file" id="file" class="form-group" /><input type="hidden" id = "jarId" name="jarId"/><p id="configJarError" style="color: red; display: none;">Please select .jar files only </p><p id="configJarError_exceeds" style="color: red; display: none;">Please upload below 20mb .jar files only </p></div><div class="form-group"><button type="submit" class="btn btn-primary" id="othersJarSubmit">Submit</button><button type="reset" class="btn btn-primary" id="others_cancel_btn">Cancel</button> </div> </div>');
			addvalue="add";
			jQuery('#jarId').val('');
		 
						jQuery("#othersSlide").slideToggle("slow");
									jQuery("#Slenium_Slide").hide();
									jQuery("#mailSlide").hide();
									jQuery("#apcheSlide").hide();
									jQuery("#Slenium_serverSlide").hide();
									jQuery("#editSeleniumJarError").hide();
									jQuery("#editServerJarError").hide();
									jQuery("#editMailJarError").hide();
									jQuery("#editApacheJarError").hide();
									jQuery("#editOthersJarError").hide();
			jQuery("#others_cancel_btn").click(function(e){
				e.preventDefault();
				window.location.href = "seleniumConfig_jar.html";
			});
		 }
		 jQuery("#file").bind("change",function() {
								jQuery('#configJarError').hide();
								jQuery('#configJarError_exceeds').hide();
								jQuery('#file')
										.css({
											"border" : "1px solid #0866c6",
										});
				});
		  });
	 jQuery("#editSeleinumJar,#editServerJar,#editMailJar,#editApacheJar,#editOthersJar").click(function(){
		var category = this.id;
		 jQuery('#uploadpanel').remove();
		 if (category == 'editSeleinumJar')
		 {
			addvalue="edit";
			jQuery("#Slenium_Slide").append('<div id="uploadpanel" class="panel panel-default"><div class="form-group"><label for="uploadJar">Upload Jar:</label><input type="hidden" name="category" value="Selenium Java"/><input type="file" name="file" id="file" class="form-group" /><input type="hidden" id = "jarId" name="jarId"/><p id="configJarError" style="color: red; display: none;">Please select .jar files only </p><p id="configJarError_exceeds" style="color: red; display: none;">Please upload below 20mb .jar files only </p></div><div class="form-group"><button type="submit" class="btn btn-primary" id="seleinumJarSubmit">Submit</button><button type="reset" class="btn btn-primary" id="editSeleinumJar_cancel_btn">Cancel</button> </div> </div>');
		var isJarChecked = false;
		var val = [];
		jQuery(':checkbox:checked').each(function(i) {
		val[i] = jQuery(this).val();
		});
		var jarId = val;
		if (jarId == '')
		{
			jQuery("#editSeleniumJarError").show();
			jQuery("#editServerJarError").hide();
			jQuery("#editMailJarError").hide();
			jQuery("#editApacheJarError").hide();
			jQuery("#editOthersJarError").hide();
		}
		else
		{
			jQuery('#jarId').val(jarId);
			jQuery("#Slenium_Slide").slideToggle("slow");
									jQuery("#Slenium_serverSlide").hide();
									jQuery("#mailSlide").hide();
									jQuery("#apcheSlide").hide();
									jQuery("#othersSlide").hide();
									jQuery("#editSeleniumJarError").hide();
									jQuery("#editServerJarError").hide();
									jQuery("#editMailJarError").hide();
									jQuery("#editApacheJarError").hide();
									jQuery("#editOthersJarError").hide();
		}
		jQuery("#editSeleinumJar_cancel_btn").click(function(e){
			e.preventDefault();
			window.location.href = "seleniumConfig_jar.html";
		});
	 }
		 if (category == 'editServerJar')
		 {
			addvalue="edit";
			jQuery("#Slenium_serverSlide").append('<div id="uploadpanel" class="panel panel-default"><div class="form-group"><label for="uploadJar">Upload Jar:</label><input type="hidden" name="category" value="Selenium Server Stand Alone"/><input type="file" name="file" id="file" class="form-group" /><input type="hidden" id = "jarId" name="jarId"/><p id="configJarError" style="color: red; display: none;">Please select .jar files only </p><p id="configJarError_exceeds" style="color: red; display: none;">Please upload below 20mb .jar files only </p></div><div class="form-group"><button type="submit" class="btn btn-primary" id="serverJarSubmit">Submit</button><button type="reset" class="btn btn-primary" id="editSeverJar_cancel_btn">Cancel</button> </div> </div>');
		var isJarChecked = false;
		var val = [];
		jQuery(':checkbox:checked').each(function(i) {
		val[i] = jQuery(this).val();
		});
		var jarId = val;
		if (jarId == '')
		{
			jQuery("#editServerJarError").show();
			jQuery("#editSeleniumJarError").hide();
			jQuery("#editMailJarError").hide();
			jQuery("#editApacheJarError").hide();
			jQuery("#editOthersJarError").hide();
		}
		else
		{
		 jQuery('#jarId').val(jarId);
		 jQuery("#Slenium_serverSlide").slideToggle("slow");
									jQuery("#Slenium_Slide").hide();
									jQuery("#editServerJarError").hide();
									jQuery("#mailSlide").hide();
									jQuery("#apcheSlide").hide();
									jQuery("#othersSlide").hide();
									jQuery("#editSeleniumJarError").hide();
									jQuery("#editServerJarError").hide();
									jQuery("#editMailJarError").hide();
									jQuery("#editApacheJarError").hide();
									jQuery("#editOthersJarError").hide();
		}
		jQuery("#editSeverJar_cancel_btn").click(function(e){
			e.preventDefault();
			window.location.href = "seleniumConfig_jar.html";
		});
	 }
		 if (category == 'editMailJar')
		 {
			addvalue="edit";
			jQuery("#mailSlide").append('<div id="uploadpanel" class="panel panel-default"><div class="form-group"><label for="uploadJar">Upload Jar:</label><input type="hidden" name="category" value="Mail"/><input type="file" name="file" id="file" class="form-group" /><input type="hidden" id = "jarId" name="jarId"/><p id="configJarError" style="color: red; display: none;">Please select .jar files only </p><p id="configJarError_exceeds" style="color: red; display: none;">Please upload below 20mb .jar files only </p></div><div class="form-group"><button type="submit" class="btn btn-primary" id="mailJarSubmit">Submit</button><button type="reset" class="btn btn-primary" id="editMail_cancel_btn">Cancel</button> </div> </div>');
			
		var isJarChecked = false;
		var val = [];
		jQuery(':checkbox:checked').each(function(i) {
		val[i] = jQuery(this).val();
		});
		var jarId = val;
		if (jarId == '')
		{
			jQuery("#editMailJarError").show();
			jQuery("#editServerJarError").hide();
			jQuery("#editSeleniumJarError").hide();
			jQuery("#editApacheJarError").hide();
			jQuery("#editOthersJarError").hide();
			
		}
		else
		{
		 jQuery('#jarId').val(jarId);
		 jQuery("#mailSlide").slideToggle("slow");
									jQuery("#Slenium_Slide").hide();
									jQuery("#editMailJarError").hide();
									jQuery("#Slenium_serverSlide").hide();
									jQuery("#apcheSlide").hide();
									jQuery("#othersSlide").hide();
									jQuery("#editSeleniumJarError").hide();
									jQuery("#editServerJarError").hide();
									jQuery("#editMailJarError").hide();
									jQuery("#editApacheJarError").hide();
									jQuery("#editOthersJarError").hide();
		}
		jQuery("#editMail_cancel_btn").click(function(e){
			e.preventDefault();
			window.location.href = "seleniumConfig_jar.html";
		});
	 }
		 if (category == 'editApacheJar')
		 {
			addvalue="edit";
			jQuery("#apcheSlide").append('<div id="uploadpanel" class="panel panel-default"><div class="form-group"><label for="uploadJar">Upload Jar:</label><input type="hidden" name="category" value="Apache Poi"/><input type="file" name="file" id="file" class="form-group" /><input type="hidden" id = "jarId" name="jarId"/><p id="configJarError" style="color: red; display: none;">Please select .jar files only </p><p id="configJarError_exceeds" style="color: red; display: none;">Please upload below 20mb .jar files only </p></div><div class="form-group"><button type="submit" class="btn btn-primary" id="apacheJarSubmit">Submit</button><button type="reset" class="btn btn-primary" id="editApache_cancel_btn">Cancel</button> </div> </div>');
			
		var isJarChecked = false;
		var val = [];
		jQuery(':checkbox:checked').each(function(i) {
		val[i] = jQuery(this).val();
		});
		var jarId = val;
		if (jarId == '')
		{
			jQuery("#editApacheJarError").show();
			jQuery("#editMailJarError").hide();
			jQuery("#editServerJarError").hide();
			jQuery("#editSeleniumJarError").hide();
			jQuery("#editOthersJarError").hide();
		}
		else
		{
		 jQuery('#jarId').val(jarId);
		 jQuery("#apcheSlide").slideToggle("slow");
									jQuery("#Slenium_Slide").hide();
									jQuery("#editApacheJarError").hide();
									jQuery("#Slenium_serverSlide").hide();
									jQuery("#mailSlide").hide();
									jQuery("#othersSlide").hide();
									jQuery("#editSeleniumJarError").hide();
									jQuery("#editServerJarError").hide();
									jQuery("#editMailJarError").hide();
									jQuery("#editApacheJarError").hide();
									jQuery("#editOthersJarError").hide();
		}
		jQuery("#editApache_cancel_btn").click(function(e){
			e.preventDefault();
			window.location.href = "seleniumConfig_jar.html";
		});
	}
		if (category == 'editOthersJar')
		{
		addvalue="edit";
		jQuery("#othersSlide").append('<div id="uploadpanel" class="panel panel-default"><div class="form-group"><label for="uploadJar">Upload Jar:</label><input type="hidden" name="category" value="Others"/><input type="file" name="file" id="file" class="form-group" /><input type="hidden" id = "jarId" name="jarId"/><p id="configJarError" style="color: red; display: none;">Please select .jar files only </p><p id="configJarError_exceeds" style="color: red; display: none;">Please upload below 20mb .jar files only </p></div><div class="form-group"><button type="submit" class="btn btn-primary" id="othersJarSubmit">Submit</button><button type="reset" class="btn btn-primary" id="editOthers_cancel_btn">Cancel</button> </div> </div>');
					
		var isJarChecked = false;
		var val = [];
		jQuery(':checkbox:checked').each(function(i) {
		val[i] = jQuery(this).val();
		});
		var jarId = val;
		if (jarId == '')
		{
			jQuery("#editOthersJarError").show();
			jQuery("#editApacheJarError").hide();
			jQuery("#editMailJarError").hide();
			jQuery("#editServerJarError").hide();
			jQuery("#editSeleniumJarError").hide();
			
		}
		else
		{
		 jQuery('#jarId').val(jarId);
		 jQuery("#othersSlide").slideToggle("slow");
									jQuery("#Slenium_Slide").hide();
									jQuery("#editOthersJarError").hide();
									jQuery("#Slenium_serverSlide").hide();
									jQuery("#mailSlide").hide();
									jQuery("#apcheSlide").hide();
									jQuery("#editSeleniumJarError").hide();
									jQuery("#editServerJarError").hide();
									jQuery("#editMailJarError").hide();
									jQuery("#editApacheJarError").hide();
									jQuery("#editOthersJarError").hide();
		}
		jQuery("#editOthers_cancel_btn").click(function(e){
			e.preventDefault();
			window.location.href = "seleniumConfig_jar.html";
		});
	}
		jQuery("#file").bind("change",function() {
								jQuery('#configJarError').hide();
								jQuery('#configJarError_exceeds').hide();
								jQuery('#file')
										.css({
											"border" : "1px solid #0866c6",
										});
							}); 
	 });
	
					//Form submit
					jQuery("#selenium-form").submit(function(e) {
					jQuery('#configJarError_exceeds').hide();
					e.preventDefault();
					var userId = sessionStorage.getItem("userId");
					var category = jQuery("#category").val();
					if(addvalue=="add"){
					var serviceURL = envConfig.serviceBaseURL
										+ '/jarVersion/saveConfigJar.action?userId='
										+ userId+'&category='+category 
			
					}
					else if(addvalue=="edit")
					{
					var serviceURL = envConfig.serviceBaseURL
										+ '/jarVersion/saveConfigJar.action?userId='
										+ userId+'&category='+category +'&jarId=' +jarId
					}
					var formData = new FormData(jQuery(this)[0]);
					var fileName = document.getElementById("file").value;
					 if (fileName == '') {
						jQuery('#configJarError').show();
						jQuery('#configJarError_exceeds').hide();
						return false;
					}
					else
					{
						var checkExt = /\.(jar)$/i.test(fileName);
						if(checkExt)
						{
							
														jQuery.ajax({
														url : serviceURL,
														cache : false,
														dataType : 'json',
														data : formData,
														type : 'POST',
														enctype : 'multipart/form-data',
														processData : false,
														contentType : false,
														success : function(data) {
															if (data.response == true) {
															jQuery('#configJarError_exceeds').hide();
															jQuery('#configJarError').hide();
															jQuery("#loading-div-background").hide();
															jQuery('#success_modal').modal('toggle');
															jQuery('#success_modal').modal('view');
															}else {
																jQuery('#configJarError_exceeds').hide();
																jQuery('#configJarError').hide();
																jQuery("#loading-div-background").hide();
																jQuery('#error_modal').modal('toggle');
																jQuery('#error_modal').modal('view');
															} 
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
												jQuery('#configJarError_exceeds').show();
										}else{
											jQuery('#configJarError').show();
										}
									}
								});
						jQuery('#ok_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "seleniumConfig_jar.html";
					});
					jQuery('#error_btn').click(function(e) {
						e.preventDefault();
						window.location.href = "seleniumConfig_jar.html";
					});
});
function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}