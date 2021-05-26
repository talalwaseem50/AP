package ap.db;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import ap.helper.*;

@WebServlet("/RenameTable")
public class RenameTable extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	String dbName;
	String old_tname;
	String new_tname;
	String uname;
	String pass;
	int flag ;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		flag = 0;
		
		JasperCookie cookies = new JasperCookie(request,response);
		
		dbName = request.getParameter("db");
		new_tname = request.getParameter("table");
		old_tname = request.getParameter("old_table");
		
		if(!cookies.exists("uname") || !cookies.exists("uname")){
			response.sendRedirect("index.jsp");
			return;
		}else if(dbName == null || "".equals(dbName))
		{
			response.sendRedirect("home.jsp");
			return;
		}else if(old_tname == null || "".equals(old_tname))
		{
			response.sendRedirect("table.jsp?db="+dbName);
			return;
		}
		
		uname = cookies.getValue("uname");
		pass = cookies.getValue("pass");
		
		String notification = null;
		
		JasperDb db = new JasperDb(dbName,uname,pass);
		ConnectionResult cr = db.getConnectionResult();
		if(!cr.isError()){
			
			QueryResult qr = db.executeQuery("SHOW TABLES IN `" + dbName + "`");
			if(!qr.isError())
			{
				ResultSet rs = qr.getResult();
				try {
					while(rs.next())
					{
						String tname = rs.getString("Tables_in_"+dbName);
						if (tname.equals(new_tname)) {
							notification = "<div class=\"alert alert-danger\"> Table `"+new_tname+"` Already Exists" + "</div>";
							request.getSession().setAttribute("message", notification);
							response.sendRedirect("table.jsp?db="+dbName);
							return;
						}
					}
					rs.close();
				} catch(SQLException ex) {
					System.err.println("SQLException: " + ex.getMessage());
				}
			}
			
			String query = "RENAME TABLE `" + old_tname + "` TO `" + new_tname + "`";
			
			int rows = db.executeUpdate(query);
			
			qr = db.executeQuery("SHOW TABLES IN " + dbName);
			if(!qr.isError())
			{
				
				ResultSet rs = qr.getResult();
				try {
					while(rs.next())
					{
						String tname = rs.getString("Tables_in_"+dbName);
						if (tname.equals(new_tname)) {
							flag = 1;
							break;
						}
					}
					rs.close();
				} catch(SQLException ex) {
                    System.err.println("SQLException: " + ex.getMessage());
				}
				if (flag == 1) {
					notification = "<div class=\"alert alert-success\"> Table Renamed Successfully<br>" + query + ";</div>";
				}else {
					notification = "<div class=\"alert alert-danger\"> Error in Renaming Table<br>" + query + ";</div>";
				}
			}
			db.close();
			request.getSession().setAttribute("message", notification);
			response.sendRedirect("table.jsp?db="+dbName);
		}
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("home.jsp");
	}

}
