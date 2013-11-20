/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class payment extends HttpServlet {

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
            else{
                response.sendRedirect("homePage");
            }
        }
        try {
            out.println("<?xml version = \"1.0\" encoding = \"utf-8\" ?>");
            out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
            out.println("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
            out.println("<!--");
            out.println("Search Results for Lauren's List Web Site");
            out.println("-->");
            out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
            out.println("<title> Payment </title>");
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
            out.println("<div class=\"pure-control-group\">");
            out.println("<label for=\"firstNamePay\">First Name</label>");
            out.println("<input name=\"firstNamePay\" type=\"text\" required=\"required\" />");
            out.println("</div>");
            out.println("<div class=\"pure-control-group\">");
            out.println("<label for=\"lastNamePay\">Last Name</label>");
            out.println("<input name=\"lastNamePay\" type=\"text\" required=\"required\" />");
            out.println("</div>");
            out.println("<div class=\"pure-control-group\">");
            out.println("<label for=\"street1Pay\">Street 1</label>");
            out.println("<input name=\"street1Pay\" type=\"text\" required=\"required\" />");
            out.println("</div>");
            out.println("<div class=\"pure-control-group\">");
            out.println("<label for=\"street2Pay\">Street 2</label>");
            out.println("<input name=\"street2Pay\" type=\"text\" />");
            out.println("</div>");
            out.println("<div class=\"pure-control-group\">");
            out.println("<label for=\"cityPay\">City</label>");
            out.println("<input name=\"cityPay\" type=\"text\" required=\"required\" />");
            out.println("</div>");
            out.println("<div class=\"pure-control-group\">");
            out.println("<label for=\"statePay\">State</label>");
            out.println("<select name=\"statePay\">");
            out.println("<option value=\"\">Select a State</option>");
            out.println("<option value=\"AL\">Alabama</option>");
            out.println("<option value=\"AK\">Alaska</option>");
            out.println("<option value=\"AZ\">Arizona</option>");
            out.println("<option value=\"AR\">Arkansas</option>");
            out.println("<option value=\"CA\">California</option>");
            out.println("<option value=\"CO\">Colorado</option>");
            out.println("<option value=\"CT\">Connecticut</option>");
            out.println("<option value=\"DE\">Delaware</option>");
            out.println("<option value=\"DC\">District Of Columbia</option>");
            out.println("<option value=\"FL\">Florida</option>");
            out.println("<option value=\"GA\">Georgia</option>");
            out.println("<option value=\"HI\">Hawaii</option>");
            out.println("<option value=\"ID\">Idaho</option>");
            out.println("<option value=\"IL\">Illinois</option>");
            out.println("<option value=\"IN\">Indiana</option>");
            out.println("<option value=\"IA\">Iowa</option>");
            out.println("<option value=\"KS\">Kansas</option>");
            out.println("<option value=\"KY\">Kentucky</option>");
            out.println("<option value=\"LA\">Louisiana</option>");
            out.println("<option value=\"ME\">Maine</option>");
            out.println("<option value=\"MD\">Maryland</option>");
            out.println("<option value=\"MA\">Massachusetts</option>");
            out.println("<option value=\"MI\">Michigan</option>");
            out.println("<option value=\"MN\">Minnesota</option>");
            out.println("<option value=\"MS\">Mississippi</option>");
            out.println("<option value=\"MO\">Missouri</option>");
            out.println("<option value=\"MT\">Montana</option>");
            out.println("<option value=\"NE\">Nebraska</option>");
            out.println("<option value=\"NV\">Nevada</option>");
            out.println("<option value=\"NH\">New Hampshire</option>");
            out.println("<option value=\"NJ\">New Jersey</option>");
            out.println("<option value=\"NM\">New Mexico</option>");
            out.println("<option value=\"NY\">New York</option>");
            out.println("<option value=\"NC\">North Carolina</option>");
            out.println("<option value=\"ND\">North Dakota</option>");
            out.println("<option value=\"OH\">Ohio</option>");
            out.println("<option value=\"OK\">Oklahoma</option>");
            out.println("<option value=\"OR\">Oregon</option>");
            out.println("<option value=\"PA\">Pennsylvania</option>");
            out.println("<option value=\"RI\">Rhode Island</option>");
            out.println("<option value=\"SC\">South Carolina</option>");
            out.println("<option value=\"SD\">South Dakota</option>");
            out.println("<option value=\"TN\">Tennessee</option>");
            out.println("<option value=\"TX\">Texas</option>");
            out.println("<option value=\"UT\">Utah</option>");
            out.println("<option value=\"VT\">Vermont</option>");
            out.println("<option value=\"VA\">Virginia</option>");
            out.println("<option value=\"WA\">Washington</option>");
            out.println("<option value=\"WV\">West Virginia</option>");
            out.println("<option value=\"WI\">Wisconsin</option>");
            out.println("<option value=\"WY\">Wyoming</option>");
            out.println("</select>");
            out.println("</div>");
            out.println("<div class=\"pure-control-group\">");
            out.println("<label for=\"zipPay\">Zip</label>");
            out.println("<input name=\"zipPay\" type=\"text\" size = \"5\" required=\"required\" /> ");
            out.println("</div>");
            out.println("<div class=\"pure-control-group\">");
            out.println("<label for=\"creditCardPay\">Credit Card Number</label>");
            out.println("<input name=\"creditCardPay\" type=\"text\" required=\"required\" /> ");
            out.println("</div>");
            out.println("<div class=\"pure-control-group\">");
            out.println("<label for=\"securityPay\">Security Number</label>");
            out.println("<input name=\"securityPay\" type=\"text\" required=\"required\" /> ");
            out.println("</div>");
            out.println("<div class=\"pure-control-group\">");
            out.println("<label for=\"expirationPay\">Expiration Date</label>");
            out.println("<select name=\"expirationPay\">");
            out.println("<option value=\"\">Select a Month</option>");
            out.println("<option value=\"01\">01 - Jan</option>");
            out.println("<option value=\"02\">02 - Feb</option>");
            out.println("<option value=\"03\">03 - Mar</option>");
            out.println("<option value=\"04\">04 - Apr</option>");
            out.println("<option value=\"05\">05 - May</option>");
            out.println("<option value=\"06\">06 - Jun</option>");
            out.println("<option value=\"07\">07 - Jul</option>");
            out.println("<option value=\"08\">08 - Aug</option>");
            out.println("<option value=\"09\">09 - Sep</option>");
            out.println("<option value=\"10\">10 - Oct</option>");
            out.println("<option value=\"11\">11 - Nov</option>");
            out.println("<option value=\"12\">12 - Dec</option>");
            out.println("</select>");
            out.println("</div>");
            
            String email = "";
            if(request.getParameter("email") != null){
                email = request.getParameter("email");
            }
            else{
                response.sendRedirect("homePage");
            }
            out.println("<input name=\"contractorEmail\" type=\"hidden\" value=\"" + email + "\" /> ");
            
            out.println("<div class=\"pure-controls\">");
            out.println("<button type=\"submit\" class=\"pure-button pure-button-primary\">Submit</button> &nbsp;");
            out.println("<button type=\"reset\" class=\"pure-button\">Reset</button>");
            out.println("</div>");
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
