package uos.selab.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class PrintItemDTO {

	private int itemNum;
	
	private int memberNum;
	
	private String catCode;
	
	private String title;
	
	private String objectUrl;
	
	private int contractNum;
	
	private String stateCode;
}
