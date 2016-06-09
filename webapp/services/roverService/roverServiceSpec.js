'use strict';

describe('myApp.roverService service', function () {

  var roverService;
  var $websocketBackend;
  var $location;

  beforeEach(module('myApp.roverService'));

  beforeEach(angular.mock.module('ngWebSocket', 'ngWebSocketMock', 'ngMaterial'));

  beforeEach(inject(function (_roverService_, _$websocketBackend_, _$location_) {
    roverService = _roverService_;
    $websocketBackend = _$websocketBackend_;
    $location = _$location_;

    $websocketBackend.mock();
    $websocketBackend.expectConnect('ws://' + $location.host() + ':' + $location.port() + '/rover');
  }));

  function defaultWSResponse() {
    $websocketBackend.expectSend({data: JSON.stringify({result: true})});
  }

  it('should responses be empty at begin', function () {
    expect(roverService.responses.length).toBe(0);
  });

  it('should set client id', function () {
    $websocketBackend.expectSend({data: JSON.stringify({jsonrpc: "2.0", method: "setClientId", params: [1234]})});
    roverService.sendPing();
    var clientId = roverService.getClientId();
    expect(clientId).toBe(1234);
  });

  it('should handle responses', function () {
    defaultWSResponse();
    roverService.sendPing();
    expect(roverService.responses.length).toBe(1);
  });

  it('should handle errors', function () {
    var errorCode = -3260;
    var errorMessage = "Invalid Request";

    $websocketBackend.expectSend({
      data: JSON.stringify({
        jsonrpc: "2.0",
        error: {code: errorCode, message: errorMessage},
        id: 1000
      })
    });
    roverService.sendPing();
    expect(roverService.errors.length).toBe(1);
    var errorResponse = roverService.getLastErrorResponse();
    expect(errorResponse.error.code).toBe(errorCode);
    expect(errorResponse.error.message).toBe(errorMessage);

  });

  describe('myApp.roverService camera head tests', function () {

    var cameraMoveStep = 20;

    beforeEach(function () {
      defaultWSResponse();
    });

    it('should send camera move up json-rpc', function () {
      roverService.cameraMoveUp();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"turnHeadUp","params":[' + cameraMoveStep + '],"id":1}');
    });

    it('should send camera move down json-rpc', function () {
      roverService.cameraMoveDown();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"turnHeadDown","params":[' + cameraMoveStep + '],"id":1}');
    });

    it('should send camera move left json-rpc', function () {
      roverService.cameraMoveLeft();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"turnHeadLeft","params":[' + cameraMoveStep + '],"id":1}');
    });

    it('should send camera move right json-rpc', function () {
      roverService.cameraMoveRight();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"turnHeadRight","params":[' + cameraMoveStep + '],"id":1}');
    });
  });

  describe('myApp.roverService drive tests', function () {
    var desiredSpeed = 500;
    var turnRate = 300;

    beforeEach(function () {
      defaultWSResponse();
    });

    it('should send drive forward json-rpc', function () {
      roverService.driveForward();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"driveForward","params":[' + desiredSpeed + '],"id":1}');
    });

    it('should send drive backward json-rpc', function () {
      roverService.driveBackward();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"driveBackward","params":[' + desiredSpeed + '],"id":1}');
    });

    it('should send turn left json-rpc', function () {
      roverService.turnLeft();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"turnLeft","params":[' + turnRate + '],"id":1}');
    });

    it('should send turn right json-rpc', function () {
      roverService.turnRight();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"turnRight","params":[' + turnRate + '],"id":1}');
    });

    it('should send stop json-rpc', function () {
      roverService.stop();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"stop","params":[],"id":1}');
    });

  });

  describe('myApp.roverService killswitch tests', function () {

    var killswitchEnabled = true;
    var message = "Important notification";

    it('should send setKillswitch json-rpc', function () {
      defaultWSResponse();
      roverService.setKillswitch(killswitchEnabled, message);
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"setKillswitch","params":[' + killswitchEnabled +',"'+message+ '"],"id":1}');
    });

  });

  describe('myApp.roverService single driver tests', function () {

    beforeEach(function () {
      $websocketBackend.expectSend({data: JSON.stringify({jsonrpc: "2.0", method: "setClientId", params: [1234]})});
      roverService.sendPing(); // to trigger the setclientId response
      //defaultWSResponse();
    });

    it('should client id be 1234',function () {
      expect(roverService.getClientId()).toBe(1234);
    });

    it('should send enterDriverMode json-rpc', function () {
      roverService.enterDriverMode();

      setTimeout(function () {
        var msg = roverService.getLastSendMsg();
        expect(msg).toBe('{"jsonrpc":"2.0","method":"enterDriverMode","params":[1234],"id":3}');
        // handled two responses in object {result:true}, setClientId is method call from backend (so no response)
        expect(roverService.responses.length).toBe(2);
      }, 100);
    });


    it('should send exitDriverMode json-rpc', function () {
      defaultWSResponse();
      roverService.exitDriverMode();

      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"exitDriverMode","params":[1234],"id":2}');

      // handled two responses in object {result:true}, setClientId is method call from backend (so no response)
      expect(roverService.responses.length).toBe(1);
    });


    it('should set correct initial rover state ', function () {
      expect(roverService.roverState.isDriverAvailable).toBe(true);
      expect(roverService.roverState.isKillswitchEnabled).toBe(false);
    });

    it('should set driver available if driver is my id', function () {
      $websocketBackend.expectSend({
        data: JSON.stringify({
          jsonrpc: "2.0",
          method: "updateRoverState",
          params: [{"currentDriverId": 1234}]
        })
      });
      roverService.enterDriverMode();
      expect(roverService.roverState.isDriverAvailable).toBe(true);
    });

    it('should set driver available if there is no driver', function () {
      $websocketBackend.expectSend({
        data: JSON.stringify({
          jsonrpc: "2.0",
          method: "updateRoverState",
          params: [{"currentDriverId": -1}]
        })
      });
      roverService.enterDriverMode();
      expect(roverService.roverState.isDriverAvailable).toBe(true);
    });

    it('should set driver not available if there is no driver but Im still on drive page', function () {
      // simulate the drive page
      $location.path('/drive');

      $websocketBackend.expectSend({
        data: JSON.stringify({
          jsonrpc: "2.0",
          method: "updateRoverState",
          params: [{"currentDriverId": -1}]
        })
      });
      defaultWSResponse();
      roverService.sendPing();
      expect(roverService.roverState.isDriverAvailable).toBe(false);
    });

    it('should set driver NOT available if there is another driver', function () {
      $websocketBackend.expectSend({
        data: JSON.stringify({
          jsonrpc: "2.0",
          method: "updateRoverState",
          params: [{"currentDriverId": 5001}]
        })
      });
      roverService.sendPing();
      expect(roverService.roverState.isDriverAvailable).toBe(false);
    });
  });

  describe('myApp.roverService notification tests',function () {

    it('should notifications be empty at begin', function () {
      expect(roverService.notifications.length).toBe(0);
    });

    it('should send alert notification to all clients', function () {
      defaultWSResponse();
      roverService.sendAlertNotification("test alert");
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"distributeAlertNotification","params":["test alert"],"id":1}');
    });

    it('should toast a normal notification',function () {
      $websocketBackend.expectSend({data: JSON.stringify({jsonrpc: "2.0", method: "incomingNotification", params: ["Normal Notification"]})});
      roverService.sendAlertNotification("");
      expect(roverService.notifications.pop()).toBe('Normal Notification');
    });

    it('should toast a alert notification',function () {
      $websocketBackend.expectSend({data: JSON.stringify({jsonrpc: "2.0", method: "showAlertNotification", params: ["Alert Notification"]})});
      roverService.sendAlertNotification("");
      expect(roverService.notifications.pop()).toBe('Alert Notification');
    });

    it('should toast a error notification',function () {
      $websocketBackend.expectSend({data: JSON.stringify({jsonrpc: "2.0", method: "showErrorNotification", params: ["Error Notification"]})});
      roverService.sendAlertNotification("");
      expect(roverService.notifications.pop()).toBe('Error Notification');
    });

  });

});
