package controller;

import dto.EspacoFisicoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.EspacoFisicoService;

import java.util.List;

@RestController
@RequestMapping("/api/espacos-fisicos")
public class EspacoFisicoController {

    private final EspacoFisicoService service;

    public EspacoFisicoController(
            EspacoFisicoService service
    ) {
        this.service = service;
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<EspacoFisicoDTO>>
    listarDisponiveis() {

        return ResponseEntity.ok(
                service.listarDisponiveis()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspacoFisicoDTO>
    buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspacoFisicoDTO>
    atualizar(
            @PathVariable Long id,
            @RequestBody EspacoFisicoDTO dto
    ) {

        return ResponseEntity.ok(
                service.atualizar(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    excluir(@PathVariable Long id) {

        service.excluir(id);

        return ResponseEntity.noContent().build();
    }
}