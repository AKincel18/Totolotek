package pl.polsl.java.adam.kincel.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pl.polsl.java.adam.kincel.model.MyException;
import pl.polsl.java.adam.kincel.model.Totolotek;

/**
 * First servlet which take numbers from user
 *
 * @author Adam Kincel
 * @version 5.0
 */
public class EnterNumbers extends HttpServlet {

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
        Totolotek totolotek = new Totolotek();
        boolean isDraw = false;
        int counter; //variable which count number of games (ID in database)
        try {
            counter = (int) session.getAttribute("counter");
            isDraw = (boolean) session.getAttribute("isDraw");
            if (isDraw == true) //to increment counter (ID) when drawn numbers 
            {
                counter++;
            }

        } catch (java.lang.NullPointerException e) {

            counter = 1; //if first game
        }

        try {
            for (int i = 0; i < totolotek.getUserArray().length; i++) {
                totolotek.setNumber(i, Integer.parseInt(request.getParameter("number" + (i + 1))));
                totolotek.isGoodNumber(totolotek.getUserArrayInPos(i));
            }
            totolotek.isRepeatable(totolotek.getUserArray());

        } catch (NumberFormatException e) {
            totolotek = null;
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Numbers must be integer");
        } catch (MyException e) {
            totolotek = null;
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage() + "Wrong number: " + e.getExceptionNumber());
        }
        if (totolotek != null) {
            out.println("<h3>Thanks for entering numbers</h3>");
            out.println("Your numbers: ");
            out.println(Arrays.toString(totolotek.getUserArray()) + "<br>");
            String tmp = "Your numbers: " + Arrays.toString(totolotek.getUserArray());
            Cookie cookie = new Cookie("userNumbers", tmp); //using cookies, send user's numbers to second servlets
            response.addCookie(cookie);
            session.setAttribute("isDraw", false); //not drawing yet
            session.setAttribute("counter", counter); //not drawing yet
            session.setAttribute("isEntered", true); //entered numbers
            session.setAttribute("totolotek", totolotek); //set model to session
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
