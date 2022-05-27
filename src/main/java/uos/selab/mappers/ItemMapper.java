package uos.selab.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import uos.selab.domains.Item;
import uos.selab.dtos.InsertItemDTO;
import uos.selab.dtos.UpdateItemDTO;

@Mapper(componentModel = "spring")
public interface ItemMapper {
	
	ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);
	
	Item toEntity(InsertItemDTO itemDTO);

	InsertItemDTO toDTO(Item item);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateFromDto(UpdateItemDTO dto, @MappingTarget Item entity);
}
