
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author si179
 */
public class Writer extends Thread{
    Connection connection;
    String[] columnNames=null;
    static String query="";
    BufferedReader br;
    PreparedStatement ps;
    Writer(Connection connection,BufferedReader br) throws SQLException {
        this.connection=connection;
        columnNames=Filehandler.getColumns();
        ps = connection.prepareStatement(generateQuery(columnNames));
        this.br=br;
    }
    @Override
    public void run()
    {
        String str;
        int counter=0;
        int flag=0,index=0;
        try {
            while(!((str=br.readLine())==null))
            {
                String record[]=str.split(Filehandler.delimiter);
                for(int i=0;i<record.length;i++)
                {
                    if(Filehandler.findType(record[i]).equals("text"))
                    {
                        ps.setString(i+1,record[i]);
                    }
                    else if(Filehandler.findType(record[i]).equals("int"))
                    {
                        ps.setInt(i+1,Integer.parseInt(record[i].trim()));
                        if(flag==0)
                        {
                            flag=1;
                            index=i;
                        }
                    }
                }
                ps.setInt(record.length+1, Integer.parseInt(record[index]));
                ps.addBatch();
                counter++;
                if(counter==6000)
                {
                    counter=0;
                    ps.executeBatch();
                }
                
            }
            ps.executeBatch();
        }catch(NumberFormatException ex){
            
        }
        catch (IOException ex) {
        } catch (SQLException ex) {
        }
    }
 
String generateQuery(String[] columns)
{
    if(!query.equals(""))
        return query;
    int length=columns.length;
    String query="insert into library(";
    for(int i=0;i<length;i++)
    {
        if(i!=0)
        {
            query=query+",";
        }
        query=query+columns[i];
    }
    query=query+")values(";
    for(int i=0;i<length;i++){
        if(i!=0)
        {
            query=query+",";
        }
        query=query+"?";
    }
    query=query+")on conflict on constraint library_bookname_author_key do update set count=library.count+?;";
    return query;
}
}
