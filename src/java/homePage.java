import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Displays the homepage for Customers.
 */
@WebServlet(name = "homePage", urlPatterns = {"/homePage"})
public class homePage extends HttpServlet {
    
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
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String firstName = "Guest";
        String lastName = "";
        HttpSession httpSession;
        
        if(request.getSession(false) != null)
        {
            httpSession = request.getSession();
            if (httpSession.getAttribute("userType").equals("customer"))
            {
                Customer customer = (Customer)httpSession.getAttribute("user");
                firstName = customer.getFirstName();
                lastName = customer.getLastName();
            }   
        }
             
        try {
            out.println("<?xml version = \"1.0\" encoding = \"utf-8\" ?>");
            out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
            out.println("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
            out.println("<!--");
            out.println("Home Page for Lauren's List Web Site");
            out.println("-->");
            out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
            out.println("<head>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
            out.println("<title> Lauren's List Home Guest </title>");
            out.println("<link rel=\"stylesheet\" href=\"http://yui.yahooapis.com/pure/0.3.0/pure-nr-min.css\" />");
            out.println("<link rel=\"stylesheet\" href=\"style.css\" />");
            out.println("<style>");
            out.println("#center{");
            out.println("text-align:center;");
            out.println("margin-top:50px;");
            out.println("padding:50px;");
            out.println("}");
            out.println("#topRight{");
            out.println("text-align:right;");
            out.println("}");
            out.println("</style>");
            out.println("</head>");

            out.println("<body class=\"pure-skin-matt\">");
            out.println("<div id=\"topRight\">");
            out.println("<p>");
            
            if(firstName.equals("Guest")){
                out.println("<a class=\"pure-button\" href=\"login.html\"> Login </a> &nbsp;");
                out.println("<a class=\"pure-button\" href=\"customerOrContractor.html\"> Create Account </a> &nbsp;<br/><br/>");
            }
            else{
                out.println("<a class=\"pure-button\" href=\"#\"> Welcome, " + firstName + " " + lastName + " </a> &nbsp;");
                out.println("<a class=\"pure-button\" href=\"logout\"> Logout </a> &nbsp;<br/><br/>");
            }
            out.println("<a class=\"pure-button\" href=\"about.html\"> What is Lauren's List? </a> &nbsp;");
                
            out.println("</p>");
            out.println("</div>");
            out.println("<div id=\"center\">");
            out.println("<img src=\"images/LLLogo.jpg\" alt=\"Lauren's List Logo\" />");
            out.println("<h1 style=\"font-size:500%; color:darkviolet; display:block\"> Lauren's List </h1>");
            out.println("<form action=\"\" method=\"get\" class=\"pure-form\">");
            out.println("<p>");
            out.println("<input type=\"text\" name=\"search\" size=\"70\" style=\"font-size:14pt\" class=\"pure-input-rounded\" /> &nbsp;");
            out.println("<input type=\"submit\" value=\"Search\" style=\"font-size:14pt\" class=\"pure-button\" />");
            out.println("</p>");
            out.println("</form>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        }catch (Exception ex) {
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
