'use strict';

describe('myApp.dpad module', function() {
    var $compile, $rootScope;
    var $httpBackend;

    beforeEach(angular.mock.module('templates'));
    beforeEach(angular.mock.module('myApp.roverService'));
    beforeEach(module('myApp.dpad'));

    /*
    beforeEach(inject(function(_$roverSerivce_) {
        $roverService = _$roverService_;
    }));
    */

    beforeEach(inject(function(_$compile_, _$rootScope_, $httpBackend) {
        $compile = _$compile_;
        $rootScope = _$rootScope_;

        $httpBackend.whenGET('assets/icons/ic_label_48px.svg').respond('');
    }));

    describe('dpad directive as movement controller', function() {
        it('contains the correct heading', inject(function() {
            var element = $compile('<dpad></dpad>')($rootScope);

            $rootScope.$digest();

            expect(element.html()).toContain("Driving");
        }));
        it('contains a button', inject(function() {
            var element = $compile('<dpad></dpad>')($rootScope);

            $rootScope.$digest();

            expect(element.find('button').length).toBe(1);
        }));
    });

});