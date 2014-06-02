BasicInstantMessenger
=====================

A simple 1 to 1 Instant Messaging Application in Java

Swing API was used for the GUI.

The server needs to be started at one end and the client at another. These two will be used for communication.
It does not support multiple clients at the moment.

The client needs to be configured with the server IP address by editing ClientTest.java and passing the IP address
in the constructor of Client object.
          
              test =  new Client("127.0.0.1");
              
Currently it is configured to run the server and the client in the same machine, using local host as the IP.

Also the port used for connecting (6789) should be open and forwarded (if required) at the server side.

This application was developed following the tutorials at http://www.thenewboston.org
