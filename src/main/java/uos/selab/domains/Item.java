package uos.selab.domains;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Items") // 테이블 명을 작성
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class Item {
    @Id
    @Column(name = "item_num")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemNum;

    /* Foreign Key */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_num", nullable = false)
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_code", nullable = false)
    @JsonIgnore
    private Category category;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_num", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private List<History> histories;
    /* */

    @Column(nullable = false, length = 45)
    private String title;

    @Column(nullable = true, length = 300)
    private String description;

    @Column(name = "img_url", nullable = false, length = 255)
    private String imgUrl;

    @Column(name = "object_url", nullable = false, length = 255)
    private String objectUrl;

    @Column(name = "token_id", nullable = true, unique = true)
    private Integer tokenId;

    @Column(name = "token_uri", nullable = true, length = 200)
    private String tokenUri;

    @Column(name = "state_code", nullable = false, length = 3)
    private String stateCode;

    @Column(nullable = true)
    private Double price;

}