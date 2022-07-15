package uos.selab.domains;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Members")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_num")
	private int memberNum;

	/* Foreign Key */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller_num", nullable = false, insertable = false, updatable = false)
	@JsonIgnore
	private List<Contract> soldContracts;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "buyer_num", nullable = false, insertable = false, updatable = false)
	@JsonIgnore
	private List<Contract> boughtContracts;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_num", nullable = false, insertable = false, updatable = false)
	@JsonIgnore
	private List<Item> items;
	/* */

	@Column(nullable = false, unique = true, length = 16)
	private String id;

	@Column(nullable = false, length = 300)
	private String password;

	@Column(nullable = false, length = 16)
	private String nickname;

	@Column(nullable = false, length = 64)
	private String email;

	@Column(nullable = true, length = 300)
	private String description;

	@Column(name = "profile_img", nullable = true, length = 255)
	private String profileImg;

	@Column(name = "thumb_profile_img", nullable = true, length = 255)
	private String thumbProfileImg;

	@Column(name = "preset", nullable = true, columnDefinition = "json")
	private String preset; 

	@Column(name = "wallet_address", nullable = true, length = 500)
	private String walletAddress;

	@Column(name = "created_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
}
