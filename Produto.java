// Pacote opcional, ajuste conforme a estrutura do seu projeto.
// Exemplo: package com.petstop.model;

import java.math.BigDecimal; // Importar BigDecimal para o preço

/**
 * Representa um Produto no sistema PetStop.
 * Esta classe é um modelo de dados que corresponde à tabela 'produtos' no banco de dados.
 * 🛒
 */
public class Produto {

    private Long id; // Corresponde à coluna 'id' (BIGINT AUTO_INCREMENT PRIMARY KEY)
    private String nome; // Corresponde à coluna 'nome' (VARCHAR(100) NOT NULL)
    private long quantidade; // Corresponde à coluna 'quantidade' (BIGINT NOT NULL)
    private BigDecimal preco; // Corresponde à coluna 'preco' (DECIMAL(10,2) NOT NULL)
    private boolean ativo; // Corresponde à coluna 'ativo' (BOOLEAN NOT NULL)

    /**
     * Construtor padrão.
     * Necessário para algumas bibliotecas de persistência ou frameworks.
     */
    public Produto() {
    }

    /**
     * Construtor completo para criar um objeto Produto com todos os atributos.
     *
     * @param id         O identificador único do produto (geralmente definido pelo banco de dados).
     * @param nome       O nome do produto.
     * @param quantidade A quantidade em estoque do produto.
     * @param preco      O preço unitário do produto.
     * @param ativo      True se o produto está ativo para venda, false caso contrário.
     */
    public Produto(Long id, String nome, long quantidade, BigDecimal preco, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.ativo = ativo;
    }

    /**
     * Construtor para criar um novo Produto sem ID (útil antes de salvar no banco,
     * onde o ID é auto-incrementado).
     *
     * @param nome       O nome do produto.
     * @param quantidade A quantidade em estoque do produto.
     * @param preco      O preço unitário do produto.
     * @param ativo      Se o produto está ativo para venda.
     */
    public Produto(String nome, long quantidade, BigDecimal preco, boolean ativo) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.ativo = ativo;
    }

    // Getters e Setters para todos os atributos

    /**
     * Obtém o ID do produto.
     * @return O ID do produto.
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o ID do produto.
     * Geralmente, este método não é usado publicamente se o ID é gerenciado pelo banco de dados.
     * @param id O novo ID do produto.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtém o nome do produto.
     * @return O nome do produto.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do produto.
     * @param nome O novo nome do produto.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém a quantidade em estoque do produto.
     * @return A quantidade do produto.
     */
    public long getQuantidade() {
        return quantidade;
    }

    /**
     * Define a quantidade em estoque do produto.
     * @param quantidade A nova quantidade do produto.
     */
    public void setQuantidade(long quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Obtém o preço do produto.
     * @return O preço do produto.
     */
    public BigDecimal getPreco() {
        return preco;
    }

    /**
     * Define o preço do produto.
     * @param preco O novo preço do produto.
     */
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    /**
     * Verifica se o produto está ativo.
     * @return true se o produto está ativo, false caso contrário.
     */
    public boolean isAtivo() {
        return ativo;
    }

    /**
     * Define o estado de ativação do produto.
     * @param ativo true se o produto está ativo, false caso contrário.
     */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    /**
     * Retorna uma representação em String do objeto Produto.
     * Útil para debugging e logging.
     * @return Uma string representando o objeto Produto.
     */
    @Override
    public String toString() {
        return "Produto{" +
               "id=" + id +
               ", nome='" + nome + '\'' +
               ", quantidade=" + quantidade +
               ", preco=" + (preco != null ? preco.toPlainString() : "null") + // Formata o BigDecimal para string
               ", ativo=" + ativo +
               '}';
    }

    // Você pode adicionar métodos equals() e hashCode() se precisar comparar objetos Produto
    // ou usá-los em coleções como HashSets ou HashMaps.
    // A implementação de equals e hashCode geralmente considera o 'id' se não for nulo,
    // ou uma combinação dos campos que definem a unicidade de um produto.
    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        if (id != null ? !id.equals(produto.id) : produto.id != null) return false;
        // Se o ID for nulo, pode comparar outros campos, como o nome.
        // Esta é uma implementação simples baseada no ID.
        return nome != null ? nome.equals(produto.nome) : produto.nome == null;
    }

    @Override
    public int hashCode() {
        // Se basear no ID para o hashCode se ele não for nulo.
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        return result;
    }
    */
}
