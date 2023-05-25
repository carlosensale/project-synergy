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
	    <version>1.0.0</version>
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
the HTTP-Server and the HTTP-Services.

TO BE CONTINUED
