/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alex-MSU
 */
public class paymentConfirm extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String userType = "Guest";
        String firstName = "";
        String lastName = "";
        String company = "";
        String adminEmail = "";
        HttpSession httpSession;

        if (request.getSession(false) != null) {
            httpSession = request.getSession();
            if (httpSession.getAttribute("userType") != null) {
                if (httpSession.getAttribute("userType").equals("customer")) {
                    Customer customer = (Customer) httpSession.getAttribute("user");
                    firstName = customer.getFirstName();
                    lastName = customer.getLastName();
                    userType = "customer";
                } else if (httpSession.getAttribute("userType").equals("contractor")) {
                    Contractor contractor = (Contractor) httpSession.getAttribute("user");
                    company = contractor.getCompany();
                    userType = "contractor";
                } else if (httpSession.getAttribute("userType").equals("admin")) {
                    Admin admin = (Admin) httpSession.getAttribute("user");
                    adminEmail = admin.getEmail();
                    userType = "admin";
                }
            }
        }
        try {
            String firstNamePay = request.getParameter("firstNamePay"); 
            String lastNamePay = request.getParameter("lastNamePay");
            String street1Pay = request.getParameter("street1Pay");
            String street2Pay = request.getParameter("street2Pay");
            String cityPay = request.getParameter("cityPay");
            String statePay = request.getParameter("statePay");
            String zipPay = request.getParameter("zipPay");
            String creditCardPay = request.getParameter("creditCardPay");
            String securityPay = request.getParameter("securityPay");
            String experationPay = request.getParameter("experationPay");
           
            boolean isValid = CardValidator.isValid(creditCardPay);
            
            out.println("<?xml version = \"1.0\" encoding = \"utf-8\" ?>");
            out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
            out.println("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
            out.println("<!--");
            out.println("Search Results for Lauren's List Web Site");
            out.println("-->");
            out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
            out.println("<title> Search Results </title>");
            out.println("<link rel=\"stylesheet\" href=\"http://yui.yahooapis.com/pure/0.3.0/pure-nr-min.css\" />");
            out.println("<link rel=\"stylesheet\" href=\"style.css\" />");
            out.println("</head>");
            out.println("<body class=\"pure-skin-matt\">");
            out.println("<div id=\"header\">");
            out.println("<div class=\"bottom_header\">");
            out.println("<a href=\"homePage\"><img src=\"images/LLLogoSmall.jpg\" alt=\"Lauren's List Logo\" /></a>");
            out.println("<h1> Lauren's List </h1>");
            out.println("</div>");
            out.println("<div class=\"right\" style=\"display:inline-block\">");
            out.println("<br/>");

            if (userType.equals("Guest")) {
                out.println("<a class=\"pure-button\" href=\"login.html\"> Login </a> &nbsp;");
                out.println("<a class=\"pure-button\" href=\"customerOrContractor.html\"> Create Account </a> &nbsp;");
            } else if (userType.equals("contractor")) {
                out.println("<a class=\"pure-button\" href=\"contractorProfile\"> " + company + " </a> &nbsp;");
                out.println("<a class=\"pure-button\" href=\"logout\"> Logout </a> &nbsp;<br/><br/>");
            } else if (userType.equals("customer")) {
                out.println("<a class=\"pure-button\" href=\"changePassword\"> " + firstName + " " + lastName + " </a> &nbsp;");
                out.println("<a class=\"pure-button\" href=\"logout\"> Logout </a> &nbsp;<br/><br/>");
            } else if (userType.equals("admin")) {
                out.println("<a class=\"pure-button\" href=\"changePassword\"> " + adminEmail + " </a> &nbsp;");
                out.println("<a class=\"pure-button\" href=\"logout\"> Logout </a> &nbsp;<br/><br/>");
            }

            out.println("</div>");
            out.println("</div>");
            out.println("<div id=\"top\" class=\"pure-menu-horizontal pure-menu pure-menu-open\">");
            out.println("<ul>");
            out.println("<li><a href=\"homePage\">Home</a></li>");
            out.println("<li><a href=\"search?search=\">Browse Contractors</a></li>");
            out.println("<li><a href=\"about\">About</a></li>");
            out.println("</ul>");
            out.println("</div>");
            out.println("<div id=\"center\">");
            out.println("<form action=\"paymentConfirm\" method=\"post\" class=\"pure-form pure-form-aligned\">");
            out.println("<div class=\"center\">");
            out.println("<fieldset>");
            out.println("<legend> Payment Information </legend>");

            if(isValid)out.println("<p> Credit Card is Valid </p>");
            else out.println("<p> Credit Card is InValid</p>");            
            
            out.println("</fieldset>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");



        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            out.close();
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(homePage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(homePage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
