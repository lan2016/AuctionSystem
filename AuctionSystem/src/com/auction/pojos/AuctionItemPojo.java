package com.auction.pojos;

import java.util.LinkedList;
import java.util.PriorityQueue;

import com.auction.comparators.BidComparator;

/**
 * 
 * @author Rajat1.Bansal
 *
 */
public class AuctionItemPojo {
	

	private String itemCode;
	private int stepRate;
	private int minimumBasePrice;
	private boolean runningStatus;
	private int currentPrice;
	private PriorityQueue<EntityPojo>usersBidsTrackingQueue=new PriorityQueue<>(new BidComparator());//this list will contain accept bit users data
	private LinkedList<EntityPojo>faliureData=new LinkedList<>();//rejected bid user data
	
	
	public LinkedList<EntityPojo> getFaliureData() {
		return faliureData;
	}


	public void setFaliureData(LinkedList<EntityPojo> faliureData) {
		this.faliureData = faliureData;
	}


	public int getCurrentPrice() {
		return usersBidsTrackingQueue.size()==0?minimumBasePrice:usersBidsTrackingQueue.peek().bidAmount;
	}
	
	
	public AuctionItemPojo(String itemCode, int stepRate, int minimumBasePrice, boolean runningStatus) {
		
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
	public int getStepRate() {
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
