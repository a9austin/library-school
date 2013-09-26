package library;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Driver;
import com.mysql.jdbc.Statement;
//import java.sql.Connection;
import com.mysql.jdbc.Connection;


/**
 * Servlet implementation class load_books
 */
@WebServlet("/LoadBooks")
public class LoadBooks extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadBooks() {
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
		// Finds the driver
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> checked_out_ids = new ArrayList<String>();
		
		// Figure out which books have been checked out
		try {
			String url = "jdbc:mysql://atr.eng.utah.edu/ps8_austint";
			Connection conn = (Connection) DriverManager.getConnection (url, "austint", "00603628");
			String query = "select bookID from CheckedOut";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet results = stmt.executeQuery();
			while(results.next()){
				checked_out_ids.add(results.getString(1));
			}
			conn.close();
		} 
		catch (SQLException e) {
			System.out.println("Crashed in connection!");
			e.printStackTrace();
		}
		
		// -- QUERY for getting book information --
		try {
			if (null == request.getSession().getAttribute("name")){
				request.getSession().setAttribute("name", "");
			}
			
			ArrayList<String> books = new ArrayList<String>();
			ArrayList<String> ids = new ArrayList<String>();
			ArrayList<String> authors= new ArrayList<String>();
			ArrayList<String> checked = new ArrayList<String>();
 			
			String url = "jdbc:mysql://atr.eng.utah.edu/collection";
			Connection conn = (Connection) DriverManager.getConnection (url, "library", "library");
			String query = "select * from books";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet results = stmt.executeQuery();
			while(results.next()){
				books.add(results.getString(2));
				ids.add(results.getString(1));
				authors.add(results.getString(3));
				// This book is checked out
				if (checked_out_ids.contains(results.getString(1))){
					checked.add("Checked out");
				}
				else{
					checked.add("Checkout");
				}
			}
			

			JSONObject result = new JSONObject();
			try {
				JSONArray resultArray1 = new JSONArray();
				JSONArray resultArray2 = new JSONArray();
				JSONArray resultArray3 = new JSONArray();
				JSONArray resultArray4 = new JSONArray();
				for (String book: books) {
					resultArray1.put(book);
				}
				for (String id: ids) {
					resultArray2.put(id);
				}
				for (String author: authors) {
					resultArray3.put(author);
				}
				for (String val: checked) {
					resultArray4.put(val);
				}
				result.put("titles", resultArray1);
				result.put("ids", resultArray2);
				result.put("authors", resultArray3);
				result.put("checked", resultArray4);
				result.put("name", request.getSession().getAttribute("name"));
				result.put("userid", request.getSession().getAttribute("id"));
			}
			catch (JSONException e) {
				e.printStackTrace();
			}

			// Send back the result as an HTTP response
			response.setContentType("application/json");
			response.getWriter().print(result);
			response.getWriter().close();
			
			conn.close();
			//request.getRequestDispatcher("index.html").forward(request, response);
		} catch (SQLException e) {
			System.out.println("Crashed in connection!");
			e.printStackTrace();
		}
		
	}

}
