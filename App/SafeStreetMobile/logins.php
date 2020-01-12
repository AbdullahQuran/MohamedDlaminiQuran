<?php
	include 'conn.php';
	
	$user = $_POST["user"];
	$pass = $_POST["pass"];
	
	if($conn){
		$q = "select * from users where Username like '$user' and Password like '$pass'";
		$result = mysqli_query($conn,$q);
		if(mysqli_num_rows($result) > 0){
		
			while($row = mysqli_fetch_array($result)){
				
				$flag[] = $row;	
			}
			
			print(json_encode($flag));
		
		}
		else{
			echo "You have not reported any data yet";
		}
	
	}
	else{
		echo "conn failed...";
	}
?>