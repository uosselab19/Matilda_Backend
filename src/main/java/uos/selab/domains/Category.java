package uos.selab.domains;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Categories") // 테이블 명을 작성
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class Category {
	@Id
	@Column(name="cat_code", length=3)
	private String catCode;
	
	/* Foreign Key */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="cat_code", nullable=false, insertable=false, updatable=false)
	private List<Item> items;
	/*  */
	
	@Column(nullable = false)
	private String title;
	
	@Column(name="img_url", nullable = false)
	private String imgUrl;
}