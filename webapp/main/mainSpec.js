'use strict';

describe('myApp.main module', function() {

  beforeEach(module('myApp.main'));
  beforeEach(module('myApp.roverService'));

  describe('main controller', function(){
    var scope, mainCtrl;

    beforeEach(inject(function($rootScope, $controller) {
      scope = $rootScope.$new();
      mainCtrl = $controller('MainCtrl', {$scope: scope});
    }));

    it('should define the main Controller', (function() {
      expect(mainCtrl).toBeDefined();
    }));

    it('easy test', function () {
      var pass = true;
      	expect(pass).toBe(true);
    });
    
  });

});
