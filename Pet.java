// Pacote opcional, ajuste conforme a estrutura do seu projeto.
// Exemplo: package com.petstop.model;

/**
 * Representa um animal (Pet) no sistema PetStop.
 * Esta classe é um modelo de dados que corresponde à tabela 'animais' no banco de dados.
 * 🐶🐱
 */
public class Pet {

    private Long id; // Corresponde à coluna 'id' (BIGINT AUTO_INCREMENT PRIMARY KEY)
    private String nome; // Corresponde à coluna 'nome' (VARCHAR(100) NOT NULL)
    private String especie; // Corresponde à coluna 'especie' (VARCHAR(50) NOT NULL)
    private int idade; // Corresponde à coluna 'idade' (BIGINT NOT NULL) - usando int em Java para idades comuns
    private boolean vacinado; // Corresponde à coluna 'vacinado' (BOOLEAN NOT NULL)

    /**
     * Construtor padrão.
     * Necessário para algumas bibliotecas de persistência ou frameworks.
     */
    public Pet() {
    }

    /**
     * Construtor completo para criar um objeto Pet com todos os atributos.
     *
     * @param id        O identificador único do animal (geralmente definido pelo banco de dados).
     * @param nome      O nome do animal.
     * @param especie   A espécie do animal (ex: "Cachorro", "Gato").
     * @param idade     A idade do animal em anos.
     * @param vacinado  True se o animal foi vacinado, false caso contrário.
     */
    public Pet(Long id, String nome, String especie, int idade, boolean vacinado) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.idade = idade;
        this.vacinado = vacinado;
    }

    /**
     * Construtor para criar um novo Pet sem ID (útil antes de salvar no banco,
     * onde o ID é auto-incrementado).
     *
     * @param nome      O nome do animal.
     * @param especie   A espécie do animal.
     * @param idade     A idade do animal.
     * @param vacinado  Se o animal está vacinado.
     */
    public Pet(String nome, String especie, int idade, boolean vacinado) {
        this.nome = nome;
        this.especie = especie;
        this.idade = idade;
        this.vacinado = vacinado;
    }

    // Getters e Setters para todos os atributos

    /**
     * Obtém o ID do animal.
     * @return O ID do animal.
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o ID do animal.
     * Geralmente, este método não é usado publicamente se o ID é gerenciado pelo banco de dados.
     * @param id O novo ID do animal.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtém o nome do animal.
     * @return O nome do animal.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do animal.
     * @param nome O novo nome do animal.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém a espécie do animal.
     * @return A espécie do animal.
     */
    public String getEspecie() {
        return especie;
    }

    /**
     * Define a espécie do animal.
     * @param especie A nova espécie do animal.
     */
    public void setEspecie(String especie) {
        this.especie = especie;
    }

    /**
     * Obtém a idade do animal.
     * @return A idade do animal.
     */
    public int getIdade() {
        return idade;
    }

    /**
     * Define a idade do animal.
     * @param idade A nova idade do animal.
     */
    public void setIdade(int idade) {
        this.idade = idade;
    }

    /**
     * Verifica se o animal está vacinado.
     * @return true se o animal está vacinado, false caso contrário.
     */
    public boolean isVacinado() {
        return vacinado;
    }

    /**
     * Define o estado de vacinação do animal.
     * @param vacinado true se o animal está vacinado, false caso contrário.
     */
    public void setVacinado(boolean vacinado) {
        this.vacinado = vacinado;
    }

    /**
     * Retorna uma representação em String do objeto Pet.
     * Útil para debugging e logging.
     * @return Uma string representando o objeto Pet.
     */
    @Override
    public String toString() {
        return "Pet{" +
               "id=" + id +
               ", nome='" + nome + '\'' +
               ", especie='" + especie + '\'' +
               ", idade=" + idade +
               ", vacinado=" + vacinado +
               '}';
    }

    // Você pode adicionar métodos equals() e hashCode() se precisar comparar objetos Pet
    // ou usá-los em coleções como HashSets ou HashMaps.
    // Exemplo (gerado por IDE):
    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return idade == pet.idade &&
               vacinado == pet.vacinado &&
               java.util.Objects.equals(id, pet.id) &&
               java.util.Objects.equals(nome, pet.nome) &&
               java.util.Objects.equals(especie, pet.especie);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, nome, especie, idade, vacinado);
    }
    */
}
