'use strict';

describe('myApp.joystick module', function() {

  var $compile, $rootScope, scope, $controller, element,controller, roverService, template;

  beforeEach(angular.mock.module('templates'));
  beforeEach(module('myApp.roverService'));
  beforeEach(module('myApp.joystick'));

  /**
   * General setup
   */
  beforeEach(inject(function(_$compile_, _$rootScope_,$httpBackend, _$controller_, _roverService_) {
    $compile = _$compile_;
    $rootScope = _$rootScope_;
    roverService = _roverService_;
    $controller = _$controller_;

    element = $compile('<joystick test="true"></joystick>')($rootScope);
    $rootScope.$digest();
    controller = element.controller("joystick");
    scope = element.isolateScope() || element.scope();
/*
    scope = $rootScope.$new();
    element = angular.element('<joystick test="true"></joystick>');
    template = $compile(element)(scope);
    scope.$digest();
    controller = element.controller('joystick');
    */
    

  }));

  it('should send drive forward request on up',function () {
    scope = element.isolateScope() || element.scope();
    scope.up();
    var shouldMsg = '{"jsonrpc":"2.0","method":"driveForward","params":[500],"id":1}';
    var lastMsg = roverService.getLastSendMsg();
    expect(lastMsg).toBe(shouldMsg);
  });

  it('should send drive backward request on down',function () {
    scope.down();
    var shouldMsg = '{"jsonrpc":"2.0","method":"driveBackward","params":[500],"id":1}';
    var lastMsg = roverService.getLastSendMsg();
    expect(lastMsg).toBe(shouldMsg);
  });

  it('should send turn left request on left',function () {
    scope.left();
    var shouldMsg = '{"jsonrpc":"2.0","method":"turnLeft","params":[300],"id":1}';
    var lastMsg = roverService.getLastSendMsg();
    expect(lastMsg).toBe(shouldMsg);
  });

  it('should send turn right request on right',function () {
    scope.right();
    var shouldMsg = '{"jsonrpc":"2.0","method":"turnRight","params":[300],"id":1}';
    var lastMsg = roverService.getLastSendMsg();
    expect(lastMsg).toBe(shouldMsg);
  });

  it('should send stop request on buttonClick',function () {
    scope.stop();
    var shouldMsg = '{"jsonrpc":"2.0","method":"stop","params":[],"id":1}';
    var lastMsg = roverService.getLastSendMsg();
    expect(lastMsg).toBe(shouldMsg);
  });

});