package com.bpa.qaproduct.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bpa.qaproduct.entity.User;
import com.bpa.qaproduct.entity.UserToken;
import com.bpa.qaproduct.repository.UserTokenDAO;

@Service
public class UserTokenService {

	@Autowired
	private UserTokenDAO userTokenDAO;

	public String createEncodedToken(Integer userId) {

		// Creating Expire Time that is after three hour from current time
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, 3);
		Timestamp expireTime = new Timestamp(cal.getTimeInMillis());

		// Creating Random token
		SecureRandom random = new SecureRandom();
		String token = new BigInteger(130, random).toString(32);

		try {

			// Save User Token
			UserToken userToken = new UserToken();
			userToken.setUserId(userId);
			userToken.setToken(token);
			userToken.setExpireTime(expireTime);
			userTokenDAO.saveUserToken(userToken);

			// Return the Encoded the token
			return userToken.getEncodedToken();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public boolean validateToken(String tokenFromRequest, String loggedInUserId) {

		UserToken userToken = new UserToken();

		try {

			String decodedToken = userToken.getDecodedToken(tokenFromRequest);

			String[] tokenParams = decodedToken.split(":");

			userToken.setUserTokenId(Integer.parseInt(tokenParams[0]));
			userToken.setUserId(Integer.parseInt(loggedInUserId));

			userToken = userTokenDAO.getUserToken(userToken);

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			Timestamp currentTimeStamp = new Timestamp(cal.getTimeInMillis());

			if (userToken.getExpireTime().after(currentTimeStamp)) {
				return true;
			} else {
				return false;
			}

		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public UserToken getUserByToken(UserToken userToken) {
		return userTokenDAO.getUserToken(userToken);
	}

	public void removeUserToken(UserToken userToken) {
		userTokenDAO.removeUserToken(userToken);
		// TODO Auto-generated method stub

	}

}
