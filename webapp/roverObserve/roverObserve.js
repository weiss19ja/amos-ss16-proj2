/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
'use strict';

angular.module('myApp.roverObserve', ['ngMaterial'])
    .controller('RoverObserveCtrl', ['roverService', '$scope', '$location', '$mdDialog', '$mdMedia', function (roverService, $scope, $location, $mdDialog, $mdMedia) {
        $scope.mjpegStreamURL = 'http://' + $location.host() + ':9000/stream/video.mjpeg';
        $scope.snapshotEnabled = true;

        $scope.snapshotClicked = function (clickEvent) {
            roverService.getCameraSnapshot(function (imageData) {

                $mdDialog.show({
                    controller: SnapshotDialogController,
                    templateUrl: 'roverObserve/snapshotDialog.html',
                    parent: angular.element(document.body),
                    targetEvent: clickEvent,
                    clickOutsideToClose: true,
                    locals: {
                        imageUrl: imageData[0]
                    }
                });

            });
        };

        function SnapshotDialogController($scope, $mdDialog, imageUrl) {
            $scope.imageUrl = imageUrl;
            $scope.cancel = function () {
                $mdDialog.cancel();
            }
        }

    }]);