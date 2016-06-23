/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
'use strict';

describe('myApp.logs module', function() {
  var logsCtrl, scope, service;

  beforeEach(module('myApp.logs'));
  beforeEach(module('myApp.roverService'));
  beforeEach(inject(function($controller, $rootScope, roverService) {
    scope = $rootScope.$new();
    service = roverService;
    logsCtrl = $controller('LogsCtrl', {$scope: scope, RoverService: service});
  }));

  describe('logs controller', function(){

    it('should ....', inject(function($controller) {
      //spec body
      expect(logsCtrl).toBeDefined();
    }));

  });
});