package dto;

import entity.enums.TurnoEnum;
import java.time.LocalDateTime;
import java.util.Set;

public record FlatSolicitacaoDTO(
        Long alunoId,
        Long disciplinaId,
        Integer prioridade,
        LocalDateTime dataSolicitacao,
        Set<TurnoEnum> turnos,
        String semestre
) {}