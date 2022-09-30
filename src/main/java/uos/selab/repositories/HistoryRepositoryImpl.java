package uos.selab.repositories;

import static uos.selab.domains.QHistory.history;

import java.util.Date;
import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import uos.selab.domains.History;
import uos.selab.dtos.SelectHistoryDTO;
import uos.selab.enums.HistorySortKey;
import uos.selab.enums.SortOrder;

@RequiredArgsConstructor
class HistoryRepositoryImpl implements HistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<History> findAllByDTO(SelectHistoryDTO historyDTO) {

        // from 검색 조건 설정
        JPAQuery<History> queryBuilder = queryFactory.selectFrom(history);

        // where 검색 조건 설정
        BooleanBuilder builder = new BooleanBuilder();
        int sellerNum = historyDTO.getSellerNum(), buyerNum = historyDTO.getBuyerNum(),
                itemNum = historyDTO.getItemNum();
        Double minPrice = historyDTO.getMinPrice(), maxPrice = historyDTO.getMaxPrice();
        Date startDate = historyDTO.getStartDate(), endDate = historyDTO.getEndDate();
        String transactionHash = historyDTO.getTransactionHash();

        if (sellerNum != 0)
            builder.and(history.seller.memberNum.eq(sellerNum));

        if (buyerNum != 0)
            builder.and(history.buyer.memberNum.eq(buyerNum));

        if (itemNum != 0)
            builder.and(history.item.itemNum.eq(itemNum));

        if (minPrice != null)
            builder.and(history.price.goe(minPrice.doubleValue()));

        if (maxPrice != null)
            builder.and(history.price.loe(maxPrice.doubleValue()));

        if (startDate != null)
            builder.and(history.createdAt.goe(startDate));

        if (endDate != null)
            builder.and(history.createdAt.loe(endDate));

        if (!StringUtils.isNullOrEmpty(transactionHash))
            builder.and(history.transactionHash.eq(transactionHash));

        queryBuilder.where(builder);

        // orderBy 검색 조건 설정
        HistorySortKey sortKey = historyDTO.getSortKey();
        if (sortKey == null)
            sortKey = HistorySortKey.ID;

        SortOrder sortOrder = historyDTO.getSortOrder();
        if (sortOrder == null)
            sortOrder = SortOrder.ASC;

        switch (sortKey) {
            // ID만 정렬 (기본값)
            case ID:
                switch (sortOrder) {
                    case ASC:
                        queryBuilder.orderBy(history.historyNum.asc());
                        break;
                    case DESC:
                        queryBuilder.orderBy(history.historyNum.desc());
                        break;
                }
                break;
            // createdAt만 정렬
            case CREATEDAT:
                switch (sortOrder) {
                    case ASC:
                        queryBuilder.orderBy(history.createdAt.asc());
                        break;
                    case DESC:
                        queryBuilder.orderBy(history.createdAt.desc());
                        break;
                }
                break;
            // PRICE만 정렬
            case PRICE:
                switch (sortOrder) {
                    case ASC:
                        queryBuilder.orderBy(history.price.asc());
                        break;
                    case DESC:
                        queryBuilder.orderBy(history.price.desc());
                        break;
                }
                break;
            // createdAt 먼저 정렬. createdAt은 sortOrder, price는 asc
            case CREATEDAT_PRICE_ASC:
                switch (sortOrder) {
                    case ASC:
                        queryBuilder.orderBy(history.createdAt.asc(), history.price.asc());
                        break;
                    case DESC:
                        queryBuilder.orderBy(history.createdAt.desc(), history.price.asc());
                        break;
                }
                break;
            // createdAt 먼저 정렬. createdAt은 sortOrder, price는 desc
            case CREATEDAT_PRICE_DESC:
                switch (sortOrder) {
                    case ASC:
                        queryBuilder.orderBy(history.createdAt.asc(), history.price.desc());
                        break;
                    case DESC:
                        queryBuilder.orderBy(history.createdAt.desc(), history.price.desc());
                        break;
                }
                break;
            // price 먼저 정렬. createdAt은 sortOrder, price는 asc
            case PRICE_CREATEDAT_ASC:
                switch (sortOrder) {
                    case ASC:
                        queryBuilder.orderBy(history.price.asc(), history.createdAt.asc());
                        break;
                    case DESC:
                        queryBuilder.orderBy(history.price.asc(), history.createdAt.desc());
                        break;
                }
                break;
            // price 먼저 정렬. createdAt은 sortOrder, price는 desc
            case PRICE_CREATEDAT_DESC:
                switch (sortOrder) {
                    case ASC:
                        queryBuilder.orderBy(history.price.desc(), history.createdAt.asc());
                        break;
                    case DESC:
                        queryBuilder.orderBy(history.price.desc(), history.createdAt.desc());
                        break;
                }
                break;
        }

        // 페이징 조건 설정
        int skip = historyDTO.getSkip(), take = historyDTO.getTake();

        queryBuilder.offset(skip);

        // take를 입력하지 않으면 10개 반환
        if (take <= 0)
            queryBuilder.limit(10);
        else
            queryBuilder.limit(take);

        return queryBuilder.fetch();
    }

}
