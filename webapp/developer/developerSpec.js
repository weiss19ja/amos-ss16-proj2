'use strict';

describe('myApp.developer module', function () {

  beforeEach(module('myApp.developer'));
  beforeEach(module('myApp.roverService'));

  describe('developer controller', function () {
    var scope, developerCtrl;

    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
      scope = $rootScope.$new();
      developerCtrl = $controller('DeveloperCtrl', {$scope: scope});

    }));

    it('should define the Developer Controller', function () {
      expect(developerCtrl).toBeDefined();
    });

    it ('should set the default values for killswitch', function () {
      expect(scope.killswitchText).toEqual('allowed');
      expect(scope.killswitch.isEnabled).toEqual(false);
    })

    it('should change the killswitch text when it gets enabled'), function() {
      scope.killswitch.isEnabled = true;
      expect(scope.killswitchText).toEqual('blocked');
    }

  });
});