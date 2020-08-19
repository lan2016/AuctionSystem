package com.auction.junit;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.core.config.Order;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.auction.cachecontroller.AuctionCacheController;
import com.auction.constants.AuctionConstants;
import com.auction.jetty.AuctionJettyServer;
import com.auction.pojos.AuctionItemPojo;

import junit.framework.TestCase;

public class JUnitTestCases extends TestCase {

	ConcurrentHashMap<String, AuctionItemPojo> runningAuctionMap = null;
	HttpClient client = null;
	HttpResponse response = null;
	HttpGet request = null;
	{
		RequestConfig.Builder requestBuilder = RequestConfig.custom();
		requestBuilder.setConnectTimeout(10);
		requestBuilder.setConnectionRequestTimeout(10);

		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setDefaultRequestConfig(requestBuilder.build());
		client = builder.build();
	}
	static boolean flag = false;

	@Before
	public void setUp() {
		if (!flag) {
			System.out.println("test..");
			runningAuctionMap = new ConcurrentHashMap<>();
			runningAuctionMap.put("123", new AuctionItemPojo("123", 250, 1000, true));
			AuctionCacheController.setRunningAuctionsItemsMap(runningAuctionMap);
			assertEquals(true, AuctionJettyServer.startJetty("localhost", "3456"));
			flag = true;
		}

	}

	@Test
	public void testJetty() {

	}

	@Test
	/**
	 * Get Running Auctions(Success Case)
	 */

	public void testgetRunningAuctionsSuccess() {

		try {
			String query = "http://localhost:" + "3456" + "/auction?operation_type=getRunningAuctions";
			System.out.println("Query:" + query);
			request = new HttpGet(query);
			System.out.println("Request:" + request);
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			JSONArray responseArray = new JSONArray(responseString.toString());
			assertEquals(AuctionCacheController.getRunningAuctionsItemsMap().get("123").getStepRate()+"", responseArray.getJSONObject(0).get("step rate")+"");
			assertEquals("123", responseArray.getJSONObject(0).get("item code"));
			assertEquals(AuctionCacheController.getRunningAuctionsItemsMap().get("123").getCurrentPrice()+"", responseArray.getJSONObject(0).get("highest bid amount")+"");
			request.releaseConnection();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
			request.releaseConnection();
			assertFalse(true);

		}
	}
	
	@Test
	/**
	 * Get Running Auctions(Failiure Case...URL Invalid)
	 */
	public void testgetRunningAuctionsFaliure() {

		try {
			String query = "http://localhost:" + "4567" + "/auction?operation_type=getRunningAuctions";
			System.out.println("Query:" + query);
			request = new HttpGet(query);
			System.out.println("Request:" + request);
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			JSONArray responseArray = new JSONArray(responseString.toString());
			assertEquals(AuctionCacheController.getRunningAuctionsItemsMap().get("123").getStepRate()+"", responseArray.getJSONObject(0).get("step rate")+"");
			assertEquals("123", responseArray.getJSONObject(0).get("item code"));
			
			assertEquals(AuctionCacheController.getRunningAuctionsItemsMap().get("123").getCurrentPrice()+"", responseArray.getJSONObject(0).get("highest bid amount")+"");
			request.releaseConnection();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			request.releaseConnection();
			assertTrue(true);

		}
	}

	@Test
	/**
	 * Get Running Auctions(BID ACCEPTED CASE)
	 */
	public void testPlaceBidAccepted() {

		try {
			String query = "http://localhost:" + "3456" + "/auction?operation_type=login";
			request = new HttpGet(query);
			request.setHeader("userName", "bansal");
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");

			assertTrue(responseString != null);
			request.releaseConnection();

			query = "http://localhost:" + "3456" + "/auction?operation_type=placeBid";
			request = new HttpGet(query);
			request.setHeader("userName", "bansal");
			request.setHeader("bidAmount", "2000");
			request.setHeader("itemCode", "123");
			request.setHeader("Authorization",
					(new JSONObject(responseString)).get(AuctionConstants.TOKEN_ID).toString());
			response = client.execute(request);
			assertEquals(201, response.getStatusLine().getStatusCode());

			request.releaseConnection();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.releaseConnection();
			assertFalse(true);

		}

	}

	@Test
	/**
	 * Get Running Auctions(BID Rejected CASE)
	 */
	public void testPlaceBidRejected() {

		try {
			String query = "http://localhost:" + "3456" + "/auction?operation_type=login";
			request = new HttpGet(query);
			request.setHeader("userName", "bansal");
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");

			assertTrue(responseString != null);
			request.releaseConnection();

			query = "http://localhost:" + "3456" + "/auction?operation_type=placeBid";
			request = new HttpGet(query);
			request.setHeader("userName", "bansal");
			request.setHeader("bidAmount", "500");
			request.setHeader("itemCode", "123");
			request.setHeader("Authorization",
					(new JSONObject(responseString)).get(AuctionConstants.TOKEN_ID).toString());
			response = client.execute(request);
			assertEquals(406, response.getStatusLine().getStatusCode());

			request.releaseConnection();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.releaseConnection();
			assertFalse(true);

		}

	}
	
	 
}
