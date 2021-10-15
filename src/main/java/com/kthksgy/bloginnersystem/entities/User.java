package com.kthksgy.bloginnersystem.entities;

import java.time.OffsetDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@NonNull
	private String username;

	@JsonIgnore
	@Column(nullable = false)
	@NonNull
	private String password;

	@Column(nullable = false)
	private Integer restriction = -1;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "user")
	private List<Article> articles;

	@Column(updatable = false, columnDefinition = "DATETIME(0)", nullable = false)
	@CreationTimestamp
	private OffsetDateTime createdAt;

	@Column(columnDefinition = "DATETIME(0)", nullable = false)
	@UpdateTimestamp
	private OffsetDateTime updatedAt;
}
