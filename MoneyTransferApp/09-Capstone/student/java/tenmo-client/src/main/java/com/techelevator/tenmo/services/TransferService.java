package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Transfer;

public class TransferService {

	public static String AUTH_TOKEN = "";
	private final String BASE_URL;
	public RestTemplate restTemplate = new RestTemplate();

	public TransferService(String url) {
		BASE_URL = url;
	}
	public Transfer sendTransfer(long senderId, long receiverId, BigDecimal transferAmount, String token) {
		AUTH_TOKEN = token;
		Transfer theTransfer = new Transfer();
		theTransfer.setAccountFrom(senderId);
		theTransfer.setAccountTo(receiverId);
		theTransfer.setAmount(transferAmount);
		
		try{
			theTransfer = restTemplate.postForObject(BASE_URL + "account/sendtransfer", makeTransferEntity(theTransfer), Transfer.class);
		} catch(RestClientResponseException e) {
			System.out.println(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
		}
		return theTransfer;
	}
	
	public Transfer[] viewAllTransfers(long id, String token) {
		AUTH_TOKEN = token;
		Transfer[] transferArray = null;
		try {
			transferArray = restTemplate.exchange(BASE_URL + "account/" + id + "/transfer", HttpMethod.GET, makeAuthEntity(), Transfer[].class)
					.getBody();
		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());

		}
		return transferArray;
	}
	
	public Transfer getTransferById(long userId, long transferId, String token) {
		AUTH_TOKEN = token;
		Transfer theTransfer = null;
		try {
			theTransfer = restTemplate.exchange(BASE_URL + "account/" + userId + "/transfer/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
		}
		return theTransfer;
	}
	
	
	
	 private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.setBearerAuth(AUTH_TOKEN);
	        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
	        return entity;
	    }

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
}
