package ap.db;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ap.helper.ConnectionResult;
import ap.helper.FacileCookie;
import ap.helper.FacileDb;
import ap.helper.QueryResult;

@WebServlet("/createTable")
public class CreateTable extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	String dbName;
	String uname;
	String pass;
	String tname;
	
	protected String getType(int type){
		return null;
	} 
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		FacileCookie cookies = new FacileCookie(request,response);
		
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
		
		FacileDb db = new FacileDb(dbName,uname,pass);
		ConnectionResult cr = db.getConnectionResult();
		if(cr.isError())
		{
			notification = "<div class=\"alert alert-danger\">"+
								"SQLError ("+cr.getMessage()+") "+
							"</div>";
			request.getSession().setAttribute("message", notification);
			response.sendRedirect("table.jsp?db="+dbName);
			return;
		}
		else{
			QueryResult qr = db.executeQuery("SHOW TABLES IN `" + dbName + "`");
			if(!qr.isError())
			{
		
				ResultSet rs = qr.getResult();
				try {
					while(rs.next())
					{
						String table = rs.getString("Tables_in_"+dbName);
						if (table.equals(tname)) {
							notification = "<div class=\"alert alert-danger\"> Table `"+tname+"` Already Exists" + "</div>";
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
		}
		
		
		
		String[] names = request.getParameterValues("field_name");
		String[] types = request.getParameterValues("field_type");
		String[] lengths = request.getParameterValues("field_length");
		String[] default_types = request.getParameterValues("field_default_type");
		String[] default_values = request.getParameterValues("field_default_value");
		String[] attributes = request.getParameterValues("field_attribute");
		String[] nulls = request.getParameterValues("field_null");
		String[] keys = request.getParameterValues("field_key");
		String[] ai = request.getParameterValues("field_extra");
		String primary_key = "";
		String index_key = "";
		String unique_key = "";
		
		String query = "CREATE TABLE `"+tname+"` ( ";
		String html_query = "CREATE TABLE `"+tname+"` <br>(<br>&nbsp;&nbsp;&nbsp;&nbsp;";
		String col = "";
		try{
			int size = names.length;
			for(int i=0;i<size;i++)
			{
				col = "";
				if(names[i]==null || "".equals(names[i])){
					continue;
				}
				col += names[i] + " ";

				if(lengths[i].isEmpty())
					col += types[i]+ " ";
				else
					col += types[i]+"("+lengths[i]+") ";
				

				boolean has_ai = false;
				if(ai!=null){
					for(int j=0;j<ai.length;j++){
						if(("AI"+Integer.toString(i)).equals(ai[j]))
						{
							has_ai = true;
							break;
						}
					}
				}
				if(has_ai){
					col += "AUTO_INCREMENT ";
				}
				
				boolean has_null = false;
				if(nulls!=null){
					for(int j=0;j<nulls.length;j++){
						if(("NULL"+Integer.toString(i)).equals(nulls[j]))
						{
							has_null = true;
							break;
						}
					}
				}
				if(!has_null){
					col += "NOT NULL ";
				}
				
				if(default_types[i].equals("NONE"))
				{
					// do nothing
				}
				else if(default_types[i].equals("USER_DEFINED"))
				{
					if(!default_values[i].isEmpty())
						col += "DEFAULT '"+default_values[i]+"' ";
					else
						col += "DEFAULT '' ";
				}
				else {
					col += "DEFAULT "+default_types[i]+" ";
				}

				if(!attributes[i].isEmpty())
				{
					col += attributes[i] + " ";
				}				

				if(keys[i].equals("PRIMARY"))
				{
					if(primary_key.isEmpty())
						primary_key += "`"+names[i]+"`";
					else
						primary_key += ", `"+names[i]+"`";
				}
				else if(keys[i].equals("INDEX"))
				{
					if(index_key.isEmpty())
						index_key += "`"+names[i]+"`";
					else
						index_key += ", `"+names[i]+"`";
				}
				else if(keys[i].equals("UNIQUE"))
				{
					if(unique_key.isEmpty())
						unique_key += "`"+names[i]+"`";
					else
						unique_key += ", `"+names[i]+"`";
				}
				
				query += col;
				html_query+=col;
				if(i < size-1 && size != 2)
				{
					query += ",";
					html_query+= ",<br>&nbsp;&nbsp;&nbsp;&nbsp;";
				}
			}
			
			if(!primary_key.isEmpty()){
				query += ", PRIMARY KEY ( "+primary_key+" )";
				html_query += ",<br>&nbsp;&nbsp;&nbsp;&nbsp;PRIMARY KEY( "+primary_key+" )";
			}
			if(!index_key.isEmpty()){
				query += ", INDEX "+dbName+"_"+tname+"_"+"index"+" ( "+index_key+" )";
				html_query += ",<br>&nbsp;&nbsp;&nbsp;&nbsp;INDEX "+dbName+"_"+tname+"_"+"index"+" ( "+index_key+" )";
			}
			if(!unique_key.isEmpty()){
				query += ", UNIQUE "+dbName+"_"+tname+"_"+"unique"+" ( "+unique_key+" )";
				html_query += ",<br>&nbsp;&nbsp;&nbsp;&nbsp;UNIQUE "+dbName+"_"+tname+"_"+"unique"+" ( "+unique_key+" )";
			}
			
			query += " )";
			html_query += "<br>)";
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
		
		int rows = db.executeUpdate(query);

		boolean tableExists = false;
		QueryResult qr = db.executeQuery("SHOW TABLES IN `" + dbName + "`");
		if(!qr.isError())
		{
			ResultSet rs = qr.getResult();
			try {
				while(rs.next())
				{
					String table = rs.getString("Tables_in_"+dbName);
					if (table.equals(tname)) {
						tableExists = true;
						break;
					}
				}
				rs.close();
			} catch(SQLException ex) {
                System.err.println("SQLException: " + ex.getMessage());
			}	
		}
		
		if(tableExists)
			notification = "<div class=\"alert alert-success\"> Table Created Successfully<br>"+ html_query + "</div>";
		else
			notification = "<div class ='alert alert-danger'> Error Creating table<br>"+html_query+";</div>";
		request.getSession().setAttribute("message", notification);
		response.sendRedirect("table.jsp?db="+dbName);
		db.close();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.sendRedirect("home.jsp");
	}
}
