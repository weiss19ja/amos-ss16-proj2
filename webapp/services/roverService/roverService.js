'use strict';

/**
 * Service to communicate with the rover via websockets and JSON-RPC.
 */
angular.module("myApp.roverService",['ngWebSocket'])
.factory("roverService", function ($websocket, $location ) {

  var ws = $websocket('ws://localhost:' + $location.port() + '/rover');

  var lastId = 0;
  var responses = [];
  var driveStepDuration = 1000; // in ms
  var turnAngle = 45;
  var cameraMoveStep = 1;
  
  function generateMessage(method,params){
    return {
        "jsonrpc":"2.0",
        "method":method,
        "params":params,
        "id": ++lastId
        };
  }

	function send(method,params){
    console.log("send JRPC");
    var msg = JSON.stringify(generateMessage(method,params));
    console.log(msg);
    ws.send(msg);
	}

  ws.onMessage(function(message) {
      console.log('new Msg:');
      console.log(message);
      responses.push(JSON.parse(message.data));
  });

  ws.onError(function(event) {
    console.log('connection Error', event);
  });

  ws.onClose(function(event) {
    console.log('connection closed', event);
  });

  ws.onOpen(function() {
    console.log('connection open');
  });

  return {
        /**
        * Get the state of the websocket connection.
        * @return {number} 0 = CONNECTING, 1 = OPEN, 2 = CLOSING, 3 = CLOSED.
        */
        readyState: function(){
          return ws.readyState;
        },
        /**
         * Check for driver mode is available.
         * @return {boolean} true if driver mode is available.
         */
        isdriverModeAvailable: function(){
          return true;
        },
        responses: responses,
        notifications: function(){
          return [];
        },
        error: function(){
          return {};
        },
        sendPing: function () {
          send("ping",[lastId]);
        },
        /**
         * Stop rover movements
         */
        stop: function () {
          send("stop","");
        },
        /**
         * Drive rover forward
         */
        driveForward : function(){
          send("driveForward",driveStepDuration);
        },
         /**
          * Drive rover backward
          */
        driveBackward : function(){
          send("driveBackward",driveStepDuration);
        },
        /**
         * Turn rover left
         */
        turnLeft : function(){
          send("turnLeft",turnAngle);
        },
        /**
         * Turn rover right
         */
        turnRight : function(){
          send("turnRight",turnAngle);
        },
        /**
         * Move camera up
         */
        cameraMoveUp : function () {
            send("cameraMoveUp",cameraMoveStep);
        },
        /**
         * Move camera down
         */
        cameraMoveDown : function () {
            send("cameraMoveDown",cameraMoveStep);
        },
        /**
         * Move camera left
         */
        cameraMoveLeft : function () {
            send("cameraMoveLeft",cameraMoveStep);
        },
        /**
         * Move camera right
         */
        cameraMoveRight : function () {
            send("cameraMoveRight",cameraMoveStep);
        }

      };
});
