package uos.selab.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uos.selab.domains.Item;
import uos.selab.domains.Member;

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

	private double price;
	
	// private Date createdAt;	

}
