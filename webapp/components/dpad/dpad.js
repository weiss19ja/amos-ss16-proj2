'use strict';

function DPadController() {
    var ctrl = this;

    ctrl.up = function() {
        console.debug("up");
    };

    ctrl.down = function() {
        console.debug("down");
    };

    ctrl.left = function() {
        console.debug("left");
    };

    ctrl.right = function() {
        console.debug("right");
    };

    ctrl.stop = function() {
        console.debug("stop");
    }
}

angular.module('myApp', [])
    .component('dpad', {
        restrict: 'EA',
        scope: {


        },
        templateUrl: '/components/dpad/dpad.html',
        css: '/components/dpad/dpad.css',
        controller: DPadController,
        controllerAs: 'ctrl'
    });