/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
'use strict';

describe('myApp.joystickService service', function () {
  var roverService, joystickService;

  beforeEach(module('myApp.roverService'));
  beforeEach(module('myApp.joystickService'));

  beforeEach(inject(function (_roverService_, _joystickService_) {
    roverService = _roverService_;
    joystickService = _joystickService_;
  }));

  it('should send stop rpc request', function () {
    joystickService.stop();
    var msg = roverService.getLastSendMsg();
    expect(msg).toBe('{"jsonrpc":"2.0","method":"stop","params":[]}');
  });

  it('should send move rpc request', function () {
    joystickService.move(0,0);
    var msg = roverService.getLastSendMsg();
    expect(msg).toBe('{"jsonrpc":"2.0","method":"driveContinuously","params":[0,0]}');

    joystickService.move(0,100);
    var msg = roverService.getLastSendMsg();
    expect(msg).toBe('{"jsonrpc":"2.0","method":"driveContinuously","params":[0,100]}');

    joystickService.move(90,100);
    var msg = roverService.getLastSendMsg();
    expect(msg).toBe('{"jsonrpc":"2.0","method":"driveContinuously","params":[90,100]}');

    joystickService.move(90,50);
    var msg = roverService.getLastSendMsg();
    expect(msg).toBe('{"jsonrpc":"2.0","method":"driveContinuously","params":[90,50]}');

    joystickService.move(180,100);
    var msg = roverService.getLastSendMsg();
    expect(msg).toBe('{"jsonrpc":"2.0","method":"driveContinuously","params":[180,100]}');

    joystickService.move(242,12);
    var msg = roverService.getLastSendMsg();
    expect(msg).toBe('{"jsonrpc":"2.0","method":"driveContinuously","params":[242,12]}');
  });

});