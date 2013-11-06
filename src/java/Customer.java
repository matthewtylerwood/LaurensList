
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*
 * Represents a Customer of Lauren's List.
 */
public class Customer {
    private String userType;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    
    public Customer()
    {
        userType = "customer";
        email = "";
        password = "";
        firstName = "";
        lastName = "";
    }

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters.">
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    // </editor-fold>
    
    
    public boolean login(String email, String password, PrintWriter out, HttpServletRequest request)
    {
        DBConnections dataSource = DBConnections.getInstance();
        Connection conn = dataSource.getConnection();
        Statement statement = null;
        ResultSet customerResult;
        
        boolean customerLogin = false;
        try 
        {
            statement = conn.createStatement();
            customerResult = statement.executeQuery("SELECT * FROM Customer WHERE email=\'" + email + "\' AND password=MD5(\'" + password + "\')");
            customerLogin = customerResult.next();
            if(customerLogin)
            {
               
                HttpSession httpSession = request.getSession();
                                
                setEmail(customerResult.getString("email"));
                setFirstName(customerResult.getString("first_name"));
                setLastName(customerResult.getString("last_name"));
                                
                httpSession.setAttribute("user", this);
                httpSession.setAttribute("userType", userType);
            }
        } 
        catch (SQLException ex)
        {
            out.println("SQLException in Query.java");
            ex.printStackTrace(out);
        }
        finally
        {
            DBUtilities.closeStatement(statement);
            dataSource.freeConnection(conn);
        }
        
        return customerLogin;
    }
        
    public void logout(HttpServletRequest request)
    {
        HttpSession httpSession = request.getSession();
        
        setPassword("");
        setEmail("");
        setFirstName("");
        setLastName("");
        
        httpSession.invalidate();
    }
    
}
