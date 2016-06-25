/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
'use strict';

describe('myApp.roverDrive module', function () {

  beforeEach(module('myApp.roverDrive'));
  beforeEach(module('myApp.roverService'));

  describe('should define roverDriveController', function () {
    var scope, roverDriveCtrl, $location, roverService;
    var asyncExampleValue;

    beforeEach(inject(function ($rootScope, $controller, _$location_, _roverService_) {
      scope = $rootScope.$new();
      roverDriveCtrl = $controller('RoverDriveCtrl', {$scope: scope});
      $location = _$location_;
      roverService = _roverService_;
    }));

    it('should define the Developer Controller', function () {
      expect(roverDriveCtrl).toBeDefined();
    });

    beforeEach(function (done) {
      setTimeout(function () {
        asyncExampleValue = -5;
        done();
      }, 1);
    });

    it("should support async execution of test preparation and expectations", function (done) {
      asyncExampleValue++;
      expect(asyncExampleValue).toBe(-4);
      done();
    });


  });
});
