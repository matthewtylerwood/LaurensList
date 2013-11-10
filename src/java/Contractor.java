
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
public class Contractor {
    private String userType;
    private String email;
    private String password;
    private String company;
    private String phone;
    private String info;
    
    public Contractor()
    {
        userType = "contractor";
        email = "";
        password = "";
        company = "";
        phone = "";
        info = "";
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
    
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    // </editor-fold>
    
    
    public boolean login(String email, String password, PrintWriter out, HttpServletRequest request)
    {
        DBConnections dataSource = DBConnections.getInstance();
        Connection conn = dataSource.getConnection();
        Statement statement = null;
        ResultSet contractorResult;
        
        boolean contractorLogin = false;
        try 
        {
            statement = conn.createStatement();
            contractorResult = statement.executeQuery("SELECT * FROM Contractor WHERE email=\'" + email + "\' AND password=MD5(\'" + password + "\')");
            contractorLogin = contractorResult.next();
            if(contractorLogin)
            {
               
                HttpSession httpSession = request.getSession();
                                
                setEmail(contractorResult.getString("email"));
                setCompany(contractorResult.getString("company"));
                setPhone(contractorResult.getString("phone"));
                setInfo(contractorResult.getString("info"));
                                
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
        
        return contractorLogin;
    }
        
    public void logout(HttpServletRequest request)
    {
        HttpSession httpSession = request.getSession();
        
        setPassword("");
        setEmail("");
        setCompany("");
        setPhone("");
        setInfo("");
        
        httpSession.invalidate();
    }
    
}
