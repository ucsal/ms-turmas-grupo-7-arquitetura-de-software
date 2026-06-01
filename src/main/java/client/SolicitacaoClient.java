package client;



import dto.SolicitacaoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.List;

@FeignClient(name = "ms-solicitacoes", url = "${app.services.ms-solicitacoes.url}")
public interface SolicitacaoClient {

    // Rota que busca todas as solicitações usando o ID de sistema/admin para a orquestração
    @GetMapping("/api/solicitacoes/resultados")
    List<SolicitacaoResponseDTO> obterTodasSolicitacoes(@RequestHeader("X-User-Id") Long sysAdminId);
}