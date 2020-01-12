<?php
use PhpOffice\PhpSpreadsheet\Spreadsheet;
use PhpOffice\PhpSpreadsheet\Writer\Xlsx;



require 'vendor/autoload.php';

include 'conn.php';



$result_message1="";

if(isset($_POST['submit1'])){

$spreadsheet = new Spreadsheet();
$sheet = $spreadsheet->getActiveSheet();

$filename = "violationdataweb.xlsx";         //File Name
function mysqli_field_name($result, $field_offset)
{
    $properties = mysqli_fetch_field_direct($result, $field_offset);
    return is_object($properties) ? $properties->name : null;
}
 
if($conn){
	
	
	$result = mysqli_query($conn,$violq);
	
	
	//start of printing column names as names of MySQL fields
	for ($i = 0; $i < mysqli_num_fields($result); $i++) {
		
		
		$fieldname = mysqli_field_name($result,$i);
		$letter = intToLetter($i);
		$number = 1;
		$cell = $letter.$number;
		
		
		
		$sheet->setCellValue("$cell","$fieldname");
		
	}
	
	//start while loop to get data
	$row_counter = 1;
	while($row = mysqli_fetch_row($result))
	{
		$row_counter++;
		$schema_insert = "";
		for($j=0; $j<mysqli_num_fields($result);$j++)
		{
			
			
			$letter = intToLetter($j);
			$value = $row[$j];
			$cell = $letter.$row_counter;
			$sheet->setCellValue("$cell","$value");
			
			
		}
	}

	$writer = new Xlsx($spreadsheet);
	$writer->save("$filename");
	include 'mailermailerweb.php';
	$result_message1 = "The requested file has been sent to your registered email!";
	
 } 
 else{
	 
	 echo 'unable to connect to db';
	 
 }
 
}
 
 function intToLetter($int){
	 
	 if($int == 0) $letter = "A";
	 elseif($int == 1) $letter = "A";
	 elseif($int == 2) $letter = "A";
	 elseif($int == 3) $letter = "B";
	 elseif($int == 4) $letter = "C";
	 elseif($int == 5) $letter = "D";
	 elseif($int == 6) $letter = "E";
	 elseif($int == 7) $letter = "F";
	 elseif($int == 8) $letter = "G";
	 elseif($int == 9) $letter = "H";
	 
	 return $letter;
 }
?>