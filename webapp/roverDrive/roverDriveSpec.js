'use strict';

describe('myApp.roverDrive module', function () {

  beforeEach(module('myApp.roverDrive'));
  beforeEach(module('myApp.roverService'));

  describe('should define roverDriveController', function () {
    var scope, roverDriveCtrl, $location, roverService;

    beforeEach(inject(function($rootScope, $controller, _$location_, _roverService_) {
      scope = $rootScope.$new();
      roverDriveCtrl = $controller('RoverDriveCtrl', {$scope: scope});
      $location = _$location_;
      roverService = _roverService_;
    }));

    it('should define the Developer Controller', function () {
      expect(roverDriveCtrl).toBeDefined();
    });

    it('should call enterDriverMode when /drive is opened', function () {
      spyOn(roverService, 'enterDriverMode');
      $location.path('/drive');
      setTimeout(function () {expect(roverService.enterDriverMode).toHaveBeenCalled()}, 250);
    });

    it('should call exitDriverMode when /drive is closed', function () {
      spyOn(roverService, 'exitDriverMode');
      $location.path('/observe');
      setTimeout(function () {expect(roverService.exitDriverMode).toHaveBeenCalled()}, 250);
    });
    


  });
});
