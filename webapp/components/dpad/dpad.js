'use strict';

function DPadController($scope, roverService) {
    var ctrl = this;


    ctrl.up = function() {
        console.debug("up");
        roverService.driveForward();
    };

    ctrl.down = function() {
        console.debug("down");
        roverService.driveBackward();
    };

    ctrl.left = function() {
        console.debug("left");
        roverService.turnLeft();
    };

    ctrl.right = function() {
        console.debug("right");
        roverService.turnRight();
    };

    ctrl.stop = function() {
        console.debug("stop");
        roverService.stop();
    }
}

angular.module('myApp.dpad', [])
    .component('dpad', {
        restrict: 'EA',
        scope: {


        },
        templateUrl: '/components/dpad/dpad.html',
        css: '/components/dpad/dpad.css',
        controller: DPadController,
        controllerAs: 'ctrl'
    });