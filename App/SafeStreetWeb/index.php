<!DOCTYPE html>
<html lang="en">
<head>
	<?php
	
		include 'loginweb.php';
	
	?>
	<title>Safestreet Login</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" type="text/css" href="css/main.css">

</head>

<body>

	<div class="login-page"style="width:360px;">
	  <div class="form" >
		<img src="img/shield.png" alt="Safestreet Logo" style="width:90px;height:90px;">
		<br><br><br>
		<form class="login-form" action="#" method="POST" >
		  <input type="text" placeholder="email address" name="username" />
		  <input type="password" placeholder="password" name="password" />
		  <button type="submit" name="submit" id="submit">login</button>
		  <div class ="result"> <?php echo $result_message; ?> </div>
		</form>
	  </div>
	  <div class="form">
		<form class="login-form" action="#" method="POST">
		  <input type="text" placeholder="email address" name="email"/>
		  <button type="submit" name="submit1" id="submit1">Opt in</button>
		  <div class ="result"> <?php echo $result_message1; ?> </div>
		</form>
	  </div>
	  
	  
	</div>
	
	<div class="login-page">
	  
	</div>

</body>

<script src="js/toggle.js"></script>

</html>

