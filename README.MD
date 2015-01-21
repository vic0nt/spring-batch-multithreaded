<h1>Spring Batch multithreaded example</h1>
There are two RDBMS (they mocked with H2 in Oracle mode in the app). We have to extract some XML from CLOB field
from the first DB, transform it with given XSL by multiple threads, and insert the transormated XML into the second DB.
