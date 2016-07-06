/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
'use strict';

/**
 * Service to communicate with the rover via websockets and JSON-RPC.
 */
angular.module("myApp.roverService", ['ngWebSocket', 'ngMaterial'])
  .factory("roverService", function ($websocket, $location, $mdToast, $mdDialog) {

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
    var clientId = 0;
    var clientIdPromise;
    var roverState = {
      isDriverAvailable: false,
      isKillswitchEnabled: false,
      maxSpeedValue: 100
    };

    var collisionInformation = {
      taintedReadings: false,
      collisionFrontLeft: "None",
      collisionFrontRight: "None",
      collisionBackLeft: "None",
      collisionBackRight: "None"
    };
    var snapshotCallback;
    var logEntriesCallback;
    var systemUpTimeCallback;
    var connectedUsers = {
      list: []
    };
    var blockedUsers = {
      list: []
    };
    var myIp = {
      ipAddress: "",
      isBlocked: false
    };
    var clientJs = new ClientJS();
    var hasConnection = false;

    if($location.port() == 8000){
      developerMode();
    }

    /**
     * Developer mode function was called if webapp runs on the developer port 8000.
     */
    function developerMode() {
      console.log('developer mode enabled');
      roverState.isDriverAvailable = true;
    }

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
      if (port == 443) {
        console.log('Can not use websockets with https');
      } else {
        url = wsURL;
      }
      return url;
    }

    /**
     * Generate JSON-RPC 2.0 Request object
     * @param method
     * @param params
     * @returns JSON-RPC 2.0 Request object
     */
    function generateRpcRequest(method, params) {
      return {
        "jsonrpc": "2.0",
        "method": method,
        "params": params,
        "id": ++lastId
      };
    }

    /**
     * Generate JSON-RPC 2.0 Notification object
     * @param method
     * @param params
     * @returns JSON-RPC 2.0 Notification object
     */
    function generateRpcNotification(method, params) {
      return {
        "jsonrpc": "2.0",
        "method": method,
        "params": params
      };
    }

    /**
     * Send a JSON-RPC 2.0 Request.
     * Backend responses to each request with a JSON-RPC 2.0 Response/Error.
     * @param method
     * @param params as array
     */
    function send(method, params) {
      var msg = JSON.stringify(generateRpcRequest(method, params));
      console.log(msg);
      ws.send(msg);
      lastSendMsg = msg;
    }

    /**
     * Send a JSON-RPC 2.0 Notification.
     * A RPC-Notification is a request without a id.
     * The Server MUST NOT reply to a Notification.
     * @param method
     * @param params as array
     */
    function sendWithoutResponse(method, params) {
      var msg = JSON.stringify(generateRpcNotification(method, params));
      console.log(msg);
      ws.send(msg);
      lastSendMsg = msg;
    }

    /**
     * Send information about the client to the backend
     * so that developer has information about the users
     */
    function sendClientInformation() {
      var userBrowser = clientJs.getBrowser();
      var operatingSystem = clientJs.getOS();
      send("setClientInformation", [clientId, userBrowser.toString(), operatingSystem.toString()]);
    }

    ws.onError(function (event) {
      console.log('connection Error', event);
    });

    ws.onClose(function (event) {
      console.log('connection closed', event);
      hasConnection = false;
    });

    ws.onOpen(function () {
      console.log('connection open to ' + wsURL);
      hasConnection = true;
    });

    ws.onMessage(function (message) {
      var msgData = JSON.parse(message.data);
      if (msgData.method && msgData.method === "incomingSnapshot") {
        console.log('new Msg: Image received');
      } else if (msgData.method && msgData.method === "incomingLogEntries") {
        console.log('new Msg: Log entries received');
      } else {
        console.log('new Msg:' + message.data);
      }

      if (msgData.method) {
        handleMethodCall(msgData);
      } else if (msgData.result) {
        handlerResponse(msgData);
      } else if (msgData.error) {
        handleError(msgData);
      }
    });

    function getConnectionStatus () {
      return hasConnection;
    };

    /**
     * Handles JSON-RPC method calls
     */
    function handleMethodCall(request) {
      switch (request.method) {
        case 'setClientId':
          setClientId(request.params[0]);
          break;
        case 'incomingNotification':
          incomingNotification(request.params[0]);
          break;
        case 'updateCollisionInformation':
          updateCollisionInformation(request.params);
          break;
          case 'updateConnectedUsers':
              updateConnectedUsers(request.params[0],request.params[1]);
              break;
        case 'incomingSnapshot':
          incomingSnapshot(request.params);
          break;
        case 'showAlertNotification':
          showAlertNotification(request.params[0]);
          break;
        case 'showErrorNotification':
          showErrorNotification(request.params[0]);
          break;
        case 'updateRoverState':
          updateRoverState(request.params[0]);
          break;
        case 'setMyBlockingState':
          setMyBlockingState(request.params[0], request.params[1]);
          break;
        case 'incomingLogEntries':
          incomingLogEntries(request.params);
          break;
        case 'incomingSystemUpTime':
          incomingSystemUpTime(request.params[0]);
        default:
          console.log('error on handleMethodCall: call function ' + request.method + ' is not allowed.');
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
    function setClientId(id) {
      clientId = id;
      console.log("ID of this client is now " + id);
    }

    /**
     * Promise for the clientId
     *    gets resolved as soon as clientId is set by the backend
     *    gets rejected when clientId is not set by backend within 3 seconds
     */
    clientIdPromise = new Promise(function (resolve, reject) {

      // wait max 3 second for clientID
      var maxTimeout = setTimeout(function () {
        reject(clientId);
        console.log('ClientId rejected: ' + clientId);
      }, 3000);

      // check clientId cyclic every 100 ms
      var checkClientIdInterval = setInterval(checkClientId, 100);

      function checkClientId() {
        if (clientId != 0) {
          clearInterval(checkClientIdInterval);
          clearTimeout(maxTimeout);
          resolve(clientId);
          console.log('ClientId resolved: ' + clientId);
        }
      };
    });


    /**
     * This is the callback that signals the clientId has been set correctly and is now available
     *    - sendClientInformation() as soon as clientId is available
     *    - further method calls
     */
    clientIdPromise.then(function (fulfilledClientId) {
      sendClientInformation();
      // further method calls
    }, function (rejectedClientId) {
      // do something here when we got no clientId from the backend
    });


    /**
     * Add new notification to notifications list pushed by the server.
     */
    function incomingNotification(msg) {
      notifications.push(msg);
      console.log("new notification: " + msg);
      $mdToast.show($mdToast.simple().textContent(msg).position('top right').hideDelay(4000));
    }

    /**
     * Show a error notification as toast
     * @param msg text message
     */
    function showAlertNotification(msg) {
      notifications.push(msg);
      console.log("show error notification: " + msg);
      $mdToast.show($mdToast.simple().textContent(msg).position('top right').theme('alert-toast').hideDelay(4000));
    }

    /**
     * Show error notification as toast
     * @param msg text message
     */
    function showErrorNotification(msg) {
      notifications.push(msg);
      console.log("show error notification: " + msg);
      $mdToast.show($mdToast.simple().textContent(msg).position('top right').theme('error-toast').hideDelay(4000));
    }

      /**
       * Update collision detection information by the server.
       */
      function updateCollisionInformation(param) {
        var collisionState = param[0];

        collisionInformation.taintedReadings = collisionState.taintedReadings;
        collisionInformation.collisionFrontLeft = collisionState.collisionFrontLeft;
        collisionInformation.collisionFrontRight = collisionState.collisionFrontRight;
        collisionInformation.collisionBackRight = collisionState.collisionBackRight;
        collisionInformation.collisionBackLeft = collisionState.collisionBackLeft;
      }

    /**
     * Update rover state
     *    -> isDriverMode available
     *    -> isKillswitch enabled
     */
    function updateRoverState(receivedRoverState) {
      if (receivedRoverState.currentDriverId) {
        setDriverAvailable(receivedRoverState.currentDriverId);
      }

      if (typeof receivedRoverState.isKillswitchEnabled !== 'undefined') {
        roverState.isKillswitchEnabled = receivedRoverState.isKillswitchEnabled;
      }

      if (receivedRoverState.maxSpeedValue) {
        roverState.maxSpeedValue = receivedRoverState.maxSpeedValue;
      }
    }

    /**
     * Set availability of driver mode.
     * when currentDriverId is my clientId, Im the driver
     * when currentDriverId is -1 nobody is driver at the moment
     * when currentDriverId is differnet to mine, driver mode is unavailable to me
     */
    function setDriverAvailable(currentDriverId) {
      if (currentDriverId == clientId) {
        // im the driver
        roverState.isDriverAvailable = true;
        console.log('driver mode is available');
      } else if (currentDriverId == -1) {
        // nobody is driver, when im already on driver page i must reaquire the driver mode
        if ($location.path().indexOf('/drive') > -1 ) {
          roverState.isDriverAvailable = false;
          if(!myIp.isBlocked){
            send('enterDriverMode', [clientId]);
          }
        }
      } else {
        // somebody else is driver at the moment
        roverState.isDriverAvailable = false;
        console.log('driver mode not available, because client with id ' + currentDriverId + ' is in driver mode.');
      }
    }

    /**
     * Update connected users
     */
    function updateConnectedUsers(connectedList,blockedList) {
        connectedUsers.list = connectedList;
        blockedUsers.list = blockedList;
    }

    /**
     * Receive image data and invoke callback function
     */
    function incomingSnapshot(imageData) {
      snapshotCallback(imageData);
    }
    /**
     * setBlocking State and display message to client if he got blocked
     * @param blockingState
     */
    function setMyBlockingState(ipAddress, blockingState){
        myIp.ipAddress = ipAddress;
        if(!(blockingState == myIp.isBlocked)){
            console.log("changed blocking state");
            if(blockingState == true){
                var msg = 'A developer blocked you, no further interaction with the rover possible';
                $mdDialog.show({
                    clickOutsideToClose: false,
                    template: '<md-dialog aria-label="Blocked"  ng-cloak>' +
                    '<md-toolbar>'+
                    '<div class="md-toolbar-tools">'+
                    '<h2>Blocked</h2>'+
                    '<span flex></span>'+
                    '</div>'+
                    '</md-toolbar>'+
                    '<md-dialog-content>' +
                    '<div class="md-dialog-content">'+
                    '<p>'+ msg +'<\p>'+
                    '<\div>'+
                    '</md-dialog-content>' +
                    '</md-dialog>'
                });

            }
            else{
                $mdDialog.hide();
            }
          }
          myIp.isBlocked = blockingState;
      }
    /**
     * Receive response for log entries request and invoke callback function
     */
    function incomingLogEntries(params) {
      logEntriesCallback(params);
    }

    function incomingSystemUpTime(param) {
      systemUpTimeCallback(param);
    }

    return {
      /**
       * Get the state of the websocket connection.
       * @return {number} 0 = CONNECTING, 1 = OPEN, 2 = CLOSING, 3 = CLOSED.
       */
      readyState: function () {
        return ws.readyState;
      },
      /**
       * Check for driver mode is available.
       * @return {boolean} true if driver mode is available.
       */
      isdriverModeAvailable: function () {
        return true;
      },
      /**
       * Get the id of the client given by the server
       * @return {number} id.
       */
      getClientIdPromise: function () {
        return clientIdPromise;
      },
      responses: responses,
      notifications: notifications,
      roverState: roverState,
      collisions: collisionInformation,
      connectedUsers: connectedUsers,
      blockedUsers: blockedUsers,
      clientJs: clientJs,
      errors: errorResponses,
      myIp: myIp,
      hasConnection: getConnectionStatus,
      getLastErrorResponse: function () {
        return lastErrorResponse;
      },
      getLastSendMsg: function () {
        return lastSendMsg;
      },
      sendPing: function () {
        send("ping", [lastId]);
      },
      /**
       * Stop rover movements
       */
      stop: function () {
        sendWithoutResponse("stop", []);
      },
      /**
       * Drive rover forward
       */
      driveForward: function () {
        sendWithoutResponse("driveForward", [desiredSpeed]);
      },
      /**
       * Drive rover backward
       */
      driveBackward: function () {
        sendWithoutResponse("driveBackward", [desiredSpeed]);
      },
      /**
       * Turn rover left
       */
      turnLeft: function () {
        sendWithoutResponse("turnLeft", [turnRate]);
      },
      /**
       * Turn rover right
       */
      turnRight: function () {
        sendWithoutResponse("turnRight", [turnRate]);
      },
      /**
       * Continuously driving 
       * @param angle in degree
       * @param speed [0..100]
       */
      driveContinuously: function (angle,speed) {
        sendWithoutResponse('driveContinuously',[angle,speed]);
      },
      /**
       * Move camera up
       */
      cameraMoveUp: function () {
        send("turnHeadUp", [cameraMoveStep]);
      },
      /**
       * Move camera down
       */
      cameraMoveDown: function () {
        send("turnHeadDown", [cameraMoveStep]);
      },
      /**
       * Move camera left
       */
      cameraMoveLeft: function () {
        send("turnHeadLeft", [cameraMoveStep]);
      },
      /**
       * Move camera right
       */
      cameraMoveRight: function () {
        send("turnHeadRight", [cameraMoveStep]);
      },
      cameraResetPosition: function () {
        send("resetHeadPosition", []);
      },
      /**
       * block or unblock the rover movements (depends on variable isBlocked)
       */
      setKillswitch: function (killswitchEnabled, notificationMessage) {
        send("setKillswitch", [killswitchEnabled, notificationMessage]);
      },
      /**
       * check whether developer blocked user interaction with rover
       */
      getKillswitchState: function () {
        send("sendKillswitchState", []);
      },
      /**
       * send the maximum speed value to the server.
       * @param speedValue percentage between 0 and 100 (inclusive)
       */
      setMaxSpeedValue: function(speedValue) {
        send("setMaxSpeedValue", [speedValue]);
      },
      /**
       * request maximum speed value that was set on the server
       */
      getMaxSpeedValue: function() {
        send("sendMaxSpeedValue", []);
      },
      /**
       * block all interactions with the rover from this ipAddress
       */
      blockIp: function (ipAddress) {
          send("blockIp", [ipAddress]);
      },
      /**
       * allow all interactions with the rover from this ipAddress
       */
      unblockIp: function (ipAddress) {
          send("unblockIp", [ipAddress]);
      },
      /**
       * releases the current driver
       */
      releaseDriver: function(){
        send("releaseDriver", []);
      },
      /**
       * show an alert notification to the user
       */
      showAlertNotification: function (msg) {
          showAlertNotification(msg);
      },
      /**
       * Request for a snapshot
       */
      getCameraSnapshot: function (callback) {
        snapshotCallback = callback;
        send("getCameraSnapshot", [clientId]);
      },
      /**
       * Request log file entries which are newer than the lastLogEntry parameter.
       * If lastLogEntry is null or empty the backend will send all log file entries.
       */
      getLoggingEntries: function (lastLogEntry, callback) {
        if (clientId) {
          logEntriesCallback = callback;
          console.log(lastLogEntry);
          send("getLoggingEntries", [clientId, lastLogEntry]);
        } else {
          showErrorNotification("Could not fetch logging entries because connecting to the rover is still in progress.")
        }
      },
      /**
       * Requests the rovers system uptime
       */
      getSystemUpTime: function (callback) {
        if (clientId) {
          systemUpTimeCallback = callback;
          send("getSystemUpTime", [clientId]);
        } else {
          showErrorNotification("Could not fetch systems uptime because connecting to the rover is still in progress.")
        }
      },
      /**
       * Send a alert notification to backend which will
       * it distribute to all users
       */
      sendAlertNotification: function (alertMsg) {
        send("distributeAlertNotification", [alertMsg]);
      },
      /**
       * Enter driver mode --> there can only be one driver at time
       * needs the clientId to register driver, uses a promise object to wait for the clientId being set by the backend
       */
      enterDriverMode: function () {
        clientIdPromise.then(function (fulfilledClientId) {
          send("enterDriverMode", [fulfilledClientId]);
        }, function (rejectedClientId) {
          console.log('do not enter driver mode because clientId promise was rejected');
        });

        return clientIdPromise;
      },

      /**
       * Exit driver mode --> backend should notify all clients that driver mode is now available
       */
      exitDriverMode: function () {
        send("exitDriverMode", [clientId]);
      }
    };
  })
;
