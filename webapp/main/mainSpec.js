'use strict';

describe('myApp.main module', function() {

  beforeEach(module('myApp.main'));

  describe('main controller', function(){
     var scope;

    beforeEach(inject(function($rootScope, $controller) {
      scope = $rootScope.$new();
    }));

    it('should ....', inject(function($controller) {
      //spec body
      var mainCtrl = $controller('MainCtrl',{$scope: scope});
      expect(mainCtrl).toBeDefined();
    }));

    it('easy test', function () {
      var pass = true;
      	expect(pass).toBe(true);
    });


  });

});
