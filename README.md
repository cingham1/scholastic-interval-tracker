# scholastic-interval-tracker
Scholastic coding exercise for interval tracking via a REST API.

by Charles Ingham

## Overview
The system is designed to handle creation and retrieval of interval data for a document.  An example of interval data could be text highlighting within a large document.




## REST API

A client may call the following endpoints:

### To Create Intervals
```
HTTP POST:  http://{host-domain}/document/{documentId}/interval
```
The body of the POST should contain the interval(s) to be added for the document referred to by the `documentId`.  For example:

```
{ 
	"intervals": [
		{ "start" : 86 , "end" : 92}, 
		{ "start" : 232 , "end" : 241}, 
		{ "start" : 1056 , "end" : 1067} 
	]
}
```
**Success response:**  When the intervals have been stored for the document.
* Status: HTTP 200 OK
* Headers: 
    * Content-Type: application/json
* Body: `{ "status" : 200, "message" : "Success" }`

**Error response:**  When some error was encountered during the request.
  Status: HTTP 400 Bad Request, or 500 Internal Error
* Headers: 
    * Content-Type: application/json
* Body: `{ "status":<status code>, "message":<error description> }`

  
### To Get Intervals
```
HTTP GET:  http://{host-domain}/document/{documentId}/interval
```
The system will return a list of Intervals which have been created for the document referred to by the `documentId`.  
If no Intervals exist for that document an empty list will be returned.

**Success response:**  When the intervals have been successfully retrieved.
* Status: HTTP 200 OK
* Headers: 
    * Content-Type: application/json
* Body: 
```
{ 
	"intervals": [
		{ "start" : 86 , "end" : 92}, 
		{ "start" : 232 , "end" : 241}, 
		{ "start" : 1056 , "end" : 1067} 
	]
}
```

**Error response:**  When some error was encountered during the request.
  Status: HTTP 400 Bad Request, or 500 Internal Error
* Headers: 
    * Content-Type: application/json
* Body: `{ "status":<status code>, "message":<error description> }`

  
## Build

This project is built with Java 8 using Maven 3.  

#### Running the tests

Spock unit tests may be run from an IDE or from the command line using the following:

```
mvn test
```

#### Building the packages
Use Maven to build, run tests, and package 

```
mvn clean package shade:shade
```

#### Deployment
Normally the application would be deployed to a staging area or Dev server, but for the purposes of this programming exercise no special deployment is implemented.

#### Running the application

After a build the interval-tracker.jar file can be found under "./target" with the current version number as part of the filename.  

It can be run with the following:
```
java -jar <.jar file> 
```
The application will listen for incoming requests on its Rest API at the default port of 8080.

## Additional Considerations
* The application currently uses no datastore to hold the Intervals, simply an in-memory map.


## Versioning

* 1.0.0 - Initial version

## Authors

* **Charles Ingham** 

## License

This project is licensed under the Apache License V2.0 - see the [LICENSE](LICENSE) file for details


