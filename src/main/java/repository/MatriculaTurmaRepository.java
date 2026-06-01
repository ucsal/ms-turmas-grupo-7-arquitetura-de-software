package repository;




import entity.MatriculaTurma;
import entity.enums.StatusMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaTurmaRepository extends JpaRepository<MatriculaTurma, Long> {
    Optional<MatriculaTurma> findByTurmaIdAndAlunoIdAndStatus(Long turmaId, Long alunoId, StatusMatricula status);
    List<MatriculaTurma> findByTurmaIdAndStatus(Long turmaId, StatusMatricula status);
    boolean existsByAlunoIdAndStatusAndTurmaIdIn(Long alunoId, StatusMatricula status, List<Long> turmaIds);
}