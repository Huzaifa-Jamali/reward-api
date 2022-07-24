package org.retailer.app.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Details about the Transaction")
@Entity
public class Transaction {
	@ApiModelProperty(notes = "The unique ID of the Transaction")
	@Id
	private String transactionId;
	@Column(nullable=false)
	private String customerId;
	@Column(nullable=false)
	private int purchasePrice;
	@Column(nullable=false)
	private String transactionDate;			//Format : dd/LLL/uuuu , eg : 05/Jul/2022
	
	public Transaction() {}
	
	public Transaction(String transactionId, String customerId, int purchasePrice, String transactionDate) {
		super();
		this.transactionId = transactionId;
		this.customerId = customerId;
		this.purchasePrice = purchasePrice;
		this.transactionDate = transactionDate;
	}

	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public int getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(int purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", customerId=" + customerId + ", purchasePrice=" + purchasePrice + ", transactionDate=" + transactionDate
				+ "]";
	}
}
