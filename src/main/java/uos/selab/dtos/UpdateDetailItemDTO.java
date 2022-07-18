package uos.selab.dtos;

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
	
	private String nftAddress;
	
	private UpdateItemOption option;
	
}
