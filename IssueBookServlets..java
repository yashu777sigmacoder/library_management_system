package servlets;

import db.DBConnection;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class IssueBookServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        String issueDate = request.getParameter("issueDate");
        String dueDate = request.getParameter("dueDate");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {
            // Check availability
            PreparedStatement check = conn.prepareStatement("SELECT Availability FROM Books WHERE Book_ID=?");
            check.setInt(1, bookId);
            ResultSet rs = check.executeQuery();

            if (rs.next() && rs.getBoolean("Availability")) {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Issues (Student_ID, Book_ID, Issue_Date, Due_Date) VALUES (?, ?, ?, ?)");
                ps.setInt(1, studentId);
                ps.setInt(2, bookId);
                ps.setString(3, issueDate);
                ps.setString(4, dueDate);
                ps.executeUpdate();

                PreparedStatement upd = conn.prepareStatement("UPDATE Books SET Availability = FALSE WHERE Book_ID=?");
                upd.setInt(1, bookId);
                upd.executeUpdate();

                out.println("<h2>Book Issued Successfully!</h2>");
            } else {
                out.println("<h3 style='color:red;'>Book is not available!</h3>");
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
