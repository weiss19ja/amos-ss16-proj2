'use strict';
module.exports = function(config){
  config.set({

    basePath : './',

    files : [
      'webapp/bower_components/angular/angular.js',
      'webapp/bower_components/angular-route/angular-route.js',
      'webapp/bower_components/angular-mocks/angular-mocks.js',
      'webapp/bower_components/angular-websocket/angular-websocket.js',
      'webapp/components/**/*.js',
      'webapp/main/**/*.js',
      'webapp/info/**/*.js',
      'webapp/example/**/*.js',
    ],

    autoWatch : true,

    frameworks: ['jasmine'],

    browsers : ['Chrome'],

    plugins : [
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-jasmine',
            'karma-junit-reporter',
            'karma-spec-reporter'
            ],

	reporters: ['spec'],

    junitReporter : {
      outputFile: 'test_out/unit.xml',
      suite: 'unit'
    }

  });
};
