jQuery(document).ready(function() {
	jQuery("#execution").addClass("active");
	jQuery("#testExecution").addClass("active");
	
	jQuery(document).ajaxStart(customblockUI);
	jQuery(document).ajaxStop(customunblockUI);
	
	var userId = sessionStorage.getItem("userId");
	
			jQueryselectOS = jQuery('#dropDown_Os');
			jQueryselectBrowser = jQuery('#dropDown_Browser');
			jQueryselectBrowserVer = jQuery('#dropDown_Version');
			
			jQueryselectOS.html('');
			jQueryselectBrowser.html('');
			jQueryselectBrowserVer.html('');
			jQueryselectOS.append('<option selected="selected" value="">Select Operating System</option>');
			jQueryselectBrowser.append('<option selected="selected" value="">Select Browser</option>');
			jQueryselectBrowserVer.append('<option selected="selected" value="">Select Browser Version</option>');
							
			var serviceURL = envConfig.serviceBaseURL
							+ '/testSuiteExecution/viewAmazonList.action?userId='
							+ userId;
			console.log(serviceURL);
			jQuery.ajax({
				url : serviceURL,
				dataType : 'json',
				type : 'GET',
				cache : 'false',
				success : function(data) {
						var JsonStringify_Data = JSON.stringify(data);
						obj = jQuery.parseJSON(JsonStringify_Data);
						var arr = [];
						jQuery.each(obj, function(i, e) {
								jQuery.each(e, function(key1, val_0) {
									arr.push(val_0);
								});
						});
						var result = jQuery.map(arr, function(val, key) {
								return {
										amazonImagesId : val.amazonImagesId,
										OSName : val.OSName,
										browser : val.browser,
										browserVersion :val.browserVersion
								};
							});
							var isChecked = false;
							var OS_Id = '';
							var arr_OS = [];	
							jQuery.each(result, function(key, val) {
							var JsonStringify_Data1 = JSON.stringify(val);
								arr_OS.push(val.OSName);
							});	
							var arr_UniqOS = [];
							jQuery.each(arr_OS, function(i, el){
								if(jQuery.inArray(el, arr_UniqOS) === -1) {
									arr_UniqOS.push(el);
									 jQueryselectOS.append('<option id="'
										+ el.amazonImagesId + '">'
										+ el +'</option>'); 
								}
							});	
							
							jQuery("#dropDown_Os").change(function() {
									OS_Id = jQuery("#dropDown_Os option:selected").val();	
									isChecked = true;
									jQueryselectBrowser.html('');
									jQueryselectBrowserVer.html('');
									jQueryselectBrowser.append('<option id=" ">Select Browser </option>');
									jQueryselectBrowserVer.append('<option id=" ">Select Browser Version</option>');
									var arr_Browser1=[];
							jQuery.each(result, function(key, val) {
								var JsonStringify_Data1 = JSON.stringify(val);
								if(val.OSName == OS_Id ){
									arr_Browser1.push(val.browser);
								}
							});	
							var arr_UniqBrowser1=[];
							jQuery.each(arr_Browser1, function(i, e){
								if(jQuery.inArray(e, arr_UniqBrowser1) === -1) {
									arr_UniqBrowser1.push(e);
									  jQueryselectBrowser.append('<option id="'
										+ e.OSName + '">'
										+ e +'</option>');  
								}
							});	
							
						});
						jQuery("#dropDown_Browser").change(function() {
								browser_Id = jQuery("#dropDown_Browser option:selected").val();	
								isChecked = true;
								jQueryselectBrowserVer.html('');
								jQueryselectBrowserVer.append('<option id=" ">Select Browser Version</option>');
								
								var arr_BrowserVersion1=[];
								jQuery.each(result, function(key, val) {
								
									var JsonStringify_Data1 = JSON.stringify(val);
									if(val.OSName == OS_Id ){
										if(val.browser == browser_Id ){
											arr_BrowserVersion1.push(val.browserVersion);
										
										}
										}
									});	
									var arr_UniqBrowserVer1=[];
									jQuery.each(arr_BrowserVersion1, function(i, e1){
										if(jQuery.inArray(e1, arr_UniqBrowserVer1) === -1) {
											arr_UniqBrowserVer1.push(e1);
											jQueryselectBrowserVer.append('<option id="'
													+ e1.browser + '">'
													+ e1 +'</option>');  
										}
									});	
							});
							jQuery("#dropDown_Version").change(function() {
							browserVersion_Id = jQuery("#dropDown_Version option:selected").val();	
								isChecked = true;
							jQueryselectBrowserVer.append('<option id=" ">Select Browser Version</option>');
							
							
							
							});
											
						},
							error : function() {
						},
						statusCode : {
							403 : function(xhr) {
								alert("Session will be Expired");
								window.location.href = "../../";
							}
						}
					});
			jQuery("#testExec_scheduleDate").datepicker({
				minDate : 0,
				maxDate : "+6M"
			});
			jQuery('#testExec_scheduleTime').timepicker();
			jQuery("#testExec_scheduleDate").keydown(function(event) {
				return false;
			});
			jQuery("#testExec_scheduleTime").keydown(function(event) {
				return false;
			});
			jQuery("#testExec_scheduleDate").click(function(event) {
				jQuery('#select_Schedule_Date').hide();
				jQuery('#testExec_scheduleDate').css({
					"border" : "1px solid #30a5ff"
				});
			});
			jQuery("#testExec_scheduleTime").click(function(event) {
				jQuery('#testExec_scheduleTime').css({
					"border" : "1px solid #30a5ff"
				});
				jQuery('#select_Schedule_Time').hide();
			});
			jQuery('#scheduled_Time').hide('fast');
			jQuery('#scheduled_Date').hide('fast');
			jQuery('#scheduled_timezone').hide('fast');
			jQuery('#testSuite_Schedule_btn').hide('fast');
			jQuery('#loading-indicator').hide('fast');
			jQuery('#serverTime_Note').hide('fast');
			jQuery('#id_now').click(function() {
				jQuery('#scheduled_Time').hide('fast');
				jQuery('#scheduled_Date').hide('fast');
				jQuery('#scheduled_timezone').hide('fast');
				jQuery('#testSuite_Schedule_btn').hide('fast');
				jQuery('#testSuite_execute_btn').show('fast');
				jQuery('#serverTime_Note').hide('fast');
			});
			jQuery('#id_scheduleLater').click(function() {
				jQuery('#execution_Type').show('fast');
				jQuery('#scheduled_Time').show('fast');
				jQuery('#scheduled_Date').show('fast');
				jQuery('#scheduled_timezone').show('fast');
				jQuery('#testSuite_Schedule_btn').show('fast');
				jQuery('#testSuite_execute_btn').hide('fast');
				jQuery('#serverTime_Note').show('fast');
			});
							jQuery("#dropdown_suite").prop("disabled", true);
							jQuery('#selectProjectError').hide();
							jQuery('#selectSuiteError').hide();
							jQuery('#executionNameError').hide();
							jQuery('#selectOsError').hide();
							jQuery('#selectBrowserError').hide();
							jQuery('#selectVersionError').hide();
							jQuery('#select_Schedule_Date').hide();
							jQuery('#select_Schedule_Time').hide();

							jQuery("#submitBtn").hide();

							jQueryselectProject = jQuery('#dropdown_project');
							jQueryselectSuite = jQuery('#dropdown_suite');

							jQueryselectProject.html('');
							jQueryselectSuite.html('');

							var data = JSON.parse(localStorage
									.getItem('JsonStringify_Data_TestSuiteInfo'));
							var userId = sessionStorage.getItem("userId");
							var obj;
							var fromPage = localStorage.getItem("fromPage");
							var projectName;
							var suiteName;
							var testSuiteId;
							var selected_testSuiteId;
							var projectId;
							if (data != null) {

								jQuery("#dropdown_suite").val(data.suiteName);
								jQuery("#dropdown_project").val(data.projectName);
							} else {

							}

							if (fromPage == 'exec') {
								projectName = localStorage
										.getItem("projectName");

								suiteName = localStorage.getItem("suiteName");
								projectId = localStorage.getItem("projectId");

								testSuiteId = localStorage
										.getItem("testSuiteId");
								jQuery('#dropdown_project').attr('disabled',
										'disabled');
								jQuery('#dropdown_suite').attr('disabled',
										'disabled');
							} else {
								jQueryselectProject = jQuery('#dropdown_project');
								jQueryselectSuite = jQuery('#dropdown_suite');

								jQueryselectProject.html('');
								jQueryselectSuite.html('');

								localStorage.removeItem("JsonStringify_Data_TestSuiteInfo");
								localStorage.removeItem("fromPage");
								localStorage.removeItem("projectName");
								localStorage.removeItem("suiteName");
								localStorage.removeItem("projectId");
								localStorage.removeItem("testSuiteId");
							}
							jQueryselectProject = jQuery('#dropdown_project');
							jQueryselectSuite = jQuery('#dropdown_suite');

							var serviceURL = envConfig.serviceBaseURL
									+ '/project/viewProjectList.action?userId='
									+ userId;
							console.log(serviceURL);
							//request the JSON data and parse into the select element
							jQuery
									.ajax({
										url : serviceURL,
										dataType : 'json',
										type : 'GET',
										cache : 'false',
										success : function(data) {
											jQueryselectProject.html('');
											jQueryselectSuite.html('');
											jQueryselectProject
													.append('<option selected="selected" value="">Select Project</option>');
											jQueryselectSuite
													.append('<option selected="selected" value="">Select Test Suite</option>');
											var JsonStringify_Data = JSON
													.stringify(data);
											obj = jQuery
													.parseJSON(JsonStringify_Data);

											var arr = [];

											jQuery.each(obj,function(i, e) {
													jQuery.each(e,function(key1,val_0) {
															arr.push(val_0);
													jQuery.each(val_0.testSuiteSet,function(key2,val_l) {
															jQuery.each(val_l,function(key3,val_2) {
																});
															});
														});
												});

											var result = jQuery.map(arr,function(val, key) {
																return {
																	projectId : val.projectId,
																	projectName : val.projectName
																};

															});

											jQuery.each(result,function(key, val) {
													jQueryselectProject.append('<option id="' + val.projectId + '">'
																				+ val.projectName
																				+ '</option>');
													jQueryselectSuite.append('<option id="' + testSuiteId + '">'
																				+ suiteName
																				+ '</option>');
															});
											jQuery("#dropdown_project").val(projectName);
											jQuery("#dropdown_project").focus();
											jQuery("#dropdown_suite").val(suiteName);
											/* UN BLOCK UI STARTED WHEN PAGE LOADING WITH AJAX CALL */
											//jQuery.unblockUI();
											/* UN BLOCK UI ENDED WHEN PAGE LOADING WITH AJAX CALL */
											jQuery("#submitBtn").show();
										},

										error : function() {
											jQuery("#submitBtn").show();
										},
										statusCode : {
											403 : function(xhr) {
												alert("Session will be Expired");
												window.location.href = "../../";

											}
										}

									});

							jQuery("#dropdown_project")
									.change(
											function() {
												jQuery("#dropdown_suite").prop(
														"disabled", false);
												jQueryselectSuite.html('');
												jQuery('#selectProjectError').hide();
												jQuery('#selectSuiteError').hide();
												jQuery('#dropdown_project')
														.css(
																{
																	"border" : "1px solid #30a5ff"
																});
												jQuery('#dropdown_suite')
														.css(
																{
																	"border" : "1px solid #30a5ff"
																});
												var id = jQuery(this).children(
														":selected").attr("id");
												projectId = id;
												var projectName = '';
												var dropDownprojectName = jQuery(
														'#dropdown_project')
														.val();
												var suiteId = '';
												jQuery.each(obj,function(i, e) {
												jQuery.each(e,function(key1,val_0) {
												projectName = val_0.projectName;
												jQuery.each(val_0.testSuiteSet,function(key2,val_l) {
												jQuery.each(val_l,function(key3,val_2) {
														if (projectName == dropDownprojectName) {
																if (key3 == 'testSuiteId') {
																	suiteId = val_2;
																}
														if (key3 == 'suiteName') {
														jQueryselectSuite.append('<option id="' + suiteId + '">'
																				+ val_2
																				+ '</option>');
													}
												}
											});
										});
									});
								});
							});
							jQuery("#executionName").keypress(function() {
								jQuery('#executionNameError').hide();
								jQuery('#executionName').css({
									"border" : "1px solid #30a5ff"
								});
							});

							localStorage.removeItem("JsonStringify_Data_TestSuiteInfo");
							localStorage.removeItem("fromPage");
							localStorage.removeItem("projectName");
							localStorage.removeItem("suiteName");
							localStorage.removeItem("projectId");
							localStorage.removeItem("testSuiteId");

							jQuery("#submitBtn").click(function(e) {
				
												if (jQuery(
														'#dropdown_project option:selected')
														.val() == '') {
													jQuery('#dropdown_project')
															.css(
																	{
																		"border" : "1px solid red",
																	//"background": "#FFCECE"
																	});
													jQuery('#selectProjectError')
															.show();
															jQuery('#dropdown_project')
															.focus();
													return false;
												} else if (jQuery(
														'#dropdown_suite option:selected')
														.val() == '') {
													jQuery('#dropdown_suite')
															.css(
																	{
																		"border" : "1px solid red",
																	//"background": "#FFCECE"
																	});
													jQuery('#selectSuiteError')
															.show();
														jQuery('#dropdown_suite')
															.focus();	
													return false;
												} else if (jQuery('#executionName')
														.val() == '') {
													jQuery('#executionName')
															.css(
																	{
																		"border" : "1px solid red",
																	//"background": "#FFCECE"
																	});
													jQuery('#executionNameError')
															.show();
															jQuery('#executionName')
															.focus();
													return false;
												} else if (jQuery('#dropDown_Os')
														.val() == ''
														|| jQuery('#dropDown_Os')
																.val() === "Select Operating System") {
													jQuery('#dropDown_Os')
															.css(
																	{
																		"border" : "1px solid red",
																	//"background": "#FFCECE"
																	});
													jQuery('#selectOsError').show();
													jQuery('#dropDown_Os')
															.focus();
													return false;
												} else if (jQuery(
														'#dropDown_Browser')
														.val() == ''
														|| jQuery(
																'#dropDown_Browser')
																.val() == "Select Browser") {
													jQuery('#dropDown_Browser')
															.css(
																	{
																		"border" : "1px solid red",
																	//"background": "#FFCECE"
																	});
													jQuery('#selectBrowserError')
															.show();
															jQuery('#dropDown_Browser')
															.focus();
													return false;
												} else if (jQuery(
														'#dropDown_Version')
														.val() == ''
														|| jQuery(
																'#dropDown_Version')
																.val() == "Select Browser Version") {
													jQuery('#dropDown_Version')
															.css(
																	{
																		"border" : "1px solid red",
																	//"background": "#FFCECE"
																	});
													jQuery('#selectVersionError')
															.show();
															jQuery('#dropDown_Version')
															.focus();
													return false;
												} else {
													e.preventDefault();
													var userId = sessionStorage
															.getItem("userId");
													var serviceURL = envConfig.serviceBaseURL
															+ '/testSuite/TestSuiteExecution.action?userId='
															+ userId;
													testSuiteId = jQuery(
															'#dropdown_suite')
															.children(
																	":selected")
															.attr("id");
													projectId_selected = jQuery(
															'#dropdown_project')
															.children(
																	":selected")
															.attr("id");
													console.log(serviceURL);
											//jQuery.blockUI({ message: jQuery('#loading_Message'), css: { border: '1px solid #30a5ff' } });
													jQuery
															.ajax({
																url : serviceURL,
																type : 'Post',
																dataType : "json",
																async : true,
																data : {
																	scheduledOn : "2014-12-10 13:01:14",
																	executionType : jQuery(
																			'input[name=executionType]:checked')
																			.val(),
																	priority : "H",
																	logLevel : "S",
																	notificationEmail : "atoc@bpatech.com",
																	fatalErrorNotificationMail : "atoc@bpatech.com",
																	status : "Y",
																	testSuiteDetailId : "3",
																	execCompleteOn : "0000-00-00 00:00:00",
																	isActive : "Y",
																	createdBy : userId,
																	updatedBy : userId,
																	testSuiteId : testSuiteId,
																	operatingSystem : jQuery(
																			'#dropDown_Os')
																			.val(),
																	browser : jQuery(
																			'#dropDown_Browser')
																			.val(),
																	browserVersion : jQuery(
																			'#dropDown_Version')
																			.val(),
																	testSuiteExecName : jQuery(
																			'#executionName')
																			.val()

																},
																success : function(
																		data)
																		{
																	//jQuery.unblockUI();
																	localStorage
																			.setItem(
																					"testSuiteExecId",
																					data.testSuiteExecId);
																	localStorage
																			.setItem(
																					"requestPage",
																					"testExecution");
																	jQuery("#loading-div-background").hide();
																	jQuery('#testExecution_modal').modal('toggle');
																	jQuery('#testExecution_modal').modal('view');
																	/* alert(data.testSuiteExecId); */
																	//window.location.href = "ExecutionResult.html";
																},
																failure : function(
																		data) {
																	/* alert("Failure "); */
																	//jQuery.unblockUI();
																	window.location.href = "testExecution.html";
																},
																statusCode : {
																	403 : function(
																			xhr) {
																		alert("Session will be Expired");
																		//jQuery.unblockUI();
																		window.location.href = "../../";

																	}
																}
															});
												}
											});

											jQuery( "#dropdown_project" ).change(function() {
		jQuery('#selectProjectError').hide();
			jQuery('#dropdown_project').css({
									"border" : "1px solid #0866c6",
								});
					});
					jQuery( "#dropdown_suite" ).change(function() {
		jQuery('#selectSuiteError').hide();
			jQuery('#dropdown_suite').css({
									"border" : "1px solid #0866c6",
								});
					});
		jQuery('#executionName').focus();
		jQuery("#executionName").on("input", function() {
								jQuery('#executionNameError').hide();
								jQuery('#executionName').css({
									"border" : "1px solid #0866c6",
									//"background" : "#FFCECE"
								});
							});
			jQuery( "#dropDown_Os" ).change(function() {
		jQuery('#selectOsError').hide();
			jQuery('#dropDown_Os').css({
									"border" : "1px solid #0866c6",
								});
					});
							
			jQuery( "#dropDown_Browser" ).change(function() {
		jQuery('#selectBrowserError').hide();
			jQuery('#dropDown_Browser').css({
									"border" : "1px solid #0866c6",
								});
					});
					jQuery( "#dropDown_Version" ).change(function() {
		jQuery('#selectVersionError').hide();
			jQuery('#dropDown_Version').css({
									"border" : "1px solid #0866c6",
								});
					});

					jQuery("#schedule_execute_btn")
									.click(
											function(e) {
												e.preventDefault();
												var userId = sessionStorage
														.getItem("userId");
												var serviceURL = envConfig.serviceBaseURL
														+ '/testSuite/TestSuiteExecution.action?userId='
														+ userId;
												var scheduledOn = jQuery(
														'#testExec_scheduleDate')
														.val()
														+ ' '
														+ jQuery(
																'#testExec_scheduleTime')
																.val();
												testSuiteId = jQuery(
														'#dropdown_suite')
														.children(":selected")
														.attr("id");
												if (jQuery(
														'#dropdown_project option:selected')
														.val() == '') {
													jQuery('#dropdown_project')
															.css(
																	{
																		"border" : "1px solid red",
																	//"background": "#FFCECE"
																	});
													jQuery('#selectProjectError')
															.show();
													return false;
												} else if (jQuery(
														'#dropdown_suite option:selected')
														.val() == '') {
													jQuery('#dropdown_suite')
															.css(
																	{
																		"border" : "1px solid red",
																	//"background": "#FFCECE"
																	});
													jQuery('#selectSuiteError')
															.show();
													return false;
												} else if (jQuery('#executionName')
														.val() == '') {
													jQuery('#executionName')
															.css(
																	{
																		"border" : "1px solid red",
																	//"background": "#FFCECE"
																	});
													jQuery('#executionNameError')
															.show();
															
													return false;
												} else if (jQuery('#dropDown_Os')
														.val() == ''
														|| jQuery('#dropDown_Os')
																.val() === "Select Operating System") {
													jQuery('#dropDown_Os')
															.css(
																	{
																		"border" : "1px solid red",
																	//"background": "#FFCECE"
																	});
													jQuery('#selectOsError').show();
													return false;
												} else if (jQuery(
														'#dropDown_Browser')
														.val() == ''
														|| jQuery(
																'#dropDown_Browser')
																.val() == "Select Browser") {
													jQuery('#dropDown_Browser')
															.css(
																	{
																		"border" : "1px solid red",
																	//"background": "#FFCECE"
																	});
													jQuery('#selectBrowserError')
															.show();
													return false;
												} else if (jQuery(
														'#dropDown_Version')
														.val() == ''
														|| jQuery(
																'#dropDown_Version')
																.val() == "Select Browser Version") {
													jQuery('#dropDown_Version')
															.css(
																	{
																		"border" : "1px solid red",
																	//"background": "#FFCECE"
																	});
													jQuery('#selectVersionError')
															.show();
													return false;
												} else if (jQuery(
														'#testExec_scheduleDate')
														.val() == ''
														|| jQuery(
																'#testExec_scheduleDate')
																.val() == "null") {
													jQuery('#testExec_scheduleDate')
															.css(
																	{
																		"border" : "1px solid red",
																	//"background": "#FFCECE"
																	});
													jQuery('#select_Schedule_Date')
															.show();
													return false;
												}

												else if (jQuery(
														'#testExec_scheduleTime')
														.val() == ''
														|| jQuery(
																'#testExec_scheduleTime')
																.val() == "") {
													jQuery('#testExec_scheduleTime')
															.css(
																	{
																		"border" : "1px solid red",
																	//"background": "#FFCECE"
																	});
													jQuery('#select_Schedule_Time')
															.show();
													return false;
												} else {
													/* BLOCK UI STARTED WHEN PAGE LOADING WITH AJAX CALL */
													/*jQuery
															.blockUI({
																message : jQuery('#loading_Message'),
																css : {
																	border : '1px solid #30a5ff'
																}
															});*/
													/* BLOCK UI ENDED WHEN PAGE LOADING WITH AJAX CALL */
													console.log(serviceURL);
													jQuery
															.ajax({
																url : serviceURL,
																dataType : 'json',
																cache : false,
																type : 'POST',
																data : {
																	scheduledOn : scheduledOn,
																	executionType : jQuery(
																			'input[name=executionType]:checked')
																			.val(),
																	priority : "H",
																	logLevel : "S",
																	notificationEmail : "bpa@gmail.com",
																	fatalErrorNotificationMail : "bpa@bpatech.com",
																	status : "Y",
																	testSuiteDetailId : "1",
																	execCompleteOn : "2014-12-30 13:01:14",
																	isActive : "Y",
																	createdBy : userId,
																	updatedBy : userId,
																	testSuiteId : testSuiteId,
																	operatingSystem : jQuery(
																			'#dropDown_Os')
																			.val(),
																	browser : jQuery(
																			'#dropDown_Browser')
																			.val(),
																	browserVersion : jQuery(
																			'#dropDown_Version')
																			.val(),
																	testSuiteExecName : jQuery(
																			'#executionName')
																			.val(),
																	scheduleDate : jQuery(
																			'#testExec_scheduleDate')
																			.val(),
																	scheduleTime : jQuery(
																			'#testExec_scheduleTime')
																			.val(),
																	scheduledTimeZone : jQuery(
																			'#dropDown_timeZone')
																			.val(),
																	schedulerStatus : "PENDING",
																},
																type : 'Post',
																success : function(
																		data) {
																	/* UN BLOCK UI STARTED WHEN PAGE LOADING WITH AJAX CALL */
																	/*jQuery
																			.unblockUI();*/
																	jQuery("#loading-div-background").hide();
																	jQuery('#schedule_testExecution_modal').modal('toggle');
																	jQuery('#schedule_testExecution_modal').modal('view');
																	/* UN BLOCK UI ENDED WHEN PAGE LOADING WITH AJAX CALL */
																},
																failure : function(
																		data) {
																	/* UN BLOCK UI STARTED WHEN PAGE LOADING WITH AJAX CALL */
																	/*jQuery
																			.unblockUI();*/
																	/* UN BLOCK UI ENDED WHEN PAGE LOADING WITH AJAX CALL */
																	window.location.href = "testExecution.html";
																},
																statusCode : {
																	403 : function(
																			xhr) {
																		alert("Session will be Expired");
																		window.location.href = "../../";

																	}
																}
															});
												}
											});

											jQuery(document).ajaxSend(function(event, request, settings) {
				jQuery("#loading-dialog").dialog({
					closeOnEscape : false,
					open : function(event, ui) {
						jQuery(".ui-dialog-titlebar-close", ui.dialog || ui).hide();
					}
				});
				jQuery('#loading-indicator').show();
			});

			jQuery(document).ajaxComplete(function(event, request, settings) {
				jQuery('#loading-indicator').hide();
			});

			jQuery('#loading_cancel_btn').click(function(event) {
				event.preventDefault();
				//jQuery.unblockUI();
			});

			jQuery('#testExecution_modal_okbtn').click(function(event) {
				event.preventDefault();
				window.location.href = "../dashboard/dashboard.html";
			});
			jQuery('#schedule_testExecution_modal_okbtn').click(function(event) {
				event.preventDefault();
				window.location.href = "../dashboard/dashboard.html";
			});

});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}