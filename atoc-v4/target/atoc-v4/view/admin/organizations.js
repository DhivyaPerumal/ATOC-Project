jQuery(document).ready(function() {
		//alert("Ready");

		jQuery(".active").removeClass("active");
		jQuery("#organizations").addClass("active");
		
		jQuery(document).ajaxStart(customblockUI);
		jQuery(document).ajaxStop(customunblockUI);
		
		var userId = sessionStorage.getItem("userId");
		var serviceURL = envConfig.serviceBaseURL
				+ '/organization/viewOrganizationList.action?userId='
				+ userId;
		console.log(serviceURL);
		/*jQuery(document).ajaxStart(jQuery.blockUI({ message: jQuery('#loading_Message'), css: { border: '1px solid #30a5ff' } }))
					   .ajaxStop(jQuery.unblockUI);*/
		var organizationList_table = jQuery(
				"#organizationList_table").DataTable({
			"sAjaxSource" : serviceURL,
			"bProcessing" : false,
			"bServerSide" : true,
			"bPaginate" : true,
			"bFilter" : true,
			"searching" : false,
			"bSort" : false,
			"bDestroy" : true,
			"bJQueryUI" : false,
			"sPaginationType" : 'simple_numbers',
			"iDisplayStart" : 0,
			"iDisplayLength" : 10,
			columnDefs : [ {
				orderable : false,
				targets : -1
			} ],
			"fnDrawCallback" : function(oSettings) {
				if (oSettings.fnRecordsTotal() <= 10) {
					jQuery('.dataTables_length').hide();
					jQuery('.dataTables_paginate').hide();
				} else {
					jQuery('.dataTables_length').show();
					jQuery('.dataTables_paginate').show();
				}
			},
			"aoColumns" : [ {
				"mData" : "organizationName"
			}, {
				"mData" : "email"
			}, {
				"mData" : "phone"
			}, {
				"mData" : "fax"
			} ]

		});
		jQuery('#loading_cancel_btn').click(function(event) {
			//jQuery.unblockUI();
		});
	});

function customblockUI() {
	jQuery("#loading-div-background").show();
}
function customunblockUI() {
	jQuery("#loading-div-background").hide();
}