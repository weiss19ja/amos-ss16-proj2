'use strict';

angular.module('myApp.roverObserve', [])
.controller('RoverObserveCtrl', ['$scope', '$location', '$http', function($scope, $location, $http) {
    $scope.mjpegStreamURL = 'http://' + $location.host() + ':9000/stream/video.mjpeg';
    //$scope.mjpegStreamURL = 'http://192.168.188.38:9000/stream/video.mjpeg';

    $scope.snapshotEnabled = true;

    $scope.snapshotClicked = function(clickEvent) {
        $http({
            method: 'GET',
            url: 'http://' + $location.host() + ':9000/stream/snapshot.jpeg'
        }).then(function successCallback(response) {
            console.log("Statuscode: " + response.status + " \nData: " + response.data);
        }, function errorCallback(response) {
            console.log(response);
        });

    };
}]);
