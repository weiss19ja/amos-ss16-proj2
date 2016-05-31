'use strict';

/**
 * Service to communicate with the rover via websockets and JSON-RPC.
 */
angular.module("myApp.roverService",['ngWebSocket'])
.factory("roverService", function ($websocket, $location) {

  var wsURL = 'ws://' + $location.host() + ':' + $location.port() + '/rover';
  var ws = $websocket(getWsURL());

  var lastId = 0;
  var responses = [];
  var errorResponses = [];
  var notifications = [];
  var desiredSpeed = 500;
  var turnRate = 300;
  var cameraMoveStep = 20;
  var lastSendMsg;
  var lastErrorResponse;
  var clientId;
  var collisionDetection = {
    frontLeft: false,
    frontRight : false,
    backLeft :false,
    backRight: false
  };
  var image;
  var snapshotCallback;

  /**
   * Get URL for websocket connection depending on used protocol (http or https)
   *
   * At the moment websocket connections over https are not provided by the backend,
   * so this function returns an emtpy string and no websocket connection will be established.
   * Trying to connect to the backend via https will crash the webapp.
   *
   * TODO: Provide https websocket in the backand or prevent crash of the webapp
   *
   * @returns {string} websocket url, if protocol is https returning empty string
   */
  function getWsURL() {
    var port = $location.port();
    var url = "";
    if(port == 443){
      console.log('Can not use websockets with https');
    } else {
      url = wsURL;
    }
    return url;
  }

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

  ws.onError(function(event) {
    console.log('connection Error', event);
  });

  ws.onClose(function(event) {
    console.log('connection closed', event);
  });

  ws.onOpen(function() {
    console.log('connection open to '+wsURL);
  });

  ws.onMessage(function(message) {
    console.log('new Msg:' + message.data);
    var msgData = JSON.parse(message.data);

    if(msgData.method){
      handleMethodCall(msgData);
    } else if (msgData.result){
      handlerResponse(msgData);
    } else if (msgData.error){
      handleError(msgData);
    }
  });

  /**
   * Handles JSON-RPC method calls
   */
  function handleMethodCall(request) {
    switch(request.method) {
      case 'setClientId':
        setClientId(request.params[0]);
        break;
      case 'incomingNotification':
        incomingNotification(request.params[0]);
        break;
      case 'updateCollisionInformation':
        updateCollisionInformation(request.params);
        break;
      case 'incomingSnapshot':
        incomingSnapshot(request.params);
      default:
        console.log('error on handleMethodCall: call function '+request.method+' is not allowed.');
    }
  }

  /**
   * Handles JSON-RPC response
   */
  function handlerResponse(response) {
    responses.push(response);
  }

  /**
   * Handles JSON-RPC error response
   */
  function handleError(error) {
    lastErrorResponse = error;
    errorResponses.push(error);
  }

  /**
  * Set id of client given by the server.
  */
  function setClientId(id){
    clientId = id;
    console.log("ID of this client is now "+id);
  }

  /**
  * Add new notification to notifications list pushed by the server.
  */
  function incomingNotification(msg){
    notifications.push(msg);
    console.log("new notification: "+msg);
    //$mdToast.show($mdToast.simple().textContent(msg).position('top right'));

  }

  /**
   * Update collision detection information by the server.
   */
  function updateCollisionInformation(collisionInfo){
      var collisionState = collisionInfo[0];

      collisionDetection.frontLeft = !!collisionState.frontLeft;
      collisionDetection.frontRight = !!collisionState.frontRight;
      collisionDetection.backLeft = !!collisionState.backLeft;
      collisionDetection.backRight = !!collisionState.backRight;
  }

  /**
   * Receive image data and invoke callback function
   */
   function incomingSnapshot(imageData) {
      snapshotCallback(imageData);
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
        /**
        * Get the id of the client given by the server
        * @return {number} id.
        */
        getClientId: function () {
          return clientId;
        },
        responses: responses,
        notifications: notifications,
        collisions: collisionDetection,
        errors: errorResponses,
        getLastErrorResponse:function () {
          return lastErrorResponse;
        },
        getLastSendMsg : function(){
          return lastSendMsg;
        },
        sendPing : function () {
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
            send("turnHeadUp",[cameraMoveStep]);
        },
        /**
         * Move camera down
         */
        cameraMoveDown : function () {
            send("turnHeadDown",[cameraMoveStep]);
        },
        /**
         * Move camera left
         */
        cameraMoveLeft : function () {
            send("turnHeadLeft",[cameraMoveStep]);
        },
        /**
         * Move camera right
         */
        cameraMoveRight : function () {
            send("turnHeadRight",[cameraMoveStep]);
        },
        cameraResetPosition : function() {
            send("resetHeadPosition", []);
        },
        /**
         * block or unblock the rover movements (depends on variable isBlocked)
         */
        setBlocked: function (isBlocked) {
            send("setBlocked", [isBlocked]);
        },
        /**
         * check whether developer blocked user interaction with rover
         */
        isBlocked: function() {
            send("isBlocked", []);
        },
        /**
         * Request for a snapshot
         */
        getCameraSnapshot: function(callback) {
            snapshotCallback = callback;
            send("getCameraSnapshot", [clientId]);
        }
  };
});
