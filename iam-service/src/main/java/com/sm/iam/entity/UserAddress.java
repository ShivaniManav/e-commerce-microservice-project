package com.sm.iam.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user_address")
public class UserAddress implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "mobile")
	private String mobileNumber;
	
	@Column(name = "address_line1")
	private String addressLineOne;
	
	@Column(name = "address_line2")
	private String addressLineTwo;
	
	@Column(name = "landmark")
	private String landmark;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "pincode")
	private String pincode;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "is_default")
	private boolean isDefault;
	
	// @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	// @JoinColumn(name = "fk_user_id")
	@JsonManagedReference
	@Column(name = "fk_user_id")
	private Long fkUser;
}
