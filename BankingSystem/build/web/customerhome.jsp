<%-- 
    Document   : customerhome
    Created on : Dec 24, 2020, 5:11:17 PM
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
        <title>Customer Home</title>
        <style>
            .btn{background-color: #4CAF50;
                 color: white;
                 width: 95%;
                 padding: 16px 20px;
                 border: none;
                 
            }
        </style>
    </head>
    <body>
        <%
            
            
            String username = request.getSession().getAttribute("session_userName").toString();
                    
            int id = Integer.parseInt(request.getSession().getAttribute("session_id").toString());
             try {
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/banking";
                String user = "root";
                String password1 = "root";
                Connection Con = null;
                Statement Stmt = null;
                Con = DriverManager.getConnection(url, user, password1);
                Stmt = Con.createStatement();
                String sql = "select BACurrentBalance,BankAccountID from BankAccount where CustomerID = '"+ id +"' ";
                ResultSet st = Stmt.executeQuery(sql);
                
                if (st.next()) {
                    int balance = st.getInt(1);
                    int bankId = st.getInt(2);
                    session.setAttribute("session_balance", balance);
                    session.setAttribute("session_bankId", bankId);
                    %>
                    <table border="1">
                        <tr>
                            <th>Account Balance</th>
                            <th> User Name </th>
                        </tr>
                        <tr>
                            <td>  <%=balance%></td>
                            <td>
                                 <%=username%>
                            </td>
                        </tr>
                    </table>
                    <form action="transactions.jsp">
                    <button type="sbmit" class="btn">Transaction</button>
                    </form>
                            <%
                } else {
                   %>
                   <h1>You Don't have Account bank Yet !</h1>
                   <form action="addaccount">
                    <button type="sbmit" class="btn">AddAccount</button>
                    </form>
        <%
                }

                Stmt.close();
                Con.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

          %>
        
    </body>
</html>