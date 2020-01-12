<?php
	
	session_start();
	
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
	
	if(isset($_SESSION['username'])){
		
		header("location:statisticsWeb.php");
	}
	else{
		
	include 'conn.php';
	
	$result_message = "";
	$result_message1 = "";
	
	if(isset($_POST['submit'])){
		
		$result_message = "Failed to query the db";
		
		if(!empty($_POST['username']) && !empty($_POST['password'])){
				
			$username = $_POST['username'];
			$password = $_POST['password'];
			$username = mysqli_real_escape_string($conn,$username);
			$password = mysqli_real_escape_string($conn,$password);
			$username = stripslashes($username);
			$password = stripslashes($password);
			
			
			$loginq = "select * from municipality where Email = '$username' and Password = '$password' and Opted = '1'";
			$result = mysqli_query($conn,$loginq);
			if(mysqli_error($conn)){
				$result_message = "Failed to query the db";
			}
			else{
				$row = mysqli_fetch_array($result);
				if($row['Email'] == $username && $row['Password'] == $password && $row['Opted'] == 1){
					$_SESSION['username'] = $username;
					$_SESSION['city'] = $row['MCity'];
					header("location:statisticsWeb.php");
				}
				else{
					$result_message = "You have either not opted in or have typed in the incorrect email and password";
				}
			}
	
			
			
		}
		else{
			$result_message = "Both email address and password are required";
		}
	}
	if(isset($_POST['submit1'])){
		
		$result_message1 = "Failed to query the db";
		
		if(!empty($_POST['email'])){
				
			$email = $_POST['email'];
			$email = mysqli_real_escape_string($conn,$email);
			$email = stripslashes($email);
			
			
			$optq = "select * from municipality where Email = '$email' and Opted = '0'";
			$result = mysqli_query($conn,$optq);
			if(mysqli_error($conn)){
				$result_message1 = "Failed to query the db";
			}
			elseif (mysqli_num_rows($result) == 0) {
				
				$result_message1 = "You have either already opted in or this email does not belong to the list of official municipalities ";
			}
			else{
				
				$newpass = randomPassword();
				
				$optq1 = "update municipality set Password = '$newpass', Opted = 1 where Email = '$email'";
				
				if(mysqli_query($conn,$optq1)){
				

				$result_message1 = "Thank you for Opting in an email with your username and password has been sent to your email";
				
				include 'mailerweb.php';
				
				
				}
			}
	
			
			
		}
		else{
			$result_message1 = "Please enter an email to opt it";
		}
	}

	
	
	
	
	}
	
	
	
?>