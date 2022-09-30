package uos.selab.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import uos.selab.domains.History;
import uos.selab.dtos.InsertHistoryDTO;
import uos.selab.dtos.PrintHistoryDTO;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

    HistoryMapper INSTANCE = Mappers.getMapper(HistoryMapper.class);

    History toEntity(InsertHistoryDTO historyDTO);

    InsertHistoryDTO toDTO(History history);

    PrintHistoryDTO toPrintDTO(History history);

}