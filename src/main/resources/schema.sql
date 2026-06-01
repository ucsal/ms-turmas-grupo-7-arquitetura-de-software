CREATE TABLE IF NOT EXISTS turmas (
    id BIGSERIAL PRIMARY KEY,
    disciplina_id BIGINT NOT NULL,
    professor_id BIGINT NOT NULL,
    semestre VARCHAR(10) NOT NULL,
    turno VARCHAR(20) NOT NULL,
    capacidade_maxima INTEGER NOT NULL,
    quantidade_alunos INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS matriculas_turma (
    id BIGSERIAL PRIMARY KEY,
    turma_id BIGINT NOT NULL,
    aluno_id BIGINT NOT NULL,
    prioridade INTEGER NOT NULL,
    data_matricula TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_matricula_turma FOREIGN KEY (turma_id) REFERENCES turmas(id)
);

CREATE TABLE IF NOT EXISTS lista_espera (
    id BIGSERIAL PRIMARY KEY,
    disciplina_id BIGINT NOT NULL,
    aluno_id BIGINT NOT NULL,
    prioridade INTEGER NOT NULL,
    posicao_fila INTEGER NOT NULL,
    data_entrada TIMESTAMP NOT NULL
);