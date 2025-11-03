package servlets;

import db.DBConnection;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class SearchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String keyword = request.getParameter("q");
        String filter = request.getParameter("filter");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Books WHERE Title LIKE ? OR Author LIKE ?";
            if ("available".equals(filter)) sql += " AND Availability = TRUE";
            else if ("issued".equals(filter)) sql += " AND Availability = FALSE";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();
            out.println("<h2>Search Results</h2><table border='1'><tr><th>ID</th><th>Title</th><th>Author</th><th>Available</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("Book_ID") + "</td><td>" +
                        rs.getString("Title") + "</td><td>" +
                        rs.getString("Author") + "</td><td>" +
                        (rs.getBoolean("Availability") ? "Yes" : "No") + "</td></tr>");
            }
            out.println("</table>");
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
