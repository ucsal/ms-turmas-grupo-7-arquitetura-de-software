package controller;

import dto.ListaEsperaResponseDTO;
import dto.TurmaResponseDTO;
import service.TurmaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List; // Importação vital para corrigir o erro "missing type List"
import java.util.Map;

@RestController
@RequestMapping("/turmas")
public class TurmaController {

    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @PostMapping("/processar")
    public ResponseEntity<Map<String, String>> processarFormacao() {
        turmaService.processarFormacaoTurmas();
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "SUCESSO");
        response.put("mensagem", "Processamento de formação de turmas executado com sucesso.");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TurmaResponseDTO>> listarTodas() {
        List<TurmaResponseDTO> turmas = turmaService.listarTurmas();
        return ResponseEntity.ok(turmas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponseDTO> buscarPorId(@PathVariable Long id) {
        TurmaResponseDTO turma = turmaService.buscarPorId(id);
        return ResponseEntity.ok(turma);
    }

    @GetMapping("/{id}/lista-espera")
    public ResponseEntity<List<ListaEsperaResponseDTO>> buscarListaEspera(@PathVariable Long id) {
        List<ListaEsperaResponseDTO> lista = turmaService.buscarFilaPorDisciplina(id);
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{turmaId}/aluno/{alunoId}")
    public ResponseEntity<Map<String, String>> cancelarMatricula(@PathVariable Long turmaId, @PathVariable Long alunoId) {
        turmaService.cancelarMatricula(turmaId, alunoId);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "SUCESSO");
        response.put("mensagem", "Matrícula cancelada com sucesso e fila reajustada.");
        
        return ResponseEntity.ok(response);
    }
}