'use strict';

describe('myApp.devOpts module', function() {

  var scope;
  beforeEach(module('myApp.devOpts'));

  describe('devOpts controller', function(){

    it('should ....', inject(function($controller, $rootScope) {
      scope = $rootScope.$new();
      //spec body
      var devOptsCtrl = $controller('DevOptsCtrl', {$scope: scope} );
      expect(devOptsCtrl).toBeDefined();
    }));

  });
});
