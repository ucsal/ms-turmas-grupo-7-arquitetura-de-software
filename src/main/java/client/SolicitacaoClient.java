package client;

import dto.AtualizarStatusRequestDTO;
import dto.SolicitacaoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-solicitacoes")
public interface SolicitacaoClient {

    @GetMapping("/api/solicitacoes/pendentes")
    List<SolicitacaoResponseDTO> obterPendentes(
            @RequestHeader("X-User-Role") String role,
            @RequestHeader("X-User-Id") Long userId
    );

    @PatchMapping("/api/solicitacoes/{id}/status")
    void atualizarStatus(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Role") String role,
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody AtualizarStatusRequestDTO body
    );
}