/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abdel
 */
@WebServlet(urlPatterns = {"/Canceltransaction"})
public class Canceltransaction extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                
                int bankId = Integer.parseInt(request.getSession().getAttribute("session_bankId").toString());
                int TransactionNumber = Integer.parseInt(request.getParameter("TransactionNumber"));
                int balance = Integer.parseInt(request.getSession().getAttribute("session_balance").toString());
                
                
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/banking";
                String user = "root";
                String password1 = "root";
                Connection Con = null;
                Statement Stmt = null;
                Con = DriverManager.getConnection(url, user, password1);
                Stmt = Con.createStatement();
                String sql = "select BTFrom,BTCreationDate,BTAmount,BTToAccount from BankTransaction where BankTransactionID = '" + TransactionNumber + "' ";
                ResultSet rs = Stmt.executeQuery(sql);
                if (rs.next()) {
                    
                    int btFrom = rs.getInt(1);
                    int BTAmount = rs.getInt(3);
                    int BTToAccount = rs.getInt(4);
                    if (btFrom == bankId) {
                        Date date = new Date();
                        Timestamp currentDate = new Timestamp(date.getTime());

                        Timestamp creationDate = rs.getTimestamp(2);
                        long diff =  currentDate.getTime()- creationDate.getTime();
                       int d =  (int)diff / (1000 * 3600);
                       
                        if (d < 24) {
                            
                            String del = "DELETE FROM BankTransaction WHERE BankTransactionID = '" + TransactionNumber + "' ";
                            Stmt.executeUpdate(del);

                            int balance1 = balance + BTAmount;

                            String sqlfrom = "update BankAccount set BACurrentBalance= '" + balance1 + "' where BankAccountID = '" + bankId + "' ";
                            Stmt.executeUpdate(sqlfrom);
                            
                             sql = "select BACurrentBalance from BankAccount where BankAccountID = '" + BTToAccount + "' ";
                             rs =Stmt.executeQuery(sql);
                             rs.next();
                            int balance2 = rs.getInt(1);
                            balance2 -= BTAmount;
                            
                             sql = "update BankAccount set BACurrentBalance= '" + balance2 + "' where BankAccountID = '" + BTToAccount + "' ";
                            Stmt.executeUpdate(sql);
                            response.sendRedirect("transactions.jsp");
                           

                        } else {
                            RequestDispatcher rd = getServletContext().getRequestDispatcher("/transactions.jsp");
                        out.write("<script>alert(\"Can't make delete transaction after  1 day \");</script>");
                        rd.include(request, response);
                        }
                        
                        
                    } else {
                        RequestDispatcher rd = getServletContext().getRequestDispatcher("/transactions.jsp");
                        out.write("<script>alert(\"Can't make delete transaction you didn't  make this \");</script>");
                        rd.include(request, response);
                    }
          

                } else {
                     RequestDispatcher rd = getServletContext().getRequestDispatcher("/transactions.jsp");
                     out.write("<script>alert(\"Can't make delete transaction you didn't  make this trasaction\");</script>");
                     rd.include(request, response);
                }
                Stmt.close();
                Con.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
/**
 * Handles the HTTP <code>GET</code> method.
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
            Logger.getLogger(Canceltransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
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
            Logger.getLogger(Canceltransaction.class.getName()).log(Level.SEVERE, null, ex);
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
