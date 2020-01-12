<?php
	include 'conn.php';
	
	function randomLicense() {
		
		$alphabet = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890';
		$pass = array(); //remember to declare $pass as an array
		$alphaLength = strlen($alphabet) - 1; //put the length -1 in cache
		for ($i = 0; $i < 8; $i++) {
			$n = rand(0, $alphaLength);
			$pass[] = $alphabet[$n];
		}
		return implode($pass); //turn the array into a string
	}
	function randomPassword() {
		
		$alphabet = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890';
		$pass = array(); //remember to declare $pass as an array
		$alphaLength = strlen($alphabet) - 1; //put the length -1 in cache
		for ($i = 0; $i < 8; $i++) {
			$n = rand(0, $alphaLength);
			$pass[] = $alphabet[$n];
		}
		return implode($pass); //turn the array into a string
	}
	
	
	$user = $_POST["userviol"];
	$city = $_POST["cityviol"];
	$street = $_POST["streetviol"];
	$streetn = $_POST["streetnviol"];
	$type = $_POST["typeviol"];
	$date = $_POST["dateviol"];
	$time = $_POST["timeviol"];
	$license = "";
	
		if($conn){
			
				$license = randomLicense();
			
				$q2 = "INSERT INTO violations (VUsername,VStreetName,VStreetNo,VCity,VDate,VTime,VType,LicensePlate)
				VALUES ('$user','$street','$streetn','$city','$date','$time','$type','$license')";
				if(mysqli_query($conn,$q2)){
					
					echo "Thank You! Your report is being processed!";
				}
			
		}
		else{
			echo "conn failed...";
		}
	
	
?>