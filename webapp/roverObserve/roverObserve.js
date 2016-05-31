'use strict';

angular.module('myApp.roverObserve', [])
.controller('RoverObserveCtrl', ['roverService', '$scope', '$location', function(roverService, $scope, $location) {
    $scope.mjpegStreamURL = 'http://' + $location.host() + ':9000/stream/video.mjpeg';
    //$scope.mjpegStreamURL = 'http://192.168.188.38:9000/stream/video.mjpeg';

    $scope.snapshotEnabled = true;
    $scope.isSnapshotFetched = false;
    $scope.snapshotUrl;

    $scope.snapshotClicked = function(clickEvent) {
        roverService.getCameraSnapshot(function (imageData) {
            $scope.$apply(function () {
                $scope.snapshotURL = imageData[0];
                $scope.isSnapshotFetched = true;
            });
            // var image = new Image();
            // image.src = imageData;
            // document.body.appendChild(image);
        });
    };
}]);
