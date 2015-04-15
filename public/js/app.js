var app = angular.module('friendly', []);
// Main Controller
app.controller('MainCtrl', function($scope, $http) {


});


app.controller('MapCtrl', function ($scope, $http) {

    var mapOptions = {
        zoom: 4,
        center: new google.maps.LatLng( 58.161758, 8.0009),
        mapTypeId: google.maps.MapTypeId.TERRAIN
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
        marker.content = '<div class="infoWindowContent">' + info.desc + '</div>';

        google.maps.event.addListener(marker, 'click', function(){
            infoWindow.setContent('<h2>' + marker.title + '</h2>' + marker.content);
            infoWindow.open($scope.map, marker);
        });

        $scope.markers.push(marker);

    }

    //Fetch some data using our api
    $http.get('/latest/eirik').success(function(res){
        console.log("Fetched data");
        console.dir(res);
        createMarker(res);
    });



    $scope.openInfoWindow = function(e, selectedMarker){
        e.preventDefault();
        google.maps.event.trigger(selectedMarker, 'click');
    }

});