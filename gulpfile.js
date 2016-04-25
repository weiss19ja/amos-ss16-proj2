'use strict';
var gulp = require('gulp');
var connect = require('gulp-connect');
var Server = require('karma').Server;
var webdriver_update = require('gulp-protractor').webdriver_update;
var del = require('del');

gulp.task('default', ['connect']);

gulp.task('connect', function () {
    connect.server({
        root: 'webapp',
        port: 80
    });
});

gulp.task('webdriver_update', webdriver_update);

/**
 * Run test once and exit
 */
gulp.task('test', function (done) {
  new Server({
    configFile: __dirname + '/karma.conf.js',
    singleRun: true
  }, done).start();
});

gulp.task('docker', ['clean','docker-copy']);

gulp.task('docker-copy',function(){
	var dockerAssets = '/docker/assets'
  	gulp.src([__dirname+'/package.json',__dirname+"/**/webapp/**/*","!src/**"])
  	.pipe(gulp.dest(__dirname+dockerAssets));
});

gulp.task('clean',function(cb){
	del(__dirname+'/docker/assets');
});
