package uos.selab.enums;

public enum HistorySortKey {
	ID,						// 기본값
	CREATEDAT,				// createdAt만 정렬
	PRICE,					// price만 정렬
	CREATEDAT_PRICE_ASC,	// createdAt 먼저 정렬. createdAt은 sortOrder, price는 asc
	CREATEDAT_PRICE_DESC,	// createdAt 먼저 정렬. createdAt은 sortOrder, price는 desc
	PRICE_CREATEDAT_ASC,	// price 먼저 정렬. createdAt은 sortOrder, price는 asc
	PRICE_CREATEDAT_DESC	// price 먼저 정렬. createdAt은 sortOrder, price는 desc
}