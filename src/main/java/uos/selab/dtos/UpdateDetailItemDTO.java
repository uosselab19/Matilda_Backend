package uos.selab.dtos;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uos.selab.enums.UpdateItemOption;

@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class UpdateDetailItemDTO {

	private int buyerNum;
	
	@Size(max = 500, message = "nftAddress의 최대 크기는 500입니다.")
	private String nftAddress;
	
	private UpdateItemOption option;
	
}
