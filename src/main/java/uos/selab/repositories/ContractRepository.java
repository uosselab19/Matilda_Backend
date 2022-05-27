package uos.selab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uos.selab.domains.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer>{
}
