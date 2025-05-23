import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//Importar as classes Conexao e Pet caso necessário.
//import com.petstop.db.Conexao;
//import com.petstop.model.Pet;

/**
 * DAO (Data Access Object) para a entidade Pet.
 * Contém métodos para realizar operações CRUD (Create, Read, Update, Delete)
 * na tabela 'animais' do banco de dados.
 */
public class PetDAO {

    /**
     * Adiciona um novo pet ao banco de dados (O ID do pet é gerado automaticamente pelo banco).
     * @ return true se o pet foi adicionado com sucesso, false caso contrário.
     */
    public boolean adicionarPet(Pet pet) {
        String sql = "INSERT INTO animais (nome, especie, idade, vacinado) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = Conexao.conectar();
            if (conn == null) {
                System.err.println("Falha ao conectar ao banco de dados para adicionar pet.");
                return false;
            }
            // O Statement.RETURN_GENERATED_KEYS permite obter o ID gerado.
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, pet.getNome());
            pstmt.setString(2, pet.getEspecie());
            pstmt.setInt(3, pet.getIdade());
            pstmt.setBoolean(4, pet.isVacinado());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                //Recupera o ID gerado pelo banco
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    pet.setId(generatedKeys.getLong(1)); //Define o ID no objeto pet
                }
                System.out.println("Pet adicionado com sucesso! ID: " + pet.getId());
                return true;
            } else {
                System.err.println("Nenhuma linha afetada ao tentar adicionar o pet.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao adicionar pet: " + e.getMessage());
            //Adicionar log mais detalhado da exceção se necessário
            //e.printStackTrace();
            return false;
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar PreparedStatement ou ResultSet: " + e.getMessage());
            }
            Conexao.fechar(conn); //Fecha a conexão principal
        }
    }

    /**
     * Busca um pet pelo seu ID.
     * @ param id O ID do pet a ser buscado.
     * @ return Um objeto Pet se encontrado, ou null caso contrário.
     */
    public Pet buscarPetPorId(long id) {
        String sql = "SELECT * FROM animais WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Pet pet = null;

        try {
            conn = Conexao.conectar();
            if (conn == null) {
                System.err.println("Falha ao conectar ao banco de dados para buscar pet por ID.");
                return null;
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pet = new Pet();
                pet.setId(rs.getLong("id"));
                pet.setNome(rs.getString("nome"));
                pet.setEspecie(rs.getString("especie"));
                pet.setIdade(rs.getInt("idade"));
                pet.setVacinado(rs.getBoolean("vacinado"));
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao buscar pet por ID: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar ResultSet ou PreparedStatement: " + e.getMessage());
            }
            Conexao.fechar(conn);
        }
        return pet;
    }

    /**
     * Lista de todos os pets cadastrados no banco de dados.
     * @ return Uma lista de objetos Pet. A lista pode estar vazia se não houver pets.
     */
    public List<Pet> listarTodosPets() {
        String sql = "SELECT * FROM animais ORDER BY nome ASC"; //Ordena por nome para melhor visualização
        List<Pet> pets = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null; //Usando Statement simples pois não há parâmetros de entrada
        ResultSet rs = null;

        try {
            conn = Conexao.conectar();
            if (conn == null) {
                System.err.println("Falha ao conectar ao banco de dados para listar pets.");
                return pets; //Retorna lista vazia
            }
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Pet pet = new Pet();
                pet.setId(rs.getLong("id"));
                pet.setNome(rs.getString("nome"));
                pet.setEspecie(rs.getString("especie"));
                pet.setIdade(rs.getInt("idade"));
                pet.setVacinado(rs.getBoolean("vacinado"));
                pets.add(pet);
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao listar todos os pets: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar ResultSet ou Statement: " + e.getMessage());
            }
            Conexao.fechar(conn);
        }
        return pets;
    }

    /**
     * Atualiza os dados de um pet existente no banco de dados.
     * @ param pet O objeto Pet com os dados atualizados. O ID do pet deve estar preenchido.
     * @ return true se o pet foi atualizado com sucesso, false caso contrário.
     */
    public boolean atualizarPet(Pet pet) {
        String sql = "UPDATE animais SET nome = ?, especie = ?, idade = ?, vacinado = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        if (pet.getId() == null || pet.getId() <= 0) {
            System.err.println("ID do pet inválido para atualização.");
            return false;
        }

        try {
            conn = Conexao.conectar();
            if (conn == null) {
                System.err.println("Falha ao conectar ao banco de dados para atualizar pet.");
                return false;
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pet.getNome());
            pstmt.setString(2, pet.getEspecie());
            pstmt.setInt(3, pet.getIdade());
            pstmt.setBoolean(4, pet.isVacinado());
            pstmt.setLong(5, pet.getId());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Pet atualizado com sucesso! ID: " + pet.getId());
                return true;
            } else {
                System.err.println("Nenhuma linha afetada. Pet com ID " + pet.getId() + " não encontrado para atualização.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao atualizar pet: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
            }
            Conexao.fechar(conn);
        }
    }

    /**
     * Remove um pet do banco de dados pelo seu ID.
     * @ param id O ID do pet a ser removido.
     * @ return true se o pet foi removido com sucesso, false caso contrário.
     */
    public boolean removerPet(long id) {
        String sql = "DELETE FROM animais WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        if (id <= 0) {
            System.err.println("ID do pet inválido para remoção.");
            return false;
        }

        try {
            conn = Conexao.conectar();
            if (conn == null) {
                System.err.println("Falha ao conectar ao banco de dados para remover pet.");
                return false;
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Pet com ID " + id + " removido com sucesso!");
                return true;
            } else {
                System.err.println("Nenhuma linha afetada. Pet com ID " + id + " não encontrado para remoção.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao remover pet: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
            }
            Conexao.fechar(conn);
        }
    }
}
