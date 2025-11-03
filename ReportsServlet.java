package servlets;

import db.DBConnection;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class ReportsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String type = request.getParameter("reportType");
        String param = request.getParameter("param");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "";
            switch (type) {
                case "available_books":
                    sql = "SELECT * FROM Books WHERE Availability = TRUE";
                    break;
                case "issued_books":
                    sql = "SELECT i.Issue_ID, b.Title, s.Name, i.Issue_Date, i.Due_Date " +
                          "FROM Issues i JOIN Books b ON i.Book_ID=b.Book_ID " +
                          "JOIN Students s ON i.Student_ID=s.Student_ID WHERE i.Return_Date IS NULL";
                    break;
                case "student_issues":
                    sql = "SELECT b.Title, i.Issue_Date, i.Due_Date FROM Issues i JOIN Books b ON i.Book_ID=b.Book_ID " +
                          "WHERE i.Student_ID = " + param;
                    break;
                case "overdue":
                    sql = "SELECT b.Title, s.Name, i.Due_Date FROM Issues i JOIN Books b ON i.Book_ID=b.Book_ID " +
                          "JOIN Students s ON i.Student_ID=s.Student_ID WHERE i.Return_Date IS NULL AND i.Due_Date < CURDATE()";
                    break;
            }

            if (sql.isEmpty()) {
                out.println("<h3 style='color:red;'>Invalid report type!</h3>");
                return;
            }

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            out.println("<h2>Report: " + type + "</h2><table border='1'>");
            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();

            out.println("<tr>");
            for (int i = 1; i <= cols; i++) out.println("<th>" + md.getColumnName(i) + "</th>");
            out.println("</tr>");

            while (rs.next()) {
                out.println("<tr>");
                for (int i = 1; i <= cols; i++) out.println("<td>" + rs.getString(i) + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
