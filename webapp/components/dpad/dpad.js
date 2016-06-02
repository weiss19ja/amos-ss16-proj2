'use strict';

/**
 * Module to enable a D-Pad component. The D-Pad has 4 buttons for directions plus one stop button.
 * D-Pad can be used to drive the rover and to move the camera.
 * Drive mode is enabled as default.
 * To use camera mode add this attribute: mode="camera".
 * 
 * For communication with the rover 'roverService' is used. 
 */
angular.module('myApp.dpad', [])

    .controller('DPadController', ['$scope', 'roverService','$attrs', function($scope, roverService, $attrs) {
        
        $scope.hideStopButton = false;

        $scope.hideCameraButton = true;

        var modeDriver = {
            buttonText: "Stop",
            up:function () {
                roverService.driveForward();
            },
            down:function () {
                roverService.driveBackward();
            },
            left:function () {
                roverService.turnLeft();
            },
            right:function () {
                roverService.turnRight();
            },
            stop:function () {
                roverService.stop();
            },
            resetCameraPosition: function() {

            }
        };

        var modeCamera = {
            buttonText: "Zentrieren",
            up:function () {
                roverService.cameraMoveUp();
            },
            down:function () {
                roverService.cameraMoveDown();
            },
            left:function () {
                roverService.cameraMoveLeft();
            },
            right:function () {
                roverService.cameraMoveRight();
            },
            stop:function () {
                
            },
            resetCameraPosition: function() {
                roverService.cameraResetPosition();

            }
        };

        var modeSelected = modeDriver;

        this.$onInit = function() {
            
            // switch to camera mode
            if($attrs.mode == 'camera'){
                $scope.hideStopButton = true;
                $scope.hideCameraButton = false;
                modeSelected = modeCamera;
            }
        };

        $scope.buttonText = modeSelected.buttonText;

        $scope.up = function() {
            console.debug("up");
            modeSelected.up();
        };

        $scope.down = function() {
            console.debug("down");
            modeSelected.down();
        };

        $scope.left = function() {
            console.debug("left");
            modeSelected.left();
        };

        $scope.right = function() {
            console.debug("right");
            modeSelected.right();
        };

        $scope.stop = function() {
            console.debug("stop");
            modeSelected.stop();
        };

        $scope.resetCameraPosition = function() {
            console.debug("reset camera position");
            modeSelected.resetCameraPosition();
        };
    }])
    .component('dpad', {
        restrict: 'EA',
        templateUrl: 'components/dpad/dpad.html',
        css: 'components/dpad/dpad.css',
        controller: 'DPadController',
        bindings: {
            mode: '<'
        }
    });