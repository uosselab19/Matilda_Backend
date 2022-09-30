package uos.selab.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import uos.selab.domains.Member;
import uos.selab.dtos.InsertMemberDTO;
import uos.selab.dtos.PrintMemberDTO;
import uos.selab.dtos.UpdateMemberDTO;
import uos.selab.dtos.UpdateMemberKlaytnDTO;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    Member toEntity(InsertMemberDTO memberDTO);

    InsertMemberDTO toDTO(Member member);

    PrintMemberDTO toPrintDTO(Member member);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateMemberDTO memberDTO, @MappingTarget Member entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateKlaytnFromDto(UpdateMemberKlaytnDTO memberDTO, @MappingTarget Member entity);

}
