package uos.selab.enums;

public enum ItemSortKey {
	ID,					// 기본값
	TITLE,				// TITLE만 정렬
	PRICE,				// PRICE만 정렬
	TITLE_PRICE_ASC,	// TITLE 먼저 정렬. title은 sortOrder, price는 asc
	TITLE_PRICE_DESC,	// TITLE 먼저 정렬. title은 sortOrder, price는 desc
	PRICE_TITLE_ASC,	// PRICE 먼저 정렬. title은 sortOrder, price는 asc
	PRICE_TITLE_DESC	// PRICE 먼저 정렬. title은 sortOrder, price는 desc
}