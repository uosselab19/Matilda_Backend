package uos.selab.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uos.selab.enums.SortOrder;

@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class SelectContractDTO {	
	
	private int memberNum; // 정렬대상 1
	
	private String stateCode; // 정렬대상 2

	private SortOrder sortOrder; // 정렬순서
	
	private String sortKey; // 정렬조건
	
	private int skip; // paging skip
	
	private int take; // paging take
	
}

enum ContractSortKey {
	SELLER,
	BUYER,
	ITEM
}
