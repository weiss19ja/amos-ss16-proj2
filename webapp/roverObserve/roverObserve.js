'use strict';

angular.module('myApp.roverObserve', ['ngRoute', 'ngWebSocket'])
.controller('RoverObserveCtrl', function($scope, $location) {
    $scope.mjpegStreamURL = 'http://' + $location.host() + ':9000/stream/video.mjpeg';
});
