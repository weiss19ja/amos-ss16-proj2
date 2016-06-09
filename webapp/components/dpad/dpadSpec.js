'use strict';

describe('myApp.dpad module', function() {
    var $compile, $rootScope, scope, $controller, element,controller, roverService;

    beforeEach(angular.mock.module('templates'));
    beforeEach(module('myApp.roverService'));
    beforeEach(module('myApp.dpad'));

    /**
     * General setup
     */
    beforeEach(inject(function(_$compile_, _$rootScope_, $httpBackend, _$controller_, _roverService_) {
        $compile = _$compile_;
        $rootScope = _$rootScope_;
        roverService = _roverService_;
        $controller = _$controller_;

        $httpBackend.whenGET('assets/icons/ic_label_48px.svg').respond('');
    }));

    describe('dpad controller in drive mode ',function () {

        beforeEach(function () {
            element = $compile('<dpad></dpad>')($rootScope);
            $rootScope.$digest();
            controller = element.controller("dpad");
            scope = element.isolateScope() || element.scope();
        });

        it('should contains four dpad buttons with correct graphics', inject(function() {
            var icons = element.find('md-icon');
            expect(icons.length).toBe(4);

            for (var i = 0; i < icons.length; i++) {
                var iconElement = icons[i];
                expect(iconElement.getAttribute('md-svg-icon')).toContain('ic_label_48px.svg');
            }
        }));

        it('should exposes a set of methods according to its buttons', function() {
            expect(scope).toBeDefined();
            expect(scope.buttonClick).toBeDefined();
            expect(scope.up).toBeDefined();
            expect(scope.down).toBeDefined();
            expect(scope.left).toBeDefined();
            expect(scope.right).toBeDefined();
        });

        it('should send drive forward request on up',function () {
            scope.up();
            var shouldMsg = '{"jsonrpc":"2.0","method":"driveForward","params":[500],"id":1}';
            var lastMsg = roverService.getLastSendMsg();
            expect(lastMsg).toBe(shouldMsg);
        });

        it('should send drive backward request on down',function () {
            scope.down();
            var shouldMsg = '{"jsonrpc":"2.0","method":"driveBackward","params":[500],"id":1}';
            var lastMsg = roverService.getLastSendMsg();
            expect(lastMsg).toBe(shouldMsg);
        });

        it('should send turn left request on left',function () {
            scope.left();
            var shouldMsg = '{"jsonrpc":"2.0","method":"turnLeft","params":[300],"id":1}';
            var lastMsg = roverService.getLastSendMsg();
            expect(lastMsg).toBe(shouldMsg);
        });

        it('should send turn right request on right',function () {
            scope.right();
            var shouldMsg = '{"jsonrpc":"2.0","method":"turnRight","params":[300],"id":1}';
            var lastMsg = roverService.getLastSendMsg();
            expect(lastMsg).toBe(shouldMsg);
        });

        it('should send stop request on buttonClick',function () {
            scope.buttonClick();
            var shouldMsg = '{"jsonrpc":"2.0","method":"stop","params":[],"id":1}';
            var lastMsg = roverService.getLastSendMsg();
            expect(lastMsg).toBe(shouldMsg);
        });
    });

    describe('dpad controller in camera mode ',function () {

        beforeEach(function () {
            element = $compile('<dpad mode="camera"></dpad>')($rootScope);
            $rootScope.$digest();
            controller = element.controller("dpad");
            scope = element.isolateScope() || element.scope();
        });

        it('should send camera up request on up',function () {
            scope.up();
            var shouldMsg = '{"jsonrpc":"2.0","method":"turnHeadUp","params":[20],"id":1}';
            var lastMsg = roverService.getLastSendMsg();
            expect(lastMsg).toBe(shouldMsg);
        });

        it('should send camera down request on down',function () {
            scope.down();
            var shouldMsg = '{"jsonrpc":"2.0","method":"turnHeadDown","params":[20],"id":1}';
            var lastMsg = roverService.getLastSendMsg();
            expect(lastMsg).toBe(shouldMsg);
        });

        it('should send camera turn left request on left',function () {
            scope.left();
            var shouldMsg = '{"jsonrpc":"2.0","method":"turnHeadLeft","params":[20],"id":1}';
            var lastMsg = roverService.getLastSendMsg();
            expect(lastMsg).toBe(shouldMsg);
        });

        it('should send camera turn right request on right',function () {
            scope.right();
            var shouldMsg = '{"jsonrpc":"2.0","method":"turnHeadRight","params":[20],"id":1}';
            var lastMsg = roverService.getLastSendMsg();
            expect(lastMsg).toBe(shouldMsg);
        });

    });

});