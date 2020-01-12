<!DOCTYPE html>
<html lang="en">
<head>
	<?php
		session_start();
		include 'statsweb.php';
		include 'exportexcweb.php'
	
	?>
	<title>Safestreet Statistics</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" type="text/css" href="css/main.css">
	<!-- Font Awesome -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
	<link rel="stylesheet" href="css/jquery.datetimepicker.min.css">
	
		
	<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
	<script type="text/javascript" src="js/moment.min.js"></script>
	
	<script type="text/javascript" src="js/jquery.datetimepicker.full.min.js"></script>
	
	
	
	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
 <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=places&key=AIzaSyD5rOyxZ19MMKoOYZF0JAnCIQVtM666PiA"></script>
	
	
	
	<script>
	
	var searchInput = 'search_input';
	var latitude;
	var longitude;
	var address = "";
	var city = "";
	var city1 = "";
	var state = "";
	var zip = "";
	var streetnum = "";
	$(document).ready(function (){
		var autocomplete;
		autocomplete = new google.maps.places.Autocomplete((document.getElementById(searchInput)),{
			types:['geocode']
		});
		
		google.maps.event.addListener(autocomplete,'place_changed',function(){
			var near_place = autocomplete.getPlace();
			latitude = near_place.geometry.location.lat();
			longitude = near_place.geometry.location.lng();
			address = "";
			city = "";
			state = "";
			zip = "";
			streetnum = "";
			
			$.each(near_place.address_components, function(){
						switch(this.types[0]){
							case "street_number":
								streetnum = this.short_name;
								break;
							case "postal_code":
								zip = this.short_name;
								break;
							case "route":
								address = this.short_name;
								break;
							case "locality":
								city = this.short_name;
								break;
							case "administrative_area_level_1":
								city1 = this.short_name;
								break;
							case "administrative_area_level_2":
								city2 = this.short_name;
								break;
						}
					});
					
					if(city === "") {
						document.getElementById("city").value = city1;
					}
					else{
						document.getElementById("city").value = city;
					}
					
					
					document.getElementById("street").value = address;
					document.getElementById("streetno").value = streetnum;
					
					alert("Location" + city);
					

		});
	});

	</script>
</head>

<body>
		
		<form align="right" name="form1" method="post" action="logout.php" style="margin-right:50px; margin-top:20px;">
			<button class="btnlogout">Logout</button>
		</form>

	
	  <div class="form" style="width:800px; margin-top:100px;">
		
		<form class="login-form" action="#" method="POST" >
		  <input type="text" placeholder="Search by city, street, zip..." name="search_input" id="search_input" style="width:500px;"></input>
		  <input type="hidden" name="street" id="street" />
		  <input type="hidden" name="streetno" id="streetno" />
		  <input type="hidden" name="city" id="city" />
		  <input type="text" placeholder="Start Date" id="pickerstart"  name="start" style="width:250px;" >
		  <input type="text" placeholder="End Date" id="pickerend"  name="end" style="width:250px;">
		  <button type="submit" name="submit" id="submit" style="width:300px;" onclick="GetAddress()">Update Chart</button>
		  
		  <div class ="result"> <?php echo $result_message; ?> </div>
		 </form>
		 
		 <input type="button" value="Get Address" onclick="GetAddress()" />
		  <div id="piechart_3d" style="width: 100%; height: 500px;"></div>
		  
		  <form class="login-form" action="#" method="POST" >
		  
		  <button type="submit" name="submit1" id="submit1" style="width:300px;">Get Raw Data</button>
		  <div class ="result1"> <?php echo $result_message1; ?> </div>
		 </form>
		</div>
	  </div>
	

</body>


	
	<script>
	$("#pickerstart").datetimepicker({
		datepicker:true,
		timepicker: false,
		format:'Y-m-d',
		value: '',
		weeks:true
		

	});
	$("#pickerend").datetimepicker({
		datepicker:true,
		timepicker:false,
		format:'Y-m-d',
		value: '',
		weeks:true

	});
</script>	
   <script type="text/javascript">
      google.charts.load("current", {packages:["corechart"]});
      google.charts.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Violation', 'Occurences'],
          ['Double Parking',    <?php echo $viol1; ?>],
          ['Non Parking Zone',      <?php echo $viol2; ?>],
          ['Bike Lane Parking',  <?php echo $viol3; ?>],
          ['Handicap Parking', <?php echo $viol4; ?>],
          ['Badly Parked',    <?php echo $viol5; ?>]
        ]);

		var title = "Violation Statistics : " + "<?php echo $location; ?>";
        var options = {
          title: title,
          is3D: true,
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));
        chart.draw(data, options);
      }
    </script>
	
	<script src="js/toggle.js"></script>

</html>

