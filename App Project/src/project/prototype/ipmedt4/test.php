  <?php
$link = mysqli_connect('sql3.pcextreme.nl', '55846IPMEDT4', 'Ipmedt4', '55846IPMEDT4');

    $query = "SELECT Attraction_ID, Attraction_name_Dutch, Attraction_description_Dutch FROM attractions WHERE Attraction_ID <=6 ORDER BY Attraction_ID ASC";

    $result = mysqli_query($link, $query) or die(mysqli_error($link));

    //if ($result = mysqli_query($link, $query)) {

		/* fetch associative array */
	$rows = array();
	while($r = $result->fetch_assoc())
	{
		$rows[] = array_map('utf8_encode', $r);
	}
	
	print json_encode($rows);
 ?>