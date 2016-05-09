'use strict';

angular.module("myApp.roverService",['ngWebSocket'])
.factory("roverService", function ($websocket, $location ) {

  var ws = $websocket('ws://localhost:' + $location.port() + '/example');

  var lastId = 0;
  var responses = [];

	function generateMessage(method,params){
			return {
    		"jsonrpc":"2.0",
    		"method":method,
    		"params":params,
    		"id": ++lastId
    		};
  }

	function send(method,params){
    console.log("send JRPC");
    var msg = JSON.stringify(generateMessage(method,params));
    console.log(msg);
    ws.send(msg);
	}

  ws.onMessage(function(message) {
      console.log('new Msg:');
      console.log(message);
      responses.push(JSON.parse(message.data));
  });

  ws.onError(function(event) {
    console.log('connection Error', event);
  });

  ws.onClose(function(event) {
    console.log('connection closed', event);
  });

  ws.onOpen(function() {
    console.log('connection open');
  });

  return {
        readyState: function(){
          /*
          0 = CONNECTING
          1 = OPEN
          2 = CLOSING
          3 = CLOSED
          */
          return ws.readyState;
        },
        driverModeAvailable: function(){
          return true;
        },
        responses: responses,
        demo: 'demo',
        notifications: function(){
          return [];
        },
        error: function(){
          return {};
        },
        sendPing: function () {
          send("ping",[lastId]);
        }
      };
});
