package com.formacaospring.dscatalog.dto;

import java.util.HashSet;
import java.util.Set;

import com.formacaospring.dscatalog.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO {
	private Long id;
	
	@Size(min = 5, max = 60, message = "Campo deve ter no minimo 5 caracteres e no máximo 60")
	@NotBlank(message = "Campo não pode ficar em branco")
	private String firstName;
	
	@Size(min = 5, max = 60, message = "Campo deve ter no minimo 5 caracteres e no máximo 60")
	@NotBlank(message = "Campo não pode ficar em branco")
	private String lastName;
	
	@Email(message = "Informar um e-mail válido!")
	private String email;
	
	Set<RoleDTO> roles = new HashSet<>();
	
	public UserDTO() {
	}

	public UserDTO(Long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;

	}
	
	public UserDTO(User entity) {
		id = entity.getId();
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		email = entity.getEmail();
		entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}
}
