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
public class PrintContractDTO {

	// private int contractNum;

	/* Foreign Key */
	private String itemTitle;
	
	private String sellerNickName;
	
	private String buyerNickName;
	/* */

	private String stateCode;

	private double price;
	
	// private Date createdAt;	
}
