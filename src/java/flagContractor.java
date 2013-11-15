/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alex-MSU
 */
public class flagContractor extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String userType = "Guest";
        String firstName = "";
        String lastName = "";
        String email = "";
        String company = "";
        String adminEmail = "";
        HttpSession httpSession;

        if (request.getSession(false) != null) {
            httpSession = request.getSession();
            if (httpSession.getAttribute("userType") != null) {
                if (httpSession.getAttribute("userType").equals("customer")) {
                    Customer customer = (Customer) httpSession.getAttribute("user");
                    firstName = customer.getFirstName();
                    lastName = customer.getLastName();
                    email = customer.getEmail();
                    userType = "customer";
                } else if (httpSession.getAttribute("userType").equals("contractor")) {
                    Contractor contractor = (Contractor) httpSession.getAttribute("user");
                    company = contractor.getCompany();
                    userType = "contractor";
                } else if (httpSession.getAttribute("userType").equals("admin")) {
                    Admin admin = (Admin) httpSession.getAttribute("user");
                    adminEmail = admin.getEmail();
                    userType = "admin";
                }
            }
        }
        
        DBConnections dataSource = DBConnections.getInstance();
        Connection conn = dataSource.getConnection();
        Statement statement = null;
        PreparedStatement stat = null;
        ResultSet flaggedResult = null;
        
        String contractorEmail = "";
        if(request.getParameter("email") != null){
            contractorEmail = request.getParameter("email");
        }
        else{
        response.sendRedirect("homePage");
        }
        
        try{
            statement = conn.createStatement();
            flaggedResult = statement.executeQuery("SELECT * FROM Flagged WHERE email=\'" + contractorEmail + "\'");
            boolean flaggedFound = flaggedResult.next();
            
            if(flaggedFound){
                int flagged = flaggedResult.getInt("flag");
                flagged++;
                String statString = "UPDATE Flagged SET flag=? WHERE email=\'" + contractorEmail + "\'";
                stat = conn.prepareStatement(statString);
                stat.setInt(1, flagged);
                stat.executeUpdate();
            }
            else{
                //insert contractor into Flagged table w/ count = 1;
                String statString = "INSERT INTO Flagged (`email`, `flag`) VALUES (?, ?)";
                stat = conn.prepareStatement(statString);
                stat.setString(1, contractorEmail);
                stat.setInt(2, 1);
                stat.executeUpdate();
            }
            
            //add user to Has_Flagged table
            String statString = "INSERT INTO Has_Flagged (`customer_email`, `contractor_email`, `is_flagged`) VALUES (?, ?, ?)";
            stat = conn.prepareStatement(statString);
            stat.setString(1, email);
            stat.setString(2, contractorEmail);
            stat.setInt(3, 1);
            stat.executeUpdate();
            
            response.sendRedirect("contractorPage?email=" + contractorEmail);
            
        }catch (SQLException ex) {
            out.println("SQLException in Query.java");
            ex.printStackTrace(out);
        } finally
        {
            DBUtilities.closeResultSet(flaggedResult);
            dataSource.freeConnection(conn);
            DBUtilities.closePreparedStatement(stat);
            DBUtilities.closeStatement(statement);
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
