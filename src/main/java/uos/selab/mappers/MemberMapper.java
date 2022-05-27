package uos.selab.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import uos.selab.domains.Member;
import uos.selab.dtos.InsertMemberDTO;
import uos.selab.dtos.UpdateMemberDTO;

@Mapper(componentModel = "spring")
public interface MemberMapper {
	
	MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);
	
	Member toEntity(InsertMemberDTO memberDTO);

	InsertMemberDTO toDTO(Member member);

	/*
	 * default MemberEntity toEntity(InsertMemberDTO memberDTO) { if (memberDTO ==
	 * null) { return null; }
	 * 
	 * MemberEntityBuilder member = MemberEntity.builder();
	 * member.id(memberDTO.getId()).password(memberDTO.getPassword());
	 * member.nickname(memberDTO.getNickname()).name(memberDTO.getName());
	 * member.email(memberDTO.getEmail()).tel_num(memberDTO.getTelNum());
	 * member.birthday(memberDTO.getBirthDay()).gender(memberDTO.getGender());
	 * member.created_at(memberDTO.getCreatedAt()); return member.build(); }
	 * 
	 * default InsertMemberDTO toDTO(MemberEntity member) { if (member == null) {
	 * return null; }
	 * 
	 * InsertMemberDTOBuilder memberDTO = InsertMemberDTO.builder();
	 * memberDTO.id(member.getId()).password(member.getPassword());
	 * memberDTO.nickname(member.getNickname()).name(member.getName());
	 * memberDTO.email(member.getEmail()).telNum(member.getTel_num());
	 * memberDTO.birthDay(member.getBirthday()).gender(member.getGender());
	 * memberDTO.createdAt(member.getCreated_at());
	 * 
	 * return memberDTO.build(); }
	 */

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateFromDto(UpdateMemberDTO dto, @MappingTarget Member entity);
}
