package entity;

import entity.enums.StatusMatricula;
import java.time.LocalDateTime;

public class MatriculaTurma {

    private Long id;
    private Long turmaId;
    private Long alunoId;
    private Integer prioridade;
    private LocalDateTime dataMatricula;
    private StatusMatricula status;

    public MatriculaTurma() {}

    public MatriculaTurma(Long id, Long turmaId, Long alunoId, Integer prioridade, LocalDateTime dataMatricula, StatusMatricula status) {
        this.id = id;
        this.turmaId = turmaId;
        this.alunoId = alunoId;
        this.prioridade = prioridade;
        this.dataMatricula = dataMatricula;
        this.status = status;
    }

    public Long getId() { return id; }
    public Long id() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTurmaId() { return turmaId; }
    public Long turmaId() { return turmaId; }
    public void setTurmaId(Long turmaId) { this.turmaId = turmaId; }

    public Long getAlunoId() { return alunoId; }
    public Long alunoId() { return alunoId; }
    public void setAlunoId(Long alunoId) { this.alunoId = alunoId; }

    public Integer getPrioridade() { return prioridade; }
    public Integer prioridade() { return prioridade; }
    public void setPrioridade(Integer prioridade) { this.prioridade = prioridade; }

    public LocalDateTime getDataMatricula() { return dataMatricula; }
    public LocalDateTime dataMatricula() { return dataMatricula; }
    public void setDataMatricula(LocalDateTime dataMatricula) { this.dataMatricula = dataMatricula; }

    public StatusMatricula getStatus() { return status; }
    public StatusMatricula status() { return status; }
    public void setStatus(StatusMatricula status) { this.status = status; }
}