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

	@Size(max = 300)
	private String password;

	@Size(min = 1, max = 16)
	private String nickname;

	@Email
	private String email;

	@Size(max = 300)
	private String description;

	@Size(max = 255)
	private String profileImg;

	@Size(max = 255)
	private String thumbProfileImg;
	
	private ArrayList<HashMap<String, Integer>> presetList;

	@Size(max = 500)
	private String walletAddress;

}
