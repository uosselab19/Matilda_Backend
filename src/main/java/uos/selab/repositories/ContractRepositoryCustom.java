package uos.selab.repositories;

import java.util.List;

import uos.selab.domains.Contract;
import uos.selab.dtos.SelectContractDTO;

public interface ContractRepositoryCustom {

	List<Contract> findAllByDTO(SelectContractDTO contractDTO);

}