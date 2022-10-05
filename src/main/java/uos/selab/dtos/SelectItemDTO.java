package uos.selab.dtos;

import java.util.List;

import javax.validation.constraints.Size;

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

	@Size(max = 45, message = "title의 최대 크기는 45입니다.")
	private String title; // 키 값 1

	private Double minPrice; // 키 값 2

	private Double maxPrice; // 키 값 2-2

	private int memberNum; // 키 값 3

	@Size(min = 2, max = 3, message = "catCode의 크기는 2에서 3 사이입니다.")
	private String catCode; // 키 값 4

	private List<String> stateCodes; // 키 값 5

	private SortOrder sortOrder; // 정렬순서

	private ItemSortKey sortKey; // 정렬조건

	private int skip; // paging skip

	private int take; // paging take

}
