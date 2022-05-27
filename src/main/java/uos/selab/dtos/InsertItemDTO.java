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
public class InsertItemDTO {

	private int memberNum;

	private String catCode;

	private String thumbImgUrl;
	
	private String imgUrl;
	
	private String objectUrl;
}
