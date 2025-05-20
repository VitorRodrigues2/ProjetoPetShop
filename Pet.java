/**
 * Representa um animal (Pet) no sistema PetStop.
 * Esta classe é um modelo de dados que corresponde à tabela 'animais' no banco de dados.
 */

public class Pet {
    
    private Long id; // Corresponde à coluna 'id' (BIGINT AUTO_INCREMENT PRIMARY KEY)
    private String nome; // Corresponde à coluna 'nome' (VARCHAR(100) NOT NULL)
    private String especie; // Corresponde à coluna 'especie' (VARCHAR(50) NOT NULL)
    private int idade; // Corresponde à coluna 'idade' (BIGINT NOT NULL) - usando int em Java para idades comuns
    private boolean vacinado; // Corresponde à coluna 'vacinado' (BOOLEAN NOT NULL)

    public Pet() {
    }

    public Pet(Long id, String nome, String especie, int idade, boolean vacinado) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.idade = idade;
        this.vacinado = vacinado;
    }

    public Pet(String nome, String especie, int idade, boolean vacinado) {
        this.nome = nome;
        this.especie = especie;
        this.idade = idade;
        this.vacinado = vacinado;
    }

    //Retorna o ID do animal
    public Long getId() {
        return id;
    }
    //Define o ID do animal.
    public void setId(Long id) {
        this.id = id;
    }

    //Retorna o Nome do animal
    public String getNome() {
        return nome;
    }
    //Define o Nome do animal
    public void setNome(String nome) {
        this.nome = nome;
    }

    //Retorna a Espécie do animal
    public String getEspecie() {
        return especie;
    }
    //Define a Espécie do animal
    public void setEspecie(String especie) {
        this.especie = especie;
    }

    //Retorna a Idade do animal
    public int getIdade() {
        return idade;
    }
    //Define a Idade do animal
    public void setIdade(int idade) {
        this.idade = idade;
    }

    //Retorna se o animal está vacinado
    public boolean isVacinado() {
        return vacinado;
    }
    //Define se o animal está vacinado
    public void setVacinado(boolean vacinado) {
        this.vacinado = vacinado;
    }

    //Retorna uma representação em String do objeto Pet.
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
}
