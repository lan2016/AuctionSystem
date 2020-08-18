package com.auction.handlers;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.auction.constants.AuctionConstants;
import com.auction.service.AuctionAPIS;

public class AuctionHandler extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {

		try {
			String userName,tokenID,itemcode,bidAmount;
			AuctionAPIS apiObj=new AuctionAPIS();
			String operationType = request.getParameter(AuctionConstants.OPERATION_TYPE);
			switch (operationType) {
			case AuctionConstants.GET_RUNNING_AUCTIONS:
				JSONArray jsonArray=apiObj.getRunningAuctions();
				response.getWriter().println(jsonArray);


				break;
				
			case AuctionConstants.LOGIN:
				 userName=request.getHeader(AuctionConstants.USER_NAME);
				 tokenID=apiObj.loginIntoAuction(userName);
				if(tokenID!=null) {
					JSONObject jsonObject=new JSONObject();
					jsonObject.put(AuctionConstants.TOKEN_ID, tokenID);
					response.setStatus(HttpServletResponse.SC_ACCEPTED);
					response.getWriter().println(jsonObject);
				}
				else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
				
				
				break; 
				
			case AuctionConstants.PLACE_BID:
				
				 userName=request.getHeader(AuctionConstants.USER_NAME);
				 tokenID=request.getHeader(AuctionConstants.TOKEN_ID);
				 bidAmount=request.getHeader(AuctionConstants.BID_AMOUNT);
				 itemcode=request.getHeader(AuctionConstants.ITEM_CODE);
				 
				 if(apiObj.placeBid(userName, tokenID, itemcode, Integer.parseInt(bidAmount))) {
					 response.setStatus(201);
					 
				 }
				 response.setStatus(406);
				 
				
				break; 

			default:
				response.setStatus(404);
				break;
			}

			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
