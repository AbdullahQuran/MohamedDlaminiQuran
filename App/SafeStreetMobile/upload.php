<?php

	include 'conn.php';
	
	$target_dir='violation_images/';
	$target_file=$target_dir.time()."-".basename($_FILES["image"]["name"]);
	$uploadOK = 1;
	$imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));
	$check = getimagesize($_FILES["image"]["tmp_name"]);
	$response =array();
	$error="";
	
	if($check !=false){
		$uploadOK = 1;
	
		
	}else{
		
		$uploadOK = 0;
		$error = "Uploaded file is not a valid image";
	}
	
	if($imageFileType != "jpg" && $imageFileType != "jpeg" && $imageFileType != "png" && $imageFileType != "gif" && $imageFileType != "JPG" && $imageFileType != "JPEG" && $imageFileType != "PNG" && $imageFileType != "GIF" ){
		
		$uploadOK = 0;
		$error = "Image type is not supported";
	}
	else{
		$uploadOK = 1;
		
	}
	
	if($uploadOK == 0){
		
		$response['status'] =0;
		$response['message'] = $error;
		
	} else{
		
		if(move_uploaded_file($_FILES["image"]["tmp_name"],$target_file)){
			
			$response['status'] = 1;
			$response['message'] = "Image Uploaded Succesfully";
			
		}else{
			$response['status'] = 0;
			$response['message'] = "Unable to upload image to server";
			
		}
		
		
	}
	
	echo json_encode($response);
	
	?>