<?php
	include 'conn.php';
	
	$user = $_POST["username"];

	
	if($conn){
		$q = "select * from violations where VUsername like '$user'";
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