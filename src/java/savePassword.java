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

/*
 * Saves the User's password after it has been changed.
 */
public class savePassword extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        DBConnections dataSource = DBConnections.getInstance();
        Connection conn = dataSource.getConnection();
        PreparedStatement stat = null;
        Statement statement = null;
        ResultSet passwordResult = null;

        HttpSession httpSession = request.getSession();

        if(httpSession.getAttribute("userType").equals("customer"))
        {
            Customer customer = (Customer)httpSession.getAttribute("user");
            String email = customer.getEmail();
            try {
                String oldPassword = request.getParameter("password");
                String newPassword = request.getParameter("passwordNew");
                String confirmNewPassword = request.getParameter("passwordNewConfirm");
                
                boolean passwordMatch = false;                
                statement = conn.createStatement();
                passwordResult = statement.executeQuery("SELECT * FROM Customer WHERE email=\'" + email + "\' AND password=MD5(\'" + oldPassword + "\')");
                passwordMatch = passwordResult.next();
                if (!passwordMatch) {
                    response.sendRedirect("changePassword");
                } else if (!newPassword.equals(confirmNewPassword)) {
                    response.sendRedirect("changePassword");
                } else if (newPassword.equals("") || confirmNewPassword.equals("")){
                    response.sendRedirect("changePassword");
                }
                else 
                {
                    try {
                        
                        String statString = "UPDATE Customer SET password=MD5(?) WHERE email=\'" + email + "\'";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, newPassword);
                        stat.executeUpdate();
                                               
                    } catch (SQLException ex) {
                        out.println("SQLException in Query.java");
                        ex.printStackTrace(out);
                    } finally
                    {
                        DBUtilities.closeResultSet(passwordResult); 
                    }

                    response.sendRedirect("homePage");
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
        else if(httpSession.getAttribute("userType").equals("contractor"))
        {
            Contractor contractor = (Contractor)httpSession.getAttribute("user");
            String email = contractor.getEmail();
            try {
                String oldPassword = request.getParameter("password");
                String newPassword = request.getParameter("passwordNew");
                String confirmNewPassword = request.getParameter("passwordNewConfirm");
                
                boolean passwordMatch = false;                
                statement = conn.createStatement();
                passwordResult = statement.executeQuery("SELECT * FROM Contractor WHERE email=\'" + email + "\' AND password=MD5(\'" + oldPassword + "\')");
                passwordMatch = passwordResult.next();
                if (!passwordMatch) {
                    response.sendRedirect("changePassword");
                } else if (!newPassword.equals(confirmNewPassword)) {
                    response.sendRedirect("changePassword");
                } else if (newPassword.equals("") || confirmNewPassword.equals("")){
                    response.sendRedirect("changePassword");
                }
                else 
                {
                    try {
                        
                        String statString = "UPDATE Contractor SET password=MD5(?) WHERE email=\'" + email + "\'";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, newPassword);
                        stat.executeUpdate();
                                               
                    } catch (SQLException ex) {
                        out.println("SQLException in Query.java");
                        ex.printStackTrace(out);
                    } finally
                    {
                        DBUtilities.closeResultSet(passwordResult); 
                    }

                    response.sendRedirect("homePage"); //change to contractor page
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
        else if(httpSession.getAttribute("userType").equals("admin"))
        {
            Admin admin = (Admin)httpSession.getAttribute("user");
            String email = admin.getEmail();
            try {
                String oldPassword = request.getParameter("password");
                String newPassword = request.getParameter("passwordNew");
                String confirmNewPassword = request.getParameter("passwordNewConfirm");
                
                boolean passwordMatch = false;                
                statement = conn.createStatement();
                passwordResult = statement.executeQuery("SELECT * FROM Admin WHERE email=\'" + email + "\' AND password=MD5(\'" + oldPassword + "\')");
                passwordMatch = passwordResult.next();
                if (!passwordMatch) {
                    response.sendRedirect("changePassword");
                } else if (!newPassword.equals(confirmNewPassword)) {
                    response.sendRedirect("changePassword");
                } else if (newPassword.equals("") || confirmNewPassword.equals("")){
                    response.sendRedirect("changePassword");
                }
                else 
                {
                    try {
                        
                        String statString = "UPDATE Admin SET password=MD5(?) WHERE email=\'" + email + "\'";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, newPassword);
                        stat.executeUpdate();
                                               
                    } catch (SQLException ex) {
                        out.println("SQLException in Query.java");
                        ex.printStackTrace(out);
                    } finally
                    {
                        DBUtilities.closeResultSet(passwordResult); 
                    }

                    response.sendRedirect("homePage"); //change to contractor page
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
