package org.retailer.app.repository;

import java.util.List;

import org.retailer.app.bean.Transaction;
import org.springframework.data.repository.CrudRepository;  

public interface RewardPointsRepository extends CrudRepository<Transaction, String> {
	
	public List<Transaction> findByCustomerId(String customerId);

}
