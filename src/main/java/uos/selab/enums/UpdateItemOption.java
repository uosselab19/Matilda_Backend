package uos.selab.enums;

public enum UpdateItemOption {
	MINT,		// 아이템을 MINT하여 NFT주소가 생성되는 경우
	STATE_OS,	// 아이템을 판매 상태로 변경하는 경우
	STATE_NOS,	// 아이템을 판매 중지 상태로 변경하는 경우
	TRADE,		// 거래가 체결되어 아이템이 판매된 경우
	STOP		// 아이템이 거래 중단 된 경우
}