package repository;

import entity.ListaEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ListaEsperaRepository extends JpaRepository<ListaEspera, Long> {
    List<ListaEspera> findByDisciplinaIdOrderByPosicaoFilaAsc(Long disciplinaId);
    Optional<ListaEspera> findFirstByDisciplinaIdOrderByPosicaoFilaAsc(Long disciplinaId);
    boolean existsByDisciplinaIdAndAlunoId(Long disciplinaId, Long alunoId);
    long countByDisciplinaId(Long disciplinaId);
}