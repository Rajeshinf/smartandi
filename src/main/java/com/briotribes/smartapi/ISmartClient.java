package com.briotribes.smartapi;

public interface ISmartClient {
	
	public void lookUp (String classToLookIn, Object lookForKey);
	
	public void search(String classToLookIn, String fieldToFind, Object havingValue, int size);
	
	public void list(String classToLookIn, String resultSize);
	
	public void createPrime(String createClass, Object dataToCreate);
	
	public void updatePrime(String createClass, Object dataToUpdate, Object keyValueForData);
	
	public void getListings(String primeClass, int pageNumber, int itemsPerPage);
	
	public void postDataTo(Object data, Object event, Object keyName, Object keyValue);
	
}
