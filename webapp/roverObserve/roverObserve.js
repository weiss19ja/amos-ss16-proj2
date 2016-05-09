'use strict';

angular.module('myApp.roverObserve', ['ngRoute', 'ngWebSocket'])
    .factory('streamDrawer', function() {
        var streamDrawer = {
            /**
             * resize the canvas
             */
            updateSize: function(height, width) {
                console.log("Change canvas size to: " + height + "x" + width);

                var canvas = document.getElementById("canvas");
                if (canvas.getContext) {
                    var ctx = canvas.getContext("2d");
                    canvas.style.width  = width+'px';
                    canvas.style.height = height+'px';
                }
            }
        };
        return streamDrawer;
    })
.controller('RoverObserveCtrl', function($scope, $location, streamDrawer) {
    $scope.streamDrawer = streamDrawer;
    $scope.canvasSize = {
        width: '640',
        height: '480'
    };
    $scope.$watch('canvasSize', function(newValue, oldValue) {
        if (newValue != oldValue) {
            streamDrawer.updateSize(newValue.height, newValue.width);
        }
    }, true);
    var init = function(){
        var canvas = document.getElementById("canvas");
        if (canvas.getContext) {
            console.log("init canvas");
            var ctx = canvas.getContext("2d");
            // set initial canvas size
            var width = $scope.canvasSize.width;
            var height = $scope.canvasSize.height;
            canvas.width  = width;
            canvas.height = height;
            canvas.style.width  = width+'px';
            canvas.style.height = height+'px';
            //clear the canvas
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            ctx.fillText('Loading video stream...', canvas.width / 3 , canvas.height / 3);
            // Setup the WebSocket connection and start the player
            console.log("Setting up WebSocket on: "+ $location.host());
            var client = new WebSocket('ws://'+ $location.host() +':8084/');
            var player = new jsmpeg(client, {canvas: canvas});
        }

    };
    init();
});
