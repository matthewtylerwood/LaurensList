/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class contractorProfile extends HttpServlet {

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
        String email = "";
        String company = "";
        String phone = "";
        String info = "";
        HttpSession httpSession;
        
        if(request.getSession(false) != null)
        {
            httpSession = request.getSession();
            if(httpSession.getAttribute("userType") != null){
                if (httpSession.getAttribute("userType").equals("customer"))
                {
                    response.sendRedirect("homePage");
                }
                else if(httpSession.getAttribute("userType").equals("contractor"))
                {
                    Contractor contractor = (Contractor)httpSession.getAttribute("user");
                    email = contractor.getEmail();
                    company = contractor.getCompany();
                    phone = contractor.getPhone();
                    info = contractor.getInfo();
                }
                else if(httpSession.getAttribute("userType").equals("admin"))
                {
                    response.sendRedirect("homePage");
                }
            }
            else{
                response.sendRedirect("homePage");
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
            
            
            out.println("<a class=\"pure-button\" href=\"contractorProfile\"> " + company + " </a> &nbsp;");
            out.println("<a class=\"pure-button\" href=\"changePassword\"> Change Password </a> &nbsp;");
            out.println("<a class=\"pure-button\" href=\"logout\"> Logout </a> &nbsp;<br/><br/>");
            
                        
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
            out.println("<div class=\"pure-g\">");
            out.println("<div class=\"pure-u-1-4\">");
            out.println("<fieldset>");
            out.println("<legend> Information </legend>");
            out.println("<p>" + company + "<br />" + email + "<br />" + phone + "</p>");
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
                    
                    ResultSet customerResult = null;
                    Statement statement2 = null;
                    try{
                        statement2 = conn.createStatement();
                        customerResult = statement2.executeQuery("SELECT * FROM Customer WHERE email=\'" + customerEmail + "\'");
                        boolean customerFound = customerResult.next();
                        String firstName = "";
                        String lastName = "";
                        if(customerFound){
                            firstName = customerResult.getString("first_name");
                            lastName = customerResult.getString("last_name");
                        }
                        out.println("<p>" + firstName + " " + lastName + "<br />" + review + "<br /><br /></p>");
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
            out.println("<p>Thirds</p>");
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
