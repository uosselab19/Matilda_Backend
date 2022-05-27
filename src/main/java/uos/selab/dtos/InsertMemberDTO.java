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
public class InsertMemberDTO {

	// private int memberNum;

	private String id;

	private String password;

	private String nickname;

	private String email;

	// private Date createdAt;	

}
