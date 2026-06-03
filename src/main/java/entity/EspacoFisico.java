package entity;

public class EspacoFisico {

    private Long id;
    private String nome;
    private Integer capacidade;
    private Boolean disponivel;

    public EspacoFisico() {
    }

    public EspacoFisico(
            Long id,
            String nome,
            Integer capacidade,
            Boolean disponivel
    ) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.disponivel = disponivel;
    }

    public Long getId() {
        return id;
    }

    public Long id() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String nome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public Integer capacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public Boolean disponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
}