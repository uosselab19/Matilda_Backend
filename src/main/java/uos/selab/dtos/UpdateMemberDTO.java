package uos.selab.dtos;

import java.util.ArrayList;
import java.util.HashMap;

import javax.validation.constraints.Email;
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
public class UpdateMemberDTO {

	@Size(min = 1, max = 300, message = "password의 크기는 1에서 300 사이입니다.")
	private String password;

	@Size(min = 1, max = 16, message = "nickname의 크기는 1에서 16 사이입니다.")
	private String nickname;

	@Email
	private String email;

	@Size(max = 300, message = "description의 최대 크기는 300입니다.")
	private String description;

	@Size(max = 255, message = "profileImg의 최대 크기는 255입니다.")
	private String profileImg;

	@Size(max = 255, message = "thumbProfileImg의 최대 크기는 255입니다.")
	private String thumbProfileImg;
	
	private ArrayList<HashMap<String, Integer>> presetList;

}
