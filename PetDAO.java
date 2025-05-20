// Pacote opcional, ajuste conforme a estrutura do seu projeto.
// Exemplo: package com.petstop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Importe suas classes Conexao e Pet.
// Exemplo: import com.petstop.db.Conexao;
// Exemplo: import com.petstop.model.Pet;

/**
 * DAO (Data Access Object) para a entidade Pet.
 * Contém métodos para realizar operações CRUD (Create, Read, Update, Delete)
 * na tabela 'animais' do banco de dados.
 */
public class PetDAO {

    /**
     * Adiciona um novo pet ao banco de dados.
     * O ID do pet é gerado automaticamente pelo banco.
     *
     * @param pet O objeto Pet a ser adicionado.
     * @return true se o pet foi adicionado com sucesso, false caso contrário.
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
                // Recupera o ID gerado pelo banco
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    pet.setId(generatedKeys.getLong(1)); // Define o ID no objeto pet
                }
                System.out.println("Pet adicionado com sucesso! ID: " + pet.getId());
                return true;
            } else {
                System.err.println("Nenhuma linha afetada ao tentar adicionar o pet.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao adicionar pet: " + e.getMessage());
            // Adicionar log mais detalhado da exceção se necessário
            // e.printStackTrace();
            return false;
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar PreparedStatement ou ResultSet: " + e.getMessage());
            }
            Conexao.fechar(conn); // Fecha a conexão principal
        }
    }

    /**
     * Busca um pet pelo seu ID.
     *
     * @param id O ID do pet a ser buscado.
     * @return Um objeto Pet se encontrado, ou null caso contrário.
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
     * Lista todos os pets cadastrados no banco de dados.
     *
     * @return Uma lista de objetos Pet. A lista pode estar vazia se não houver pets.
     */
    public List<Pet> listarTodosPets() {
        String sql = "SELECT * FROM animais ORDER BY nome ASC"; // Ordena por nome para melhor visualização
        List<Pet> pets = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null; // Usando Statement simples pois não há parâmetros de entrada
        ResultSet rs = null;

        try {
            conn = Conexao.conectar();
            if (conn == null) {
                System.err.println("Falha ao conectar ao banco de dados para listar pets.");
                return pets; // Retorna lista vazia
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
     *
     * @param pet O objeto Pet com os dados atualizados. O ID do pet deve estar preenchido.
     * @return true se o pet foi atualizado com sucesso, false caso contrário.
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
     *
     * @param id O ID do pet a ser removido.
     * @return true se o pet foi removido com sucesso, false caso contrário.
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

    // --- Método Principal para Teste (Opcional) ---
    // Remova ou comente este método em produção.
    public static void main(String[] args) {
        PetDAO petDAO = new PetDAO();

        System.out.println("--- Testando PetDAO ---");

        // Teste de Adicionar
        System.out.println("\n1. Adicionando Pets...");
        Pet pet1 = new Pet("Rex", "Cachorro", 5, true);
        Pet pet2 = new Pet("Mimi", "Gato", 3, false);
        Pet pet3 = new Pet("Loro", "Papagaio", 10, true);

        petDAO.adicionarPet(pet1); // ID será definido aqui dentro
        petDAO.adicionarPet(pet2);
        petDAO.adicionarPet(pet3);

        // Teste de Listar Todos
        System.out.println("\n2. Listando todos os Pets:");
        List<Pet> todosPets = petDAO.listarTodosPets();
        if (todosPets.isEmpty()) {
            System.out.println("   Nenhum pet encontrado.");
        } else {
            for (Pet p : todosPets) {
                System.out.println("   " + p);
            }
        }

        // Teste de Buscar por ID (usando o ID do pet1, se ele foi inserido)
        if (pet1.getId() != null && pet1.getId() > 0) {
            System.out.println("\n3. Buscando Pet com ID " + pet1.getId() + ":");
            Pet petEncontrado = petDAO.buscarPetPorId(pet1.getId());
            if (petEncontrado != null) {
                System.out.println("   Encontrado: " + petEncontrado);

                // Teste de Atualizar
                System.out.println("\n4. Atualizando Pet com ID " + petEncontrado.getId() + " (idade para 6, vacinado para false):");
                petEncontrado.setIdade(6);
                petEncontrado.setVacinado(false);
                if (petDAO.atualizarPet(petEncontrado)) {
                    Pet petAtualizado = petDAO.buscarPetPorId(petEncontrado.getId());
                    System.out.println("   Dados após atualização: " + petAtualizado);
                } else {
                    System.out.println("   Falha ao atualizar o pet.");
                }
            } else {
                System.out.println("   Pet com ID " + pet1.getId() + " não encontrado para teste de busca/atualização.");
            }
        } else {
             System.out.println("\nAVISO: Não foi possível testar busca por ID e atualização pois o primeiro pet não obteve um ID válido.");
        }


        // Teste de Remover (usando o ID do pet2, se ele foi inserido)
        if (pet2.getId() != null && pet2.getId() > 0) {
            System.out.println("\n5. Removendo Pet com ID " + pet2.getId() + ":");
            if (petDAO.removerPet(pet2.getId())) {
                System.out.println("   Pet removido. Verificando lista novamente:");
                todosPets = petDAO.listarTodosPets();
                 for (Pet p : todosPets) {
                    System.out.println("   " + p);
                }
            } else {
                System.out.println("   Falha ao remover o pet com ID " + pet2.getId());
            }
        } else {
            System.out.println("\nAVISO: Não foi possível testar remoção pois o segundo pet não obteve um ID válido.");
        }
        
        System.out.println("\n--- Fim dos Testes PetDAO ---");
    }
}
