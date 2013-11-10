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

/**
 *
 * @author Matthew
 */
public class contractorCreateAccount extends HttpServlet {

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
        
        DBConnections dataSource = DBConnections.getInstance();
        Connection conn = dataSource.getConnection();
        PreparedStatement stat = null;
        Statement statement = null;
        ResultSet customerResult = null;
        ResultSet contractorResult = null;
        Contractor contractor = new Contractor();
        
        try {
            String company = request.getParameter("companyName");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("passwordConfirm");
            String info = request.getParameter("info");

            if (email.equals("") || password.equals("") || confirmPassword.equals("") || company.equals("") || phone.equals("") || info.equals("")) {
                response.sendRedirect("contractorCreateAccount.html");
            } else if (!password.equals(confirmPassword)) {
                response.sendRedirect("contractorCreateAccount.html");
            } else {

                try {
                    statement = conn.createStatement();
                    customerResult = statement.executeQuery("SELECT * FROM Customer WHERE email=\'" + email + "\'");
                    boolean customerFound = customerResult.next();
                    contractorResult = statement.executeQuery("SELECT * FROM Contractor WHERE email=\'" + email + "\'");
                    boolean contractorFound = contractorResult.next();

                    if (!customerFound && !contractorFound) {
                        String statString = "INSERT INTO Contractor (`email`, `password`, `company`"
                                + ", `phone`, `info`) VALUES (?, MD5(?), ?, ?, ?)";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, email);
                        stat.setString(2, password);
                        stat.setString(3, company);
                        stat.setString(4, phone);
                        stat.setString(5, info);
                        stat.executeUpdate();
                        contractor.login(email, password, out, request);
                        response.sendRedirect("homePage"); //change to contractor page
                    } else {
                        response.sendRedirect("contractorCreateAccount.html");
                    }
                } catch (SQLException ex) {
                    out.println("SQLException in Query.java");
                    ex.printStackTrace(out);
                } finally
                {
                    DBUtilities.closeResultSet(contractorResult);
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
