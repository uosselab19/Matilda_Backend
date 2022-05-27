package uos.selab.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uos.selab.enums.ContractKeywordType;

@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class SelectContractDTO {	
	
	private int sellerNum; // 검색 값 1
	
	private int buyerNum; // 검색 값 2
	
	private int itemNum; // 검색 값 3
	
	private ContractKeywordType keywordType;
	
	private int skip; // paging skip
	
	private int take; // paging take 15
	
}