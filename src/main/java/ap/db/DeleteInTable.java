package ap.db;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ap.helper.AsciiString;
import ap.helper.ConnectionResult;
import ap.helper.JasperCookie;
import ap.helper.JasperDb;

@WebServlet("/deleteInTable")
public class DeleteInTable extends HttpServlet{
	private static final long serialVersionUID = 1L;
	String dbName;
	String tname;
	String uname;
	String pass;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JasperCookie cookies = new JasperCookie(request,response);
		
		dbName = request.getParameter("db");
		tname = request.getParameter("table");
		String data = request.getParameter("data");
		
		if(!cookies.exists("uname") || !cookies.exists("uname"))
		{
			response.sendRedirect("index.jsp");
			return;
		}
		else if(dbName == null || dbName.isEmpty())
		{
			response.sendRedirect("home.jsp");
			return;
		}
		else if(tname == null || tname.isEmpty() || data == null || data.isEmpty())
		{
			response.sendRedirect("table.jsp?db="+dbName);
			return;
		}
		
		uname = cookies.getValue("uname");
		pass = cookies.getValue("pass");
		
		String notification = null;
		String actualData = AsciiString.getStringFromAscii(data);
		
		JasperDb db = new JasperDb(dbName,uname,pass);
		ConnectionResult cr = db.getConnectionResult();
		if(!cr.isError()){
			String query = "DELETE FROM `" + tname + "` WHERE " + actualData + " LIMIT 1";
			int rows = db.executeUpdate(query);
			if(rows == 0){
				notification = "<div class=\"alert alert-danger\">"+
									"Error Deleting Data<br>"+
									query+";"+
								"</div>";
			}
			else{
				notification = "<div class=\"alert alert-success\">"+
									"Deleted Successfully<br>"+
									query+";"+
								"</div>";
			}
		}
		request.getSession().setAttribute("message", notification);
		response.sendRedirect("tablecontent.jsp?db="+dbName+"&table="+tname);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.sendRedirect("home.jsp");
	}

}
