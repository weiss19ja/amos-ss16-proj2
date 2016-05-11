'use strict';

angular.module('myApp.roverObserve', ['ngRoute', 'ngWebSocket'])
    .factory('streamDrawer', function() {
        var streamDrawer = {
            /**
             * resize the canvas
             */
            updateSize: function(maxHeight, maxWidth) {
                var ratio = 640/480;
                var newWidth = maxHeight * ratio;
                var newHeight = maxWidth / ratio;
                var resizeWidth;
                var resizeHeight;
                if(newWidth > maxWidth){
                    console.log("newWidth too big");
                    resizeWidth = maxWidth;
                    resizeHeight = maxWidth / ratio;
                }
                else if(newHeight > maxHeight){
                    console.log("newHeight too big");
                    resizeWidth = maxHeight * ratio;
                    resizeHeight = maxHeight;
                }
                var canvas = document.getElementById("canvas");
                if (canvas.getContext) {
                    console.log("Change canvas size to: " + resizeHeight + "x" + resizeWidth);
                    var ctx = canvas.getContext("2d");
                    canvas.style.width  = resizeWidth+'px';
                    canvas.style.height = resizeHeight+'px';
                }
            }
        };
        return streamDrawer;
    })
.controller('RoverObserveCtrl', function($scope, $location, $window, streamDrawer) {
    $scope.streamDrawer = streamDrawer;
    $scope.canvasSize = {
        width: '640',
        height: '480',
    };
    // Resize canvas if Window changes
    var window = angular.element($window);
    window.bind('resize', function () {
        //console.log('resize');
        streamDrawer.updateSize(document.getElementById("canvascontainer").clientHeight, document.getElementById("canvascontainer").clientWidth);
    });

    var init = function(){
        var canvas = document.getElementById("canvas");
        if (canvas.getContext) {
            console.log("init canvas");
            var ctx = canvas.getContext("2d");
            // set initial canvas size to 640 x 480 px
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

            // Initial canvas-resize
            streamDrawer.updateSize(document.getElementById("canvascontainer").clientHeight, document.getElementById("canvascontainer").clientWidth);

        }
    };
    init();

});
