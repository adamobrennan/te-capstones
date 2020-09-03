package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.User;

public class UserService {
	public static String AUTH_TOKEN = "";
	private final String BASE_URL;
	public RestTemplate restTemplate = new RestTemplate();

	public UserService(String url) {
		BASE_URL = url;
	}

	public User[] getAllUsers(String token) {
		AUTH_TOKEN = token;
		User[] userArray = null;
		try {
			userArray = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, makeAuthEntity(), User[].class)
					.getBody();
		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());

		}
		return userArray;

	}
	public User findUserById(long id, String token) {
		AUTH_TOKEN = token;
		User theUser = new User();
		try {
			theUser = restTemplate.exchange(BASE_URL + "users/" + id, HttpMethod.GET, makeAuthEntity(), User.class)
					.getBody();
		} catch (RestClientResponseException e) {
			System.out.println(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
		}
		return theUser;
	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
}
