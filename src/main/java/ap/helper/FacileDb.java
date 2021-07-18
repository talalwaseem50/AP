package ap.helper;

import java.util.*;

public class FacileDb {
	ConnectionResult cr;
	FacileConnection conn;
	List<FacileStatement> listJs;
	boolean usePrev;
	FacileStatement stmt;
	
	public FacileDb(String db, String uname, String pass){
		conn = new FacileConnection(db, uname, pass);
		cr = conn.start();
		listJs = new ArrayList<FacileStatement>();
		usePrev = true;
		stmt = new FacileStatement(conn);
	}
	
	public QueryResult executeQuery(String query){
		if(usePrev)
		{
			usePrev = false;
			return stmt.executeQuery(query);
		}
		FacileStatement newStmt = new FacileStatement(conn);
		listJs.add(newStmt);
		return newStmt.executeQuery(query);
	}
	
	public int executeUpdate(String query){
		if(usePrev)
		{
			usePrev = false;
			return stmt.executeUpdate(query);
		}
		FacileStatement newStmt = new FacileStatement(conn);
		listJs.add(newStmt);
		return newStmt.executeUpdate(query);
	}
	
	public void usePrev()
	{
		usePrev = true;
	}
	
	public ConnectionResult getConnectionResult(){
		return cr;
	}
	
	public void close()
	{
		stmt.close();
		Iterator itr=listJs.iterator();
		while(itr.hasNext())
		{
			FacileStatement temp = (FacileStatement)itr.next();
			temp.close();
		}
		conn.close();
	}
}
