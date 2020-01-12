<?php
	
	if(!isset($_SESSION['username'])){
		
		header("location:index.php");
	}
	else{
		include 'conn.php';
		$result_message = "";
		$fail = 0;
		$viol1=0;
		$viol2=0;
		$viol3=0;
		$viol4=0;
		$viol5=0;
		$city = $_SESSION['city'];
		$location = $city;
		
		
		if(isset($_POST['submit'])){
			
			
			if(!empty($_POST['city']) && !empty($_POST['street']) &&!empty($_POST['streetno']) &&!empty($_POST['start'])&&!empty($_POST['end']) ){
					
				$city = $_POST['city'];
				$street = $_POST['street'];
				$streetno = $_POST['streetno'];
				$start = $_POST['start'];
				$end = $_POST['end'];
				$city = mysqli_real_escape_string($conn,$city);
				$street = mysqli_real_escape_string($conn,$street);
				$streetno = mysqli_real_escape_string($conn,$streetno);
				$start = mysqli_real_escape_string($conn,$start);
				$end = mysqli_real_escape_string($conn,$end);
				$city = stripslashes($city);
				$street = stripslashes($street);
				$streetact = 'via '.$street;  
				$streetno = stripslashes($streetno);
				$start = stripslashes($start);
				$end = stripslashes($end);
				$starttime = substr("$start", -8);
				$startdate = substr("$start", -19,10);
				$endtime= substr("$end", -8);
				$enddate= substr("$end", -19,10);
				$location = ucfirst($city).", ".ucfirst($street)." ".$streetno;
				
				
				$violq = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VStreetNo like '$streetno' and VCity like '$city' 
						  and VDate >= '$startdate' and VDate <= '$enddate'";
				
		
				
				
			}
			
			elseif(!empty($_POST['city']) && !empty($_POST['street']) && !empty($_POST['streetno'])&&!empty($_POST['start'])){
				
				$city = $_POST['city'];
				$street = $_POST['street'];
				$streetno = $_POST['streetno'];
				$start = $_POST['start'];
				$city = mysqli_real_escape_string($conn,$city);
				$street = mysqli_real_escape_string($conn,$street);
				$streetno = mysqli_real_escape_string($conn,$streetno);
				$start = mysqli_real_escape_string($conn,$start);
				$city = stripslashes($city);
				$street = stripslashes($street);
				$streetact = 'via '.$street;  
				$streetno = stripslashes($streetno);
				$start = stripslashes($start);
				$starttime = substr("$start", -8);
				$startdate = substr("$start", -19,10);
				$location = ucfirst($city).", ".ucfirst($street)." ".$streetno;

				
				
				$violq = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VStreetNo like '$streetno' and VCity like '$city' 
						  and VDate >= '$startdate'";
				
			}
			elseif(!empty($_POST['city']) && !empty($_POST['street']) && !empty($_POST['streetno'])&&!empty($_POST['end'])){
				
				$city = $_POST['city'];
				$street = $_POST['street'];
				$streetno = $_POST['streetno'];
				$end = $_POST['end'];
				$city = mysqli_real_escape_string($conn,$city);
				$street = mysqli_real_escape_string($conn,$street);
				$streetno = mysqli_real_escape_string($conn,$streetno);
				$end = mysqli_real_escape_string($conn,$end);
				$city = stripslashes($city);
				$street = stripslashes($street);
				$streetact = 'via '.$street;  
				$streetno = stripslashes($streetno);
				$end = stripslashes($end);
				$endtime= substr("$end", -8);
				$enddate= substr("$end", -19,10);
				$location = ucfirst($city).", ".ucfirst($street)." ".$streetno;;
				
				
				$violq = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VStreetNo like '$streetno' and VCity like '$city' 
						  and VStreetNo = '$streetno' and VDate <= '$enddate'";
			}
			elseif(!empty($_POST['city']) && !empty($_POST['street'])&& !empty($_POST['start']) &&!empty($_POST['end'])){
				$city = $_POST['city'];
				$street = $_POST['street'];
				$start = $_POST['start'];
				$end = $_POST['end'];
				$city = mysqli_real_escape_string($conn,$city);
				$street = mysqli_real_escape_string($conn,$street);
				$start = mysqli_real_escape_string($conn,$start);
				$end = mysqli_real_escape_string($conn,$end);
				$city = stripslashes($city);
				$street = stripslashes($street);
				$streetact = 'via '.$street;  
				$start = stripslashes($start);
				$end = stripslashes($end);
				$starttime = substr("$start", -8);
				$startdate = substr("$start", -19,10);
				$endtime= substr("$end", -8);
				$enddate= substr("$end", -19,10);
				$location = ucfirst($city).", ".ucfirst($street);
				
				
				$violq = "select * from violations (VStreetName like '$street' or VStreetName like '$streetact') and VCity like '$city' 
						  and VDate >= '$startdate' and VDate <= '$enddate'";
				
			}
			
			elseif(!empty($_POST['city']) && !empty($_POST['street']) && !empty($_POST['streetno'])){
				$city = $_POST['city'];
				$street = $_POST['street'];
				$streetno = $_POST['streetno'];
				$city = mysqli_real_escape_string($conn,$city);
				$street = mysqli_real_escape_string($conn,$street);
				$streetno = mysqli_real_escape_string($conn,$streetno);
				$city = stripslashes($city);
				$street = stripslashes($street);
				$streetno = stripslashes($streetno);
				$streetact = 'via '.$street;  
				$location = ucfirst($city).", ".ucfirst($street)." ".$streetno;
				
				
				
				$violq = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VStreetNo like '$streetno' and VCity like '$city'";

				
			}
			elseif(!empty($_POST['city']) && !empty($_POST['street'])&&!empty($_POST['start'])){
				
				$city = $_POST['city'];
				$street = $_POST['street'];
				$start = $_POST['start'];
				$city = mysqli_real_escape_string($conn,$city);
				$street = mysqli_real_escape_string($conn,$street);
				$start = mysqli_real_escape_string($conn,$start);
				$city = stripslashes($city);
				$street = stripslashes($street);
				$streetact = 'via '.$street;  
				$start = stripslashes($start);
				$starttime = substr("$start", -8);
				$startdate = substr("$start", -19,10);
				$location = ucfirst($city).", ".ucfirst($street);
				
				
				
				$violq = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VCity like '$city' and VDate >= '$startdate'";
				
			}
			elseif(!empty($_POST['city']) && !empty($_POST['street'])&&!empty($_POST['end'])){
				
				$city = $_POST['city'];
				$street = $_POST['street'];
				$end = $_POST['end'];
				$city = mysqli_real_escape_string($conn,$city);
				$street = mysqli_real_escape_string($conn,$street);
				$streetact = 'via '.$street;  
				$end = mysqli_real_escape_string($conn,$end);
				$city = stripslashes($city);
				$street = stripslashes($street);
				$end = stripslashes($end);
				$endtime= substr("$end", -8);
				$enddate= substr("$end", -19,10);
				$location = ucfirst($city).", ".ucfirst($street);
				
				
				$violq = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VCity like '$city' and VDate <= '$enddate'";
				
			}
			elseif(!empty($_POST['city']) && !empty($_POST['street'])){
				
				$city = $_POST['city'];
				$street = $_POST['street'];
				$city = mysqli_real_escape_string($conn,$city);
				$street = mysqli_real_escape_string($conn,$street);
				$streetact = 'via '.$street; 
				$city = stripslashes($city);
				$street = stripslashes($street);
				$location = ucfirst($city).", ".ucfirst($street);
				
				
				$violq = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VCity like '$city'";
				
			}
			
			else{
				$result_message = "Please atleast enter city and street in order to get useful data";
				$violq = "select * from violations";
				
			}
			$viol1=0;
			$viol2=0;
			$viol3=0;
			$viol4=0;
			$viol5=0;
				
			$result = mysqli_query($conn,$violq);
			if (mysqli_num_rows($result) == 0) {
				
				$result_message = "There are no violations reported with the contraints provided";
			}
			
			

				
				while($row = mysqli_fetch_assoc($result)) {
					
					if($row["VType"] == 'Double Parking'){ $viol1++;}
					if($row["VType"] == 'Non Parking Zone'){ $viol2++;}
					if($row["VType"] == 'Bike Lane Parking'){ $viol3++;}
					if($row["VType"] == 'Handicap Parking'){ $viol4++;}
					if($row["VType"] == 'Badly Parked'){ $viol5++;}
				
				}
			
				
			
				
		}
		else{
			
			$violq = "select * from violations";
			$result = mysqli_query($conn,$violq);
			
			if(mysqli_error($conn)){
				$result_message = "Failed to query the db";
			}
			else{
				
				
				while($row = mysqli_fetch_assoc($result)) {
					
					if($row["VType"] == 'Double Parking'){ $viol1++;}
					if($row["VType"] == 'Non Parking Zone'){ $viol2++;}
					if($row["VType"] == 'Bike Lane Parking'){ $viol3++;}
					if($row["VType"] == 'Handicap Parking'){ $viol4++;}
					if($row["VType"] == 'Badly Parked'){ $viol5++;}
				
				}
			
				
			}
			
		}
	
	
	}
	
	
	
?>