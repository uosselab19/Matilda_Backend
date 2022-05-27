package uos.selab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uos.selab.domains.Contract;
import uos.selab.domains.Item;
import uos.selab.domains.Member;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer>{
	List<Contract> findALLByItem(Item item);
	List<Contract> findALLBySeller(Member seller);
	List<Contract> findALLByBuyer(Member buyer);
	List<Contract> findALLBySellerOrBuyer(Member seller, Member buyer);
}
