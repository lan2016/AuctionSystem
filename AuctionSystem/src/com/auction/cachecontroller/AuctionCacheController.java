package com.auction.cachecontroller;

import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.server.handler.ContextHandler.StaticContext;

import com.auction.pojos.AuctionItemPojo;
import com.auction.pojos.UserPojo;

/**
 * 
 * @author Rajat1.Bansal
 *
 */
public class AuctionCacheController {
	
	private static ConcurrentHashMap<String,UserPojo>tokenForUsers=new ConcurrentHashMap<String,UserPojo>();
	private static ConcurrentHashMap<String,AuctionItemPojo>runningAuctionsItemsMap=new ConcurrentHashMap<>();
	private static String ip;
	private static String port;
	public static String getIp() {
		return ip;
	}
	public static void setIp(String ip) {
		AuctionCacheController.ip = ip;
	}
	public static String getPort() {
		return port;
	}
	public static void setPort(String port) {
		AuctionCacheController.port = port;
	}
	public static ConcurrentHashMap<String, UserPojo> getTokenForUsers() {
		return tokenForUsers;
	}
	public static void setTokenForUsers(ConcurrentHashMap<String, UserPojo> tokenForUsers) {
		AuctionCacheController.tokenForUsers = tokenForUsers;
	}
	public static ConcurrentHashMap<String, AuctionItemPojo> getRunningAuctionsItemsMap() {
		return runningAuctionsItemsMap;
	}
	public static void setRunningAuctionsItemsMap(ConcurrentHashMap<String, AuctionItemPojo> runningAuctionsItemsMap) {
		AuctionCacheController.runningAuctionsItemsMap = runningAuctionsItemsMap;
	}
	
	

}
