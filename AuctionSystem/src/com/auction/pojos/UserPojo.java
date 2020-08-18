package com.auction.pojos;

/**
 * 
 * @author Rajat1.Bansal
 *
 */
public class UserPojo {
	public UserPojo(String userId, String tokenID) {
		
		this.userId = userId;
		this.tokenID = tokenID;
	}
	String userId;
	String tokenID;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTokenID() {
		return tokenID;
	}
	public void setTokenID(String tokenID) {
		this.tokenID = tokenID;
	}
	
	

}
