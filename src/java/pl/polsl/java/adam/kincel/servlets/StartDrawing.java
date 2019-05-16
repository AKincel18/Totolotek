package pl.polsl.java.adam.kincel.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pl.polsl.java.adam.kincel.model.Totolotek;

/**
 * Second servlet which draw numbers
 *
 * @author Adam Kincel
 * @version 5.0
 */
public class StartDrawing extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(true);
        Totolotek totolotek = (Totolotek) session.getAttribute("totolotek");
        if (totolotek == null) {
            out.println("First, please enter correct numbers!");
            session.setAttribute("isDraw", false); //no drawing
        } else {
            out.println("<h3>New drawing</h3>");
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {

                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("userNumbers")) //get cookies from previous servlet
                    {
                        out.println(cookie.getValue() + "<br>");
                        break;
                    }
                }
            }
            totolotek.drawing();
            Connection con = null;

            try {
                //start connection with database
                Class.forName(this.getInitParameter("driver"));
                con = DriverManager.getConnection(this.getInitParameter("url"), this.getInitParameter("user"), this.getInitParameter("password"));
                Statement stmt = con.createStatement();
                int counter = (int) session.getAttribute("counter");
                boolean isEntered;
                try {
                    isEntered = (boolean) session.getAttribute("isEntered");

                } catch (java.lang.NullPointerException e) {
                    isEntered = false;
                }
                if (!isEntered) {
                    counter++; // if not entered numbers before drawing counter must be increment, it can a lot of drawing for the same entered numebrs
                }

                session.setAttribute("counter", counter); //set counter
                //set record in database
                stmt.executeUpdate("INSERT INTO History VALUES ("
                        + counter + ",'"
                        + Arrays.toString(totolotek.getUserArray()) + "','"
                        + totolotek.getDrawnArray().toString() + "',"
                        + totolotek.getPoints() + ")");

            } catch (ClassNotFoundException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
            out.println("Drawing numbers: " + totolotek.getDrawnArray() + "<br>");
            out.println("Point: " + totolotek.getPoints() + "<br>");
            session.setAttribute("totolotek", totolotek);
            session.setAttribute("isDraw", true); //drawing correct
            session.setAttribute("isEntered", false); //no entered numbers
            session.setAttribute("con", con); //set connection

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
