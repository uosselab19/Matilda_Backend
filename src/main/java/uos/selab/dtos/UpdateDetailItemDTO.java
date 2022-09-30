package uos.selab.dtos;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uos.selab.enums.UpdateItemOption;

@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class UpdateDetailItemDTO {

    private int buyerNum;

    private int tokenId;

    @Size(max = 200, message = "tokenUri의 최대 크기는 200입니다.")
    private String tokenUri;

    private Double price;

    @Size(max = 100, message = "transactionHash의 최대 크기는 100입니다.")
    private String transactionHash;

    private UpdateItemOption option;

}
