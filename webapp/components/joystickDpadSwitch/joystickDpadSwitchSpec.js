/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
'use strict';

describe('myApp.joystickDpadSwitch module', function() {
  var $compile, $rootScope, scope, $controller, element,controller;

  beforeEach(angular.mock.module('templates'));
  beforeEach(module('myApp.joystickDpadSwitch'));
  beforeEach(module('myApp.joystickService'));
  beforeEach(module('myApp.roverService'));

  /**
   * General setup
   */
  beforeEach(inject(function(_$compile_, _$rootScope_, _$controller_) {
    $compile = _$compile_;
    $rootScope = _$rootScope_;
    $controller = _$controller_;
    element = $compile('<joystick-dpad-switch></joystick-dpad-switch>')($rootScope);
    $rootScope.$digest();
    controller = element.controller("joystick-dpad-switch");
    scope = element.isolateScope() || element.scope();
  }));

  it('should exposes Joystick state', function() {
    expect(scope).toBeDefined();
    console.log(scope);
    expect(scope.joystickState).toBeDefined();
  });

});