function setUserTokenToRequestHeader(userToken){
	
jQuery.ajaxSetup({
	headers : {
		'userToken' : userToken
	}
});

jQuery().ajaxStart(function(){
	   jQuery('body').css('cursor', 'wait');
	});

jQuery().ajaxStop(function(){
	   jQuery('body').css('cursor', 'auto');
	});

}

//This function is used to set profile image in every page.
function setProfileImage(profileImgData) 
{
	
	if (profileImgData == '' || profileImgData == 'undefined') 
	{
		document.getElementById("profileImg").src = "../../resources/images/avatar.png";
	} 
	else 
	{
		document.getElementById("profileImg").src = "data:image/png;base64,"
				+ profileImgData;
	}

}