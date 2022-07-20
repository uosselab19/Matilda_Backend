package uos.selab.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	
	@NotNull
	private int memberNum;
	
	@NotNull
	@Size(min = 2, max = 3, message = "catCode의 크기는 2에서 3 사이입니다.")
	private String catCode;
	
	@NotNull
	@Size(min = 1, max = 255, message = "imgUrl의 크기는 1에서 255 사이입니다.")
	private String imgUrl;

	@NotNull
	@Size(min = 1, max = 255, message = "objectUrl의 크기는 1에서 255 사이입니다.")
	private String objectUrl;

}
