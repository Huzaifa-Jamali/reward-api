# Reward Points API

## Application:

- A retailer offers a rewards program to its customers awarding points based on each recorded purchase as follows: 
- For every dollar spent over $50 on the transaction, the customer receives one point.
- In addition, for every dollar spent over $100, the customer receives another point.
- Ex: for a $120 purchase, the customer receives
- (120 - 50) x 1 + (120 - 100) x 1 = 90 points 
- Given a record of every transaction during a three-month period, calculate the reward points earned for each customer per month and total. 
- ●	Make up a data set to best demonstrate your solution
- ●	Check solution into GitHub

- Create a JVM based backend application using REST. 
### That contains the following endpoints:
- GET /transaction/reward (Return list of all the reward points earned for each customer per month and total)
- GET /transaction/transactionId/{transactionId} (Find the transaction by transaction ID)
- GET /transaction/customerId/{customerId} (Find the customer by customerId and return reward points earned by that customer per month and total)
- POST /transaction (Add a new transaction)
- PATCH /transaction (Update the existing transaction)
- DELETE/transaction/{id} (Removes the transaction by transaction ID)


1. The initial list of transactions should be created on application start-up. 
2. All the endpoints return data in JSON format.

### The transaction object contains the following fields:
- transactionId : Must be unique and must not be null or blank 
- customerId : Must not be null or blank
- purchasePrice : Must not be null or blank or negative
- transactionDate : Must not be null or blank and must be in the format : dd/LLL/uuuu, eg : 05/Jul/2022


## How to run the project

### Pre-requisite
- Java 8 or higher installed on the system or in the Eclipse
- Maven is installed
- Access to github.

### Instructions to run the project 
- Clone the project from the github to your local system
- Import the project as Maven Project in the Eclipse or any other IDE
- Perform : Update Maven Project (Alt+F5 in Eclipse)
- Open /src/main/java/org/retailer/app/RewardPointsApplication.java file and run as Java Application.
- Wait for the message : "Started RewardPointsApplication" in the console.
- Open the browser and hit the link : http://localhost:8080/swagger-ui.html 
- Click Open reward-points-controller, to check and test all the endpoints for the API.

### Instructions to run the test cases
- Open the /src/test/java/org/retailer/app/controller/RewardPointsControllerTest.java file.
- Right click, run as JUnit test.
- Open the /src/test/java/org/retailer/app/service/RewardPointsServiceTest.java file.
- Right click, run as JUnit test.

## Sample initial DB dataset (few records) :
TRANSACTION_ID  	CUSTOMER_ID  	PURCHASE_PRICE  	TRANSACTION_DATE  
1					Cust1			25					01/Jul/2022
2					Cust1			1250				01/Jun/2022
3					Cust1			2050				10/Jul/2022
4					Cust1			2053				09/May/2022
5					Cust2			2					01/Jul/2022
6					Cust3			125					01/Jun/2022
7					Cust3			250					10/Jul/2022

- To check complete initial dataset of the DB :
- Open the browser and hit the link : http://localhost:8080/h2-console
- JDBC URL:	jdbc:h2:mem:testdb
- User Name: sa
- Don't enter any Password, keep Password empty.
- click Connect
- Select Transaction table in the left panel.
- Click Run button in the header row.

## To access the API actuator and health :
-- use this link in the browser to access the Actuator : http://localhost:8080/actuator/
-- use this link in the browser to access the Health of the API : http://localhost:8080/actuator/health/
 