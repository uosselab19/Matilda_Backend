package uos.selab.dtos;

import java.util.Date;

import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uos.selab.enums.HistorySortKey;
import uos.selab.enums.SortOrder;

@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class SelectHistoryDTO {

    private int sellerNum;

    private int buyerNum;

    private int itemNum;

    private Double minPrice;

    private Double maxPrice;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    private SortOrder sortOrder;

    private HistorySortKey sortKey;

    @Size(max = 100, message = "transactionHash의 최대 크기는 100입니다.")
    private String transactionHash;

    private int skip;

    private int take;

}