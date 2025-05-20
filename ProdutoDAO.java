import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//Importando as classes Conexao e Produto.
import com.petstop.db.Conexao;
import com.petstop.model.Produto;

/**
 * DAO (Data Access Object) para a entidade Produto.
 * Contém métodos para realizar operações CRUD (Create, Read, Update, Delete)
 * na tabela 'produtos' do banco de dados.
 */
public class ProdutoDAO {

    /**
     * Adiciona um novo produto ao banco de dados.
     * O ID do produto é gerado automaticamente pelo banco.
     * @return true se o produto foi adicionado com sucesso, false caso contrário.
     */
    public boolean adicionarProduto(Produto produto) {
        String sql = "INSERT INTO produtos (nome, quantidade, preco, ativo) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = Conexao.conectar();
            if (conn == null) {
                System.err.println("Falha ao conectar ao banco de dados para adicionar produto.");
                return false;
            }
            //O Statement.RETURN_GENERATED_KEYS permite obter o ID gerado.
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, produto.getNome());
            pstmt.setLong(2, produto.getQuantidade());
            pstmt.setBigDecimal(3, produto.getPreco());
            pstmt.setBoolean(4, produto.isAtivo());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                //Recupera o ID gerado pelo banco
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    produto.setId(generatedKeys.getLong(1)); //Define o ID no objeto produto
                }
                System.out.println("Produto adicionado com sucesso! ID: " + produto.getId());
                return true;
            } else {
                System.err.println("Nenhuma linha afetada ao tentar adicionar o produto.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao adicionar produto: " + e.getMessage());
            //e.printStackTrace(); //Para depuração mais detalhada
            return false;
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar PreparedStatement ou ResultSet (adicionarProduto): " + e.getMessage());
            }
            Conexao.fechar(conn); //Fecha a conexão principal
        }
    }

    /**
     * Busca um produto pelo seu ID.
     * @return Um objeto Produto se encontrado, ou null caso contrário.
     */
    public Produto buscarProdutoPorId(long id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Produto produto = null;

        try {
            conn = Conexao.conectar();
            if (conn == null) {
                System.err.println("Falha ao conectar ao banco de dados para buscar produto por ID.");
                return null;
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                produto = new Produto();
                produto.setId(rs.getLong("id"));
                produto.setNome(rs.getString("nome"));
                produto.setQuantidade(rs.getLong("quantidade"));
                produto.setPreco(rs.getBigDecimal("preco"));
                produto.setAtivo(rs.getBoolean("ativo"));
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao buscar produto por ID: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar ResultSet ou PreparedStatement (buscarProdutoPorId): " + e.getMessage());
            }
            Conexao.fechar(conn);
        }
        return produto;
    }

    /**
     * Lista todos os produtos cadastrados no banco de dados.
     * @return Uma lista de objetos Produto. A lista pode estar vazia se não houver produtos.
     */
    public List<Produto> listarTodosProdutos() {
        String sql = "SELECT * FROM produtos ORDER BY nome ASC"; //Ordena por nome
        List<Produto> produtos = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.conectar();
            if (conn == null) {
                System.err.println("Falha ao conectar ao banco de dados para listar produtos.");
                return produtos; //Retorna lista vazia
            }
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getLong("id"));
                produto.setNome(rs.getString("nome"));
                produto.setQuantidade(rs.getLong("quantidade"));
                produto.setPreco(rs.getBigDecimal("preco"));
                produto.setAtivo(rs.getBoolean("ativo"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao listar todos os produtos: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar ResultSet ou Statement (listarTodosProdutos): " + e.getMessage());
            }
            Conexao.fechar(conn);
        }
        return produtos;
    }

    /**
     * Atualiza os dados de um produto existente no banco de dados.
     * @param produto O objeto Produto com os dados atualizados. O ID do produto deve estar preenchido.
     * @return true se o produto foi atualizado com sucesso, false caso contrário.
     */
    public boolean atualizarProduto(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, quantidade = ?, preco = ?, ativo = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        if (produto.getId() == null || produto.getId() <= 0) {
            System.err.println("ID do produto inválido para atualização.");
            return false;
        }

        try {
            conn = Conexao.conectar();
            if (conn == null) {
                System.err.println("Falha ao conectar ao banco de dados para atualizar produto.");
                return false;
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, produto.getNome());
            pstmt.setLong(2, produto.getQuantidade());
            pstmt.setBigDecimal(3, produto.getPreco());
            pstmt.setBoolean(4, produto.isAtivo());
            pstmt.setLong(5, produto.getId());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Produto atualizado com sucesso! ID: " + produto.getId());
                return true;
            } else {
                System.err.println("Nenhuma linha afetada. Produto com ID " + produto.getId() + " não encontrado para atualização.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao atualizar produto: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar PreparedStatement (atualizarProduto): " + e.getMessage());
            }
            Conexao.fechar(conn);
        }
    }

    /**
     * Remove um produto do banco de dados pelo seu ID.
     * @return true se o produto foi removido com sucesso, false caso contrário.
     */
    public boolean removerProduto(long id) {
        String sql = "DELETE FROM produtos WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        if (id <= 0) {
            System.err.println("ID do produto inválido para remoção.");
            return false;
        }

        try {
            conn = Conexao.conectar();
            if (conn == null) {
                System.err.println("Falha ao conectar ao banco de dados para remover produto.");
                return false;
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Produto com ID " + id + " removido com sucesso!");
                return true;
            } else {
                System.err.println("Nenhuma linha afetada. Produto com ID " + id + " não encontrado para remoção.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL ao remover produto: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar PreparedStatement (removerProduto): " + e.getMessage());
            }
            Conexao.fechar(conn);
        }
    }

    // --- Método Principal para Teste (Opcional) ---
    // Remova ou comente este método em produção.
    public static void main(String[] args) {
        ProdutoDAO produtoDAO = new ProdutoDAO();

        System.out.println("--- Testando ProdutoDAO ---");

        //Teste de Adicionar um produto
        System.out.println("\n1. Adicionando Produtos...");
        Produto prod1 = new Produto("Ração Premium Cães Adultos", 50L, new BigDecimal("150.99"), true);
        Produto prod2 = new Produto("Arranhador para Gatos Simples", 100L, new BigDecimal("45.50"), true);
        Produto prod3 = new Produto("Coleira Antipulgas (descontinuada)", 0L, new BigDecimal("75.00"), false);

        //ID definido nesse trecho
        produtoDAO.adicionarProduto(prod1);
        produtoDAO.adicionarProduto(prod2);
        produtoDAO.adicionarProduto(prod3);

        //Teste de Listar todos os produtos
        System.out.println("\n2. Listando todos os Produtos:");
        List<Produto> todosProdutos = produtoDAO.listarTodosProdutos();
        if (todosProdutos.isEmpty()) {
            System.out.println("   Nenhum produto encontrado.");
        } else {
            for (Produto p : todosProdutos) {
                System.out.println("   " + p);
            }
        }

        //Teste de Buscar por ID (usando o ID do prod1, se ele foi inserido)
        if (prod1.getId() != null && prod1.getId() > 0) {
            System.out.println("\n3. Buscando Produto com ID " + prod1.getId() + ":");
            Produto produtoEncontrado = produtoDAO.buscarProdutoPorId(prod1.getId());
            if (produtoEncontrado != null) {
                System.out.println("   Encontrado: " + produtoEncontrado);

                //Teste de Atualizar um produto
                System.out.println("\n4. Atualizando Produto com ID " + produtoEncontrado.getId() + " (preço para 155.00, quantidade para 45):");
                produtoEncontrado.setPreco(new BigDecimal("155.00"));
                produtoEncontrado.setQuantidade(45L);
                if (produtoDAO.atualizarProduto(produtoEncontrado)) {
                    Produto produtoAtualizado = produtoDAO.buscarProdutoPorId(produtoEncontrado.getId());
                    System.out.println("   Dados após atualização: " + produtoAtualizado);
                } else {
                    System.out.println("   Falha ao atualizar o produto.");
                }
            } else {
                System.out.println("   Produto com ID " + prod1.getId() + " não encontrado para teste de busca/atualização.");
            }
        } else {
             System.out.println("\nAVISO: Não foi possível testar busca por ID e atualização pois o primeiro produto não obteve um ID válido.");
        }


        //Teste de Remover um produto (usando o ID do prod2, se ele foi inserido)
        if (prod2.getId() != null && prod2.getId() > 0) {
            System.out.println("\n5. Removendo Produto com ID " + prod2.getId() + ":");
            if (produtoDAO.removerProduto(prod2.getId())) {
                System.out.println("   Produto removido. Verificando lista novamente:");
                todosProdutos = produtoDAO.listarTodosProdutos();
                 for (Produto p : todosProdutos) {
                    System.out.println("   " + p);
                }
            } else {
                System.out.println("   Falha ao remover o produto com ID " + prod2.getId());
            }
        } else {
            System.out.println("\nAVISO: Não foi possível testar remoção pois o segundo produto não obteve um ID válido.");
        }
        
        System.out.println("\n--- Fim dos Testes ProdutoDAO ---");
    }
}
