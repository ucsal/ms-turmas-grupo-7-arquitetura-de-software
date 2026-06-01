package repository;



import entity.Turma;
import entity.enums.StatusTurma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    List<Turma> findByDisciplinaIdAndStatus(Long disciplinaId, StatusTurma status);
    long countByDisciplinaIdAndProfessorIdAndStatusNot(Long disciplinaId, Long professorId, StatusTurma status);
}