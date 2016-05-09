'use strict';

angular.module('myApp.roverObserve', [])
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
.controller('RoverObserveCtrl', function($scope, streamDrawer) {
    $scope.streamDrawer = streamDrawer;
    $scope.canvasSize = {
        width: '640',
        height: '480'
    };
    $scope.$watch('canvasSize', function(newValue, oldValue) {
        if (newValue != oldValue) {
            //console.log('value changed: ' + JSON.stringify($scope.square));
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
            var client = new WebSocket('ws://192.168.0.8:8084/');
            var player = new jsmpeg(client, {canvas: canvas});
        }

    };
    init();
});
