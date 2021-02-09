package com.github.mikesafonov.operatorbot.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "full_name")
	private String fullName;
	@Column(name = "telegram_id")
	private String telegramId;
	@Column(name = "status")
	private Status status;
	@Column(name = "role")
	private Role role;
}
