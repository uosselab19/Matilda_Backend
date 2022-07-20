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
public class InsertContractDTO {

	// private int contractNum;

	/* Foreign Key */
	private int itemNum;

	private int sellerNum;

	private int buyerNum;
	/* */

	private String stateCode;

	private Double price;

	// private Date createdAt;

}
