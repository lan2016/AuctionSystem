package com.auction.service;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.auction.cachecontroller.AuctionCacheController;
import com.auction.pojos.EntityPojo;
import com.auction.pojos.ItemPojo;
import com.auction.pojos.UserPojo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class AuctionAPIS {
	
	public JSONArray getRunningAuctions()
	{
		
		JSONArray responseArray = null;
		try {
			String itemCode;
			responseArray=new JSONArray();
			ConcurrentHashMap<String,ItemPojo>itemMap=AuctionCacheController.getRunningAuctionsItemsMap();
			Iterator<String>itemItr=itemMap.keySet().iterator();
			JSONObject jsonObject;
			ItemPojo itemPojo;
			while(itemItr.hasNext())
			{
				itemCode=itemItr.next();
				itemPojo=itemMap.get(itemCode);
				jsonObject=new JSONObject();
				jsonObject.put("item code", itemCode);
				jsonObject.put("step rate", itemPojo.getStepRate());
				jsonObject.put("highest bid amount",itemPojo.getUsersBidsTrackingQueue().peek().getBidAmount());
				
				responseArray.put(jsonObject);
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return responseArray;
		
	}
	
	public boolean placeBid(String userID,String token,String itemcode,int bidAmount)
	{
		
		
		try {
			
			if(!checkAuthenticateUser(token, userID)||!checkItemExists(itemcode))
			{
				return false;
			}
			synchronized (this) {
				
				ItemPojo itemPojo=AuctionCacheController.getRunningAuctionsItemsMap().get(itemcode);
				UserPojo userPojo=AuctionCacheController.getTokenForUsers().get(token);
				if(bidAmount<itemPojo.getUsersBidsTrackingQueue().peek().getBidAmount()+itemPojo.getStepRate()) {
					return false;
				}
				
				itemPojo.getUsersBidsTrackingQueue().add(new EntityPojo(bidAmount,userPojo));
				
				return true;
				
			}
			
			
			}
			
			
		 catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
		}
		return false;
		
		
	}
	
	public boolean checkAuthenticateUser(String token,String userId)
	{
		try {
			
			if(!AuctionCacheController.getTokenForUsers().containsKey(token)||!AuctionCacheController.getTokenForUsers().get(token).getUserId().equals(userId)) {
				return false;
			}
			return true;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
		
	}
	
	public boolean checkItemExists(String itemId)
	{
		try {
			
			if(!AuctionCacheController.getRunningAuctionsItemsMap().containsKey(itemId)) {
				return false;
			}
			return true;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
		
	}
	
	public String loginIntoAuction(String userID)
	{
		try {
			
			Algorithm algorithm = Algorithm.HMAC256("secret");
		    String token = JWT.create()
		        .withIssuer(""+Math.random())
		        .sign(algorithm);
		    
		    AuctionCacheController.getTokenForUsers().put(token, new UserPojo(userID,token));
		    
		    return token;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
