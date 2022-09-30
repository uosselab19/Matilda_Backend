package uos.selab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uos.selab.domains.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer>, HistoryRepositoryCustom {

}
