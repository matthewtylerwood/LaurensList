
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*
 * Represents a Admin of Lauren's List.
 */
public class Admin {
    private String userType;
    private String email;
    private String password;
    
    public Admin()
    {
        userType = "admin";
        email = "";
        password = "";
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
    // </editor-fold>
    
 /*
 * Code for loggining in the admin
 * checks to make sure that it is the admin logging in
 * set the user type to admin if the information matches the data base
 */
    public boolean login(String email, String password, PrintWriter out, HttpServletRequest request)
    {
        DBConnections dataSource = DBConnections.getInstance();
        Connection conn = dataSource.getConnection();
        Statement statement = null;
        ResultSet adminResult;
        
        boolean adminLogin = false;
        try 
        {
            statement = conn.createStatement();
            adminResult = statement.executeQuery("SELECT * FROM Admin WHERE email=\'" + email + "\' AND password=MD5(\'" + password + "\')");
            adminLogin = adminResult.next();
            if(adminLogin)
            {
               
                HttpSession httpSession = request.getSession();
                                
                setEmail(adminResult.getString("email"));
                                
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
        
        return adminLogin;
    }
        
    public void logout(HttpServletRequest request)
    {
        HttpSession httpSession = request.getSession();
        
        setPassword("");
        setEmail("");
        
        httpSession.invalidate();
    }
    
}
