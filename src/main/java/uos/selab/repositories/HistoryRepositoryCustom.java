package uos.selab.repositories;

import java.util.List;

import uos.selab.domains.History;
import uos.selab.dtos.SelectHistoryDTO;

public interface HistoryRepositoryCustom {

    List<History> findAllByDTO(SelectHistoryDTO historyDTO);

}