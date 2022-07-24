package org.retailer.app.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.retailer.app.bean.CustomerReward;
import org.retailer.app.bean.Transaction;
import org.retailer.app.repository.RewardPointsRepository;
import org.retailer.app.utility.RewardPointsUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RewardPointsService {

	@Autowired
	private RewardPointsRepository rewardRepo;
	
	/**
	 * Calculate the reward points earned by the customer on the purchase.
	 * @param purchasePrice
	 * @return rewardPoints
	 */
	public int calculateRewardPoints(int purchasePrice) {
		int rewardPoints = 0;
		if(purchasePrice>100)
			rewardPoints = (purchasePrice - 50)*1 + (purchasePrice - 100)*1;
		else if(purchasePrice>50)
			rewardPoints = (purchasePrice - 50)*1;
		return rewardPoints;
	}
	
	/**
	 * Creates an instance of CustomerReward using the parameter values
	 * @param customerId
	 * @param month
	 * @param rewardPoints
	 * @return CustomerReward instance
	 */
	public CustomerReward createCustomerReward(String customerId, String month, int rewardPoints) {
		CustomerReward customerReward = new CustomerReward();
		customerReward.setCustomerId(customerId);
		
		HashMap<String, Integer> rewardPointsPerMonth = new HashMap<String, Integer>();
		rewardPointsPerMonth.put(month, rewardPoints);
		customerReward.setRewardPointsPerMonth(rewardPointsPerMonth);
		
		customerReward.setTotalRewardPoints(rewardPoints);
		return customerReward;
	}
	
	/**
	 * Updates an instance CustomerReward, passed in the parameter
	 * @param customerReward
	 * @param month
	 * @param rewardPoints
	 */
	private void updateCustomerReward(CustomerReward customerReward, String month, int rewardPoints) {
		HashMap<String, Integer> rewardPointsPerMonth = customerReward.getRewardPointsPerMonth();
		int newRewardPoints = rewardPoints;
		if(rewardPointsPerMonth.containsKey(month)) {
			newRewardPoints = rewardPoints + rewardPointsPerMonth.get(month);
		}
		rewardPointsPerMonth.put(month, newRewardPoints);
		int totalRewardPoints = customerReward.getTotalRewardPoints()+rewardPoints;
		customerReward.setTotalRewardPoints(totalRewardPoints);
	}
	
	/**
	 * Creates a list of CustomerReward instances using the transaction data from the DB
	 * @param allTransactions
	 * @return List of CustomerReward
	 */
	private List<CustomerReward> createCustomerRewardList(List<Transaction> allTransactions) {
		HashMap<String, CustomerReward> customerRewardMap = new HashMap<String, CustomerReward>();
		for (Transaction tran : allTransactions) {
			String customerId = tran.getCustomerId();
			String month = tran.getTransactionDate().split("/")[1];
			int rewardPoints = calculateRewardPoints(tran.getPurchasePrice());
			if(!customerRewardMap.containsKey(customerId)) {
				customerRewardMap.put(customerId, createCustomerReward(customerId, month, rewardPoints));
			} else {
				CustomerReward customerReward = customerRewardMap.get(customerId);
				updateCustomerReward(customerReward, month, rewardPoints);
			}
		}
		return new ArrayList<CustomerReward>(customerRewardMap.values());
	}
	
	/**
	 * Get all the transaction data from DB, converts and return the CustomerReward list 
	 * @return List<CustomerReward> or null if no data in DB
	 */
	public List<CustomerReward> getAllCustomerReward() {
		List<Transaction> allTransactions = new ArrayList<Transaction>();
		rewardRepo.findAll().forEach(allTransactions::add);
		if (RewardPointsUtility.isListEmpty(allTransactions))
			return null;
		
		return createCustomerRewardList(allTransactions);
	}
	
	/**
	 * Fetch the Transaction from DB by Transaction ID
	 * @param id
	 * @return Transaction or null if no Transaction matches in DB
	 */
	public Transaction getTransactionByID(String id) {
		Optional<Transaction> opTransaction = rewardRepo.findById(id);
		if(opTransaction.isPresent()) 
			return opTransaction.get();
		else
			return null;
	}
	
	/**
	 * Fetch the customer data from DB for the passed customer id and calculate the CustomerReward
	 * @param customerId
	 * @return CustomerReward or null if no data in DB for the passed customer id
	 */
	public CustomerReward getCustomerReward(String customerId) {
		List<Transaction> customerTransactions = rewardRepo.findByCustomerId(customerId);
		if (RewardPointsUtility.isListEmpty(customerTransactions))
			return null;
		
		Transaction tran = customerTransactions.get(0);
		String month = tran.getTransactionDate().split("/")[1];
		int rewardPoints = calculateRewardPoints(tran.getPurchasePrice());
		CustomerReward customerReward = createCustomerReward(customerId, month, rewardPoints);
		
		for (int i=1; i<customerTransactions.size(); i++) {
			tran = customerTransactions.get(i);
			month = tran.getTransactionDate().split("/")[1];
			rewardPoints = calculateRewardPoints(tran.getPurchasePrice());
			updateCustomerReward(customerReward, month, rewardPoints);
		}
		return customerReward;
	}
	
	/**
	 * Add new transaction in DB
	 * @param transaction
	 * @return true if addition successful otherwise false 
	 */
	public boolean addTransaction(Transaction transaction) {
		Optional<Transaction> opTransaction = rewardRepo.findById(transaction.getTransactionId());
		if(opTransaction.isPresent()) 
			return false;
		else {
			rewardRepo.save(transaction);
			return true;
		}
	}
	
	/**
	 * Update existing transaction in DB
	 * @param transaction
	 * @return updated transaction if successful otherwise null
	 */
	public Transaction updateTransaction(Transaction transaction) {
		Optional<Transaction> opTransaction = rewardRepo.findById(transaction.getTransactionId());
		if(opTransaction.isPresent()) {
			Transaction existingTransaction = opTransaction.get();
			if(transaction.getCustomerId() != null && !transaction.getCustomerId().isEmpty())
				existingTransaction.setCustomerId(transaction.getCustomerId());
			if(transaction.getPurchasePrice() > 0)
				existingTransaction.setPurchasePrice(transaction.getPurchasePrice());	
			if(transaction.getTransactionDate() != null && !transaction.getTransactionDate().isEmpty())
				existingTransaction.setTransactionDate(transaction.getTransactionDate());
			
			rewardRepo.save(existingTransaction);
			return existingTransaction;	
		}
		else 
			return null;
	}
	
	/**
	 * Remove Transaction from DB by transaction id
	 * @param id
	 * @return true if successful otherwise false
	 */
	public boolean removeTransaction(String id) {
		Optional<Transaction> opTransaction = rewardRepo.findById(id);
		if(opTransaction.isPresent()) {
			rewardRepo.deleteById(id);
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Validates the date passed as per the format dd/LLL/uuuu
	 * @param transactionDate
	 * @return true if date is valid otherwise false
	 */
	public boolean isValidTransactionDate(String transactionDate) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/LLL/uuuu").withResolverStyle(ResolverStyle.STRICT);
		try {
		    LocalDate.parse(transactionDate.trim(), format);
		    return true;
		} catch (DateTimeParseException e) {
		    return false;
		}	
	}
}
