/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
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

        /**
         * Currently set mapping for the buttons of the control.
         * @type {Object}
         */
        var modeSelected = modeDriver;

        /**
         * Contains function mapping for driver mode.
         * @type {object}
         */
        var modeDriver = {
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
            buttonClick: function() {
                roverService.stop();
            }
        };

        /**
         * Contains function mapping for camera control mode.
         * @type {object}
         */
        var modeCamera = {
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
            buttonClick: function() {
                roverService.cameraResetPosition();
            }
        };


        this.$onInit = function() {
            
            // switch to camera mode
            if($attrs.mode == 'camera'){
                $scope.hideStopButton = true;
                $scope.hideCenterButton = false;
                modeSelected = modeCamera;
            } else {
                $scope.hideStopButton = false;
                $scope.hideCenterButton = true;
                modeSelected = modeDriver;
            }
        };

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

        $scope.buttonClick = function() {
            console.debug("dpad extra button clicked");
            modeSelected.buttonClick();
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