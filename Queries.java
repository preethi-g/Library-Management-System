/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author si179
 */
@WebServlet(urlPatterns = {"/Queries"})
@MultipartConfig
public class Queries extends HttpServlet {

   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String str = request.getParameter("check");
            String req = request.getParameter("bauthor");
            String req1=request.getParameter("details");
            Class.forName("org.postgresql.Driver");
            Connection con = DBconnection.getConnection();
            if (str != null) {
                String str2 = request.getParameter("book");
                String str3 = request.getParameter("author");
                ResultSet rs = available(str2, str3, con);
                out.println("<html><head></head> <body><center><table><tr><th>BOOKNAME</th><th>AUTHOR</th><th>COUNT</th></tr>");
                while(rs.next()) {
                    out.println("<tr><td>"+ rs.getString("bookname")+"</td><td>" + rs.getString("author") + "</td><td>"+rs.getInt("count")+"</td></tr>");
                }
                 out.println("</table></center>,</body></html>");
            }
            if (req != null) {
                String str3 = request.getParameter("author");
                ResultSet rs = findbook_of_author(str3, con);
                out.println("<html> <head> </head> <body><center><table><tr><th>BOOKS</th><th>AUTHOR</th></tr>"); 
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getString("bookname") + "</td><td>"+rs.getString("author")+"</td></tr>");
                }
                out.println("</table></center>,</body></html>");
            }
            if(req1!=null)
            {
                String str1=request.getParameter("book");
                ResultSet rs=book_details(str1,con);
                out.println("<html> <head> </head> <body><center><table><tr><th>AUTHOR</th><th>COUNT</th></tr>");
                 while(rs.next())
               {
                       out.println("<tr><td>" + rs.getString("author") + "</td><td>"+rs.getInt("count")+"</td></tr>");
                     
                 }
                 out.println("</table></center>,</body></html>");
            }
        }
    }

    ResultSet available(String str1, String str2, Connection con) throws Exception {
        String bookname = str1;
        String author = str2;
        String qry = "select * from library where bookname like '%"+ bookname +"%' and author like '%"+author+"%';";
        Connection connection = con;
        Statement state = connection.createStatement();
        ResultSet rs = state.executeQuery(qry);
        connection.close();
        return rs;
        
    }

    ResultSet findbook_of_author(String str, Connection con)throws Exception
    {
         String author=str;
         String qry = "select bookname,author from library where author like '%"+author+"%';";
         Connection connection=con;
         Statement state = connection.createStatement();
         ResultSet rs = state.executeQuery(qry);
         con.close();
         return rs;
    }
     ResultSet book_details(String s1,Connection con)throws Exception
    {
        String str1=s1;
        String qry = "select author,count from library where bookname = '"+str1+"';";
         Connection connection = con;
        Statement state = connection.createStatement();
        ResultSet rs = state.executeQuery(qry);
       
           return rs;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
