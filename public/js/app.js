var app = angular.module('friendly', []);
// Main Controller
app.controller('MainCtrl', function($scope, $http) {

    //Fetch some data using our api
    $http.get('/test').success(function(res){
       console.log("Fetched data using /test");
        $scope.data = res;
    });
});
