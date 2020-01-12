<?php
	include 'conn.php';
	
	
	$user = $_POST["userreg"];
	$pass = $_POST["passreg"];
	$passc = $_POST["passcreg"];
	$email = $_POST["emailreg"];
	$phone = $_POST["phonereg"];
	
	if($pass == $passc){
		if($conn){
			
			$q = "select * from users where Username = '$user' or Email = '$email'";
			$result = mysqli_query($conn,$q);
			if(mysqli_num_rows($result) > 0){
				
				echo "a user with this email or username already exists";
			
			}
			else {
				
				$q2 = "INSERT INTO users (Username,Email,Phone,Password) VALUES ('$user','$email','$phone','$pass')";
				if(mysqli_query($conn,$q2)){
					
					echo "Congradulations! You have registered successfully!";
				}
			
			}
		}
		else{
			echo "conn failed...";
		}
	}
	else{
		echo "passwords dont match";
	}
?>