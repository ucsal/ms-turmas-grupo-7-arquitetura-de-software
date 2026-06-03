package dto;


public record EspacoFisicoDTO(
        Long id,
        String nome,
        Integer capacidade,
        Boolean disponivel
) {
}