/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
'use strict';

describe('myApp.driverCard module', function() {
  var $compile, $rootScope, scope, $controller, element,controller;

  beforeEach(angular.mock.module('templates'));
  beforeEach(module('myApp.driverCard'));
  beforeEach(module('myApp.joystickService'));
  beforeEach(module('myApp.roverService'));
  beforeEach(module('myApp.joystick'));

  /**
   * General setup
   */
  beforeEach(inject(function(_$compile_, _$rootScope_, _$controller_) {
    $compile = _$compile_;
    $rootScope = _$rootScope_;
    $controller = _$controller_;
    element = $compile('<drivercard></drivercard>')($rootScope);
    $rootScope.$digest();
    controller = element.controller("drivercard");
    scope = element.isolateScope() || element.scope();
  }));

  it('should exposes Joystick state', function() {
    expect(scope).toBeDefined();
    console.log(scope);
    expect(scope.joystickState).toBeDefined();
  });
  
});