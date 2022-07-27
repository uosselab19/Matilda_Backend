package uos.selab.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uos.selab.domains.Category;
import uos.selab.domains.Item;
import uos.selab.domains.Member;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>, ItemRepositoryCustom {

	public List<Item> findByMember(Member member);

	public List<Item> findByCategory(Category category);
	
	public Optional<Item> findByNftAddress(String nftAddress);

}
