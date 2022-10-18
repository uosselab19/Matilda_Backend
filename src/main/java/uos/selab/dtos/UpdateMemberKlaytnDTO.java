package uos.selab.dtos;

import javax.validation.constraints.Size;

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
public class UpdateMemberKlaytnDTO {

    @Size(max = 100, message = "walletAddress의 최대 크기는 100입니다.")
    private String walletAddress;

    @Size(max = 200, message = "walletPrivateKey의 최대 크기는 200입니다.")
    private String walletPrivateKey;

}
