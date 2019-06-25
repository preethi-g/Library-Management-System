
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author si179
 */
public class Filehandler {
    BufferedReader br;
     static String filePath,delimiter=",",firstLine;
    static String [] fileColumns,dbColumns;
    static int c;
    Filehandler(BufferedReader br) throws IOException, SQLException
    {
        this.br=br;
        firstLine=br.readLine();
        findDelimiter(firstLine);
        findColumns(firstLine);
        Connection con = DBconnection.getConnection();
        Statement statement=con.createStatement();
        ResultSet rs=statement.executeQuery("select * from library limit 1");
        getdbcolumns(rs);
    }
     static String findDelimiter(String str)
    {
         for(c=str.length()-1;Character.isLetterOrDigit(str.charAt(c));c--);
         String temp1=Character.toString(str.charAt(c));
         for(c=0;Character.isLetterOrDigit(str.charAt(c));c++);
         String temp2=Character.toString(str.charAt(c));
         if(temp1.equals(temp2))
         {
             delimiter=temp1;
         }
         else
         {
             for(++c;Character.isLetterOrDigit(str.charAt(c));c++);
             String temp3=Character.toString(str.charAt(c));
             if(temp1.equals(temp3))
             {
             delimiter=temp1;
             }
            else if(temp2.equals(temp3))
            {
             delimiter=temp2;
             }
         }
        return delimiter;
    }
    static String[] findColumns(String str)
    {
        fileColumns=str.toLowerCase().split(delimiter);
        return fileColumns;
    }
    static String[] getColumns()
    {
        return fileColumns;
    }
    static String[] getdbcolumns(ResultSet rs)
    {
        try{
            ResultSetMetaData columns= rs.getMetaData();
            int columncount=columns.getColumnCount();
            String[] columnNames=new String[columncount];
            String[] columnTypes=new String[columncount];
            for(int i=0;i<columncount;i++)
            {
                columnNames[i]=columns.getColumnName(i+1);
                columnTypes[i]=columns.getColumnTypeName(i+1);
            }
            dbColumns=columnNames;
        }catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        return dbColumns;
    }
    static String findType(String str)
    {
        char [] temp=str.trim().toCharArray();
        for(char c:temp)
        {
            if(c<'0'||c>'9')
                return "text";
        }
        return "int";
    }
    void pushtoDB() throws SQLException, InterruptedException
    {
        Writer w1 = new Writer(DBconnection.getConnection(),br);
        Writer w2 = new Writer(DBconnection.getConnection(),br);
        Writer w3 = new Writer(DBconnection.getConnection(),br);
        Writer w4 = new Writer(DBconnection.getConnection(),br);
        w1.start();
        w2.start();
        w3.start();
        w4.run();
            w1.join();
            System.out.print("hI I HVAE CROSSSED w1 join");
            w2.join();
            w3.join();
            w4.join();
            
        
        
        
    }
}
