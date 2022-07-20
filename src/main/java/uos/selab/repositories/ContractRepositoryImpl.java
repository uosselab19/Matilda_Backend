package uos.selab.repositories;

import static uos.selab.domains.QContract.contract;

import java.util.Date;
import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import uos.selab.domains.Contract;
import uos.selab.dtos.SelectContractDTO;
import uos.selab.enums.ContractSortKey;
import uos.selab.enums.SortOrder;

@RequiredArgsConstructor
class ContractRepositoryImpl implements ContractRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Contract> findAllByDTO(SelectContractDTO contractDTO) {

		// from 검색 조건 설정
		JPAQuery<Contract> queryBuilder = queryFactory.selectFrom(contract);

		// where 검색 조건 설정
		BooleanBuilder builder = new BooleanBuilder();
		int sellerNum = contractDTO.getSellerNum(), buyerNum = contractDTO.getBuyerNum(), itemNum = contractDTO.getItemNum();
		Double minPrice = contractDTO.getMinPrice(), maxPrice = contractDTO.getMaxPrice();
		Date startDate = contractDTO.getStartDate(), endDate = contractDTO.getEndDate();

		if (sellerNum != 0)
			builder.and(contract.seller.memberNum.eq(sellerNum));

		if (buyerNum != 0)
			builder.and(contract.buyer.memberNum.eq(buyerNum));

		if (itemNum != 0)
			builder.and(contract.item.itemNum.eq(itemNum));

		if (minPrice != null)
			builder.and(contract.price.goe(minPrice.doubleValue()));

		if (maxPrice != null)
			builder.and(contract.price.loe(maxPrice.doubleValue()));

		if (startDate != null)
			builder.and(contract.createdAt.goe(startDate));

		if (endDate != null)
			builder.and(contract.createdAt.loe(endDate));

		queryBuilder.where(builder);

		// orderBy 검색 조건 설정
		ContractSortKey sortKey = contractDTO.getSortKey();
		if (sortKey == null)
			sortKey = ContractSortKey.ID;

		SortOrder sortOrder = contractDTO.getSortOrder();
		if (sortOrder == null)
			sortOrder = SortOrder.ASC;

		switch (sortKey) {
			// ID만 정렬 (기본값)
			case ID:
				switch (sortOrder) {
					case ASC:
						queryBuilder.orderBy(contract.contractNum.asc());
						break;
					case DESC:
						queryBuilder.orderBy(contract.contractNum.desc());
						break;
				}
				break;
			// createdAt만 정렬
			case CREATEDAT:
				switch (sortOrder) {
					case ASC:
						queryBuilder.orderBy(contract.createdAt.asc());
						break;
					case DESC:
						queryBuilder.orderBy(contract.createdAt.desc());
						break;
				}
				break;
			// PRICE만 정렬
			case PRICE:
				switch (sortOrder) {
					case ASC:
						queryBuilder.orderBy(contract.price.asc());
						break;
					case DESC:
						queryBuilder.orderBy(contract.price.desc());
						break;
				}
				break;
			// createdAt 먼저 정렬. createdAt은 sortOrder, price는 asc
			case CREATEDAT_PRICE_ASC:
				switch (sortOrder) {
					case ASC:
						queryBuilder.orderBy(contract.createdAt.asc(), contract.price.asc());
						break;
					case DESC:
						queryBuilder.orderBy(contract.createdAt.desc(), contract.price.asc());
						break;
				}
				break;
			// createdAt 먼저 정렬. createdAt은 sortOrder, price는 desc
			case CREATEDAT_PRICE_DESC:
				switch (sortOrder) {
					case ASC:
						queryBuilder.orderBy(contract.createdAt.asc(), contract.price.desc());
						break;
					case DESC:
						queryBuilder.orderBy(contract.createdAt.desc(), contract.price.desc());
						break;
				}
				break;
			// price 먼저 정렬. createdAt은 sortOrder, price는 asc
			case PRICE_CREATEDAT_ASC:
				switch (sortOrder) {
					case ASC:
						queryBuilder.orderBy(contract.price.asc(), contract.createdAt.asc());
						break;
					case DESC:
						queryBuilder.orderBy(contract.price.asc(), contract.createdAt.desc());
						break;
				}
				break;
			// price 먼저 정렬. createdAt은 sortOrder, price는 desc
			case PRICE_CREATEDAT_DESC:
				switch (sortOrder) {
					case ASC:
						queryBuilder.orderBy(contract.price.desc(), contract.createdAt.asc());
						break;
					case DESC:
						queryBuilder.orderBy(contract.price.desc(), contract.createdAt.desc());
						break;
				}
				break;
		}

		// 페이징 조건 설정
		int skip = contractDTO.getSkip(), take = contractDTO.getTake();

		queryBuilder.offset(skip);

		// take를 입력하지 않으면 10개 반환
		if (take <= 0)
			queryBuilder.limit(10);
		else
			queryBuilder.limit(take);

		return queryBuilder.fetch();
	}

}
