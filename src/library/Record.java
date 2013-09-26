package library;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class Browsing
 */
@WebServlet("/Record")
public class Record extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Record() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// Handle the query for all of the book ID the user has checked out
		String id = request.getParameter("hidden_id");
		ArrayList<String> book_ids = new ArrayList<String>();
		try{
		String url = "jdbc:mysql://atr.eng.utah.edu/ps8_austint";
		Connection conn = (Connection) DriverManager.getConnection (url, "austint", "00603628");
		String query = "SELECT bookID FROM CheckedOut WHERE userID = " +  id;
		PreparedStatement stmt = conn.prepareStatement(query);
		ResultSet results = stmt.executeQuery();
		while(results.next()){
			book_ids.add(results.getString(1));
		}
		
		conn.close();
		}
		catch (SQLException e) {
			System.out.println("Crashed in connection!");
			e.printStackTrace();
		}
		
		ArrayList<String> titles = new ArrayList<String>();
		ArrayList<String> authors = new ArrayList<String>();
		ArrayList<String> ids = new ArrayList<String>();
		
		String books_formatted = "";
		for (int i = 0; i < book_ids.size(); i++){
			books_formatted += book_ids.get(i) + ",";
		}
		books_formatted = books_formatted.substring(0, books_formatted.length()-1);
	

		try{
			String url = "jdbc:mysql://atr.eng.utah.edu/collection";
			Connection conn = (Connection) DriverManager.getConnection (url, "library", "library");
			String query = "SELECT * FROM books WHERE SerialNumber IN ("+ books_formatted +")";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet results = stmt.executeQuery();
			while(results.next())
			{
				ids.add(results.getString(1));
				titles.add(results.getString(2));
				authors.add(results.getString(3));
			}
			
			
			request.setAttribute("ids",ids);
			request.setAttribute("titles",titles);
			request.setAttribute("authors",authors);
			request.setAttribute("rec_id", id);
			
			conn.close();
			}
		catch (SQLException e) {
			System.out.println("Crashed in connection!");
			e.printStackTrace();
		}
		
		
		
		request.getRequestDispatcher("record.jsp").forward(request, response);
	}

}
