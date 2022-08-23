package uos.selab.domains;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Contracts")
@AllArgsConstructor()
@NoArgsConstructor()
@Setter
@Getter
@Builder
public class Contract {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "contract_num")
	private int contractNum;

	/* Foreign Key */
	@ManyToOne
	@JoinColumn(name = "seller_num", nullable = true)
	@JsonIgnore
	private Member seller;

	@ManyToOne
	@JoinColumn(name = "buyer_num", nullable = true)
	@JsonIgnore
	private Member buyer;

	@ManyToOne
	@JoinColumn(name = "item_num", nullable = false)
	@JsonIgnore
	private Item item;
	/* */

	@Column(name = "state_code", nullable = false, length = 3)
	private String stateCode;

	@Column(name = "tx_hash", nullable = true, length = 100)
	private String transactionHash;

	@Column(nullable = true)
	private Double price;

	@Column(name = "created_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
}
