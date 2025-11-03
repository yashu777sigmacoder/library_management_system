package servlets;

import db.DBConnection;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class DeleteStudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int studentId = Integer.parseInt(request.getParameter("studentId"));

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Students WHERE Student_ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);

            int rows = ps.executeUpdate();
            out.println("<h2>" + rows + " Student Deleted Successfully!</h2>");
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
