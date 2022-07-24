package org.retailer.app.controller;

import java.util.List;
import org.retailer.app.bean.CustomerReward;
import org.retailer.app.bean.Transaction;
import org.retailer.app.service.RewardPointsService;
import org.retailer.app.utility.RewardPointsUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/transaction")
public class RewardPointsController {

	@Autowired
	private RewardPointsService rewardPointsServ;

	@GetMapping("/reward")
	@ApiOperation(value = "Return list of all the reward points earned for each customer per month and total.", 
	response = ResponseEntity.class)
	public ResponseEntity<?> getAllRewardPoints() {
		List<CustomerReward> customerRewardList = rewardPointsServ.getAllCustomerReward();
		 if(RewardPointsUtility.isListEmpty(customerRewardList)) { 
			 return new ResponseEntity<>(RewardPointsUtility.NO_TRANSACTION, HttpStatus.NOT_FOUND); 
		 }
		 
		 return new ResponseEntity<>(customerRewardList, HttpStatus.OK);
	}

	@GetMapping("/transactionId/{transactionId}")
	@ApiOperation(value = "Find the transaction by transaction ID.", 
	response = ResponseEntity.class)
	public ResponseEntity<?> getTransactionByID(@PathVariable("transactionId") String transactionId) {
		Transaction transaction = rewardPointsServ.getTransactionByID(transactionId);
		 if(transaction == null) { 
			 return new ResponseEntity<>(RewardPointsUtility.TRANSACTION_NOT_FOUND, HttpStatus.NOT_FOUND); 
		 }
		 return new ResponseEntity<>(transaction, HttpStatus.OK);
	}

	@GetMapping("/customerId/{customerId}")
	@ApiOperation(value = "Find the customer by customerId and return reward points earned by that customer per month and total.", 
	response = ResponseEntity.class)
	public ResponseEntity<?> getCustomerReward(@PathVariable("customerId") String customerId) {
		CustomerReward customerReward = rewardPointsServ.getCustomerReward(customerId);
		 if(customerReward == null) { 
			 return new ResponseEntity<>(RewardPointsUtility.CUSTOMER_NOT_FOUND, HttpStatus.NOT_FOUND); 
		 }
		 return new ResponseEntity<>(customerReward, HttpStatus.OK);
	}

	@PostMapping
	@ApiOperation(value = "Add a new transaction", 
	notes = "Returns the newly added transaction if transaction added successfully otherwise return error message if transaction already exists. "
			+ "Transaction ID must not be null or empty or duplicate. "
			+ "Purchase Price must not be negative for a transaction. "
			+ "Customer ID must not be null or empty. "
			+ "The transaction date must not be null or empty, and must be in the format : dd/LLL/uuuu, eg : 05/Jul/2022.", 
	response = ResponseEntity.class)
	public ResponseEntity<?> addTransaction(@RequestBody Transaction newTransaction) {
		if(newTransaction == null || newTransaction.getTransactionId() == null || newTransaction.getTransactionId().trim().equals(""))
			return new ResponseEntity<>(RewardPointsUtility.INVALID_TRANSACTION, HttpStatus.BAD_REQUEST);
		
		if(newTransaction.getCustomerId() == null || newTransaction.getCustomerId().trim().equals(""))
			return new ResponseEntity<>(RewardPointsUtility.INVALID_CUSTOMER_ID, HttpStatus.BAD_REQUEST);
		
		if(newTransaction.getPurchasePrice() < 0)
			return new ResponseEntity<>(RewardPointsUtility.INVALID_PURCHASE_PRICE, HttpStatus.BAD_REQUEST);
		
		if(newTransaction.getTransactionDate() == null || newTransaction.getTransactionDate().trim().equals("") || !rewardPointsServ.isValidTransactionDate(newTransaction.getTransactionDate()))
			return new ResponseEntity<>(RewardPointsUtility.INVALID_TRANSACTION_DATE, HttpStatus.BAD_REQUEST);
		
		boolean isCreated = rewardPointsServ.addTransaction(newTransaction);
		 
		if(isCreated) {
			return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(newTransaction.getTransactionId() + " : " + RewardPointsUtility.TRANSACTION_ALREADY_EXISTS, HttpStatus.CONFLICT);
	}

	@PatchMapping()
	@ApiOperation(value = "Update the existing transaction", 
	notes = "Returns the updated transaction if transaction updates successfully otherwise return errorr message if transaction doesn't exists. "
			+ "Transaction ID must not be null or empty. "
			+ "Purchase Price must not be negative for a transaction. "
			+ "Customer ID must not be null or empty. "
			+ "The transaction date must not be null or empty, and must be in the format : dd/LLL/uuuu, eg : 05/Jul/2022."
			+ "If any attribute/field of the passed Transaction is empty, then those attributes will "
			+ "be ignored and won't be updated.", 
	response = ResponseEntity.class)
	public ResponseEntity<?> updateTransaction(@RequestBody Transaction transaction) {
		if(transaction == null || transaction.getTransactionId() == null || transaction.getTransactionId().trim().equals(""))
			return new ResponseEntity<>(RewardPointsUtility.INVALID_TRANSACTION, HttpStatus.BAD_REQUEST);
		
		if(transaction.getCustomerId() == null || transaction.getCustomerId().trim().equals(""))
			return new ResponseEntity<>(RewardPointsUtility.INVALID_CUSTOMER_ID, HttpStatus.BAD_REQUEST);
		
		if(transaction.getPurchasePrice() < 0)
			return new ResponseEntity<>(RewardPointsUtility.INVALID_PURCHASE_PRICE, HttpStatus.BAD_REQUEST);
		
		if(transaction.getTransactionDate() == null || transaction.getTransactionDate().trim().equals("") || !rewardPointsServ.isValidTransactionDate(transaction.getTransactionDate()))
			return new ResponseEntity<>(RewardPointsUtility.INVALID_TRANSACTION_DATE, HttpStatus.BAD_REQUEST);
		
		Transaction updatedTransaction = rewardPointsServ.updateTransaction(transaction);
		 if(updatedTransaction!=null) {
			 return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
		 }
		 return new ResponseEntity<>(transaction.getTransactionId() + " : " + RewardPointsUtility.TRANSACTION_DOES_NOT_EXISTS, HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/{id}")
	@ApiOperation(value = "Removes the transaction by transaction ID.", 
	response = ResponseEntity.class)
	public ResponseEntity<?> removeTransaction(@PathVariable("id") String id) {
		boolean isRemoved = rewardPointsServ.removeTransaction(id);
		 if(isRemoved) {
			 return new ResponseEntity<>(id + " : " + RewardPointsUtility.TRANSACTION_REMOVED, HttpStatus.OK);
		 }
		 return new ResponseEntity<>(id + " : " + RewardPointsUtility.TRANSACTION_DOES_NOT_EXISTS, HttpStatus.NOT_FOUND);
	}
}
