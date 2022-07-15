package uos.selab.dtos;

import java.util.Date;

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

	private int contractNum;

	private int itemNum;

	private String itemTitle;

	private int sellerNum;

	private String sellerNickName;

	private int buyerNum;

	private String buyerNickName;

	private String stateCode;

	private double price;

	private Date createdAt;

}
