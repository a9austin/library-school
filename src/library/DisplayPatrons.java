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
 * Servlet implementation class DisplayPatrons
 */
@WebServlet("/DisplayPatrons")
public class DisplayPatrons extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DisplayPatrons() {
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
		
		// Connect to the connection
		try {
			ArrayList<String> name_list = new ArrayList<String>();
			ArrayList<String> id_list = new ArrayList<String>();
			String url = "jdbc:mysql://atr.eng.utah.edu/ps8_austint";
			Connection conn = (Connection) DriverManager.getConnection (url, "austint", "00603628");
			String query = "SELECT * FROM Patrons ORDER BY name";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet results = stmt.executeQuery();
			while(results.next()){
				id_list.add(results.getString(1));
				name_list.add(results.getString(2));
			}
			
			JSONObject result = new JSONObject();
			try {
				JSONArray resultArray1 = new JSONArray();
				JSONArray resultArray2 = new JSONArray();
				for (String book: id_list) {
					resultArray1.put(book);
				}
				for (String id: name_list) {
					resultArray2.put(id);
				}
				result.put("ids", resultArray1);
				result.put("names", resultArray2);
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
