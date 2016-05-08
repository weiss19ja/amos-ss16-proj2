'use strict';

angular.module('myApp.camera', ['ngRoute', 'ngWebSocket'])
    .factory('PingExample', function($websocket, $location) {
        var dataStream = $websocket('ws://' + $location.host() + ':' + $location.port() + '/camera');
        var replyCollection = [];
        var sqn = 0;

        dataStream.onMessage(function(message) {
            replyCollection.push(JSON.parse(message.data));
        });

        var methods = {
            replyCollection: replyCollection,
            ping: function(param) {
                dataStream.send(JSON.stringify({
                    "jsonrpc":  "2.0",
                    "method": "ping",
                    "params": [param],
                    "id": sqn++
                }))
            }
        };

        return methods;
    })
    .controller('CameraCtrl', function($scope, PingExample) {
        $scope.PingExample = PingExample;
        $scope.callparameter = 0;
        $scope.replies = PingExample.replyCollection;

        $scope.ping = function() {
            console.log("ping!");
            PingExample.ping(this.callparameter);
            this.callparameter++;
        }
    });
