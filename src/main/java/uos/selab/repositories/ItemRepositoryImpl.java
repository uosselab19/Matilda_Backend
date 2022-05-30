package uos.selab.repositories;

import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import uos.selab.domains.Item;
import uos.selab.dtos.SelectItemDTO;

import static uos.selab.domains.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Item> findAllByDTO(SelectItemDTO itemDTO) {
		BooleanBuilder builder = new BooleanBuilder();

		String title = itemDTO.getTitle(), stateCode = itemDTO.getStateCode();

		if (!StringUtils.isNullOrEmpty(title)) {
			builder.and(item.title.contains(title));
		}
		

		if (!StringUtils.isNullOrEmpty(stateCode)) {
			builder.and(item.stateCode.eq(stateCode));
		}
		
		JPAQuery<Item> queryBuilder = queryFactory.selectFrom(item);
		
		queryBuilder.offset(itemDTO.getSkip()).limit(itemDTO.getTake()).orderBy(item.title.asc(), item.stateCode.desc()).where(builder);

		return queryBuilder.fetch();
	}

}
