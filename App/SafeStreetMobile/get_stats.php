<?php
	include 'conn.php';
	
	//$street = 'Mountain View';
	//$streetn = '94043';
	//$type = 'viol 2';
	//$dateS = '0000-00-00';
	//$dateE = '2020-12-20';
	//$time = '10:30';
	//$city = 'milan';
	
	$street = $_POST["streetviol"];
	$streetact = 'via '.$street;
	$streetn = $_POST["streetnviol"];
	$type = $_POST["typeviol"];
	$dateS = $_POST["dateviolS"];
	$timeS = $_POST["timeviolS"];
	$dateE = $_POST["dateviolE"];
	$timeE = $_POST["timeviolE"];
	$city = $_POST["cityviol"];
	
	
	$viol1 = 0;
	$viol2 = 0;
	$viol3 = 0;
	$viol4 = 0;
	$viol5 = 0;
	
	function violcount ($violcount) {
		
			$violcount = str_replace("\0", "", $violcount);
			return $violcount;
		}
	
	
	if($conn){
		
		if($street == 'init'){
			$q1 = "select * from violations where VType like 'Double Parking'";
		}
		else if($streetn != 'NA' && $street != 'NA' ){
			$q1 = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VStreetNo like '$streetn' and VCity like '$city' and VType like 'Double Parking' and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}
		else if( $streetn == 'NA' && $street != 'NA'){
			$q1 = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VCity like '$city' and VType like 'Double Parking' and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}
		else{
			$q1 = "select * from violations where VCity like '$city' and VType like 'Double Parking' and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}
		$result1 = mysqli_query($conn,$q1);
		$viol1 = mysqli_num_rows($result1);
		echo violcount($viol1);
		
		if($street == 'init'){
			$q2 = "select * from violations where VType like 'Non Parking Zone'";
		}
		else if($streetn != 'NA' && $street != 'NA'){
			$q2 = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VStreetNo like '$streetn' and VCity like '$city' and VType like 'Non Parking Zone'and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}
		else if($streetn == 'NA' && $street != 'NA'){
			$q2 = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VCity like '$city' and VType like 'Non Parking Zone' and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}
		else{
			$q2 = "select * from violations where VCity like '$city' and VType like 'Non Parking Zone' and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}
		$result2 = mysqli_query($conn,$q2);
		$viol2 = mysqli_num_rows($result2);
		echo violcount($viol2);
		
		if($street == 'init'){
			$q3 = "select * from violations where VType like 'Bike Lane Parking'";
		}
		else if($streetn != 'NA' && $street != 'NA'){
			$q3 = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VStreetNo like '$streetn' and VCity like '$city' and VType like 'Bike Lane Parking'and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}
		else if( $streetn == 'NA' && $street != 'NA'){
			$q3 = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VCity like '$city' and VType like 'Bike Lane Parking' and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}
		else{
			$q3 = "select * from violations where VCity like '$city' and VType like 'Bike Lane Parking' and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}
		$result3 = mysqli_query($conn,$q3);
		$viol3 = mysqli_num_rows($result3);
		echo violcount($viol3); 
		if($street == 'init'){
			$q4 = "select * from violations where VType like 'Handicap Parking'";
		}
		else if($streetn != 'NA' && $street != 'NA'){
			$q4 = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VStreetNo like '$streetn' and VCity like '$city' and VType like 'Handicap Parking'and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}
		else if($streetn == 'NA' && $street != 'NA'){
			$q4 = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VCity like '$city' and VType like 'Handicap Parking' and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}
		else{
			$q4 = "select * from violations where VCity like '$city' and VType like 'Handicap Parking' and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}$result4 = mysqli_query($conn,$q4);
		$viol4 = mysqli_num_rows($result4);
		echo violcount($viol4);
		
		if($street == 'init'){
			$q5 = "select * from violations where VType like 'Badly Parked'";
		}
		else if($streetn != 'NA' && $street != 'NA'){
			$q5 = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VStreetNo like '$streetn' and VCity like '$city' and VType like 'Badly Parked' and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}
		else if( $streetn == 'NA' && $street != 'NA'){
			$q5 = "select * from violations where (VStreetName like '$street' or VStreetName like '$streetact') and VCity like '$city' and VType like 'Badly Parked' and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}
		else{
			$q5 = "select * from violations where VCity like '$city' and VType like 'Badly Parked' and VDate >= '$dateS' and VDate <= '$dateE' and VTime >= '$timeS' and VTime <= '$timeE'";
		}
		$result5 = mysqli_query($conn,$q5);
		$viol5 = mysqli_num_rows($result5);
		echo violcount($viol5);	
		
	}
	else{
		echo "conn failed...";
	}
	
	
?>