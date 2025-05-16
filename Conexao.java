import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Classe que contém os métodos para abrir e fechar a conexão com o banco de dados.
public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/PetStop";
    private static final String USUARIO = "root"; //usuário padrão do banco
    private static final String SENHA = "";

    //Método no qual vai conectar com o banco PetStop
    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }

    //Método no qual vai encerrar a conexão com o banco PetStop
    public static void fechar(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
