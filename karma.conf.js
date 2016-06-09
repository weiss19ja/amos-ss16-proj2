'use strict';
module.exports = function(config){
    config.set({
    preprocessors: {
      'webapp/**/*.html': 'ng-html2js',
      'webapp/**/*.svg': 'ng-html2js'
    },

    basePath : './',

    files : [
      'webapp/bower_components/angular/angular.js',
      'webapp/bower_components/angular-route/angular-route.js',
      'webapp/bower_components/angular-mocks/angular-mocks.js',
      'webapp/bower_components/angular-websocket/dist/angular-websocket.js',
      'webapp/bower_components/angular-websocket/dist/angular-websocket-mock.js',
      'webapp/bower_components/angular-animate/angular-animate.min.js',
      'webapp/bower_components/angular-aria/angular-aria.min.js',
      'webapp/bower_components/angular-material/angular-material.js',
      'webapp/bower_components/angular-material/angular-material-mocks.js',
      'webapp/bower_components/angular-img-fallback/angular.dcb-img-fallback.js',
      'webapp/bower_components/angular-css/angular-css.js',
      'webapp/components/**/*.html',
      'webapp/components/**/*.js',
      'webapp/developer/**/*.js',
      'webapp/main/**/*.js',
      'webapp/roverDrive/**/*.js',
      'webapp/info/**/*.js',
      'webapp/logs/**/*.js',
      'webapp/settings/**/*.js',
      'webapp/services/**/*.js'
    ],
    ngHtml2JsPreprocessor: {
      stripPrefix: 'webapp/',
      moduleName: 'templates'
    },

    autoWatch : true,

    frameworks: ['jasmine'],

    browsers : ['Chrome'],

    plugins : [
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-jasmine',
            'karma-junit-reporter',
            'karma-spec-reporter',
            'karma-ng-html2js-preprocessor'
            ],

	reporters: ['spec'],

    junitReporter : {
      outputFile: 'test_out/unit.xml',
      suite: 'unit'
    }

  });
};
