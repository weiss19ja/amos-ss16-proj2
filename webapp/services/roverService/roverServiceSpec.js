'use strict';

describe('myApp.roverService service', function() {

  var roverService;
  var $websocket;
  var $websocketBackend;
  var $location;

  beforeEach(module('myApp.roverService'));
  beforeEach(angular.mock.module('ngWebSocket', 'ngWebSocketMock'));

  beforeEach(inject(function (_roverService_,_$websocket_,_$websocketBackend_,_$location_) {
    roverService = _roverService_;
    $websocket = _$websocket_;
    $websocketBackend = _$websocketBackend_;
    $location = _$location_;

    $websocketBackend.mock();
    $websocketBackend.expectConnect('ws://' + $location.host() + ':' + $location.port() + '/rover');
    $websocketBackend.expectSend({data: JSON.stringify({result: true})});

  }));

  it('should responses be empty at begin',function () {
    expect(roverService.responses.length).toBe(0);
  });

  it('should notifications be empty at begin',function () {
    expect(roverService.notifications().length).toBe(0);
  });

  describe('myApp.roverService camera head tests', function() {

    var cameraMoveStep = 1;

    it('should send camera move up json-rpc', function () {
      roverService.cameraMoveUp();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"turnHeadUp","params":['+cameraMoveStep+'],"id":1}');
    });

    it('should send camera move down json-rpc', function () {
      roverService.cameraMoveDown();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"turnHeadDown","params":['+cameraMoveStep+'],"id":1}');;
    });

    it('should send camera move left json-rpc', function () {
      roverService.cameraMoveLeft();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"turnHeadLeft","params":['+cameraMoveStep+'],"id":1}');
    });

    it('should send camera move right json-rpc', function () {
      roverService.cameraMoveRight();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"turnHeadRight","params":['+cameraMoveStep+'],"id":1}');
    });
  });

  describe('myApp.roverService drive tests', function() {
    var desiredSpeed = 500;
    var turnRate = 300;

    it('should send drive forward json-rpc', function () {
      roverService.driveForward();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"driveForward","params":['+desiredSpeed+'],"id":1}');
    });

    it('should send drive backward json-rpc', function () {
      roverService.driveBackward();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"driveBackward","params":['+desiredSpeed+'],"id":1}');
    });

    it('should send turn left json-rpc', function () {
      roverService.turnLeft();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"turnLeft","params":['+turnRate+'],"id":1}');
    });

    it('should send turn right json-rpc', function () {
      roverService.turnRight();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"turnRight","params":['+turnRate+'],"id":1}');
    });

    it('should send stop json-rpc', function () {
      roverService.stop();
      expect(roverService.responses.length).toBe(1);
      var msg = roverService.getLastSendMsg();
      expect(msg).toBe('{"jsonrpc":"2.0","method":"stop","params":"","id":1}');
    });

  });


});