'use strict';

angular.module('myApp.roverDrive', [])
    .controller('RoverDriveCtrl', ['roverService','$scope','$location',function(roverService,$scope, $location) {
        $scope.mjpegStreamURL = 'http://' + $location.host() + ':9000/stream/video.mjpeg';

        $scope.stop = function () {
             roverService.stop();
        }
    }]);
