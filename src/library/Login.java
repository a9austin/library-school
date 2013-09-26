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
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
			String error = "";
			String name = "";
			String login_id = request.getParameter("login_id").trim();
			System.out.println(login_id);
			String url = "jdbc:mysql://atr.eng.utah.edu/ps8_austint";
			Connection conn = (Connection) DriverManager.getConnection (url, "austint", "00603628");
			String query = "SELECT name FROM Patrons WHERE userID = " +  login_id;
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet results = stmt.executeQuery();
			if (results.next()){
				name = results.getString(1);					
				System.out.println("Success");
				System.out.println("Name: " + name);
				request.getSession().setAttribute("name", name);
				request.getSession().setAttribute("id", login_id);
				request.getSession().setAttribute("error", "");
				error = "";
				System.out.println("---------------");
			}
			else
			{
				System.out.println("Error");
				System.out.println("Name: " + name);
				request.getSession().setAttribute("error", "Library ID does not exist, please try again");
				request.getSession().setAttribute("name", "");
				request.getSession().setAttribute("id", "");
				error = "Library ID does not exist, please try again";
				System.out.println("---------------");
			}
			
			JSONObject result = new JSONObject();
			try {
				result.put("userid", login_id); 
				result.put("name", name);
				result.put("error", error);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
			
			//Send back the result as an HTTP response
			response.setContentType("application/json");
			response.getWriter().print(result);
			response.getWriter().close();
			
			conn.close();
			//request.getRequestDispatcher("index.jsp").forward(request, response);
		} catch (SQLException e) {
			System.out.println("Crashed in connection!");
			e.printStackTrace();
		}
	}

}
