<!DOCTYPE html>
<html lang="en">
<head>
	<?php
		session_start();
	
	?>
	<title>Safestreet Login</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" type="text/css" href="css/main.css">

</head>

<body>

	<div class="login-page" style="width:360px;">
	  <div class="form" >
		<img src="img/shield.png" alt="Safestreet Logo" style="width:90px;height:90px;">
		<br><br><br>
		
		  <button onclick="window.location.href='/statisticsWeb.php'">Violation Statistics</button>
		  <br><br><br>
		  <button onclick="#">Suggestions</button>
		  <br><br><br>
		  <button type="submit" name="submit" id="submit">Logout</button>
		
	  </div>
	  
	</div>
	

</body>

<script src="js/toggle.js"></script>

</html>

