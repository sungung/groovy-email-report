# groovy-email-report

REST service to create tabular report from groovy script

## Code Example

Groovy report script must be extended from GroovyAdapter, barebone class similar to the following sample:

import com.sungung.adapter.GroovyAdapter
import groovy.sql.Sql
import com.sungung.adapter.GroovyReportException

class BrewerReport extends GroovyAdapter{
    void execute(Map<String, String> params) throws GroovyReportException {
    }
}

## Motivation

The nature of user requirement for the report is keep changing. Also user expects the change is very straightforward.
However report module built in Java web application is not enough to be agile to be implemented the change.

So I decide to migrate actual reporting logic to groovy script to make it possible to change on the fly without any compile
and deployment.

## Installation

groovy-email-report running on Spring Boot framework with Maven build.
Get the code and then run mvn package. Java version 1.7.X used with Maven 3.1.X
'mvn package' runs unit tests and return some failures, then try to start fakeSMTP and rerun 'mvn package'

## Tests

To start application, enter 'mvn spring-boot:run", default application shall run on port 9000, you can run below line to run the tests.

curl http://127.0.0.1:9000/report/BrewerReport/user@helloworld.com

To check email integration, I found fakeSMTP(https://nilhcem.github.io/FakeSMTP) is very useful.


Test