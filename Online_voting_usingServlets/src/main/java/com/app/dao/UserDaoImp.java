package com.app.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.app.entites.User;

import static com.app.utils.DBUtils.*;

public class UserDaoImp implements UserDao {
	// state

	private Connection cn;
	private PreparedStatement pst1, pst2, pst3, pst4, pst5, pst6;//pst7, pst8;

	public UserDaoImp() throws SQLException {

		cn = openConnection();

		pst1 = cn.prepareStatement("select* from users where email=? and password=?");

		pst2 = cn.prepareStatement("select * from users where role='voter' and dob between ? and ?");

		pst3 = cn.prepareStatement("insert into users values(default,?,?,?,?,?,?,?)");

		pst4 = cn.prepareStatement("update users set password=? where email=? and password=?");

		pst5 = cn.prepareStatement("delete from users where id=?");
	//	pst6 = cn.prepareStatement("select * from users where role='candidate'");
		pst6= cn.prepareStatement("update users set status=1 where id=?");
	//	pst8 = cn.prepareStatement("update candidates set votes=votes+1 where id=?");
		System.out.println("user dao created...");

	}

	@Override
	public User signIn(String email, String password) throws SQLException {
		// 1. set IN params

		pst1.setString(1, email);
		pst1.setString(2, password);
		// 2. exec query

		try (ResultSet rst = pst1.executeQuery()) {
			// rst cursor : before the 1 st row

			if (rst.next())// => successful login

				return new User(rst.getInt(1), rst.getString(2), rst.getString(3), email, password, rst.getDate(6),
						rst.getBoolean(7), rst.getString(8));

		}
		return null;
	}

	@Override
	public List<User> getUserDetails(Date begin, Date end) throws SQLException {
		// 0. create empty list
		List<User> users = new ArrayList<>();

		// 1.set IN params
		pst2.setDate(1, begin);
		pst2.setDate(2, end);

		// 2. exec--exec query
		try (ResultSet rst = pst2.executeQuery()) {

			while (rst.next())
				users.add(new User(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4),
						rst.getString(5), rst.getDate(6), rst.getBoolean(7), rst.getString(8)));

		}

		return users;
	}

	@Override
	public String voterRegistration(User newVoter) throws SQLException {
		/*
		 * int userId, String firstName, String lastName, String email, String password,
		 * Date dob, boolean status, String role
		 */
		pst3.setString(1, newVoter.getFirstName());
		pst3.setString(2, newVoter.getLastName());
		pst3.setString(3, newVoter.getEmail());
		pst3.setString(4, newVoter.getPassword());
		pst3.setDate(5, newVoter.getDob());
		pst3.setBoolean(6, newVoter.isStatus());
		pst3.setString(7, newVoter.getRole());

		// exec: executeUpdate

		int rows = pst3.executeUpdate();
		if (rows == 1)
			return "Voter registered..";
		return " Voter registered failed...";

	}

	@Override
	public String changePassword(String email, String oldPwd, String newPwd) throws SQLException {
		// 1.set IN params
		pst4.setString(1, newPwd);
		pst4.setString(2, email);
		pst4.setString(3, oldPwd);
		// 2. exec update : DML
		int rows = pst4.executeUpdate();
		if (rows == 1)
			return "Password Changed !";

		return "Changing password failed (invalid credentials)";
	}

	@Override
	public String deleteVoterDetails(int voterId) throws SQLException {
		pst5.setInt(1, voterId);
		int rows = pst5.executeUpdate();
		return rows == 1 ? "voter deatils deleted." : "Failed to delete voter details.";

	}

	// add cleanUp method to close DB resorces
	public void cleanUp() throws SQLException {
		System.out.println("user dao cleaned up");
		if (pst1 != null)
			pst1.close();
		if (pst2 != null)
			pst2.close();
		if (pst3 != null)
			pst3.close();
		if (pst4 != null)
			pst4.close();
		if (pst5 != null)
			pst5.close();
		if (pst6 != null)
			pst6.close();
		/*
		 * if (pst7 != null) pst7.close();
		 *  if (pst8 != null) pst8.close();
		 */
		closeConnection();
	}

	/*
	 * @Override public List<User> getAllCandidates() throws SQLException {
	 * List<User> candidates = new ArrayList<>(); try (ResultSet rst =
	 * pst6.executeQuery()) { while (rst.next()) { candidates.add(new
	 * User(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4),
	 * rst.getString(5), rst.getDate(6), rst.getBoolean(7), rst.getString(8))); }
	 * 
	 * } return candidates; }
	 */

	@Override
	public String updateVotingStatus(int voterId) throws SQLException {
	//	pst6.setBoolean(1, status);
		pst6.setInt(1, voterId);
		int rows = pst6.executeUpdate();
		return rows == 1 ? "Voting Status updated" : "Failed to upadate voting Status";
	}

	/*
	 * @Override public String incrementCandidateVotes(int candidateId) throws
	 * SQLException { pst8.setInt(1, candidateId); int rows = pst8.executeUpdate();
	 * return rows == 1 ? "candidate votes incremented." :
	 * "Failed to increment votes"; }
	 */

	

}
