package entity;

import java.time.LocalDateTime;

public class ListaEspera {

    private Long id;
    private Long disciplinaId;
    private Long alunoId;
    private Integer prioridade;
    private Integer posicaoFila;
    private LocalDateTime dataEntrada;

    public ListaEspera() {}

    public ListaEspera(Long id, Long disciplinaId, Long alunoId, Integer prioridade, Integer posicaoFila, LocalDateTime dataEntrada) {
        this.id = id;
        this.disciplinaId = disciplinaId;
        this.alunoId = alunoId;
        this.prioridade = prioridade;
        this.posicaoFila = posicaoFila;
        this.dataEntrada = dataEntrada;
    }

    public Long getId() { return id; }
    public Long id() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDisciplinaId() { return disciplinaId; }
    public Long disciplinaId() { return disciplinaId; }
    public void setDisciplinaId(Long disciplinaId) { this.disciplinaId = disciplinaId; }

    public Long getAlunoId() { return alunoId; }
    public Long alunoId() { return alunoId; }
    public void setAlunoId(Long alunoId) { this.alunoId = alunoId; }

    public Integer getPrioridade() { return prioridade; }
    public Integer prioridade() { return prioridade; }
    public void setPrioridade(Integer prioridade) { this.prioridade = prioridade; }

    public Integer getPosicaoFila() { return posicaoFila; }
    public Integer posicaoFila() { return posicaoFila; }
    public void setPosicaoFila(Integer posicaoFila) { this.posicaoFila = posicaoFila; }

    public LocalDateTime getDataEntrada() { return dataEntrada; }
    public LocalDateTime dataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDateTime dataEntrada) { this.dataEntrada = dataEntrada; }
}