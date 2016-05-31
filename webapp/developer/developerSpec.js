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

    it ('should have the matching text for killswitch state', function () {
      if (scope.killswitch.enabled) {
        expect(scope.killswitchText).toEqual('blocked');
      }
      else {
         expect(scope.killswitchText).toEqual('allowed');
      }
    });

    it('should change the killswitch text when it gets enabled'), function() {
      scope.killswitch.enabled = true;
      expect(scope.killswitchText).toEqual('blocked');
    }

  });
});