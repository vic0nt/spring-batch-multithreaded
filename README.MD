<h1>Spring Batch multithreaded example</h1>
There are two RDBMS (they mocked with H2 in Oracle mode in the app). We have to extract some XML from CLOB field
from the first DB, transform it with given XSL by multiple threads, and insert the transormated XML into the second DB.

If you're running the app from IDE, please use the following parameters:
main class: org.springframework.batch.core.launch.support.CommandLineJobRunner
program arguments: jobs/migrationJob.xml migrationJob