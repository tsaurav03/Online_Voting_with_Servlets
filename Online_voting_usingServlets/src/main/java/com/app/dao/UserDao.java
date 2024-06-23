package com.app.dao;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.app.entites.User;

public interface UserDao {

	// add a method for User's signin

	User signIn(String email, String password) throws SQLException;

	List<User> getUserDetails(Date begin, Date end) throws SQLException;

	String voterRegistration(User newVoter) throws SQLException;

	String changePassword(String email, String oldPwd, String newPwd) throws SQLException;

	String deleteVoterDetails(int voterId) throws SQLException;
    // new methods
	
	// add a method to update voting status
		String updateVotingStatus(int voterId) throws SQLException;
}
