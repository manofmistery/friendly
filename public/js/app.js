var app = angular.module('friendly', []);
// Main Controller
app.controller('MainCtrl', function($scope, $http) {


});


app.controller('MapCtrl', function ($scope, $http) {

    var mapOptions = {
        zoom: 13,
        center: new google.maps.LatLng( 58.161758, 8.0009),
        mapTypeId: google.maps.MapTypeId.HYBRID
    }

    $scope.map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

    $scope.markers = [];

    var infoWindow = new google.maps.InfoWindow();

    var createMarker = function (info){

        var marker = new google.maps.Marker({
            map: $scope.map,
            position: new google.maps.LatLng(info.latitude, info.longitude),
            title: info.user
        });
        marker.content = '<div class="infoWindowContent">' + info.user + ' @ '+info.date + '</div>';

        google.maps.event.addListener(marker, 'click', function(){
            infoWindow.setContent('<h2>' + marker.title + '</h2>' + marker.content);
            infoWindow.open($scope.map, marker);
        });

        $scope.markers.push(marker);

    }

    //Fetch last 10 locations, and create a path using polylines
    var createPath = function(){

        $http.get('/latest/eirik/10').success(function(res){
            var waypoints = [];
            var i;
            //Generate path from json data
            for(i = 0;i<res.length;i++){
                console.log("lat: "+res[i].latitude+", lon: "+res[i].longitude);
                waypoints.push( new google.maps.LatLng(res[i].latitude, res[i].longitude ));
            }
         var route = new google.maps.Polyline({
             path: waypoints,
            geodesic: true,
            strokeColor: '#FF0000',
            strokeOpacity: 1.0,
            strokeWeight: 2
            });

    route.setMap($scope.map);
        });
    }


    //Fetch some data using our api
    $http.get('/latest/eirik').success(function(res){
        console.log("Fetched data");
        console.dir(res);
        createMarker(res);
    });

    createPath();
    $scope.openInfoWindow = function(e, selectedMarker){
        e.preventDefault();
        google.maps.event.trigger(selectedMarker, 'click');
    }

});
