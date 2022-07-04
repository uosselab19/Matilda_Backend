package uos.selab.repositories;

import java.util.HashMap;
import java.util.List;

import uos.selab.domains.Item;

public interface ItemRepositoryCustom {

	List<Item> findAllByDTO(HashMap<String, String> itemDTO);

}