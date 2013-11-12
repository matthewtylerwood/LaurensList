
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


public class search extends HttpServlet {
    
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
        HttpSession httpSession;
        String firstName = "";
        String lastName = "";
        String company = "";
        String adminEmail = "";
        
        if(request.getSession(false) != null)
        {
            httpSession = request.getSession();
            if(httpSession.getAttribute("userType") != null){
                if (httpSession.getAttribute("userType").equals("customer"))
                {
                    Customer customer = (Customer)httpSession.getAttribute("user");
                    firstName = customer.getFirstName();
                    lastName = customer.getLastName();
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
        
        String keywords = request.getParameter("search");
        
        DBConnections dataSource = DBConnections.getInstance();
        Connection conn = dataSource.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        String statString;
        
        try 
        {
            statString = "SELECT * FROM Contractor WHERE company LIKE ? OR info LIKE ?;";
            stat = conn.prepareStatement(statString);
            stat.setString(1, "%" + keywords + "%");
            stat.setString(2, "%" + keywords + "%");
            result = stat.executeQuery();
            
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
            out.println("<form action=\"savePassword\" method=\"post\" class=\"pure-form pure-form-aligned\">");
            out.println("<div class=\"center\">");
            out.println("<fieldset>");
            out.println("<legend> Search Results </legend>");
            while(result.next())
            {
                
                
                out.println( result.getString("company"));
                out.println("<br/>");
                out.println( result.getString("info"));
                out.println("<br/>");
                out.println("<hr/>");
            }
            out.println("</table>");
            out.println("    </body>");
            out.println("</html>");
            
            out.println("</fieldset>");
            out.println("</div>");
            out.println("</form>");
            out.println("</div>");
            out.println("</body>");             
            out.println("</html>");
        }
        
        catch (SQLException ex)
        {
            out.println("SQLException in Query.java");
            ex.printStackTrace(out);
        }
        
        finally
        {
            out.close();
            DBUtilities.closeResultSet(result);
            DBUtilities.closeStatement(stat);
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
