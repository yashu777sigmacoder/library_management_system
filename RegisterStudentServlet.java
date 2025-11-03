package servlets;

import db.DBConnection;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class RegisterStudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String department = request.getParameter("department");
        int year = Integer.parseInt(request.getParameter("year"));
        String email = request.getParameter("email");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Students (Name, Department, Year, Email) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, department);
            ps.setInt(3, year);
            ps.setString(4, email);

            int rows = ps.executeUpdate();
            out.println("<h2>" + rows + " Student Registered Successfully!</h2>");
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
