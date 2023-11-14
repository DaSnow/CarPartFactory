package main;

import interfaces.Map;

public class Order {

	private int id;
	private String customerName;
	private Map<Integer, Integer> requestedParts;
	private boolean fulfilled;

	/**
	 * Creates new instance of Order
	 * 
	 * @param id
	 * @param customerName
	 * @param requestedParts
	 * @param fulfilled
	 */
	public Order(int id, String customerName, Map<Integer, Integer> requestedParts, boolean fulfilled) {
		this.id = id;
		this.customerName = customerName;
		this.requestedParts = requestedParts;
		this.fulfilled = fulfilled;
	}

	/**
	 * Gets ID of instance
	 * 
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Changes ID of instance
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets status of instance
	 * 
	 * @return fulfilled
	 */
	public boolean isFulfilled() {
		return this.fulfilled;
	}

	/**
	 * Changes status of instance
	 * 
	 * @param fulfilled
	 */
	public void setFulfilled(boolean fulfilled) {
		this.fulfilled = fulfilled;
	}

	/**
	 * Gets requestedParts map of instance
	 * 
	 * @return requestedParts
	 */
	public Map<Integer, Integer> getRequestedParts() {
		return this.requestedParts;
	}

	/**
	 * Changes requestedParts of instance
	 * 
	 * @param requestedParts
	 */
	public void setRequestedParts(Map<Integer, Integer> requestedParts) {
		this.requestedParts = requestedParts;
	}

	/**
	 * Gets customer of instance
	 * 
	 * @return
	 */
	public String getCustomerName() {
		return this.customerName;
	}

	/**
	 * Changes customer of instance
	 * 
	 * @param customerName
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * Returns the order's information in the following format: {id} {customer name}
	 * {number of parts requested} {isFulfilled}
	 */
	@Override
	public String toString() {
		return String.format("%d %s %d %s", this.getId(), this.getCustomerName(), this.getRequestedParts().size(),
				(this.isFulfilled()) ? "FULFILLED" : "PENDING");
	}

}
