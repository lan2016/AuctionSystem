package com.auction.pojos;

public class EntityPojo {

	int bidAmount;
	public EntityPojo(int bidAmount, UserPojo user) {
		this.bidAmount = bidAmount;
		this.user = user;
	}
	UserPojo user;
	public int getBidAmount() {
		return bidAmount;
	}
	public void setBidAmount(int bidAmount) {
		this.bidAmount = bidAmount;
	}
	public UserPojo getUser() {
		return user;
	}
	public void setUser(UserPojo user) {
		this.user = user;
	}
	
}
