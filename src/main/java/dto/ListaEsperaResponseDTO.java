package dto;

import java.time.LocalDateTime;

public record ListaEsperaResponseDTO(
        Long id,
        Long disciplinaId,
        Long alunoId,
        Integer prioridade,
        Integer posicaoFila,
        LocalDateTime dataEntrada
) {}