/*
 * code for the contractors profile when being view when you are not the contractor
 */

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
 *
 * @author Matthew
 */
public class contractorPage extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
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
        String userType = "Guest";
        String firstName = "";
        String lastName = "";
        String customerEmailCurrent = "";
        String company = "";
        String adminEmail = "";
        HttpSession httpSession;
        //checks to make sure the user is not a contractor
        if(request.getSession(false) != null)
        {
            httpSession = request.getSession();
            if(httpSession.getAttribute("userType") != null){
                if (httpSession.getAttribute("userType").equals("customer"))
                {
                    Customer customer = (Customer)httpSession.getAttribute("user");
                    firstName = customer.getFirstName();
                    lastName = customer.getLastName();
                    customerEmailCurrent = customer.getEmail();
                    userType = "customer";
                }
                else if(httpSession.getAttribute("userType").equals("contractor"))
                {
                    Contractor contractor = (Contractor)httpSession.getAttribute("user");
                    company = contractor.getCompany();
                    userType = "contractor";
                }
                else if(httpSession.getAttribute("userType").equals("admin"))
                {
                    Admin admin = (Admin)httpSession.getAttribute("user");
                    adminEmail = admin.getEmail();
                    userType = "admin";
                }
            }
        }
        
        DBConnections dataSource = DBConnections.getInstance();
        Connection conn = dataSource.getConnection();
        Statement statement = null;
        ResultSet reviewResult = null;
        
             
        try {
            out.println("<?xml version = \"1.0\" encoding = \"utf-8\" ?>");
            out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
            out.println("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
            out.println("<!--");
            out.println("Contractor Profile for Lauren's List Web Site");
            out.println("-->");
            out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
            out.println("<title> Contractor Profile </title>");
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
            //buttons at the top right
            if(userType.equals("Guest")){
                out.println("<a class=\"pure-button\" href=\"login.html\"> Login </a> &nbsp;");
                out.println("<a class=\"pure-button\" href=\"customerOrContractor.html\"> Create Account </a> &nbsp;");
            }
            else if(userType.equals("contractor")){
                out.println("<a class=\"pure-button\" href=\"contractorProfile\"> " + company + " </a> &nbsp;");
                out.println("<a class=\"pure-button\" href=\"logout\"> Logout </a> &nbsp;<br/><br/>");
            }
            else if(userType.equals("customer")){
                out.println("<a class=\"pure-button\" href=\"changePassword\"> " + firstName + " " + lastName + " </a> &nbsp;");
                out.println("<a class=\"pure-button\" href=\"logout\"> Logout </a> &nbsp;<br/><br/>");
            }
            else if(userType.equals("admin")){
                out.println("<a class=\"pure-button\" href=\"adminProfile\"> " + adminEmail + " </a> &nbsp;");
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
            out.println("<div class=\"center\">");
            
            //email is recieved from GET method
            String email = "";
            if(request.getParameter("email") != null){
                email = request.getParameter("email");
            }
            else{
                response.sendRedirect("homePage");
            }
            //locks certain buttons depending on what the user type is
            if(userType.equals("Guest")){
                out.println("<a class=\"pure-button pure-button-disabled\" href=\"#\"> Pay Contractor </a> &nbsp;");
                out.println("<a class=\"pure-button pure-button-disabled\" href=\"#\"> Rate Contractor </a> &nbsp;");
                out.println("<a class=\"pure-button pure-button-disabled\" href=\"#\"> Flag Contractor </a> &nbsp;<br/><br/>");
            }
            else if(userType.equals("contractor")){
                out.println("<a class=\"pure-button pure-button-disabled\" href=\"#\"> Pay Contractor </a> &nbsp;");
                out.println("<a class=\"pure-button pure-button-disabled\" href=\"#\"> Rate Contractor </a> &nbsp;");
                out.println("<a class=\"pure-button pure-button-disabled\" href=\"#\"> Flag Contractor </a> &nbsp;<br/><br/>");
            }
            else if(userType.equals("customer")){
                out.println("<a class=\"pure-button\" href=\"payment?email=" + email + "\"> Pay Contractor </a> &nbsp;");
                ResultSet paymentResult = null;
                Statement statement5 = null;
                try{
                    statement5 = conn.createStatement();
                    paymentResult = statement5.executeQuery("SELECT * FROM Payment WHERE customer_email=\'" + customerEmailCurrent + "\' AND contractor_email=\'" + email + "\' AND reviewed=0");
                    boolean paymentFound = paymentResult.next();
                    // must pay in order to rate the contractor
                    if(paymentFound){
                        out.println("<a class=\"pure-button\" href=\"rateContractor?email=" + email + "\"> Rate Contractor </a> &nbsp;");
                    }
                    else{
                        out.println("<a class=\"pure-button pure-button-disabled\" href=\"#\"> Rate Contractor </a> &nbsp;");
                    }
                }catch(SQLException ex){
                    out.println("SQLException in Query.java");
                    ex.printStackTrace(out);
                }finally
                {
                    DBUtilities.closeResultSet(paymentResult);
                    DBUtilities.closeStatement(statement5);
                }
                
                ResultSet hasFlaggedResult = null;
                Statement statement6 = null;
                try{
                    statement6 = conn.createStatement();
                    hasFlaggedResult = statement6.executeQuery("SELECT * FROM Has_Flagged WHERE customer_email=\'" + customerEmailCurrent + "\' AND contractor_email=\'" + email + "\' AND is_flagged=1");
                    boolean hasFlaggedFound = hasFlaggedResult.next();
                    //you only can flag the contractor once
                    if(hasFlaggedFound){
                        out.println("<a class=\"pure-button pure-button-disabled\" href=\"#\"> Flag Contractor </a> &nbsp;<br/><br/>");
                    }
                    else{
                        out.println("<a class=\"pure-button\" href=\"flagContractor?email=" + email + "\"> Flag Contractor </a> &nbsp;<br/><br/>");
                    }
                }catch(SQLException ex){
                    out.println("SQLException in Query.java");
                    ex.printStackTrace(out);
                }finally
                {
                    DBUtilities.closeResultSet(hasFlaggedResult);
                    DBUtilities.closeStatement(statement6);
                }
                
            }
            //admin can do anything
            else if(userType.equals("admin")){
                out.println("<a class=\"pure-button pure-button-disabled\" href=\"#\"> Pay Contractor </a> &nbsp;");
                out.println("<a class=\"pure-button pure-button-disabled\" href=\"#\"> Rate Contractor </a> &nbsp;");
                out.println("<a class=\"pure-button pure-button-disabled\" href=\"#\"> Flag Contractor </a> &nbsp;");
                out.println("<a class=\"pure-button\" href=\"deleteContractor?email=" + email + "\"> Delete Contractor </a> &nbsp;<br/><br/>");
            }
            
            out.println("<div class=\"pure-g\">");
            out.println("<div class=\"pure-u-1-4\">");
            out.println("<fieldset>");
            out.println("<legend> Information </legend>");
            //sets up the contractor's profile
            ResultSet contractorResult = null;
            Statement statement4 = null;
            try{
                statement4 = conn.createStatement();
                contractorResult = statement4.executeQuery("SELECT * FROM Contractor WHERE email=\'" + email + "\'");
                boolean contractorFound = contractorResult.next();
                String contractorCompany = "";
                String phone = "";
                String info = "";
                if(contractorFound){
                    contractorCompany = contractorResult.getString("company");
                    email = contractorResult.getString("email");
                    phone = contractorResult.getString("phone");
                    info = contractorResult.getString("info");
                    
                    String areaCode = phone.substring(0, 3);
                    String threeDigit = phone.substring(3, 6);
                    String fourDigit = phone.substring(6, 10);
                    
                    phone = "("+areaCode+")"+threeDigit+"-"+fourDigit+"";
                }
                out.println("<p>" + contractorCompany + "<br />" + email + "<br />" + phone + "<br /><br />" + info + "<br /><br /> <a href=\"https://www.google.com/search?q="+ contractorCompany +"\">More Information</a> <br /></p>");
            }catch(SQLException ex){
                out.println("SQLException in Query.java");
                ex.printStackTrace(out);
            }finally
            {
                DBUtilities.closeResultSet(contractorResult);
                DBUtilities.closeStatement(statement4);
            }
            
            out.println("</fieldset>");
            out.println("</div>");
            out.println("<div class=\"pure-u-1-2\">");
            out.println("<fieldset>");
            out.println("<legend> Reviews </legend>");
                        
            try {
                statement = conn.createStatement();
                reviewResult = statement.executeQuery("SELECT * FROM Review WHERE contractor_email=\'" + email + "\'");
                        
                while(reviewResult.next()){
                    String customerEmail = reviewResult.getString("customer_email");
                    String review = reviewResult.getString("review");
                    float stars = reviewResult.getFloat("stars");
                    
                    ResultSet customerResult = null;
                    Statement statement2 = null;
                    try{
                        statement2 = conn.createStatement();
                        customerResult = statement2.executeQuery("SELECT * FROM Customer WHERE email=\'" + customerEmail + "\'");
                        boolean customerFound = customerResult.next();
                        String first_Name = "";
                        String last_Name = "";
                        if(customerFound){
                            first_Name = customerResult.getString("first_name");
                            last_Name = customerResult.getString("last_name");
                        }
                        out.println("<p>" + first_Name + " " + last_Name + "&nbsp;&nbsp; (" + stars + "&nbsp;of&nbsp;5 stars)<br />" + review + "<br /><br /></p>");
                    }catch(SQLException ex){
                        out.println("SQLException in Query.java");
                        ex.printStackTrace(out);
                    }finally
                    {
                        DBUtilities.closeResultSet(customerResult);
                        DBUtilities.closeStatement(statement2);
                    }          
                }
                    
            } catch (SQLException ex) {
                out.println("SQLException in Query.java");
                ex.printStackTrace(out);
            } finally
            {
                DBUtilities.closeResultSet(reviewResult); 
            }
            
            out.println("</fieldset>");
            out.println("</div>");
            out.println("<div class=\"pure-u-1-4\">");
            out.println("<fieldset>");
            out.println("<legend> Overall Rating </legend>");
            out.println("<p>");
            
            out.println("<article>");
            out.println("<div class=\"star-rating\">");
            
            ResultSet ratingResult = null;
            Statement statement3 = null;
            try{
                statement3 = conn.createStatement();
                ratingResult = statement3.executeQuery("SELECT * FROM Rating WHERE email=\'" + email + "\'");
                boolean ratingFound = ratingResult.next();
                float rating = 0;
                // gets the star rating
                if(ratingFound){
                    rating = ratingResult.getFloat("rating");
                }
                float averageRating = Math.round(rating * 2) / 2.0f;
                
                if(averageRating == 0){
                    out.println("<input class=\"rb0\" id=\"Ans_1\" name=\"numericRating\" type=\"radio\" value=\"0\" checked=\"checked\" />");
                    out.println("<input class=\"rb1\" id=\"Ans_2\" name=\"numericRating\" type=\"radio\" value=\"0.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb2\" id=\"Ans_3\" name=\"numericRating\" type=\"radio\" value=\"1\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb3\" id=\"Ans_4\" name=\"numericRating\" type=\"radio\" value=\"1.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb4\" id=\"Ans_5\" name=\"numericRating\" type=\"radio\" value=\"2\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb5\" id=\"Ans_6\" name=\"numericRating\" type=\"radio\" value=\"2.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb6\" id=\"Ans_7\" name=\"numericRating\" type=\"radio\" value=\"3\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb7\" id=\"Ans_8\" name=\"numericRating\" type=\"radio\" value=\"3.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb8\" id=\"Ans_9\" name=\"numericRating\" type=\"radio\" value=\"4\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb9\" id=\"Ans_10\" name=\"numericRating\" type=\"radio\" value=\"4.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb10\" id=\"Ans_11\" name=\"numericRating\" type=\"radio\" value=\"5\" disabled=\"disabled\" />");
                }
                else if(averageRating == 0.5){
                    out.println("<input class=\"rb0\" id=\"Ans_1\" name=\"numericRating\" type=\"radio\" value=\"0\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb1\" id=\"Ans_2\" name=\"numericRating\" type=\"radio\" value=\"0.5\" checked=\"checked\" />");
                    out.println("<input class=\"rb2\" id=\"Ans_3\" name=\"numericRating\" type=\"radio\" value=\"1\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb3\" id=\"Ans_4\" name=\"numericRating\" type=\"radio\" value=\"1.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb4\" id=\"Ans_5\" name=\"numericRating\" type=\"radio\" value=\"2\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb5\" id=\"Ans_6\" name=\"numericRating\" type=\"radio\" value=\"2.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb6\" id=\"Ans_7\" name=\"numericRating\" type=\"radio\" value=\"3\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb7\" id=\"Ans_8\" name=\"numericRating\" type=\"radio\" value=\"3.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb8\" id=\"Ans_9\" name=\"numericRating\" type=\"radio\" value=\"4\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb9\" id=\"Ans_10\" name=\"numericRating\" type=\"radio\" value=\"4.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb10\" id=\"Ans_11\" name=\"numericRating\" type=\"radio\" value=\"5\" disabled=\"disabled\" />");
                }
                else if(averageRating == 1){
                    out.println("<input class=\"rb0\" id=\"Ans_1\" name=\"numericRating\" type=\"radio\" value=\"0\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb1\" id=\"Ans_2\" name=\"numericRating\" type=\"radio\" value=\"0.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb2\" id=\"Ans_3\" name=\"numericRating\" type=\"radio\" value=\"1\" checked=\"checked\" />");
                    out.println("<input class=\"rb3\" id=\"Ans_4\" name=\"numericRating\" type=\"radio\" value=\"1.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb4\" id=\"Ans_5\" name=\"numericRating\" type=\"radio\" value=\"2\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb5\" id=\"Ans_6\" name=\"numericRating\" type=\"radio\" value=\"2.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb6\" id=\"Ans_7\" name=\"numericRating\" type=\"radio\" value=\"3\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb7\" id=\"Ans_8\" name=\"numericRating\" type=\"radio\" value=\"3.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb8\" id=\"Ans_9\" name=\"numericRating\" type=\"radio\" value=\"4\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb9\" id=\"Ans_10\" name=\"numericRating\" type=\"radio\" value=\"4.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb10\" id=\"Ans_11\" name=\"numericRating\" type=\"radio\" value=\"5\" disabled=\"disabled\" />");
                }
                else if(averageRating == 1.5){
                    out.println("<input class=\"rb0\" id=\"Ans_1\" name=\"numericRating\" type=\"radio\" value=\"0\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb1\" id=\"Ans_2\" name=\"numericRating\" type=\"radio\" value=\"0.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb2\" id=\"Ans_3\" name=\"numericRating\" type=\"radio\" value=\"1\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb3\" id=\"Ans_4\" name=\"numericRating\" type=\"radio\" value=\"1.5\" checked=\"checked\" />");
                    out.println("<input class=\"rb4\" id=\"Ans_5\" name=\"numericRating\" type=\"radio\" value=\"2\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb5\" id=\"Ans_6\" name=\"numericRating\" type=\"radio\" value=\"2.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb6\" id=\"Ans_7\" name=\"numericRating\" type=\"radio\" value=\"3\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb7\" id=\"Ans_8\" name=\"numericRating\" type=\"radio\" value=\"3.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb8\" id=\"Ans_9\" name=\"numericRating\" type=\"radio\" value=\"4\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb9\" id=\"Ans_10\" name=\"numericRating\" type=\"radio\" value=\"4.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb10\" id=\"Ans_11\" name=\"numericRating\" type=\"radio\" value=\"5\" disabled=\"disabled\" />");
                }
                else if(averageRating == 2){
                    out.println("<input class=\"rb0\" id=\"Ans_1\" name=\"numericRating\" type=\"radio\" value=\"0\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb1\" id=\"Ans_2\" name=\"numericRating\" type=\"radio\" value=\"0.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb2\" id=\"Ans_3\" name=\"numericRating\" type=\"radio\" value=\"1\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb3\" id=\"Ans_4\" name=\"numericRating\" type=\"radio\" value=\"1.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb4\" id=\"Ans_5\" name=\"numericRating\" type=\"radio\" value=\"2\" checked=\"checked\" />");
                    out.println("<input class=\"rb5\" id=\"Ans_6\" name=\"numericRating\" type=\"radio\" value=\"2.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb6\" id=\"Ans_7\" name=\"numericRating\" type=\"radio\" value=\"3\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb7\" id=\"Ans_8\" name=\"numericRating\" type=\"radio\" value=\"3.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb8\" id=\"Ans_9\" name=\"numericRating\" type=\"radio\" value=\"4\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb9\" id=\"Ans_10\" name=\"numericRating\" type=\"radio\" value=\"4.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb10\" id=\"Ans_11\" name=\"numericRating\" type=\"radio\" value=\"5\" disabled=\"disabled\" />");
                }
                else if(averageRating == 2.5){
                    out.println("<input class=\"rb0\" id=\"Ans_1\" name=\"numericRating\" type=\"radio\" value=\"0\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb1\" id=\"Ans_2\" name=\"numericRating\" type=\"radio\" value=\"0.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb2\" id=\"Ans_3\" name=\"numericRating\" type=\"radio\" value=\"1\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb3\" id=\"Ans_4\" name=\"numericRating\" type=\"radio\" value=\"1.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb4\" id=\"Ans_5\" name=\"numericRating\" type=\"radio\" value=\"2\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb5\" id=\"Ans_6\" name=\"numericRating\" type=\"radio\" value=\"2.5\" checked=\"checked\" />");
                    out.println("<input class=\"rb6\" id=\"Ans_7\" name=\"numericRating\" type=\"radio\" value=\"3\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb7\" id=\"Ans_8\" name=\"numericRating\" type=\"radio\" value=\"3.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb8\" id=\"Ans_9\" name=\"numericRating\" type=\"radio\" value=\"4\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb9\" id=\"Ans_10\" name=\"numericRating\" type=\"radio\" value=\"4.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb10\" id=\"Ans_11\" name=\"numericRating\" type=\"radio\" value=\"5\" disabled=\"disabled\" />");
                }
                else if(averageRating == 3){
                    out.println("<input class=\"rb0\" id=\"Ans_1\" name=\"numericRating\" type=\"radio\" value=\"0\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb1\" id=\"Ans_2\" name=\"numericRating\" type=\"radio\" value=\"0.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb2\" id=\"Ans_3\" name=\"numericRating\" type=\"radio\" value=\"1\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb3\" id=\"Ans_4\" name=\"numericRating\" type=\"radio\" value=\"1.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb4\" id=\"Ans_5\" name=\"numericRating\" type=\"radio\" value=\"2\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb5\" id=\"Ans_6\" name=\"numericRating\" type=\"radio\" value=\"2.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb6\" id=\"Ans_7\" name=\"numericRating\" type=\"radio\" value=\"3\" checked=\"checked\" />");
                    out.println("<input class=\"rb7\" id=\"Ans_8\" name=\"numericRating\" type=\"radio\" value=\"3.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb8\" id=\"Ans_9\" name=\"numericRating\" type=\"radio\" value=\"4\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb9\" id=\"Ans_10\" name=\"numericRating\" type=\"radio\" value=\"4.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb10\" id=\"Ans_11\" name=\"numericRating\" type=\"radio\" value=\"5\" disabled=\"disabled\" />");
                }
                else if(averageRating == 3.5){
                    out.println("<input class=\"rb0\" id=\"Ans_1\" name=\"numericRating\" type=\"radio\" value=\"0\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb1\" id=\"Ans_2\" name=\"numericRating\" type=\"radio\" value=\"0.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb2\" id=\"Ans_3\" name=\"numericRating\" type=\"radio\" value=\"1\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb3\" id=\"Ans_4\" name=\"numericRating\" type=\"radio\" value=\"1.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb4\" id=\"Ans_5\" name=\"numericRating\" type=\"radio\" value=\"2\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb5\" id=\"Ans_6\" name=\"numericRating\" type=\"radio\" value=\"2.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb6\" id=\"Ans_7\" name=\"numericRating\" type=\"radio\" value=\"3\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb7\" id=\"Ans_8\" name=\"numericRating\" type=\"radio\" value=\"3.5\" checked=\"checked\" />");
                    out.println("<input class=\"rb8\" id=\"Ans_9\" name=\"numericRating\" type=\"radio\" value=\"4\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb9\" id=\"Ans_10\" name=\"numericRating\" type=\"radio\" value=\"4.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb10\" id=\"Ans_11\" name=\"numericRating\" type=\"radio\" value=\"5\" disabled=\"disabled\" />");
                }
                else if(averageRating == 4){
                    out.println("<input class=\"rb0\" id=\"Ans_1\" name=\"numericRating\" type=\"radio\" value=\"0\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb1\" id=\"Ans_2\" name=\"numericRating\" type=\"radio\" value=\"0.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb2\" id=\"Ans_3\" name=\"numericRating\" type=\"radio\" value=\"1\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb3\" id=\"Ans_4\" name=\"numericRating\" type=\"radio\" value=\"1.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb4\" id=\"Ans_5\" name=\"numericRating\" type=\"radio\" value=\"2\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb5\" id=\"Ans_6\" name=\"numericRating\" type=\"radio\" value=\"2.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb6\" id=\"Ans_7\" name=\"numericRating\" type=\"radio\" value=\"3\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb7\" id=\"Ans_8\" name=\"numericRating\" type=\"radio\" value=\"3.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb8\" id=\"Ans_9\" name=\"numericRating\" type=\"radio\" value=\"4\" checked=\"checked\" />");
                    out.println("<input class=\"rb9\" id=\"Ans_10\" name=\"numericRating\" type=\"radio\" value=\"4.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb10\" id=\"Ans_11\" name=\"numericRating\" type=\"radio\" value=\"5\" disabled=\"disabled\" />");
                }
                else if(averageRating == 4.5){
                    out.println("<input class=\"rb0\" id=\"Ans_1\" name=\"numericRating\" type=\"radio\" value=\"0\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb1\" id=\"Ans_2\" name=\"numericRating\" type=\"radio\" value=\"0.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb2\" id=\"Ans_3\" name=\"numericRating\" type=\"radio\" value=\"1\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb3\" id=\"Ans_4\" name=\"numericRating\" type=\"radio\" value=\"1.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb4\" id=\"Ans_5\" name=\"numericRating\" type=\"radio\" value=\"2\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb5\" id=\"Ans_6\" name=\"numericRating\" type=\"radio\" value=\"2.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb6\" id=\"Ans_7\" name=\"numericRating\" type=\"radio\" value=\"3\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb7\" id=\"Ans_8\" name=\"numericRating\" type=\"radio\" value=\"3.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb8\" id=\"Ans_9\" name=\"numericRating\" type=\"radio\" value=\"4\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb9\" id=\"Ans_10\" name=\"numericRating\" type=\"radio\" value=\"4.5\" checked=\"checked\" />");
                    out.println("<input class=\"rb10\" id=\"Ans_11\" name=\"numericRating\" type=\"radio\" value=\"5\" disabled=\"disabled\" />");
                }
                else if(averageRating == 5){
                    out.println("<input class=\"rb0\" id=\"Ans_1\" name=\"numericRating\" type=\"radio\" value=\"0\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb1\" id=\"Ans_2\" name=\"numericRating\" type=\"radio\" value=\"0.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb2\" id=\"Ans_3\" name=\"numericRating\" type=\"radio\" value=\"1\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb3\" id=\"Ans_4\" name=\"numericRating\" type=\"radio\" value=\"1.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb4\" id=\"Ans_5\" name=\"numericRating\" type=\"radio\" value=\"2\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb5\" id=\"Ans_6\" name=\"numericRating\" type=\"radio\" value=\"2.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb6\" id=\"Ans_7\" name=\"numericRating\" type=\"radio\" value=\"3\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb7\" id=\"Ans_8\" name=\"numericRating\" type=\"radio\" value=\"3.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb8\" id=\"Ans_9\" name=\"numericRating\" type=\"radio\" value=\"4\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb9\" id=\"Ans_10\" name=\"numericRating\" type=\"radio\" value=\"4.5\" disabled=\"disabled\" />");
                    out.println("<input class=\"rb10\" id=\"Ans_11\" name=\"numericRating\" type=\"radio\" value=\"5\" checked=\"checked\" />");
                }
                        
                }catch(SQLException ex){
                    out.println("SQLException in Query.java");
                    ex.printStackTrace(out);
                }finally
                {
                    DBUtilities.closeResultSet(ratingResult);
                    DBUtilities.closeStatement(statement3);
                }

            out.println("<label for=\"Ans_1\" class=\"star rb0l\" onclick=\"\"></label>");
            out.println("<label for=\"Ans_2\" class=\"star rb1l\" onclick=\"\"></label>");
            out.println("<label for=\"Ans_3\" class=\"star rb2l\" onclick=\"\"></label>");
            out.println("<label for=\"Ans_4\" class=\"star rb3l\" onclick=\"\"></label>");
            out.println("<label for=\"Ans_5\" class=\"star rb4l\" onclick=\"\"></label>");
            out.println("<label for=\"Ans_6\" class=\"star rb5l\" onclick=\"\"></label>");
            out.println("<label for=\"Ans_7\" class=\"star rb6l\" onclick=\"\"></label>");
            out.println("<label for=\"Ans_8\" class=\"star rb7l\" onclick=\"\"></label>");
            out.println("<label for=\"Ans_9\" class=\"star rb8l\" onclick=\"\"></label>");
            out.println("<label for=\"Ans_10\" class=\"star rb9l\" onclick=\"\"></label>");
            out.println("<label for=\"Ans_11\" class=\"star rb10l last\" onclick=\"\"></label>");
            
            out.println("<label for=\"Ans_1\" class=\"rb\" onclick=\"\">0</label>");
            out.println("<label for=\"Ans_2\" class=\"rb\" onclick=\"\">1</label>");
            out.println("<label for=\"Ans_3\" class=\"rb\" onclick=\"\">2</label>");
            out.println("<label for=\"Ans_4\" class=\"rb\" onclick=\"\">3</label>");
            out.println("<label for=\"Ans_5\" class=\"rb\" onclick=\"\">4</label>");
            out.println("<label for=\"Ans_6\" class=\"rb\" onclick=\"\">5</label>");
            out.println("<label for=\"Ans_7\" class=\"rb\" onclick=\"\">6</label>");
            out.println("<label for=\"Ans_8\" class=\"rb\" onclick=\"\">7</label>");
            out.println("<label for=\"Ans_9\" class=\"rb\" onclick=\"\">8</label>");
            out.println("<label for=\"Ans_10\" class=\"rb\" onclick=\"\">9</label>");
            out.println("<label for=\"Ans_11\" class=\"rb\" onclick=\"\">10</label>");
            
            out.println("<div class=\"rating\"></div>");
            out.println("<div class=\"rating-bg\"></div> ");
            out.println("</div> <!-- star-rating -->");
            out.println("</article>");
            
            out.println("</p>");
            out.println("</fieldset>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");             
            out.println("</html>");

        }catch (Exception ex) {
                ex.printStackTrace();
        } finally {            
            out.close();
            DBUtilities.closeStatement(statement);
            dataSource.freeConnection(conn);
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
        processRequest(request, response);
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
    }// </editor-fold>
}
