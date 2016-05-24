'use strict';

angular.module('myApp.roverDrive', ['ngRoute', 'ngWebSocket'])
    .controller('RoverDriveCtrl', function($scope, $location) {
        $scope.mjpegStreamURL = 'http://' + $location.host() + ':9000/stream/video.mjpeg';
    });
