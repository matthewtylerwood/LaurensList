
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

/*
 * Logs in a user.
 */
public class login extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
               
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        
//        Administrator administrator = new Administrator();
//        Contractor contractor = new Contractor();
        Customer customer = new Customer();
        
        try 
        {
           String email = request.getParameter("email"); 
           String password = request.getParameter("password");
           
//           boolean administratorLogin = administrator.login(email, password, out, request);
//           boolean contractorLogin = contractor.login(email, password, out, request);
           boolean customerLogin = customer.login(email, password, out, request);
           
           if (customerLogin) //(managerLogin && !contractorLogin && !customerLogin) 
           {
               response.sendRedirect("adminHome");              
           } 
//           else if(!managerLogin && contractorLogin && !customerLogin) 
//           {
//               response.sendRedirect("contractorPage");
//           }
//           else if(!managerLogin && !contractorLogin && customerLogin) 
//           {
//               response.sendRedirect("homePage");
//           }
           else
           {
               response.sendRedirect("login.html");
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
