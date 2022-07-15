package uos.selab.dtos;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uos.selab.enums.ContractSortKey;
import uos.selab.enums.SortOrder;

@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class SelectContractDTO {

	private int sellerNum;

	private int buyerNum;

	private int itemNum;

	private Double minPrice;

	private Double maxPrice;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endDate;

	private SortOrder sortOrder;

	private ContractSortKey sortKey;

	private int skip;

	private int take;

}