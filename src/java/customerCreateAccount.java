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

/*
 * Signs up a new Customer.
 */
public class customerCreateAccount extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        DBConnections dataSource = DBConnections.getInstance();
        Connection conn = dataSource.getConnection();
        PreparedStatement stat = null;
        Statement statement = null;
        ResultSet customerResult = null;
        ResultSet managerResult = null;
        
        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("passwordConfirm");

            if (email.equals("") || password.equals("") || confirmPassword.equals("")) {
                response.sendRedirect("createAccount.html");
            } else if (!password.equals(confirmPassword)) {
                response.sendRedirect("createAccount.html");
            } else {

                try {
                    statement = conn.createStatement();
                    customerResult = statement.executeQuery("SELECT * FROM Customer WHERE email=\'" + email + "\'");
                    boolean customerFound = customerResult.next();
                    //managerResult = statement.executeQuery("SELECT * FROM Managers WHERE ID=\'" + email + "\'");
                    //boolean managerFound = managerResult.next();

                    if (!customerFound)/* && !managerFound)*/ {
                        String statString = "INSERT INTO Customer (`email`, `password`, `first_name`"
                                + ", `last_name`) VALUES (?, MD5(?), ?, ?)";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, email);
                        stat.setString(2, password);
                        stat.setString(3, firstName);
                        stat.setString(4, lastName);
                        stat.executeUpdate();
                        response.sendRedirect("index.jsp"); //change later
                    } else {
                        response.sendRedirect("createAccount.html");
                    }
                } catch (SQLException ex) {
                    out.println("SQLException in Query.java");
                    ex.printStackTrace(out);
                } finally
                {
                    DBUtilities.closeResultSet(managerResult);
                    DBUtilities.closeResultSet(customerResult); 
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(out);
        } finally {
            out.close();
            DBUtilities.closeStatement(stat);
            DBUtilities.closeStatement(statement);
            dataSource.freeConnection(conn);
        }
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
