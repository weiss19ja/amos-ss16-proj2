'use strict';

/**
 * Module to enable a D-Pad component. The D-Pad has 4 buttons for directions plus one stop button.
 */
angular.module('myApp.dpad', [])

    .controller('DPadController', ['$scope', 'roverService', function($scope, roverService) {
        $scope.up = function() {
            console.debug("up");
            roverService.driveForward();
        };

        $scope.down = function() {
            console.debug("down");
            roverService.driveBackward();
        };

        $scope.left = function() {
            console.debug("left");
            roverService.turnLeft();
        };

        $scope.right = function() {
            console.debug("right");
            roverService.turnRight();
        };

        $scope.stop = function() {
            console.debug("stop");
            roverService.stop();
        };
    }])
    .component('dpad', {
        restrict: 'EA',
        templateUrl: 'components/dpad/dpad.html',
        css: 'components/dpad/dpad.css',
        controller: 'DPadController'
    });