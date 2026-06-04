package dto;

import java.time.LocalDateTime;
import java.util.List;

public record SolicitacaoResponseDTO(
        Long id ,
        Long alunoId,
        String semestre,
        LocalDateTime dataSolicitacao,
        String status,
        List<ItemSolicitacaoResponseDTO> disciplinas
) {}