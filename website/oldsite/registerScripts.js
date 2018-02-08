function checkPasswordMatching(){
	//check if the text in password matches text in retype password
	var firstEntry = document.getElementById('ps1').value;
	var secondEntry = document.getElementById('ps2').value;
	if(firstEntry === secondEntry){
		//send info to the database
		alert("Account has been created!")
		return true;
	}else{
		//text for passwords don't match - prompt reentering info
		alert("Passwords do not match!");
		return false;
	}
}
