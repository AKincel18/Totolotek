package pl.polsl.java.adam.kincel.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Third servlet which shows history of drawing
 *
 * @author Adam Kincel
 * @version 5.0
 */
public class History extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(true);
        Connection con = (Connection) session.getAttribute("con");
        if (con == null) {
            out.println("No connection with database");
        } else {
            try {
                //start create connection with database
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM History"); //get all records from database

                //show history
                if (rs.next() == false) {
                    out.println("<h2>No history!</h2>");
                    session.setAttribute("counter", 1);
                } else {
                    out.println("<h1>History</h1>");
                    //create table in html language 
                    out.println("<P ALIGN='left'><TABLE BORDER=1>");
                    out.println("<TH>" + "ID" + "</TH>");
                    out.println("<TH>" + "User numbers" + "</TH>");
                    out.println("<TH>" + "Drawn numbers" + "</TH>");
                    out.println("<TH>" + "Points" + "</TH>");
                    out.println("</TR>");
                    //print history
                    do {

                        out.println("<TR>");
                        out.println("<TD>" + rs.getInt(1) + "</TD>");
                        out.println("<TD>" + rs.getString(2) + "</TD>");
                        out.println("<TD>" + rs.getString(3) + "</TD>");
                        out.println("<TD>" + rs.getInt(4) + "</TD>");
                        out.println("</TR>");
                    } while (rs.next());

                    out.println("</TABLE></P>");

                    //stats
                    rs = stmt.executeQuery("SELECT count(*) as count FROM HISTORY");
                    rs.next();
                    int rows = rs.getInt("count"); //get count of games 
                    rs = stmt.executeQuery("SELECT sum(POINTS) as suma FROM HISTORY"); //get sum points scored in all games
                    rs.next();
                    out.println("<h2>Total points scored: " + rs.getInt("suma") + "/" + rows * 6 + "</h2>");
                }

                rs.close();
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        }

    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
