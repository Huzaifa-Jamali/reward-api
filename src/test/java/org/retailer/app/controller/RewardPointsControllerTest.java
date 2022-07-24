package org.retailer.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.retailer.app.bean.CustomerReward;
import org.retailer.app.bean.Transaction;
import org.retailer.app.repository.RewardPointsRepository;
import org.retailer.app.service.RewardPointsService;
import org.retailer.app.utility.RewardPointsUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RewardPointsControllerTest {

	@MockBean
	private RewardPointsService rewardPointsServ;
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private RewardPointsRepository rewardRepo;

	@Test
	@DisplayName("Get /transaction/reward - Found")
	void testGetAllRewardPoints() throws Exception {
		List<CustomerReward> mockCustomerRewardList = new ArrayList<CustomerReward>();
		mockCustomerRewardList.add(new CustomerReward());
		doReturn(mockCustomerRewardList).when(rewardPointsServ).getAllCustomerReward();

		mockMvc.perform(get("/transaction/reward")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}

	@Test
	@DisplayName("Get /transaction/reward - Not Found")
	void testNegativeGetAllRewardPoints() throws Exception {

		doReturn(null).when(rewardPointsServ).getAllCustomerReward();

		mockMvc.perform(get("/transaction/reward")).andExpect(status().isNotFound())
				.andExpect(content().string(RewardPointsUtility.NO_TRANSACTION));
	}

	@Test
	@DisplayName("Get /transaction/transactionId/{transactionId} - Found")
	void testGetTransactionByID() throws Exception {
		doReturn(rewardRepo.findById("1").get()).when(rewardPointsServ).getTransactionByID("1");

		mockMvc.perform(get("/transaction/transactionId/{transactionId}", "1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.customerId").value("Cust1"));
	}

	@Test
	@DisplayName("Get /transaction/transactionId/{transactionId} - Not Found")
	void testNegativeGetTransactionByID() throws Exception {
		doReturn(null).when(rewardPointsServ).getTransactionByID("Dummy ID");

		mockMvc.perform(get("/transaction/transactionId/{transactionId}", "Dummy ID")).andExpect(status().isNotFound())
				.andExpect(content().string(RewardPointsUtility.TRANSACTION_NOT_FOUND));
	}

	@Test
	@DisplayName("Get /transaction/transactionId/{ } - Not Found")
	void testNegatEmptyGetTransactionByID() throws Exception {
		doReturn(null).when(rewardPointsServ).getTransactionByID(" ");

		mockMvc.perform(get("/transaction/transactionId/{transactionId}", " ")).andExpect(status().isNotFound())
				.andExpect(content().string(RewardPointsUtility.TRANSACTION_NOT_FOUND));
	}

	@Test
	@DisplayName("Get /transaction/customerId/{customerId} - Found")
	void testGetCustomerReward() throws Exception {
		doReturn(new CustomerReward()).when(rewardPointsServ).getCustomerReward("Cust1");

		mockMvc.perform(get("/transaction/customerId/{customerId}", "Cust1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}

	@Test
	@DisplayName("Get /transaction/customerId/{customerId} - Not Found")
	void testNegativeGetCustomerReward() throws Exception {
		doReturn(null).when(rewardPointsServ).getCustomerReward("Dummy");

		mockMvc.perform(get("/transaction/customerId/{customerId}", "Dummy")).andExpect(status().isNotFound())
				.andExpect(content().string(RewardPointsUtility.CUSTOMER_NOT_FOUND));
	}

	@Test
	@DisplayName("Get /transaction/customerId/{ } - Not Found")
	void testNegatEmptyGetCustomerReward() throws Exception {
		doReturn(null).when(rewardPointsServ).getCustomerReward(" ");

		mockMvc.perform(get("/transaction/customerId/{customerId}", " ")).andExpect(status().isNotFound())
				.andExpect(content().string(RewardPointsUtility.CUSTOMER_NOT_FOUND));
	}

	@Test
	@DisplayName("Post /transaction - Created")
	void testAddTransaction() throws Exception {
		Transaction mockTransaction = rewardRepo.findById("1").get();
		doReturn(true).when(rewardPointsServ).addTransaction(any());
		doReturn(true).when(rewardPointsServ).isValidTransactionDate(any());

		mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_JSON).content(asJsonString(mockTransaction)))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}

	@Test
	@DisplayName("Post /transaction - Conflict")
	void testNegativeAddTransaction() throws Exception {
		Transaction mockTransaction = rewardRepo.findById("1").get();

		doReturn(false).when(rewardPointsServ).addTransaction(any());
		doReturn(true).when(rewardPointsServ).isValidTransactionDate(any());

		mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_JSON).content(asJsonString(mockTransaction)))
				.andExpect(status().isConflict()).andExpect(content().string(mockTransaction.getTransactionId() + " : " + RewardPointsUtility.TRANSACTION_ALREADY_EXISTS));

	}

	@Test
	@DisplayName("Patch /transaction - Success")
	void testUpdateTransaction() throws Exception {
		Transaction mockTransaction = rewardRepo.findById("1").get();

		doReturn(mockTransaction).when(rewardPointsServ).updateTransaction(any());
		doReturn(true).when(rewardPointsServ).isValidTransactionDate(any());

		mockMvc.perform(patch("/transaction").contentType(MediaType.APPLICATION_JSON).content(asJsonString(mockTransaction)))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}

	@Test
	@DisplayName("Patch /transaction - Not Found")
	void testNegativeUpdateTransaction() throws Exception {
		Transaction mockTransaction = rewardRepo.findById("1").get();

		doReturn(null).when(rewardPointsServ).updateTransaction(any());
		doReturn(true).when(rewardPointsServ).isValidTransactionDate(any());

		mockMvc.perform(patch("/transaction").contentType(MediaType.APPLICATION_JSON).content(asJsonString(mockTransaction)))
				.andExpect(status().isNotFound())
				.andExpect(content().string(mockTransaction.getTransactionId() + " : " + RewardPointsUtility.TRANSACTION_DOES_NOT_EXISTS));

	}

	@Test
	@DisplayName("Delete /transaction/{id} - Success")
	void testRemoveTransaction() throws Exception {
		String id = "3";
		doReturn(true).when(rewardPointsServ).removeTransaction(any());

		mockMvc.perform(delete("/transaction/{id}", id)).andExpect(status().isOk())
				.andExpect(content().string(id + " : " + RewardPointsUtility.TRANSACTION_REMOVED));
	}

	@Test
	@DisplayName("Delete /transaction/{id} - Not Found")
	void testNegativeRemoveTransaction() throws Exception {
		String id = "Dummy ID";
		doReturn(false).when(rewardPointsServ).removeTransaction(any());

		mockMvc.perform(delete("/transaction/{id}", id)).andExpect(status().isNotFound())
				.andExpect(content().string(id + " : " + RewardPointsUtility.TRANSACTION_DOES_NOT_EXISTS));
	}

	public String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
