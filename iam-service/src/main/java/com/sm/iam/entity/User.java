package com.sm.iam.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user")
public class User implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "mobile")
	private String mobile;
	
	@Column(name = "active")
	private boolean active;
	
	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name = "modified_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedAt;
	
	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles",
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	//@JsonIgnoreProperties
	private Collection<Role> roles;
	
	
	/*
	 * @JsonBackReference helps in keeping lazy initialization feature because
	 * jackson would want userAddressses to be present at the time of response but
	 * because of lazy initialization it won't be available at that time.
	 * hence it ignores this property at the time of response and hence keeps the lazy nature.
	 * for more info {@link JsonBackReference}
	 */	
	// @OneToMany(mappedBy = "fkUser", cascade = CascadeType.ALL)
	@JsonBackReference
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_user_id")
	private Collection<UserAddress> userAddresses;
	
	public void addUserAddress(UserAddress userAddress) {
		if(userAddresses == null) {
			userAddresses = new ArrayList<>();
		}
		userAddresses.add(userAddress);
		userAddress.setFkUser(this.getId());
	}
	
}
