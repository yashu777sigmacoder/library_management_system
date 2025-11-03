package servlets;

import db.DBConnection;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class DeleteBookServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Books WHERE Book_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bookId);

            int rows = ps.executeUpdate();
            out.println("<h2>" + rows + " Book Deleted Successfully!</h2>");
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
