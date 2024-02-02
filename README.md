![project_synergy_banner](Banner.svg)
<br>
![Label](https://img.shields.io/badge/Project-Synergy-success) 
![Label](https://img.shields.io/badge/Language-Java-red)
![GitHub manifest version (path)](https://img.shields.io/github/manifest-json/v/carlosensale/Project-Synergy)

Project Syngery is a backend framework for web services. It's written in Java and communicates over the REST-Protocol.
The used web-server is implemented with the help of the [Netty Framework](https://github.com/netty/netty).
<hr>

## Architecture
The following modules build the backbone of the framework and provide the main functionality:
### Database
Data persistence is one of the main features of this framework. Until now there are several ways to
persist data. You can use an abstract SQLDataHandler, a MongoDataHandler or a complete own implementation
of the generic interface DataHandler.
### Session
With this module you get control over the session of a user. You can implement a complete own
Session Class by extending the Session interface. For persist the session you need a DataHandler
that provides a way to save and fetch the session.
### HTTP-Server
The real magic happens in the HTTP-Server module. This module provides you all necessary interfaces
and some default implementation for a HTTP-Server. The only thing you need to add are some HTTP-Services.

## Installation
You can simply add Project Synergy to your own work by adding a dependency in maven or gradle.

### Maven
- Add Jitpack to your Project as repository:
```xml
    <repositories>
	    <repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
    </repositories>
```

- Next you can add the dependency:
```xml
    <dependency>
	    <groupId>com.github.carlosensale</groupId>
	    <artifactId>Project-Synergy</artifactId>
	    <version>v1.0.0</version>
    </dependency>
```
- Finally, you can reload your maven project

### Gradle
- Add Jitpack to your root build.gradle
```groovy
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
```
- Then you can add the dependency
```groovy
	dependencies {
	        implementation 'com.github.carlosensale:Project-Synergy:1.0.0'
	}
```
- Sync your build.gradle

Now you are ready to code! <br>
Note: There will be a version without jitpack coming soon.

## Usage

After adding the dependency to your project, you are ready to go. There are two mayor features of the project:
the HTTP-Server and the HTTP-Services. In the following instructions 'Session' is a placeholder
for your implementation of the Session interface.

* Step 1:  
  First you need to an instance of a java.util.logging.Logger.  
  ```java
    Logger logger = Logger.getLogger("Logger");
  ```
* Step 2:
  To persist information about the sessions, a DataHandler is needed.
  ```java
    DataHandler<Session> sessionDatabase = ...;
  ```
* Step 3:
  For handling the sessions, an implementation of a SessionHandler<?> will be needed.
  You can use a DefaultSessionHandler for example.
  ```java
    SessionHandler<Session> sessionHandler = new DefaultSessionHandler<>(sessionDatabase,logger);
  ```
* Step 4:
  Now an HTTP-Server can be declared and initialized with a fitting implementation e.g. NettyHttpServer.
  ```java
    HttpServer server = new NettyHttpServer<>(sessionHandler,logger);
  ```
* Step 5:
  Now we need to create a Service that will be registered. In this example
  we implmented this TestService. It shows difrent methods and path, as well as one method where a session
  is needed and one without.
  ```java
  public class TestService implements HttpService {

  @Override
  public String getName() {
    return "TestService";
  }
  
  //will listen to localhost:11111/set
  @HttpMethodHandler(path = "/set", method = "GET", session = false)
  public FullHttpResponse set(RequestContext context){
    return new HttpResponseBuilder().content("Success! cookie set!").addCookie("KEY=ABC").build();
  }
  //will listen to localhost:11111/test
  @HttpMethodHandler(path = "/test", method = "GET")
  public FullHttpResponse test(RequestContext context, Test2Session testSession){
    return NettyHttpResponseUtils.simpleHttpText200("Success! session: "+testSession);
  }
  ```
* Step 6:
  After setting up everything, we can register our HTTP-Services.
  ```java
    server.registerService(new TestService());
  ```
* Step 7:
  Last but not least we can start the HTTP-Server. In this example, the server will be
  started at localhost:11111.
  ```java
  server.startHttpServer("localhost", 11111);
  ```
Now everything is ready. You can register as many services as you want. Just remember
that you only can register one method to you path.

