'use strict';

describe('myApp.example module', function() {

  var scope;
  beforeEach(module('myApp.example'));

  describe('example controller', function(){

    it('should ....', inject(function($controller, $rootScope) {
      scope = $rootScope.$new();
      //spec body
      var exampleCtrl = $controller('ExampleCtrl', {$scope: scope} );
      expect(exampleCtrl).toBeDefined();
    }));

  });
});
