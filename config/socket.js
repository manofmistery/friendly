module.exports = function (io) {
  'use strict';

    var bc = function(msg){
        io.sockets.emit('broadcast', {
            payload: msg,
            source: "Server"
        });
    }


  io.on('connection', function (socket) {

      //console.log("[chat] Incoming connection:\n" + clientInfo[1]);
      //console.log("[chat] Connected users: ", Clients.length-1); //Don't count Chatbot
      //console.log(Object.keys(socket));

      /** Disconnect */
      socket.on('disconnect', function(){
         // console.log("[chat] Socket Disconnected: "+client_ip);

      });

      /** Message */
    socket.on('message', function (from, msg) {

	  console.log("Chat> "+from+": "+JSON.stringify(msg));
        //Check if we have name saved in Clients array

      
      io.sockets.emit('broadcast', {
        payload: msg,
        source: from
      });

        var time = new Date();

        //Save message to db
        var message = new Message({
           body: msg,
           author: from,
           date: time
        });

        message.save(function(err){
           if(err){
               console.log(err);
           }
        });
    });
  });
  };

