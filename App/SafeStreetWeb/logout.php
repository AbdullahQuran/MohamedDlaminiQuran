<!DOCTYPE html>
<html lang="en">

	<?php
		session_start();
	
		if(isset($_SESSION['username'])){
		
			session_destroy();
			header("location:index.php");
		}
		else{
			header("location:index.php");
		}
	
	
	
	?>


