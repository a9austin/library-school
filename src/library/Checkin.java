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
 * Servlet implementation class Checkin
 */
@WebServlet("/Checkin")
public class Checkin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Checkin() {
        super();
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
		
		// Query for handling deletion
		String id = request.getParameter("id");
		String row = request.getParameter("row");
		try{
			String url = "jdbc:mysql://atr.eng.utah.edu/ps8_austint";
			Connection conn = (Connection) DriverManager.getConnection (url, "austint", "00603628");
			String query = "DELETE FROM CheckedOut WHERE bookID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			stmt.executeUpdate();
			
			JSONObject result = new JSONObject();
			try {
				result.put("row", row); // Have row to delete
			}
			catch (JSONException e) {
				e.printStackTrace();
			}

			// Send back the result as an HTTP response
			response.setContentType("application/json");
			response.getWriter().print(result);
			response.getWriter().close();
		}	
		catch (SQLException e) {
			System.out.println("Crashed in connection!");
			e.printStackTrace();
		}
	}

}
