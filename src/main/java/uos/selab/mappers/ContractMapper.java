package uos.selab.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import uos.selab.domains.Contract;
import uos.selab.dtos.InsertContractDTO;

@Mapper(componentModel = "spring")
public interface ContractMapper {
	
	ContractMapper INSTANCE = Mappers.getMapper(ContractMapper.class);
	
	Contract toEntity(InsertContractDTO contractDTO);

	InsertContractDTO toDTO(Contract contract);
}