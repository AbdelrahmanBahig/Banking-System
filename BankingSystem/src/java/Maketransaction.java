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

/**
 *
 * @author abdel
 */
@WebServlet(urlPatterns = {"/Maketransaction"})
public class Maketransaction extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            int amount = Integer.parseInt(request.getParameter("Amount"));
            int accountNumber = Integer.parseInt(request.getParameter("AccountNumber"));
            int bankId = Integer.parseInt(request.getSession().getAttribute("session_bankId").toString());
            int balance = Integer.parseInt(request.getSession().getAttribute("session_balance").toString());
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/banking";
                String user = "root";
                String password1 = "root";
                Connection Con = null;
                Statement Stmt = null;
                Con = DriverManager.getConnection(url, user, password1);
                Stmt = Con.createStatement();
                String sql = "select BACurrentBalance from BankAccount where BankAccountID = '" + accountNumber + "' ";
                ResultSet rs = Stmt.executeQuery(sql);
                if (rs.next()) {
                    int balanc = rs.getInt(1);

                    if (balance >= amount) {
                        
                        int balance1 = balance - amount;
                        int balance2 = balanc + amount;
                        String sqlfrom = "update BankAccount set BACurrentBalance= '" + balance1 + "'where BankAccountID = '" + bankId + "' ";
                        Stmt.executeUpdate(sqlfrom);
                        request.getSession().setAttribute("session_balance", balance1);
                        String sqlto = "update BankAccount set BACurrentBalance= '" + balance2 + "'where BankAccountID = '" + accountNumber + "' ";
                        Stmt.executeUpdate(sqlto);
                        Date date = new Date();
                          Timestamp Currdate = new Timestamp(date.getTime());
                        sql = "insert into BankTransaction (BTCreationDate,BTAmount,BTFrom ,BTToAccount) values ('"+Currdate+"' ,'"+amount+"', '"+bankId+"', '"+accountNumber+"' )";
                        Stmt.executeUpdate(sql);
                         response.sendRedirect("transactions.jsp");
                        
                        

                    } else {
                        RequestDispatcher rd = getServletContext().getRequestDispatcher("/transactions.jsp");
                        out.write("<script>alert(\"Can't make transaction Becaouse Current Balance does't enough to make this Transaction\");</script>");
                        rd.include(request, response);

                    }
                  
                } else {
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/transactions.jsp");
                        out.write("<script>alert(\"Can't make transaction Becaouse Account number does't match to any Users\");</script>");
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
        processRequest(request, response);
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
