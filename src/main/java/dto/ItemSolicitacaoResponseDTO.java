package dto;

import entity.enums.TurnoEnum;
import java.util.Set;

public record ItemSolicitacaoResponseDTO(
        Long id,
        Long disciplinaId,
        Set<TurnoEnum> turnos,
        Integer prioridade,
        String status
) {}