package com.ak.poc.messagebus.springbus.consumer;

import java.util.ArrayList;
import java.util.List;

public final class PartitionOffsetHolder {

	private final static List<Long> offsetList = new ArrayList<Long>();
	
	private PartitionOffsetHolder() {
		
	}
	
	public static void addOffset(Long offset){
		offsetList.add(offset);
	}
	
	public static void removeOffset(Long offset){
		offsetList.remove(offset);
	}
	
	public static Long getOffset(Long offset){
		return offsetList.get(offset.intValue());
	}
	
	public static int getSize(){
		return offsetList.size();
	}
}
