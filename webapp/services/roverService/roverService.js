'use strict';

/**
 * Service to communicate with the rover via websockets and JSON-RPC.
 */
angular.module("myApp.roverService", ['ngWebSocket', 'ngMaterial'])
  .factory("roverService", function ($websocket, $location, $mdToast) {

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
    var roverState = {
      isDriverAvailable: true,
      isKillswitchEnabled: false
    }
    var killswitch = {
      enabled: false
    };
    var collisionDetection = {
      frontLeft: false,
      frontRight: false,
      backLeft: false,
      backRight: false
    };
    var snapshotCallback;
    var connectedUsers = {
        list: ['no connected user']
    }
    var blockedUsers = ['evilAttacker'];
    var clientJs = new ClientJS();


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

    function generateMessage(method, params) {
      return {
        "jsonrpc": "2.0",
        "method": method,
        "params": params,
        "id": ++lastId
      };
    }

    function send(method, params) {
      console.log("send JRPC");
      var msg = JSON.stringify(generateMessage(method, params));
      console.log(msg);
      ws.send(msg);
      lastSendMsg = msg;
    }
    /**
     * Send information about the client to the backend
     * so that developer has information about the users
     */
    function sendClientInformation(){
        var fingerprint = clientJs.getFingerprint();
        var userBrowser = clientJs.getBrowser();
        var operatingSystem = clientJs.getOS();
        send("setClientInformation", [clientId, fingerprint.toString(), userBrowser.toString(), operatingSystem.toString()]);
    }

    ws.onError(function (event) {
      console.log('connection Error', event);
    });

    ws.onClose(function (event) {
      console.log('connection closed', event);
    });

    ws.onOpen(function () {
      console.log('connection open to ' + wsURL);
    });

    ws.onMessage(function (message) {
      var msgData = JSON.parse(message.data);
      if (msgData.method && msgData.method === "incomingSnapshot") {
        console.log('new Msg: Image received');
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

    /**
     * Handles JSON-RPC method calls
     */
    function handleMethodCall(request) {
      switch (request.method) {
        case 'setClientId':
          setClientId(request.params[0]);
          break;
        case 'incomingNotification':
          showNormalNotification(request.params[0]);
          break;
        case 'updateKillswitchEnabled':
          updateKillswitchEnabled(request.params[0]);
          break;
        case 'updateCollisionInformation':
          updateCollisionInformation(request.params);
          break;
        case 'updateConnectedUsers':
          updateConnectedUsers(request.params[0]);
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
      sendClientInformation();
    }

    /**
     * Add new notification to notifications list pushed by the server.
     */
    function showNormalNotification(msg) {
      notifications.push(msg);
      console.log("show normal notification: " + msg);
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

    function updateKillswitchEnabled(state) {
      killswitch.enabled = state;
      console.log("Updating Killswitch state received from server to: " + state);
    }

    /**
     * Update collision detection information by the server.
     */
    function updateCollisionInformation(collisionInfo) {
      var collisionState = collisionInfo[0];

      collisionDetection.frontLeft = !!collisionState.frontLeft;
      collisionDetection.frontRight = !!collisionState.frontRight;
      collisionDetection.backLeft = !!collisionState.backLeft;
      collisionDetection.backRight = !!collisionState.backRight;
    }

    /**
     * Update rover state
     *    -> isDriverMode available
     *    -> isKillswitch enabled
     */
    function updateRoverState(receivedRoverState) {

      /**
       *    currentDriverId: when currentDriverId is my clientId, Im the driver
       *                     when currentDriverId is -1 nobody is driver at the moment
       *                     when currentDriverId is differnet to mine, driver mode is unavailable to me
       */
      if (receivedRoverState.currentDriverId) {

        // im the driver
        if (receivedRoverState.currentDriverId == clientId) {
          roverState.isDriverAvailable = true;
          console.log('driver is available');
        }
        // nobody is driver, when im already on driver page i must reaquire the driver mode
        if (receivedRoverState.currentDriverId == -1) {
          if ($location.path() == '/drive') {
            roverState.isDriverAvailable = false;
            send("enterDriverMode", [clientId]);
          } else {
            roverState.isDriverAvailable = true;
            console.log('driver available');
          }
        }

        // somebody else is driver at the moment
        if (receivedRoverState.currentDriverId != clientId && receivedRoverState.currentDriverId != -1) {
          roverState.isDriverAvailable = false;
          console.log('driver not available');
        }
      }

      if (receivedRoverState.isKillswitchEnabled) {
        roverState.isKillswitchEnabled = receivedRoverState.isKillswitchEnabled;
      }
      console.log(roverState);
    }
    /**
       * Update connected users
       */
    function updateConnectedUsers(userList) {
        connectedUsers.list = userList;
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
      getClientId: function () {
        return clientId;
      },
      responses: responses,
      notifications: notifications,
      killswitch: killswitch,
      roverState: roverState,
      collisions: collisionDetection,
      connectedUsers: connectedUsers,
      blockedUsers: blockedUsers,
      errors: errorResponses,
      clientJs: clientJs,
      showNormalNotification: showNormalNotification,
      showAlertNotification : showAlertNotification,
      showErrorNotification: showErrorNotification,
      sendClientInformation: sendClientInformation,
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
        send("stop", []);
      },
      /**
       * Drive rover forward
       */
      driveForward: function () {
        send("driveForward", [desiredSpeed]);
      },
      /**
       * Drive rover backward
       */
      driveBackward: function () {
        send("driveBackward", [desiredSpeed]);
      },
      /**
       * Turn rover left
       */
      turnLeft: function () {
        send("turnLeft", [turnRate]);
      },
      /**
       * Turn rover right
       */
      turnRight: function () {
        send("turnRight", [turnRate]);
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
       * Request for a snapshot
       */
      getCameraSnapshot: function (callback) {
        snapshotCallback = callback;
        send("getCameraSnapshot", [clientId]);
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

        //TODO: MFischer should driver mode be available when promise is rejected?

        var clientIdPromise = new Promise(function (resolve, reject) {

          // wait max 5 second for clientID
          var maxTimeout = setTimeout(function () {
            reject(clientId);
          }, 1000);

          // check clientId cyclic every 100 ms
          var checkClientIdInterval = setInterval(checkClientId, 100);

          function checkClientId() {
            if (clientId != 0) {
              clearInterval(checkClientIdInterval);
              clearTimeout(maxTimeout);
              resolve(clientId);
            }
          };
        });

        clientIdPromise.then(function (fulfilledClientId) {
          send("enterDriverMode", [fulfilledClientId]);
          console.log('enterDriverMode promise fulfilled: ' + fulfilledClientId);
        }, function (rejectedClientId) {
          console.log('enterDriverMode promise rejected ' + rejectedClientId);
        });

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
