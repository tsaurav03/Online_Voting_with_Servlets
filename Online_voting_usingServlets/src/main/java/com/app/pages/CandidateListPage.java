package com.app.pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.app.dao.CandidateDaoImp;
import com.app.entites.User;
import com.app.entites.Candidate;

/**
 * Servlet implementation class CandidateListPage
 */
@WebServlet("/candidate_list")
public class CandidateListPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1.set resp cont type

		response.setContentType("text/html");
		// 2.PW
		try (PrintWriter pw = response.getWriter()) {
			pw.print("<h5>IN candidate list page<h5>");
         // 3. get HttpSession from WC
			
			HttpSession hs = request.getSession();
			
			System.out.println("session new" + hs.isNew());
			System.out.println("session ID"+hs.getId());
			
			// 4.get user details from HttpSession
			
			User userInfo =(User) hs.getAttribute("user_details");
			
			if(userInfo != null) {
				// get candidate dao from session
				
				CandidateDaoImp dao =(CandidateDaoImp)hs.getAttribute("candidate_dao");
				
				pw.print("<h5> Hello , " + userInfo.getFirstName() + " " + userInfo.getLastName() + "</h5>");
//				//5. get candidate list from candidate dao
			List<Candidate> candidates = dao.getAllCandidates();
			// dynamic form generation
			pw.print("<form action='logout' method='post'>");
			// iterate over candidate list
			for (Candidate c : candidates)
				pw.print("<h5> <input type='radio' "
						+ "name='candidate_id' "
						+ "value='" + c.getCandidateId() + "'/>"
						+ c.getName() + "</h5>");
			//submit button
			pw.print("<h6> <input type='submit' value='Cast A Vote'/></h6>");
			pw.print("</form>");

			} else
				pw.print("<h5> can't remember the clnt, NO cookies !!</h5>");
		}
		catch(Exception e) {
			
			throw new ServletException("ee in do-get of"+getClass(),e);
			
		}

	}

}
