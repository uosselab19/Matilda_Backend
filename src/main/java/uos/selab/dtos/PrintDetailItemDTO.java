package uos.selab.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class PrintDetailItemDTO {

    private int itemNum;

    private int memberNum;

    private String memberNickName;

    private String memberThumbImgUrl;

    private String catCode;

    private String title;

    private String description;

    private String imgUrl;

    private int tokenId;

    private String tokenUri;

    private String stateCode;

    private double price;

}
