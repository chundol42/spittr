package com.devchun.spittr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Email;

@Entity
public class Spitter {
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min=5, max=16, message="{username.size}")
	@Column(name="username")
	private String username;
	
	@NotNull
	@Size(min=5, max=25, message="{password.size}")
	@Column(name="password")
	private String password;
	
	@NotNull
	@Size(min=2, max=30, message="{firstName.size}")
	@Column(name="first_name")
	private String firstName;
	
	@NotNull
	@Size(min=2, max=30, message="{lastName.size}")
	@Column(name="last_name")
	private String lastName;
	
	@NotNull
	@Email(message="{email.valid}")
	@Column(name="email")
	private String email;
	
	public Spitter() {}
	public Spitter(String username, String password, String firstName, String lastName, String email) {
		this(null, username, password, firstName, lastName, email);
	}
	public Spitter(Long id, String username, String password, String firstName, String lastName, String email) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, "firstName", "lastName", "username", "password", "email");
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "firstName", "lastName", "username", "password", "email");
	}
	
	@Override
	public String toString() {
		return firstName + " / " + lastName  + " / " + username + " / " + password + " / " + email;
	}
}
