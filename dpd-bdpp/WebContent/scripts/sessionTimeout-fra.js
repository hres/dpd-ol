function ShowTimeoutWarning(reactionTime)
{
	var timeoutMessage="Votre session est sur le point d\'expirer!  Vous avez jusqu\'� #expireTime# pour s�lectionner \"OK\" ci-dessous, afin de prolonger votre session de 29 minutes.";
	var lngExpiredTime = getExpireTime(reactionTime);
	var answer = confirm(timeoutMessage.replace("#expireTime#", lngExpiredTime));
	if (getExpiredSession(lngExpiredTime)){
		window.location="/dpd-bdpp/jsp/timeout-fra.jsp";
	}
	else
	{
		if (answer){
			// resets the time out delay to 28 minutes.
			window.setTimeout("ShowTimeoutWarning(60000)", 1680000); //1680000
		}
		else{
			//if cancel button is clicked, will be redirected to the session timeout page.
			window.location="/dpd-bdpp/jsp/timeout-fra.jsp";
		}
	}
}

function getTime() {
	var dTime = new Date();
	var hours = dTime.getHours();
	var minute = dTime.getMinutes();
	var period = "AM";
	if (hours > 12) {
		period = "PM";
	}
	else {
		period = "AM";
	}
		hours = ((hours > 12) ? hours - 12 : hours)
		return hours + ":" + minute + " " + period
	}

function getExpireTime(reactionTime) {
	var expire = new Date( getCurrentTimeMs() + reactionTime );
	var hours = expire.getHours(), 
		minutes = expire.getMinutes(), 
		seconds = expire.getSeconds();
	
	var timeformat=hours<12 ? " AM":" PM";
    hours = hours%12;
	if (hours==0)
    	hours=12;
		
    // Add a zero if needed in the time
    hours = hours<10?'0'+hours:hours;
    minutes = minutes<10?'0'+minutes:minutes;
    seconds = seconds<10?'0'+seconds:seconds;
	
	return hours+":"+minutes+":"+seconds+timeformat;
}

function getExpiredSession(lngExpiredTime){
	
	var lngCurrentTime = getCurrentTime();
	if (lngCurrentTime > lngExpiredTime)
	{
		return true;
	}
}

function getCurrentTimeMs(){return (new Date()).getTime();}

function getCurrentTime() {
	var currentTime = new Date();
	var hours = currentTime.getHours(), 
		minutes = currentTime.getMinutes(), 
		seconds = currentTime.getSeconds();
	
	var timeformat=hours<12 ? " AM":" PM";
    hours = hours%12;
	if (hours==0)
    	hours=12;
		
    // Add a zero if needed in the time
    hours = hours<10?'0'+hours:hours;
    minutes = minutes<10?'0'+minutes:minutes;
    seconds = seconds<10?'0'+seconds:seconds;
	
	return hours+":"+minutes+":"+seconds+timeformat;
}

