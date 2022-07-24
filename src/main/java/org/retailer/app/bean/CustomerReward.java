package org.retailer.app.bean;

import java.util.HashMap;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Details about the Customer Reward Points")
public class CustomerReward {
	@ApiModelProperty(notes = "The unique ID of the customer")
	private String customerId;
	@ApiModelProperty(notes = "Map with month as key and reward points earned in that month as value")
	private HashMap<String, Integer> rewardPointsPerMonth;
	@ApiModelProperty(notes = "The total reward points earned by the customer")
	private int totalRewardPoints;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public HashMap<String, Integer> getRewardPointsPerMonth() {
		return rewardPointsPerMonth;
	}
	public void setRewardPointsPerMonth(HashMap<String, Integer> rewardPointsPerMonth) {
		this.rewardPointsPerMonth = rewardPointsPerMonth;
	}
	public int getTotalRewardPoints() {
		return totalRewardPoints;
	}
	public void setTotalRewardPoints(int totalRewardPoints) {
		this.totalRewardPoints = totalRewardPoints;
	}

}
