package com.auction.service;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.auction.cachecontroller.AuctionCacheController;
import com.auction.handlers.AuctionHandler;
import com.auction.pojos.EntityPojo;
import com.auction.pojos.AuctionItemPojo;
import com.auction.pojos.UserPojo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class AuctionAPIS {
	
	private static Logger logger =
			LogManager.getLogger(AuctionAPIS.class);
	
	public JSONArray getRunningAuctions()
	{
		
		JSONArray responseArray = null;
		try {
			String itemCode;
			responseArray=new JSONArray();
			ConcurrentHashMap<String,AuctionItemPojo>itemMap=AuctionCacheController.getRunningAuctionsItemsMap();
			Iterator<String>itemItr=itemMap.keySet().iterator();
			JSONObject jsonObject;
			AuctionItemPojo itemPojo;
			while(itemItr.hasNext())
			{
				itemCode=itemItr.next();
				itemPojo=itemMap.get(itemCode);
				jsonObject=new JSONObject();
				jsonObject.put("item code", itemCode);
				jsonObject.put("step rate", itemPojo.getStepRate());
				jsonObject.put("highest bid amount",itemPojo.getUsersBidsTrackingQueue().size()!=0?
						itemPojo.getUsersBidsTrackingQueue().peek().getBidAmount():itemPojo.getMinimumBasePrice());
				
				responseArray.put(jsonObject);
			}
			
			logger.info("Response Array for Running Auctions:"+responseArray);
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception",e);
			e.printStackTrace();
		}
		return responseArray;
		
	}
	
	public boolean placeBid(String userID,String token,String itemcode,int bidAmount)
	{
		
		
		try {
			
			if(!checkAuthenticateUser(token, userID)||!checkItemExists(itemcode))
			{
				logger.error("token invalid or user invalid"+userID+"---"+token+"---"+bidAmount+"---"+itemcode);
				return false;
			}
			synchronized (this) {
				
				AuctionItemPojo itemPojo=AuctionCacheController.getRunningAuctionsItemsMap().get(itemcode);
				UserPojo userPojo=AuctionCacheController.getTokenForUsers().get(token);
				if(itemPojo.getUsersBidsTrackingQueue().size()==0&&bidAmount<itemPojo.getMinimumBasePrice()) {
					logger.error("Bid price is minimum then base price"+userID+"---"+token+"---"+bidAmount+"---"+itemcode);
					return false;
				}
				if(itemPojo.getUsersBidsTrackingQueue().size()!=0&&bidAmount<itemPojo.getUsersBidsTrackingQueue().peek().getBidAmount()+itemPojo.getStepRate()) {
					logger.error("Bid price is minimum then lastPrice+step rate"+userID+"---"+token+"---"+bidAmount+"---"+itemcode);
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
			logger.error("exception",e);
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
			logger.error("exception",e);
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
		    
		    logger.info("Generated Token for User"+userID+"---"+token);
		    return token;
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("excepton",e);
			e.printStackTrace();
		}
		return null;
	}

}
