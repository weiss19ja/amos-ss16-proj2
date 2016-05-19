'use strict';

/**
 * Service to communicate with the rover via websockets and JSON-RPC.
 */
angular.module("myApp.roverService",['ngWebSocket'])
.factory("roverService", function ($websocket, $location ) {

  var ws = $websocket('ws://' + $location.host() + ':' + $location.port() + '/rover');
  console.log('create websocket on ws://' + $location.host() + ':' + $location.port() + '/rover');

  var lastId = 0;
  var responses = [];
  var driveStepDuration = 1000; // in ms
  var desiredSpeed = 500;
  var turnAngle = 45;
  var turnRate = 300;
  var cameraMoveStep = 1;
  var lastSendMsg;
  var clientId;
  
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
    lastSendMsg = msg;
	}

  ws.onMessage(function(message) {
      console.log('new Msg:');
      responses.push(JSON.parse(message.data));
      var req = JSON.parse(message.data);
      if (req.method == "setClientId")  {
        setClientId(req.params[0]);
      }
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

  /**
  * Set id of client given by the server.
  */
  function setClientId(id){
    clientId = id;
    console.log("ID of this client is now "+id);
  }

  /**
  * Add new notification to notifications list.
  * Notification object:
  * {
  * message: "notification message",
  * time: "12:03:00",
  * seen: false
  * }
  */
  function addNotification(msg){
    console.log("new notification: "+msg);
  }

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
        getLastSendMsg:function(){
          return lastSendMsg;
        },
        sendPing: function () {
          send("ping",[lastId]);
        },
        /**
         * Stop rover movements
         */
        stop: function () {
          send("stop",[]);
        },
        /**
         * Drive rover forward
         */
        driveForward : function(){
          send("driveForward",[desiredSpeed]);
        },
         /**
          * Drive rover backward
          */
        driveBackward : function(){
          send("driveBackward",[desiredSpeed]);
        },
        /**
         * Turn rover left
         */
        turnLeft : function(){
          send("turnLeft",[turnRate]);
        },
        /**
         * Turn rover right
         */
        turnRight : function(){
          send("turnRight",[turnRate]);
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
