package repository;

import entity.EspacoFisico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspacoFisicoRepository
        extends JpaRepository<EspacoFisico, Long> {

    List<EspacoFisico> findByDisponivelTrue();

}