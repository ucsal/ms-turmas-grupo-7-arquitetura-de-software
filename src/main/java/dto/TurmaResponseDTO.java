package dto;

import entity.enums.StatusTurma;
import entity.enums.TurnoEnum;

public record TurmaResponseDTO(
        Long id,
        Long disciplinaId,
        Long professorId,
        String semestre,
        TurnoEnum turno,
        Integer capacidadeMaxima,
        Integer quantidadeAlunos,
        StatusTurma status
) {}