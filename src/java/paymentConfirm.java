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
public class paymentConfirm extends HttpServlet {

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
        ResultSet paymentResult = null;
        
        try {
            String firstNamePay = request.getParameter("firstNamePay"); 
            String lastNamePay = request.getParameter("lastNamePay");
            String street1Pay = request.getParameter("street1Pay");
            String street2Pay = request.getParameter("street2Pay");
            String cityPay = request.getParameter("cityPay");
            String statePay = request.getParameter("statePay");
            String zipPay = request.getParameter("zipPay");
            String creditCardPay = request.getParameter("creditCardPay");
            String securityPay = request.getParameter("securityPay");
            String expirationPay = request.getParameter("expirationPay");
            String contractorEmail = request.getParameter("contractorEmail");
           
            boolean isValid = CardValidator.isValid(creditCardPay);

            if(isValid){
                
                try{
                    statement = conn.createStatement();
                    paymentResult = statement.executeQuery("SELECT * FROM Payment WHERE customer_email=\'" + email + "\' AND contractor_email=\'" + contractorEmail + "\'");
                    boolean paymentFound = paymentResult.next();
                    if(paymentFound){
                        PreparedStatement stat2 = null;
                        try{
                            String statString2 = "UPDATE Payment SET reviewed=? WHERE customer_email=\'" + email + "\' AND contractor_email=\'" + contractorEmail + "\'";
                            stat2 = conn.prepareStatement(statString2);
                            stat2.setInt(1, 0);
                            stat2.executeUpdate();
                        }catch(SQLException ex){
                            out.println("SQLException in Query.java");
                            ex.printStackTrace(out);
                        }finally
                        {
                            DBUtilities.closePreparedStatement(stat2);
                        }
                    }
                    else{
                        try{
                            String statString = "INSERT INTO Payment (`customer_email`, `contractor_email`, `reviewed`) VALUES (?, ?, ?)";
                            stat = conn.prepareStatement(statString);
                            stat.setString(1, email);
                            stat.setString(2, contractorEmail);
                            stat.setInt(3, 0);
                            stat.executeUpdate();
                        }catch(SQLException ex){
                            out.println("SQLException in Query.java");
                            ex.printStackTrace(out);
                        }finally
                        {
                            DBUtilities.closePreparedStatement(stat);
                        }
                    }
                    
                }catch(SQLException ex){
                    out.println("SQLException in Query.java");
                    ex.printStackTrace(out);
                }finally
                {
                    DBUtilities.closeResultSet(paymentResult);
                    DBUtilities.closeStatement(statement);
                    dataSource.freeConnection(conn);
                    
                }
                               
                response.sendRedirect("contractorPage?email=" + contractorEmail);
            }
            else{
                response.sendRedirect("payment?email=" + contractorEmail);
            }          
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            out.close();
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
