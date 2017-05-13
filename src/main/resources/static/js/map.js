var map, heatmap;
var locations;
mapLoading();
function mapLoading () {
	//event.preventDefault();
	var keywords = null;
	$('input[id^="dys"]').each(function( index ) {
		var id = $(this).attr("id");
		var val = $(this).val();
		if (val.trim())
			if (index === 0) {
				keywords = id + "=" + val;
			} else {
				keywords = keywords + ";" + id + "=" + val;
			}
	});
	
	if (keywords) {
		$.get("/api/map/mixed;" + keywords, function(data){
			if(!$.isEmptyObject(data)){
				locations = data;
				initMap();
			}
		});
	}
};

// Map initialization

function initMap() {
	var center = new google.maps.LatLng(20.5074, 0.1278);
	console.log(locations);
    var map = new google.maps.Map(document.getElementById('map'), {
      zoom: 2,
      center: center,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    });
    var labels = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

    var markers = locations.map(function(location, i) {
        return new google.maps.Marker({
          position: location,
          label: labels[i % labels.length]
        });
      });
    
    var options = {
        imagePath: '/img/images/m',
        gridSize: 1,
        minimumClusterSize: 1
    };

    var markerCluster = new MarkerClusterer(map, markers, options);
}