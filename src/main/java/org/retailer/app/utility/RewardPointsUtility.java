package org.retailer.app.utility;

import java.util.List;
import java.util.Map;

public class RewardPointsUtility {
	
	public static final String CUSTOMER_NOT_FOUND = "Cannot find the customer you are looking for.";
	public static final String TRANSACTION_NOT_FOUND = "Cannot find the transaction you are looking for.";
	public static final String NO_TRANSACTION = "No transaction found.";
	public static final String TRANSACTION_ALREADY_EXISTS = "This transaction id already exists.";
	public static final String TRANSACTION_DOES_NOT_EXISTS = "This transaction does not exists.";
	public static final String TRANSACTION_REMOVED = "This transaction is successfully removed.";
	public static final String INVALID_TRANSACTION = "The transaction id must not be empty for a transaction.";
	public static final String INVALID_CUSTOMER_ID = "The customer id must not be empty for a transaction.";
	public static final String INVALID_PURCHASE_PRICE = "The purchase price of the transaction must not be negative.";
	public static final String INVALID_TRANSACTION_DATE = "Invalid transaction date, The transaction date must be in the format : dd/LLL/uuuu, eg : 05/Jul/2022.";
	
	public static boolean isMapEmpty(Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static boolean isListEmpty(List<?> list) {
		if (list == null || list.isEmpty()) {
			return true;
		}
		return false;
	}
}
