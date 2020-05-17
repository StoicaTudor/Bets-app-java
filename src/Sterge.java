
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

//import java.io.IOException;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
public class Sterge {

    public Connection con;
    public Statement st;
    public ResultSet rs;

    public Sterge() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/test", user = "root", password = "";
            Connection conn = DriverManager.getConnection(url, user, password);
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Sterge.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ins();
    }
    
    private void ins(){
        String q="INSERT into `test`.`aaa` (`id`,`t1`,`t2`,`t3`) VALUES (NULL,'1','2','3'),(NULL,'1','2','3'),(NULL,'1','2','3')";
        try {
            st.executeUpdate(q, Statement.RETURN_GENERATED_KEYS);
            rs = st.getGeneratedKeys();
            
            while(rs.next()){
                System.out.println(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Sterge.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        new Sterge();
    }

}
