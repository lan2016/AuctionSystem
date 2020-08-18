package com.auction.pojos;

import java.util.PriorityQueue;

import com.auction.comparators.BidComparator;

/**
 * 
 * @author Rajat1.Bansal
 *
 */
public class ItemPojo {
	

	String itemCode;
	int stepRate;
	int minimumBasePrice;
	boolean runningStatus;
	PriorityQueue<EntityPojo>usersBidsTrackingQueue=new PriorityQueue<>(new BidComparator());
	
	public ItemPojo(String itemCode, int stepRate, int minimumBasePrice, boolean runningStatus) {
		
		this.itemCode = itemCode;
		this.stepRate = stepRate;
		this.minimumBasePrice = minimumBasePrice;
		this.runningStatus = runningStatus;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public long getStepRate() {
		return stepRate;
	}
	public void setStepRate(int stepRate) {
		this.stepRate = stepRate;
	}
	public long getMinimumBasePrice() {
		return minimumBasePrice;
	}
	public void setMinimumBasePrice(int minimumBasePrice) {
		this.minimumBasePrice = minimumBasePrice;
	}
	public boolean isRunningStatus() {
		return runningStatus;
	}
	public void setRunningStatus(boolean runningStatus) {
		this.runningStatus = runningStatus;
	}
	public PriorityQueue<EntityPojo> getUsersBidsTrackingQueue() {
		return usersBidsTrackingQueue;
	}
	public void setUsersBidsTrackingQueue(PriorityQueue<EntityPojo> usersBidsTrackingQueue) {
		this.usersBidsTrackingQueue = usersBidsTrackingQueue;
	}
	
	

}
