package ap.db;
import ap.helper.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Createdb
 */
@WebServlet("/createDatabase")
public class Createdb extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	String dbName;
	String uname;
	String pass;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		FacileCookie cookies = new FacileCookie(request,response);
		
		dbName = request.getParameter("db");
		
		if(!cookies.exists("uname") || !cookies.exists("uname")){
			response.sendRedirect("index.jsp");
		}else if(dbName == null || dbName.isEmpty())
			response.sendRedirect("home.jsp");
		
		uname = cookies.getValue("uname");
		pass = cookies.getValue("pass");
		
		String notification = null;
		
		FacileDb db = new FacileDb("",uname,pass);
		ConnectionResult cr = db.getConnectionResult();
		if(!cr.isError()){
			String query = "CREATE DATABASE `" + dbName + "`";
			int rows = db.executeUpdate(query);
			if(rows == 0){
				notification = "<div class=\"alert alert-warning\">Error Creating Database</div>";
			}
			else{
				notification = "<div class=\"alert alert-success\">Database Created Successfully</div>";
			}
			request.getSession().setAttribute("message", notification);
			response.sendRedirect("home.jsp");
		}
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("home.jsp");
	}

}
