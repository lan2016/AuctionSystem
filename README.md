# Requirements
1.JAVA

## FrameWorks Used...
 1. Apache POI- For Reading Configuration Excel Sheet At runntime <br/>
 2. Apache JETTY- For Web server <br/>
 3. JWT- To Generate JWT token for login user <br/>
 4. JUNIT- To provide Automated test cases <br/>

## Setup Instructions
1. Clone Project into IDE<br/>
2. Right Click on build.xml and run this file<br/> 
3.  After Successfull Build Following message will be showing,this will also show automated test cases progess.. </br>
<pre>
    <code>
           [javac] [total 3101ms]
test:
    [junit] Testsuite: com.auction.junit.JUnitTestCases
    [junit] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.942 sec
    [junit] ------------- Standard Output ---------------
    [junit] test..
    [junit]  Jetty Server Started
    [junit] Query:http://localhost:4567/auction?operation_type=getRunningAuctions
    [junit] Request:GET http://localhost:4567/auction?operation_type=getRunningAuctions HTTP/1.1
    [junit] 21:22:20.172 [qtp1677921169-15] ERROR com.auction.service.AuctionAPIS - Bid price is minimum then base pricebansal---eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIwLjk4MzIxMDQ5OTA2Mzk5MzgifQ.SIyvB_Ek254FkaeBkil8dBhYTUqI6v7gY4k8XuasrqA---500---123
    [junit] Query:http://localhost:3456/auction?operation_type=getRunningAuctions
    [junit] Request:GET http://localhost:3456/auction?operation_type=getRunningAuctions HTTP/1.1
    [junit] ------------- ---------------- ---------------
    [junit] ------------- Standard Error -----------------
    [junit] ERROR StatusLogger No log4j2 configuration file found. Using default configuration: logging only errors to the console.
    [junit] 2020-08-19 21:22:19.127:INFO::main: Logging initialized @7107ms to org.eclipse.jetty.util.log.StdErrLog
    [junit] 2020-08-19 21:22:19.259:INFO:oejs.Server:main: jetty-9.4.5.v20170502
    [junit] 2020-08-19 21:22:19.307:INFO:oejsh.ContextHandler:main: Started o.e.j.s.ServletContextHandler@1f52eb6f{/auction,null,AVAILABLE}
    [junit] 2020-08-19 21:22:19.380:INFO:oejs.AbstractConnector:main: Started ServerConnector@cc239ba{HTTP/1.1,[http/1.1]}{localhost:3456}
    [junit] 2020-08-19 21:22:19.381:INFO:oejs.Server:main: Started @7354ms
    [junit] log4j:WARN No appenders could be found for logger (org.apache.http.client.protocol.RequestAddCookies).
    [junit] log4j:WARN Please initialize the log4j system properly.
    [junit] log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
    [junit] ------------- ---------------- ---------------
create_run_jar:
      [jar] Building jar: C:\Users\Rajat1.Bansal\gitAuctionsystem\AuctionSystem\dist\Auction\bin\rtAuction.jar
build_completion:
BUILD SUCCESSFUL
Total time: 7 seconds
    </code>
</pre>
4. Referesh Project -> go dist->copy Auction Folder<br/>
5. Paste Auction folder on linux server <br/>
6. Go Auction->Configuration(On linux directory where it was pasted) <br/>
7. Insert All items into itemcode.xlsx (Items informations for which want to start auction)<br/>
8. Change server.properties file(change server ip->ip which are you using for making auction server(linux server ip)  port -> auction server port)<br/>
9. Now Done with Configurations<br/>
10. Go to bin directory and execute following command

<pre>
<code>
./runAuction.sh
</code>
</pre>
11. Auction server started... you can Use APIS to get results

## API Strcture
###### GET RUNNING AUCTIONS
<pre>
<code>
Request API
GET http://IP:PORT/auction?operation_type=getRunningAuctions

Response
[{"step rate":250,"item code":"123","highest bid amount":1000},{"step rate":500,"item code":"456","highest bid amount":2000},{"step rate":1000,"item code":"789","highest bid amount":5000}]

</code>
</pre>

###### PLACE BID---first user have to login before placing bid

<pre>
<code>
LOGIN API {<b>User have to send user name in header then auction server will return a token which user have to send when placing bid</b>}
GET http://IP:PORT/auction?operation_type=login

Headers INFO----
1. userName

Response
{"tokenID":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIwLjI0ODgxOTAyMzc0MTIyODg4In0.CMKbFGUQ2i7-PMz9plm89r6QTM4NB-4Joc8ow9z5ha4"}

</code>


<code>
Place bid API(<b> user have to send same token which was received from login API otherwise bid will be rejected</b>)
GET http://IP:PORT/auction?operation_type=placeBid

Headers INFO----(KEYS)
1. userName
2. bidAmount
3. itemCode
4. Authorization (</b>place the same token which was recieved from login</b>)

Response
Status Codes
201- bid accepted
406 -bid rejected


</code>
<code>
<b>Note- if you are using postman for executing requests then use Authorization in postman for place token and Use TYPE=OAAuth2.0 and 
place the token into ACCES TOKEN and Header Prefix should be empty</b>
	</code>
</pre>







