# Mobile Robot Framework - Software Architecture Description

## Purpose

This document provides the architectural layout of the mobile robot framework. The scope is to describe the project structure, used technologies as well as third party software and the build process.

## Client Server Communication
The Project is divided in two main parts. The webapp which can be consumed by any client and the backend which runs directly on the rover.    
The webapp uses AngularJs and MaterialAngular to provide the design of the mobile application while the backend is based on Java and uses jetty websockets in combination with the JSON RPC protocol for the  communication with its clients.

Both parts are further described in the upcoming sections. 

## Backend

### Programming Language
``` 
Name: Java  
Version: 1.8 
License: mostly open-source with a few proprietary components
```

### Build Tool
``` 
Name: Maven
Version: 1.2
License: gnu
``` 

### Additional Plugins/Packages
``` 
Name: pi4j Java I/O library for Raspberry Pi
Version: 1.0 
License: GNU LESSER GENERAL PUBLIC LICENSE   
``` 
``` 
Name: jetty websocket
Version: 9.2.7.v20150116
License: Eclipse Public License - v 1.0 
``` 
``` 
Name: junit
Version: 4.4 
License: Eclipse Public License - v 1.0 
``` 
``` 
Name: gson
Version: 2.3.1 
License: Apache License - v 2.0
``` 
``` 
Name: slf4j Simple Logging Facade for Java
Version: 1.7.18 
License: MIT License
``` 
``` 
Name: Apache Commons
Version: 3.0  
License: Apache License - v 2.0
``` 
``` 
Name: Cfg4j
Version: 4.1.4 
License: Apache License - v 2.0
``` 
``` 
Name: Mockito Mock objects library for java 
Version: 1.10.19 
License: MIT License
``` 


## Webapp

### Programming Language and Framework
``` 
Name: Node.js 
Version: 6.0.0
License: MIT License
``` 
``` 
Name: AngularJS
Version: 1.4.0
License: MIT License
``` 
``` 
Name: Angular Material
Version: 1.0
License: MIT License
``` 
``` 
Name: Angular Image Fallback
Verion: 0.1.4
License: MIT License
``` 

### Build and Package Tool
``` 
Name: npm
Version: 3.8.7
License: Artistic License 2.0
``` 
``` 
Name: Bower 
Version: 1.7.9
License: MIT License
``` 

### Images and Icons
The icons in the webapp are the official material design icons from Google.

License: Creative Common Attribution 4.0 International License (CC-BY 4.0)  
[Link to GitHub](https://github.com/google/material-design-icons/)

## Raspbery Pi

### Video Stream

```
Name: UV4L, User space Video4Linux collection
Version: 4.12.5
License: Public Domain
```

## Project Structure
```
backend/                      // Java backend 
|  pom.xml                
+--src/  
|   +--main/  
|   |   +--java/  
|   |   |   +--de.developgroup.mrf.  
|   |   |        +--rover/   // rover controls
|   |   |        +--server/  // server, servlet, sockets
|   |   +--ressources/       // rover.properties, stream.sh
|   |   +--webapp/           // html files copied from /webapp
|   +--test/  
+--target/	                 // generated jar file  
|  
info/                        // pdf files about rover hardware
videostream/		         // shellscripts and config file to install/remove uv4l on the pi
webapp/                      // webproject
|  index.html                // landing page
|  app.js
+--components/               // reusable components
+--viewXYZ /                 // some views
e2e-tests/                   // integration tests
docker/                      // files for docker image generation
  |  Dockerfile  
```
## Build Process
There are two build tools working together for a complete project build. Npm builds the webapp while the backend is built by maven.
The text below serves a step by step description of the build process:

#### Local Build    
1. NPM resolves dependencies of the webapp and installs it     
2. NPM calls Karma to run the unit-tests     
2. NPM copy build webapp to backend/src/main/webapp    
3. NPM calls maven to build the backend und creates a jar file into the target folder      
4. Copy jar to rover and start it   

#### Continues Integration     
1. On a new commit at GitHub Travis-ci pulls the project   
2. Travis-ci builds the project (npm/maven)    
3. Travis-ci runs unit and integration tests    
4. Travis-ci create a docker image from the dockerfile     
5. Travis-ci push created docker image to dockerhub     
6. Jenkins pull docker image from dockerhub and deploy docker image to FAU Server     

## Additional Documentation
[GitHub Wiki](https://github.com/weiss19ja/amos-ss16-proj2/wiki)     
[GitHub PFD Files](https://github.com/weiss19ja/amos-ss16-proj2/tree/master/info)    
