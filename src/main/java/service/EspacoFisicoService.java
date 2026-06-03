package service;

import dto.EspacoFisicoDTO;
import entity.EspacoFisico;
import exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import repository.EspacoFisicoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EspacoFisicoService {

    private final EspacoFisicoRepository repository;

    public EspacoFisicoService(
            EspacoFisicoRepository repository
    ) {
        this.repository = repository;
    }

    public List<EspacoFisicoDTO> listarDisponiveis() {

        return repository.findByDisponivelTrue()
                .stream()
                .map(e -> new EspacoFisicoDTO(
                        e.id(),
                        e.nome(),
                        e.capacidade(),
                        e.disponivel()
                ))
                .collect(Collectors.toList());
    }

    public EspacoFisicoDTO buscarPorId(Long id) {

        EspacoFisico e = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Espaço físico não encontrado."
                        ));

        return new EspacoFisicoDTO(
                e.id(),
                e.nome(),
                e.capacidade(),
                e.disponivel()
        );
    }

    public EspacoFisicoDTO atualizar(
            Long id,
            EspacoFisicoDTO dto
    ) {

        EspacoFisico e = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Espaço físico não encontrado."
                        ));

        e.setNome(dto.nome());
        e.setCapacidade(dto.capacidade());
        e.setDisponivel(dto.disponivel());

        repository.save(e);

        return new EspacoFisicoDTO(
                e.id(),
                e.nome(),
                e.capacidade(),
                e.disponivel()
        );
    }

    public void excluir(Long id) {

        EspacoFisico e = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Espaço físico não encontrado."
                        ));

        repository.delete(e);
    }
}