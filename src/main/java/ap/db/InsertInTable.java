
package ap.db;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ap.helper.*;
import java.sql.*;

/**
 * Servlet implementation class InsertInTable
 */
@WebServlet("/insertInTable")
public class InsertInTable extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    String uname;
    String pass;
    String dbName;
    String tname;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect("home.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		JasperCookie cookies = new JasperCookie(request,response);
		
		dbName = request.getParameter("db");
		tname = request.getParameter("table");
		
		if(!cookies.exists("uname") || !cookies.exists("uname")){
			response.sendRedirect("index.jsp");
			return;
		}else if(dbName == null || dbName.isEmpty()) {
			response.sendRedirect("home.jsp");
			return;
		}else if(tname == null || tname.isEmpty()){
			response.sendRedirect("table.jsp?db="+dbName);
			return;
		}
		
		
		uname = cookies.getValue("uname");
		pass = cookies.getValue("pass");
		
		String notification = null;
		JasperDb db = new JasperDb("information_schema",uname,pass);
		JasperDb db1 = new JasperDb(dbName, uname, pass);
		String query = "INSERT INTO `" + tname + "` VALUES ( ";
		if(db.getConnectionResult().isError())
		{
			notification = "<div class=\"alert alert-danger\">"+
								"Error Reading Database information_schema <br>"+
								query+";"+
							"</div>";
			request.getSession().setAttribute("message", notification);
			response.sendRedirect("home.jsp");
			return;
		}
		QueryResult qr = db.executeQuery("select * from COLUMNS where TABLE_SCHEMA = \""+dbName+"\" and TABLE_NAME = \""+tname+"\"");
		if(!qr.isError())
		{
			ResultSet rs = qr.getResult();
			try {
				while(rs.next())
				{
					String name = rs.getString("COLUMN_NAME");
					String is_nullable = rs.getString("IS_NULLABLE");
					String value = request.getParameter(name);
					String data_type = rs.getString("DATA_TYPE");
					String col_default = rs.getString("COLUMN_DEFAULT");
					
					if (value != null) {
						if(value.isEmpty())
						{
							if(col_default == null)
								query = query + "NULL" + ", ";
							else
								query = query + "'"+col_default+"'" + ", ";
						}
						else if(data_type.equals("tinyint"))
							query = query + value + ", ";
						else
							query = query + "'" + value + "'" + ", ";
					}
				}
			} catch(SQLException ex) {
				notification = "<div class=\"alert alert-danger\">"+
									"SQLError ("+ex.getSQLState()+") <br>"+
									ex.getMessage()+
								"</div>";
                System.err.println("SQLException: " + ex.getMessage());
                response.sendRedirect("tablecontent.jsp?db=" + dbName + "&table=" + tname);
                return;
			}

			query = query.substring(0, query.length()-2);
			query = query + " )";
			int rows = db1.executeUpdate(query);
			if(rows == 0){
				notification = "<div class=\"alert alert-danger\">"+
									"Error Inserting Row<br>"+
									query+";"+
								"</div>";
			}
			else{
				notification = "<div class=\"alert alert-success\">"+
									"Row Inserted<br>"+
									query+";"+
								"</div>";
			}
			request.getSession().setAttribute("message", notification);
		}
		else{
			notification = "<div class=\"alert alert-danger\">"+
								"Error Reading Database information_schema <br>"+
								query+";"+
							"</div>";
		}
		request.getSession().setAttribute("message", notification);
		response.sendRedirect("tablecontent.jsp?db=" + dbName + "&table=" + tname);
			
	}

}

	
