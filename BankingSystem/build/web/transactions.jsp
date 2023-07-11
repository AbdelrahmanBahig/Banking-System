<%-- 
    Document   : transactions
    Created on : Dec 27, 2020, 8:04:58 PM
    Author     : abdel
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Transaction</title>
    </head>
    <body>
        
        <%
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
                String sql ="select * from BankTransaction where BTFrom='"+bankId+"' or BTToAccount='"+bankId+"' " ;
                ResultSet rs =Stmt.executeQuery(sql);
               %>
               <table border="1">
                   <tr>
                       <th>BankTransactionID</th>
                       <th>BTCreationDate</th>
                       <th>BTAmount</th>
                       <th>BTFrom </th>
                       <th>BTToAccount </th>
                   </tr>
                    <% while(rs.next()){ %>
                   <tr>
                       
                       <td><%=rs.getString("BankTransactionID")%></td>
                       <td><%=rs.getString("BTCreationDate")%></td>
                       <td><%=rs.getString("BTAmount")%></td>
                       <td><%=rs.getString("BTFrom")%></td>
                       <td><%=rs.getString("BTToAccount")%></td>
                   </tr>
                   <% }%>
               </table>
               <form action="Maketransaction">
               <input type="number" name="AccountNumber" placeholder="Enter Account Number ">
               <input type="number" name="Amount" placeholder="Enter Amount ">
               <input type="submit" name="submit" value="MakeTransaction">
               </form>
              <form action="Canceltransaction">
               <input type="number" name="TransactionNumber" placeholder="Enter Transaction Number ">
               <input type="submit" name="submit" value="CancelTransaction">
              </form>
               <%
                
                
                

                Stmt.close();
                Con.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        %>
    </body>
</html>
