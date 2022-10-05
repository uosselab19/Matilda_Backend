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
import uos.selab.enums.ItemSortKey;
import uos.selab.enums.SortOrder;

@RequiredArgsConstructor
class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Item> findAllByDTO(SelectItemDTO itemDTO) {

        // from 검색 조건 설정
        JPAQuery<Item> queryBuilder = queryFactory.selectFrom(item);

        // where 검색 조건 설정
        BooleanBuilder builder = new BooleanBuilder();
        String title = itemDTO.getTitle(), catCode = itemDTO.getCatCode();
        List<String> stateCodes = itemDTO.getStateCodes();
        int memberNum = itemDTO.getMemberNum();
        Double minPrice = itemDTO.getMinPrice(), maxPrice = itemDTO.getMaxPrice();

        if (!StringUtils.isNullOrEmpty(title))
            builder.and(item.title.contains(title));

        if (minPrice != null)
            builder.and(item.price.goe(minPrice.doubleValue()));

        if (maxPrice != null)
            builder.and(item.price.loe(maxPrice.doubleValue()));

        if (memberNum != 0)
            builder.and(item.member.memberNum.eq(memberNum));

        if (!StringUtils.isNullOrEmpty(catCode))
            builder.and(item.category.catCode.eq(catCode));
        
        if (stateCodes != null) {
            for (String stateCode : stateCodes) {
                builder.or(item.stateCode.eq(stateCode));
            }
        }

        queryBuilder.where(builder);

        // orderBy 검색 조건 설정
        ItemSortKey sortKey = itemDTO.getSortKey();
        if (sortKey == null)
            sortKey = ItemSortKey.ID;

        SortOrder sortOrder = itemDTO.getSortOrder();
        if (sortOrder == null)
            sortOrder = SortOrder.ASC;

        switch (sortKey) {
            // ID만 정렬 (기본값)
            case ID:
                switch (sortOrder) {
                    case ASC:
                        queryBuilder.orderBy(item.itemNum.asc());
                        break;
                    case DESC:
                        queryBuilder.orderBy(item.itemNum.desc());
                        break;
                }
                // TITLE만 정렬
            case TITLE:
                switch (sortOrder) {
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
                switch (sortOrder) {
                    case ASC:
                        queryBuilder.orderBy(item.price.asc());
                        break;
                    case DESC:
                        queryBuilder.orderBy(item.price.desc());
                        break;
                }
                break;
            // TITLE 먼저 정렬. TITLE은 sortOrder, PRICE는 asc 고정
            case TITLE_PRICE_ASC:
                switch (sortOrder) {
                    case ASC:
                        queryBuilder.orderBy(item.title.asc(), item.price.asc());
                        break;
                    case DESC:
                        queryBuilder.orderBy(item.title.desc(), item.price.asc());
                        break;
                }
                break;
            // TITLE 먼저 정렬. TITLE은 sortOrder, PRICE는 desc 고정
            case TITLE_PRICE_DESC:
                switch (sortOrder) {
                    case ASC:
                        queryBuilder.orderBy(item.title.asc(), item.price.desc());
                        break;
                    case DESC:
                        queryBuilder.orderBy(item.title.desc(), item.price.desc());
                        break;
                }
                break;
            // PRICE 먼저 정렬. TITLE은 sortOrder, PRICE는 asc 고정
            case PRICE_TITLE_ASC:
                switch (sortOrder) {
                    case ASC:
                        queryBuilder.orderBy(item.price.asc(), item.title.asc());
                        break;
                    case DESC:
                        queryBuilder.orderBy(item.price.asc(), item.title.desc());
                        break;
                }
                break;
            // PRICE 먼저 정렬. TITLE은 sortOrder, PRICE는 desc 고정
            case PRICE_TITLE_DESC:
                switch (sortOrder) {
                    case ASC:
                        queryBuilder.orderBy(item.price.desc(), item.title.asc());
                        break;
                    case DESC:
                        queryBuilder.orderBy(item.price.desc(), item.title.desc());
                        break;
                }
                break;
        }

        // 페이징 조건 설정
        int skip = itemDTO.getSkip(), take = itemDTO.getTake();

        queryBuilder.offset(skip);

        // take를 입력하지 않으면 24개 반환
        if (take <= 0)
            queryBuilder.limit(24);
        else
            queryBuilder.limit(take);

        return queryBuilder.fetch();
    }

}
