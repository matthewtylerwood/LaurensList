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
public class saveRating extends HttpServlet {

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
        ResultSet starsResult = null;
        
        try {
            String numericRating = request.getParameter("numericRating");
            String review = request.getParameter("review");
            String contractorEmail = request.getParameter("contractorEmail");
           
            int numRatings;
            float numRating = Float.parseFloat(numericRating);
            try{
                statement = conn.createStatement();
                starsResult = statement.executeQuery("SELECT * FROM Stars WHERE email=\'" + contractorEmail + "\'");
                boolean starsFound = starsResult.next();
                
                if(starsFound){
                    numRatings = starsResult.getInt(numericRating);
                    numRatings++;
                    String statString = "UPDATE Stars SET `" + numericRating + "`=? WHERE email=\'" + contractorEmail + "\'";
                    stat = conn.prepareStatement(statString);
                    stat.setFloat(1, numRatings);
                    stat.executeUpdate();
                    
                    ResultSet starsResult2 = null;
                    Statement statement2 = null;
                    float average;
                    try{
                        statement2 = conn.createStatement();
                        starsResult2 = statement2.executeQuery("SELECT * FROM Stars WHERE email=\'" + contractorEmail + "\'");
                        boolean starsFound2 = starsResult2.next();
                        int stars0 = 0;
                        int stars0_5 = 0;
                        int stars1 = 0;
                        int stars1_5 = 0;
                        int stars2 = 0;
                        int stars2_5 = 0;
                        int stars3 = 0;
                        int stars3_5 = 0;
                        int stars4 = 0;
                        int stars4_5 = 0;
                        int stars5 = 0;
                        if(starsFound2){
                            stars0 = starsResult2.getInt("0");
                            stars0_5 = starsResult2.getInt("0.5");
                            stars1 = starsResult2.getInt("1");
                            stars1_5 = starsResult2.getInt("1.5");
                            stars2 = starsResult2.getInt("2");
                            stars2_5 = starsResult2.getInt("2.5");
                            stars3 = starsResult2.getInt("3");
                            stars3_5 = starsResult2.getInt("3.5");
                            stars4 = starsResult2.getInt("4");
                            stars4_5 = starsResult2.getInt("4.5");
                            stars5 = starsResult2.getInt("5");
                        }
                        //calculate average
                        average = (float)((stars5 * 5 + stars4_5 * 4.5 + stars4 * 4 + stars3_5 * 3.5 + stars3 * 3 + stars2_5 * 2.5 + stars2 * 2 + stars1_5 * 1.5 + stars1 * 1 + stars0_5 * 0.5 + stars0 * 0) / (stars5 + stars4_5 + stars4 + stars3_5 + stars3 + stars2_5 + stars2 + stars1_5 + stars1 + stars0_5 + stars0));
                        PreparedStatement stat3 = null;
                        try{
                            String statString2 = "UPDATE Rating SET rating=? WHERE email=\'" + contractorEmail + "\'";
                            stat3 = conn.prepareStatement(statString2);
                            stat3.setFloat(1, average);
                            stat3.executeUpdate();
                        }catch(SQLException ex){
                            out.println("SQLException in Query.java");
                            ex.printStackTrace(out);
                        }finally
                        {
                            DBUtilities.closePreparedStatement(stat3);
                        }
                        
                    }catch(SQLException ex){
                        out.println("SQLException in Query.java");
                        ex.printStackTrace(out);
                    }finally
                    {
                        DBUtilities.closeResultSet(starsResult2);
                        DBUtilities.closeStatement(statement2);
                    }

                }
                else{
                    numRatings = 1;
                    if(numericRating.equals("0")){
                        String statString = "INSERT INTO Stars (`email`, `0`, `0.5`, `1`, `1.5`, `2`, `2.5`, `3`, `3.5`, `4`, `4.5`, `5`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, contractorEmail);
                        stat.setInt(2, numRatings);
                        stat.setInt(3, 0);
                        stat.setInt(4, 0);
                        stat.setInt(5, 0);
                        stat.setInt(6, 0);
                        stat.setInt(7, 0);
                        stat.setInt(8, 0);
                        stat.setInt(9, 0);
                        stat.setInt(10, 0);
                        stat.setInt(11, 0);
                        stat.setInt(12, 0);
                        stat.executeUpdate();
                    }
                    else if(numericRating.equals("0.5")){
                        String statString = "INSERT INTO Stars (`email`, `0`, `0.5`, `1`, `1.5`, `2`, `2.5`, `3`, `3.5`, `4`, `4.5`, `5`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, contractorEmail);
                        stat.setInt(2, 0);
                        stat.setInt(3, numRatings);
                        stat.setInt(4, 0);
                        stat.setInt(5, 0);
                        stat.setInt(6, 0);
                        stat.setInt(7, 0);
                        stat.setInt(8, 0);
                        stat.setInt(9, 0);
                        stat.setInt(10, 0);
                        stat.setInt(11, 0);
                        stat.setInt(12, 0);
                        stat.executeUpdate();
                    }
                    else if(numericRating.equals("1")){
                        String statString = "INSERT INTO Stars (`email`, `0`, `0.5`, `1`, `1.5`, `2`, `2.5`, `3`, `3.5`, `4`, `4.5`, `5`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, contractorEmail);
                        stat.setInt(2, 0);
                        stat.setInt(3, 0);
                        stat.setInt(4, numRatings);
                        stat.setInt(5, 0);
                        stat.setInt(6, 0);
                        stat.setInt(7, 0);
                        stat.setInt(8, 0);
                        stat.setInt(9, 0);
                        stat.setInt(10, 0);
                        stat.setInt(11, 0);
                        stat.setInt(12, 0);
                        stat.executeUpdate();
                    }
                    else if(numericRating.equals("1.5")){
                        String statString = "INSERT INTO Stars (`email`, `0`, `0.5`, `1`, `1.5`, `2`, `2.5`, `3`, `3.5`, `4`, `4.5`, `5`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, contractorEmail);
                        stat.setInt(2, 0);
                        stat.setInt(3, 0);
                        stat.setInt(4, 0);
                        stat.setInt(5, numRatings);
                        stat.setInt(6, 0);
                        stat.setInt(7, 0);
                        stat.setInt(8, 0);
                        stat.setInt(9, 0);
                        stat.setInt(10, 0);
                        stat.setInt(11, 0);
                        stat.setInt(12, 0);
                        stat.executeUpdate();
                    }
                    else if(numericRating.equals("2")){
                        String statString = "INSERT INTO Stars (`email`, `0`, `0.5`, `1`, `1.5`, `2`, `2.5`, `3`, `3.5`, `4`, `4.5`, `5`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, contractorEmail);
                        stat.setInt(2, 0);
                        stat.setInt(3, 0);
                        stat.setInt(4, 0);
                        stat.setInt(5, 0);
                        stat.setInt(6, numRatings);
                        stat.setInt(7, 0);
                        stat.setInt(8, 0);
                        stat.setInt(9, 0);
                        stat.setInt(10, 0);
                        stat.setInt(11, 0);
                        stat.setInt(12, 0);
                        stat.executeUpdate();
                    }
                    else if(numericRating.equals("2.5")){
                        String statString = "INSERT INTO Stars (`email`, `0`, `0.5`, `1`, `1.5`, `2`, `2.5`, `3`, `3.5`, `4`, `4.5`, `5`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, contractorEmail);
                        stat.setInt(2, 0);
                        stat.setInt(3, 0);
                        stat.setInt(4, 0);
                        stat.setInt(5, 0);
                        stat.setInt(6, 0);
                        stat.setInt(7, numRatings);
                        stat.setInt(8, 0);
                        stat.setInt(9, 0);
                        stat.setInt(10, 0);
                        stat.setInt(11, 0);
                        stat.setInt(12, 0);
                        stat.executeUpdate();
                    }
                    else if(numericRating.equals("3")){
                        String statString = "INSERT INTO Stars (`email`, `0`, `0.5`, `1`, `1.5`, `2`, `2.5`, `3`, `3.5`, `4`, `4.5`, `5`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, contractorEmail);
                        stat.setInt(2, 0);
                        stat.setInt(3, 0);
                        stat.setInt(4, 0);
                        stat.setInt(5, 0);
                        stat.setInt(6, 0);
                        stat.setInt(7, 0);
                        stat.setInt(8, numRatings);
                        stat.setInt(9, 0);
                        stat.setInt(10, 0);
                        stat.setInt(11, 0);
                        stat.setInt(12, 0);
                        stat.executeUpdate();
                    }
                     else if(numericRating.equals("3.5")){
                        String statString = "INSERT INTO Stars (`email`, `0`, `0.5`, `1`, `1.5`, `2`, `2.5`, `3`, `3.5`, `4`, `4.5`, `5`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, contractorEmail);
                        stat.setInt(2, 0);
                        stat.setInt(3, 0);
                        stat.setInt(4, 0);
                        stat.setInt(5, 0);
                        stat.setInt(6, 0);
                        stat.setInt(7, 0);
                        stat.setInt(8, 0);
                        stat.setInt(9, numRatings);
                        stat.setInt(10, 0);
                        stat.setInt(11, 0);
                        stat.setInt(12, 0);
                        stat.executeUpdate();
                    }
                    else if(numericRating.equals("4")){
                        String statString = "INSERT INTO Stars (`email`, `0`, `0.5`, `1`, `1.5`, `2`, `2.5`, `3`, `3.5`, `4`, `4.5`, `5`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, contractorEmail);
                        stat.setInt(2, 0);
                        stat.setInt(3, 0);
                        stat.setInt(4, 0);
                        stat.setInt(5, 0);
                        stat.setInt(6, 0);
                        stat.setInt(7, 0);
                        stat.setInt(8, 0);
                        stat.setInt(9, 0);
                        stat.setInt(10, numRatings);
                        stat.setInt(11, 0);
                        stat.setInt(12, 0);
                        stat.executeUpdate();
                    }
                    else if(numericRating.equals("4.5")){
                        String statString = "INSERT INTO Stars (`email`, `0`, `0.5`, `1`, `1.5`, `2`, `2.5`, `3`, `3.5`, `4`, `4.5`, `5`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, contractorEmail);
                        stat.setInt(2, 0);
                        stat.setInt(3, 0);
                        stat.setInt(4, 0);
                        stat.setInt(5, 0);
                        stat.setInt(6, 0);
                        stat.setInt(7, 0);
                        stat.setInt(8, 0);
                        stat.setInt(9, 0);
                        stat.setInt(10, 0);
                        stat.setInt(11, numRatings);
                        stat.setInt(12, 0);
                        stat.executeUpdate();
                    }
                    else if(numericRating.equals("5")){
                        String statString = "INSERT INTO Stars (`email`, `0`, `0.5`, `1`, `1.5`, `2`, `2.5`, `3`, `3.5`, `4`, `4.5`, `5`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        stat = conn.prepareStatement(statString);
                        stat.setString(1, contractorEmail);
                        stat.setInt(2, 0);
                        stat.setInt(3, 0);
                        stat.setInt(4, 0);
                        stat.setInt(5, 0);
                        stat.setInt(6, 0);
                        stat.setInt(7, 0);
                        stat.setInt(8, 0);
                        stat.setInt(9, 0);
                        stat.setInt(10, 0);
                        stat.setInt(11, 0);
                        stat.setInt(12, numRatings);
                        stat.executeUpdate();
                    }
                    //store new average
                    PreparedStatement stat4 = null;
                    try{
                        String statString2 = "INSERT INTO Rating (`email`, `rating`) VALUES (?, ?)";
                        stat4 = conn.prepareStatement(statString2);
                        stat4.setString(1, contractorEmail);
                        stat4.setFloat(2, numRating);
                        stat4.executeUpdate();
                    }catch(SQLException ex){
                        out.println("SQLException in Query.java");
                        ex.printStackTrace(out);
                    }finally
                    {
                        DBUtilities.closePreparedStatement(stat4);
                    }
                    
                }
                       
                //Save Review
                int reviewNum = 1;
                ResultSet reviewResult = null;
                Statement statement3 = null;
                try{
                    statement3 = conn.createStatement();
                    reviewResult = statement3.executeQuery("SELECT * FROM Review WHERE customer_email=\'" + email + "\' AND contractor_email=\'" + contractorEmail + "\'");
                    //boolean contractorFound = reviewResult.next();
                    int reviewID;
                    while(reviewResult.next()){
                        reviewID = reviewResult.getInt("review_num");
                        if(reviewID > reviewNum){
                            reviewNum = reviewID;
                        }
                    }
                    if(reviewNum != 1){
                        reviewNum++;
                    }
                    PreparedStatement stat5 = null;
                    try{
                        String statString2 = "INSERT INTO Review (`review_num`, `customer_email`, `contractor_email`, `review`, `stars`) VALUES (?, ?, ?, ?, ?)";
                        stat5 = conn.prepareStatement(statString2);
                        stat5.setInt(1, reviewNum);
                        stat5.setString(2, email);
                        stat5.setString(3, contractorEmail);
                        stat5.setString(4, review);
                        stat5.setFloat(5, numRating);
                        stat5.executeUpdate();
                    }catch(SQLException ex){
                        out.println("SQLException in Query.java");
                        ex.printStackTrace(out);
                    }finally
                    {
                        DBUtilities.closePreparedStatement(stat5);
                    }
                    
                }catch(SQLException ex){
                    out.println("SQLException in Query.java");
                    ex.printStackTrace(out);
                }finally
                {
                    DBUtilities.closeResultSet(reviewResult);
                    DBUtilities.closeStatement(statement3);
                }
                
                //write code to update the payment table
                PreparedStatement stat2 = null;
                try{
                    String statString2 = "UPDATE Payment SET reviewed=? WHERE contractor_email=\'" + contractorEmail + "\' AND customer_email=\'" + email + "\'";
                    stat2 = conn.prepareStatement(statString2);
                    stat2.setInt(1, 1);
                    stat2.executeUpdate();
                    response.sendRedirect("contractorPage?email=" + contractorEmail);
                }catch(SQLException ex){
                    out.println("SQLException in Query.java");
                    ex.printStackTrace(out);
                }finally
                {
                    DBUtilities.closePreparedStatement(stat2);
                }

            }catch(SQLException ex){
                out.println("SQLException in Query.java");
                ex.printStackTrace(out);
            }finally
            {
                DBUtilities.closeResultSet(starsResult);
                DBUtilities.closeStatement(statement);
                DBUtilities.closePreparedStatement(stat);
            }                      
        
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            out.close();
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
