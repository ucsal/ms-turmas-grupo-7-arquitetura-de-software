package client;

import dto.SolicitacaoResponseDTO;
import entity.enums.StatusSolicitacao;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "ms-solicitacoes",
        url = "${app.services.ms-solicitacoes.url}"
)
public interface SolicitacaoClient {

    @GetMapping("/api/solicitacoes/resultados")
    List<SolicitacaoResponseDTO> obterTodasSolicitacoes(
            @RequestHeader("X-User-Id") Long sysAdminId
    );

    @PatchMapping("/api/solicitacoes/{id}/status")
    void atualizarStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") StatusSolicitacao status
    );
}