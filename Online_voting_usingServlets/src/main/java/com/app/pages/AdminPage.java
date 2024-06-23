package com.app.pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.app.utils.DBUtils.*;

/**
 * Servlet implementation class AdminPage
 */
@WebServlet("/admin_page")
public class AdminPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection cn;
	private PreparedStatement pst1, pst2;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException {
		try {
			cn = openConnection();
			pst1 = cn.prepareStatement("SELECT name, party, votes FROM candidates ORDER BY votes DESC LIMIT 2");
			pst2 = cn.prepareStatement("SELECT party, SUM(votes) as total_votes FROM candidates GROUP BY party");

		} catch (SQLException e) {
			throw new ServletException("Error initializing servlet", e);

		}
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		try {
			if (pst1 != null)
				pst1.close();
			if (pst2 != null)
				pst2.close();
			if (cn != null)
				cn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			ResultSet rst1 = pst1.executeQuery();
			List<String> topCandidates = new ArrayList<>();
			while (rst1.next()) {
				topCandidates.add("Name: " + rst1.getString("name") + ", Party: " + rst1.getString("party")
						+ ", Votes: " + rst1.getInt("votes"));
			}

			// Get votes analysis (political party wise)
			ResultSet rs2 = pst2.executeQuery();
			List<String> partyVotes = new ArrayList<>();
			while (rs2.next()) {
				partyVotes.add(rs2.getString("party") + ": " + rs2.getInt("total_votes"));
			}

			// Display the results
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<h1>Top 2 Candidates</h1>");
			for (String candidate : topCandidates) {
				out.println(candidate + "<br>");
			}
			out.println("<h1>Votes Analysis (Political Party Wise)</h1>");
			out.println("<ul>");
			for (String partyVote : partyVotes) {
				out.println("<li>" + partyVote + "</li>");
			}
			out.println("</ul>");

		} catch (SQLException e) {
			throw new ServletException("Error processing request", e);
		}
	}

}
