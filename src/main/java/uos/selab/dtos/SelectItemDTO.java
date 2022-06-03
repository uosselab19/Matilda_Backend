package uos.selab.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uos.selab.enums.ItemSortKey;
import uos.selab.enums.SortOrder;

@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class SelectItemDTO {	
	private String title; // 키 값 1

	private double minPrice; // 키 값 2
	
	private double maxPrice; // 키 값 2-2
	
	private int memberNum; // 키 값 3
	
	private String catCode; // 키 값 4
	
	private String stateCode; // 키 값 5

	private SortOrder sortOrder; // 정렬순서
	
	private ItemSortKey sortKey; // 정렬조건
	
	private int skip; // paging skip
	
	private int take; // paging take
	
}
