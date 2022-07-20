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

	// private int memberNum;
	
	@NotNull
	@Size(min = 1, max = 16, message = "id의 크기는 1에서 16 사이입니다.")
	private String id;
	
	@NotNull
	@Size(min = 1, max = 300, message = "password의 크기는 1에서 300 사이입니다.")
	private String password;
	
	@NotNull
	@Size(min = 1, max = 16, message = "nickname의 크기는 1에서 16 사이입니다.")
	private String nickname;

	@NotNull
	@Email
	private String email;

	// private Date createdAt;

}
