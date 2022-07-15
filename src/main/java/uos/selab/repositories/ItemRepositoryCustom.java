package uos.selab.repositories;

import java.util.List;

import uos.selab.domains.Item;
import uos.selab.dtos.SelectItemDTO;

public interface ItemRepositoryCustom {

	List<Item> findAllByDTO(SelectItemDTO itemDTO);

}