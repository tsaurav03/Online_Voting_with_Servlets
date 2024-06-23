package com.app.pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.dao.UserDaoImp;
import com.app.entites.User;

/**
 * Servlet implementation class VoterRegistrationServlet
 */
@WebServlet("/register")
public class VoterRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDaoImp userDao;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException {

		try {
			userDao = new UserDaoImp();

		} catch (Exception e) {
			throw new ServletException("Error initializing VoterRegistrationServlet", e);

		}
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		try {
			userDao.cleanUp();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		try (PrintWriter pw = response.getWriter()) {

			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			Date dob = Date.valueOf(request.getParameter("dob"));

			LocalDate birthdate = dob.toLocalDate();
			LocalDate currentDate = LocalDate.now();

			int age = Period.between(birthdate, currentDate).getYears();

			if (age >= 21) {
				User newVoter = new User(firstName, lastName, email, password, dob, "voter");
				String result = userDao.voterRegistration(newVoter);

				if (result.equals("Voter registered..")) {
					pw.print("<h5>Registration successful!</h5>");
					pw.print("<a href='login.html'>Login Again</a>");

				} else {
					pw.print("<h5>Registration Failed!</h5>");
					pw.print("<a href='voter_register.html'>Back</a>");

				}
			} else {
				pw.print("<h5>Age must be 21 or Older to register</h5>");
				pw.print("<a href='voter_register.html'>Back</a>");
			}

		}

		catch (Exception e) {
			throw new ServletException("Error in doPost of VoterRegistrationServlet", e);
		}
	}
}
