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
 * Servlet implementation class Checkout
 */
@WebServlet("/Checkout")
public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Checkout() {
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
		
		// Connect to the connection
		try {
			String book_id = request.getParameter("book_id");
			String user_id = request.getParameter("login_id");
			String url = "jdbc:mysql://atr.eng.utah.edu/ps8_austint";
			Connection conn = (Connection) DriverManager.getConnection (url, "austint", "00603628");
			// Query the book regarding the given serial number
			String query = "INSERT INTO CheckedOut (`bookID`, `userID`) VALUES (?,?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, book_id);
			stmt.setString(2, user_id);
			stmt.executeUpdate();

			JSONObject result = new JSONObject();
			try {
				result.put("book_id", book_id);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}

			// Send back the result as an HTTP response
			response.setContentType("application/json");
			response.getWriter().print(result);
			response.getWriter().close();
			
			conn.close();
		}
		catch (SQLException e) {
			System.out.println("Crashed in connection!");
			e.printStackTrace();
		}	
	}

}
