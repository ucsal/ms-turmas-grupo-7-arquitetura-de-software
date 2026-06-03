package entity;

import entity.enums.StatusTurma;
import entity.enums.TurnoEnum;

public class Turma {

    private Long id;
    private Long disciplinaId;
    private Long professorId;
    private Long espacoId; // FALTAVA ISSO
    private String semestre;
    private TurnoEnum turno;
    private Integer capacidadeMaxima;
    private Integer quantidadeAlunos;
    private StatusTurma status;

    public Turma() {
    }

    public Turma(
            Long id,
            Long disciplinaId,
            Long professorId,
            Long espacoId,
            String semestre,
            TurnoEnum turno,
            Integer capacidadeMaxima,
            Integer quantidadeAlunos,
            StatusTurma status
    ) {
        this.id = id;
        this.disciplinaId = disciplinaId;
        this.professorId = professorId;
        this.espacoId = espacoId; // FALTAVA ISSO
        this.semestre = semestre;
        this.turno = turno;
        this.capacidadeMaxima = capacidadeMaxima;
        this.quantidadeAlunos = quantidadeAlunos;
        this.status = status;
    }

    public Long getId() { return id; }
    public Long id() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDisciplinaId() { return disciplinaId; }
    public Long disciplinaId() { return disciplinaId; }
    public void setDisciplinaId(Long disciplinaId) { this.disciplinaId = disciplinaId; }

    public Long getProfessorId() { return professorId; }
    public Long professorId() { return professorId; }
    public void setProfessorId(Long professorId) { this.professorId = professorId; }

    public Long getEspacoId() { return espacoId; }
    public Long espacoId() { return espacoId; }
    public void setEspacoId(Long espacoId) { this.espacoId = espacoId; }

    public String getSemestre() { return semestre; }
    public String semestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public TurnoEnum getTurno() { return turno; }
    public TurnoEnum turno() { return turno; }
    public void setTurno(TurnoEnum turno) { this.turno = turno; }

    public Integer getCapacidadeMaxima() { return capacidadeMaxima; }
    public Integer capacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(Integer capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }

    public Integer getQuantidadeAlunos() { return quantidadeAlunos; }
    public Integer quantidadeAlunos() { return quantidadeAlunos; }
    public void setQuantidadeAlunos(Integer quantidadeAlunos) { this.quantidadeAlunos = quantidadeAlunos; }

    public StatusTurma getStatus() { return status; }
    public StatusTurma status() { return status; }
    public void setStatus(StatusTurma status) { this.status = status; }
}