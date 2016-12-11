RandoMS - Simple Microservice for Random Data
=============================================

A simple microservice built on top of Spark (sparkjava.com) to provide random
data via HTTP.

Build
-----
To build you will need Java 8 + Gradle and need to run: 

$ gradle build

Run
---
To run the microservice:

$ gradle run

Use
---
To use the service, access the URL:

http://localhost:4567/randoms/<type>

where <type> can be:

* long
* int
* double
* float
* gaussian
* bytes/<n> (n = number of bytes)

