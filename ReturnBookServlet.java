package servlets;

import db.DBConnection;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class ReturnBookServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        String returnDate = request.getParameter("returnDate");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE Issues SET Return_Date = ? WHERE Student_ID = ? AND Book_ID = ? AND Return_Date IS NULL");
            ps.setString(1, returnDate);
            ps.setInt(2, studentId);
            ps.setInt(3, bookId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                PreparedStatement upd = conn.prepareStatement("UPDATE Books SET Availability = TRUE WHERE Book_ID=?");
                upd.setInt(1, bookId);
                upd.executeUpdate();
                out.println("<h2>Book Returned Successfully!</h2>");
            } else {
                out.println("<h3 style='color:red;'>No matching issued book found!</h3>");
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
