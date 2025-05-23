import java.math.BigDecimal; //Pacote BigDecimal para o preço

/**
 * Representa um Produto no sistema PetStop.
 * Esta classe é um modelo de dados que corresponde à tabela 'produtos' no banco de dados.
 */
public class Produto {

    private Long id; // Corresponde à coluna 'id' (BIGINT AUTO_INCREMENT PRIMARY KEY)
    private String nome; // Corresponde à coluna 'nome' (VARCHAR(100) NOT NULL)
    private long quantidade; // Corresponde à coluna 'quantidade' (BIGINT NOT NULL)
    private BigDecimal preco; // Corresponde à coluna 'preco' (DECIMAL(10,2) NOT NULL)
    private boolean disponivel; // Corresponde à coluna 'disponivel' (BOOLEAN NOT NULL)

    //Construtor padrão.
    public Produto() {
    }

    public Produto(Long id, String nome, long quantidade, BigDecimal preco, boolean disponivel) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.disponivel = disponivel;
    }
    
    public Produto(String nome, long quantidade, BigDecimal preco, boolean disponivel) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.disponivel = disponivel;
    }

    //Retorna o ID do produto.
    public Long getId() {
        return id;
    }
    //Define o ID do produto
    public void setId(Long id) {
        this.id = id;
    }

    //Retorna o Nome do produto
    public String getNome() {
        return nome;
    }
    //Define o nome do produto
    public void setNome(String nome) {
        this.nome = nome;
    }

    //Retorna a Quantidade de um produto
    public long getQuantidade() {
        return quantidade;
    }
    //Define a Quantidade de um produto
    public void setQuantidade(long quantidade) {
        this.quantidade = quantidade;
    }

    //Retorna o Preço do Produto
    public BigDecimal getPreco() {
        return preco;
    }

    //Define o Preço do Produto
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    //Retorna se o produto está disponivel
    public boolean isDisponivel() {
        return disponivel;
    }
    //Define se o produto está disponivel
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    //Retorna uma representação em String do objeto Produto.
    @Override
    public String toString() {
        return "Produto{" +
               "id=" + id +
               ", nome='" + nome + '\'' +
               ", quantidade=" + quantidade +
               ", preco=" + (preco != null ? preco.toPlainString() : "null") + // Formata o BigDecimal para string
               ", disponivel=" + disponivel +
               '}';
    }
}
