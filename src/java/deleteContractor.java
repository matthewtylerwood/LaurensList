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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Matthew
 */
public class deleteContractor extends HttpServlet {

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
            throws ServletException, IOException {
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
        PreparedStatement stat = null;
        PreparedStatement stat2 = null;
        PreparedStatement stat3 = null;
        PreparedStatement stat4 = null;
        PreparedStatement stat5 = null;
        PreparedStatement stat6 = null;
        PreparedStatement stat7 = null;
        
        String contractorEmail = "";
        if(request.getParameter("email") != null){
            contractorEmail = request.getParameter("email");
        }
        else{
        response.sendRedirect("homePage");
        }
        
        try{
            
            //delete part 1
            String statString = "DELETE FROM Review WHERE contractor_email = ?";
            stat = conn.prepareStatement(statString);
            stat.setString(1, contractorEmail);
            stat.executeUpdate();
            
            String statString2 = "DELETE FROM Payment WHERE contractor_email = ?";
            stat2 = conn.prepareStatement(statString2);
            stat2.setString(1, contractorEmail);
            stat2.executeUpdate();
            
            String statString3 = "DELETE FROM Has_Flagged WHERE contractor_email = ?";
            stat3 = conn.prepareStatement(statString3);
            stat3.setString(1, contractorEmail);
            stat3.executeUpdate();
            
            //delete part 2
            String statString4 = "DELETE FROM Stars WHERE email = ?";
            stat4 = conn.prepareStatement(statString4);
            stat4.setString(1, contractorEmail);
            stat4.executeUpdate();
            
            String statString5 = "DELETE FROM Rating WHERE email = ?";
            stat5 = conn.prepareStatement(statString5);
            stat5.setString(1, contractorEmail);
            stat5.executeUpdate();
            
            String statString6 = "DELETE FROM Flagged WHERE email = ?";
            stat6 = conn.prepareStatement(statString6);
            stat6.setString(1, contractorEmail);
            stat6.executeUpdate();
            
            String statString7 = "DELETE FROM Contractor WHERE email = ?";
            stat7 = conn.prepareStatement(statString7);
            stat7.setString(1, contractorEmail);
            stat7.executeUpdate();
            
            response.sendRedirect("adminProfile");
            
        }catch (SQLException ex) {
            out.println("SQLException in Query.java");
            ex.printStackTrace(out);
        } finally
        {
            dataSource.freeConnection(conn);
            DBUtilities.closePreparedStatement(stat);
            DBUtilities.closePreparedStatement(stat2);
            DBUtilities.closePreparedStatement(stat3);
            DBUtilities.closePreparedStatement(stat4);
            DBUtilities.closePreparedStatement(stat5);
            DBUtilities.closePreparedStatement(stat6);
            DBUtilities.closePreparedStatement(stat7);
            
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
        processRequest(request, response);
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
        processRequest(request, response);
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
