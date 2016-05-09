'use strict';

angular.module('myApp.roverObserve', [])
    .factory('squareDrawer', function() {
        var registry = {};

        var squareDrawer = {
            /**
             * draw the square on the canvas
             */
            draw: function(height, width) {
                console.log("square to draw: " + height + "x" + width);

                var canvas = document.getElementById("canvas");
                if (canvas.getContext) {
                    console.log("drawing");
                    var ctx = canvas.getContext("2d");
                    //clear the canvas
                    ctx.clearRect(0,0, 200, canvas.height);
                    ctx.fillRect(0, 0, width, height);
                }
            }
        };
        return squareDrawer;
    })
.controller('RoverObserveCtrl', function($scope, squareDrawer) {
    $scope.squareDrawer = squareDrawer;
    $scope.square = {
        height: '100',
        width: '100'
    };
    $scope.$watch('square', function(newValue, oldValue) {
        if (newValue != oldValue) {
            console.log('value changed: ' + JSON.stringify($scope.square));
            squareDrawer.draw(newValue.height, newValue.width);
        }
    }, true);
});
