package uos.selab.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
public class PrintMemberDTO {

	private int memberNum;

	private String id;

	private String nickname;

	private String email;

	private String description;

	private String profileImg;

	private String thumbProfileImg;

	private ArrayList<HashMap<String, Integer>> presetList;

	private String walletAddress;

	private Date createdAt;

}
