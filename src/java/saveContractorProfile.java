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
 * Saves the Contractor's profile after it has been changed.
 */
public class saveContractorProfile extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        DBConnections dataSource = DBConnections.getInstance();
        Connection conn = dataSource.getConnection();
        PreparedStatement stat = null;
        Statement statement = null;
        ResultSet getResult = null;

        HttpSession httpSession = request.getSession();

        if(httpSession.getAttribute("userType").equals("contractor"))
        {
            Contractor contractor = (Contractor)httpSession.getAttribute("user");
            String email = contractor.getEmail();
            try {
                String companyNew = request.getParameter("companyName");
                String phoneNew = request.getParameter("phone");
                String infoNew = request.getParameter("info");
                
                if (phoneNew.length()!=10) 
                {
                    response.sendRedirect("contractorCreateAccount.html");
                }
                else
                {
                    statement = conn.createStatement();
                    getResult = statement.executeQuery("SELECT * FROM Contractor WHERE email=\'" + email + "\' ");

                    String statString = "UPDATE Contractor SET company=?, phone=?, info=? WHERE email=\'" + email + "\'";
                    stat = conn.prepareStatement(statString);
                    stat.setString(1, companyNew);
                    stat.setString(2, phoneNew);
                    stat.setString(3, infoNew);
                    stat.executeUpdate();

                    contractor.setCompany(companyNew);
                    contractor.setPhone(phoneNew);
                    contractor.setInfo(infoNew);

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
