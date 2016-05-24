'use strict';

angular.module('myApp.roverObserve', [])
.controller('RoverObserveCtrl', ['$scope', '$location',function($scope, $location) {
    $scope.mjpegStreamURL = 'http://' + $location.host() + ':9000/stream/video.mjpeg';
}]);
