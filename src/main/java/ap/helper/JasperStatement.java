package ap.helper;
import java.sql.*;

public class JasperStatement {
	Statement stmt;
	public JasperStatement(JasperConnection conn){
		try{
			stmt = conn.getConnection().createStatement();			
		}catch(Exception e)
		{
			stmt = null;
		}
	}
	
	public QueryResult executeQuery(String query){
		if(stmt == null)
			return new QueryResult(QueryResult.ResultType.ERROR, null);
		try{
			ResultSet rs = stmt.executeQuery(query);
			return new QueryResult(QueryResult.ResultType.OK, rs);
		}catch(Exception e)
		{
			return new QueryResult(QueryResult.ResultType.ERROR, null);
		}
	}
	
	public int executeUpdate(String query){
		if(stmt == null)
			return 0;
		try{
			return stmt.executeUpdate(query);
		}catch(Exception e)
		{
			return 0;
		}
	}
	
	public Statement getStatement(){
		return stmt;
	}
	
	public void close(){
		try{
			if(stmt!=null)
				stmt.close();
		}catch(SQLException se){
			se.printStackTrace();
		}
	}
}
