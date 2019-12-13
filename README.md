*Revolut Transfer Money challenge code*

### This application is developed by below technologies and languages:
- Java 8
- Maven
- Intellij IDEA

---


## Get latest source code from Bitbucket
git clone https://bitbucket.org/ffarnia/revolut.git

---

## Build and run source code

1. mvn clean compile install
2. java -jar target/revolut-transfer-money-1.0.jar

after run jar file you can see this message:
"Embedded Server start successfully"
then 3 rest endpoint is available for call
- notice: if after run you get a message for access network please accept it. because
tests uses java.net classes for open connections.

---

## Rest endpoints

1. /api/account/create (POST): provide Account entity as json for request body like:
{
	"accountNumber" : 200,
	"ownerName" : "Fazel farnia",
	"balance" : 3000
}
2. /api/account/loadall (GET): without body, response include array of accounts as json
3. /api/transaction/transferMoney (POST): provide Transaction entity as json in request body like:
{
	"fromAccountNumber":100,
	"toAccountNumber":200,
	"amount":3
}

notice: at first create two Account by first endpoint and then call transfer money, then for see the result of previous rest call you can call second endpoint and find modified balances of accounts.

## There are 27 test case in 3 different categories:
1. repository: for testing store and retrieve accounts from hashMap as a in memory repository
2. service: for testing methods of service implementation, also provide concurrent test for thread safety
3. rest: for testing rest endpoints, send and response would be json

-----

## Conclusion
As document mentioned i obey the rule of simplicity so i did not used heavy frameworks and complex libraries
 such as H2 in memory database and Jersey Restful webservice and other application servers. so this application is implemented by pure java libs
 and just a simple and well known Jackson jar for mapping entities to json and junit for test cases.