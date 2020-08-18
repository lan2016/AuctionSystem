package com.auction.comparators;

import java.util.Comparator;

import com.auction.pojos.EntityPojo;

/**
 * 
 * @author Rajat1.Bansal
 *
 */
public class BidComparator implements Comparator<EntityPojo>{

	@Override
	public int compare(EntityPojo obj1, EntityPojo obj2) {
		// TODO Auto-generated method stub
		return obj2.getBidAmount()-obj1.getBidAmount();
	}

}
