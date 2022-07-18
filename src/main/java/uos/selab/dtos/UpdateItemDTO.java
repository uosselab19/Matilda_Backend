package uos.selab.dtos;

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
public class UpdateItemDTO {

	@Size(max = 45)
	private String title;

	@Size(max = 300)
	private String description;

	private double price;

}
