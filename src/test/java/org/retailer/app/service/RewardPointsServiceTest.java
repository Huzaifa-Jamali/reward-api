package org.retailer.app.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.retailer.app.bean.CustomerReward;
import org.retailer.app.bean.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RewardPointsServiceTest {
	@Autowired
	private RewardPointsService rewardServ;
	
	@Test
	@DisplayName("Get all CustomerReward - Found")
	void testGetAllCustomerReward() {
		List<CustomerReward> customerRewardList = rewardServ.getAllCustomerReward();
		assertNotNull(customerRewardList);
	}

	@Test
	@DisplayName("Get transaction by Id - Found")
	void testGetTransactionById() {
		Transaction transaction = rewardServ.getTransactionByID("1");
		assertNotNull(transaction);
		assertEquals("1", transaction.getTransactionId());
	}
	
	@Test
	@DisplayName("Get transaction by Id - Not Found")
	void testNegativeGetTransactionById() {
		Transaction transaction = rewardServ.getTransactionByID("Dummy Id");
		assertNull(transaction);
	}
	
	@Test
	@DisplayName("Get CustomerReward by customerId - Found")
	void testGetCustomerReward() {
		CustomerReward customerReward = rewardServ.getCustomerReward("Cust1");
		assertNotNull(customerReward);
	}
	
	@Test
	@DisplayName("Get CustomerReward by customerId - Not Found")
	void testNegativeGetCustomerReward() {
		CustomerReward customerReward = rewardServ.getCustomerReward("Dummy Transaction");
		assertNull(customerReward);
	}
	
	@Test
	@DisplayName("Add Transaction - Success")
	void testAddTransaction() {
		Transaction transaction = new Transaction("53", "Cust52", 1000, "12/Jun/2022");
		assertTrue(rewardServ.addTransaction(transaction));
	}
	
	@Test
	@DisplayName("Add Transaction - Failed")
	void testNegativeAddTransaction() {
		Transaction transaction = new Transaction("1", "Cust52", 1000, "12/Jun/2022");
		assertFalse(rewardServ.addTransaction(transaction));
	}
	
	@Test
	@DisplayName("Update Transaction - Success")
	void testUpdateTransaction() {
		Transaction transaction = new Transaction("1", "Cust52", 1000, "12/Jun/2022");
		Transaction returnedTransaction = rewardServ.updateTransaction(transaction);
		assertNotNull(returnedTransaction);
		assertEquals("1", returnedTransaction.getTransactionId());
		assertEquals("Cust52", returnedTransaction.getCustomerId());
		
	}
	
	@Test
	@DisplayName("Update Transaction - Failed")
	void testNegativeUpdateTransaction() {
		Transaction transaction = new Transaction("81", "Cust52", 1000, "12/Jun/2022");
		assertNull(rewardServ.updateTransaction(transaction));
	}
	
	@Test
	@DisplayName("Remove Transaction - Success")
	void testRemoveTransaction() {
		assertTrue(rewardServ.removeTransaction("5"));
	}
	
	@Test
	@DisplayName("Remove Transaction - Failed")
	void testNegativeRemoveTransaction() {
		assertFalse(rewardServ.removeTransaction("Dummy Id"));
	}
	
	@Test
	@DisplayName("Calculate Reward Points - Success")
	void testCalculateRewardPoints() {
		assertEquals(250, rewardServ.calculateRewardPoints(200));
		assertEquals(50, rewardServ.calculateRewardPoints(100));
	}
	
	@Test
	@DisplayName("Calculate Reward Points - Negative")
	void testNegativeCalculateRewardPoints() {
		assertEquals(0, rewardServ.calculateRewardPoints(20));
		assertEquals(0, rewardServ.calculateRewardPoints(0));
	}
	
	@Test
	@DisplayName("Create CustomerReward - Success")
	void testCreateCustomerReward() {
		CustomerReward customerReward = rewardServ.createCustomerReward("Cust12", "Jul", 100);
		assertEquals("Cust12", customerReward.getCustomerId());
		assertEquals(100, customerReward.getTotalRewardPoints());
	}
}
