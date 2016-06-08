'use strict';

describe('myApp.roverDrive module', function () {

  beforeEach(module('myApp.roverDrive'));
  beforeEach(module('myApp.roverService'));

  describe('should define roverDriveController', function () {
    var scope, roverDriveCtrl;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
      scope = $rootScope.$new();
      roverDriveCtrl = $controller('RoverDriveCtrl', {$scope: scope});

    }));

    it('should define the Developer Controller', function () {
      expect(roverDriveCtrl).toBeDefined();
    });

    // test routeView on Desktop devices
    // call of enter and exit driver mode



  });
});
