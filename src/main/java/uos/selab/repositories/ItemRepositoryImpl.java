package uos.selab.repositories;

import static uos.selab.domains.QItem.item;

import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import uos.selab.domains.Item;
import uos.selab.dtos.SelectItemDTO;

@RequiredArgsConstructor
class ItemRepositoryImpl implements ItemRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Item> findAllByDTO(SelectItemDTO itemDTO) {

		// from 검색 조건 설정
		JPAQuery<Item> queryBuilder = queryFactory.selectFrom(item);

		// where 검색 조건 설정
		BooleanBuilder builder = new BooleanBuilder();
		String title = itemDTO.getTitle(), catCode = itemDTO.getCatCode(), stateCode = itemDTO.getStateCode();
		int memberNum = itemDTO.getMemberNum();
		double minPrice = itemDTO.getMinPrice(), maxPrice = itemDTO.getMaxPrice();

		if (!StringUtils.isNullOrEmpty(title)) {
			builder.and(item.title.contains(title));
		}
		
		// minPrice의 기본값은 -1
		if (minPrice >= 0) {
			builder.and(item.price.goe(minPrice));
		}
		
		// maxPrice의 기본값은 -1
		if (maxPrice >= 0 && maxPrice >= minPrice) {
			builder.and(item.price.loe(maxPrice));
		}

		if (memberNum != 0) {
			builder.and(item.member.memberNum.eq(memberNum));
		}

		if (!StringUtils.isNullOrEmpty(catCode)) {
			builder.and(item.category.catCode.eq(catCode));
		}

		if (!StringUtils.isNullOrEmpty(stateCode)) {
			builder.and(item.stateCode.eq(stateCode));
		}

		queryBuilder.where(builder);

		// orderBy 검색 조건 설정
		switch (itemDTO.getSortKey()) {
			// ID만 정렬
			case ID:
				switch (itemDTO.getSortOrder()) {
					case ASC:
						queryBuilder.orderBy(item.itemNum.asc());
						break;
					case DESC:
						queryBuilder.orderBy(item.itemNum.desc());
						break;
				}
			// TITLE만 정렬
			case TITLE:
				switch (itemDTO.getSortOrder()) {
					case ASC:
						queryBuilder.orderBy(item.title.asc());
						break;
					case DESC:
						queryBuilder.orderBy(item.title.desc());
						break;
				}
				break;
			// PRICE만 정렬
			case PRICE:
				switch (itemDTO.getSortOrder()) {
					case ASC:
						queryBuilder.orderBy(item.price.asc());
						break;
					case DESC:
						queryBuilder.orderBy(item.price.desc());
						break;
				}
				break;
			// TITLE은 sortOrder, PRICE는 asc 고정
			case TITLEPRICE0:
				switch (itemDTO.getSortOrder()) {
					case ASC:
						queryBuilder.orderBy(item.title.asc(), item.price.asc());
						break;
					case DESC:
						queryBuilder.orderBy(item.title.desc(), item.price.asc());
						break;
				}
				break;
			// TITLE은 sortOrder, PRICE는 desc 고정
			case TITLEPRICE1:
				switch (itemDTO.getSortOrder()) {
					case ASC:
						queryBuilder.orderBy(item.title.asc(), item.price.desc());
						break;
					case DESC:
						queryBuilder.orderBy(item.title.desc(), item.price.desc());
						break;
				}
				break;
		}

		// 페이징 조건 설정
		queryBuilder.offset(itemDTO.getSkip()).limit(itemDTO.getTake());

		return queryBuilder.fetch();
	}

}
