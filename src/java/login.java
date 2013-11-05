
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * Logs in a user.
 */
public class login extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
               
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        
//        Manager manager = new Manager();
        Customer customer = new Customer();
        
        try 
        {
           String userName = request.getParameter("userName"); 
           String password = request.getParameter("password");
           
//           boolean managerLogin = manager.login(userName, password, out, request);
           boolean customerLogin = customer.login(userName, password, out, request);
           
           if (customerLogin) //(managerLogin && !customerLogin) 
           {
               response.sendRedirect("managerHome");              
           } 
//           else if(customerLogin && !managerLogin) 
//           {
//               response.sendRedirect("homePage");
//           }
           else
           {
               response.sendRedirect("loginSignUp.html");
           }
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace(out);
        } 
        finally 
        {
            out.close();
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    public String getServletInfo() {
        return "Short description";
    }
}
