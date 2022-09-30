package uos.selab.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
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
public class InsertMemberDTO {

    @NotNull(message = "id는 null일 수 없습니다.")
    @Size(min = 1, max = 16, message = "id의 크기는 1에서 16 사이입니다.")
    private String id;

    @NotNull(message = "password는 null일 수 없습니다.")
    @Size(min = 1, max = 300, message = "password의 크기는 1에서 300 사이입니다.")
    private String password;

    @NotNull(message = "nickname은 null일 수 없습니다.")
    @Size(min = 1, max = 16, message = "nickname의 크기는 1에서 16 사이입니다.")
    private String nickname;

    @NotNull(message = "email은 null일 수 없습니다.")
    @Email(message = "email의 형식이 맞지 않습니다.")
    private String email;

}
