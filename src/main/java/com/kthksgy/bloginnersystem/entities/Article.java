package com.kthksgy.bloginnersystem.entities;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Article {
	@Id
	@GeneratedValue
	@Type(type = "uuid-char")
	@Column(length = 36)
	private UUID id;

	@NonNull
	@ManyToOne
	@JsonProperty("username")
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
    @JsonIdentityReference(alwaysAsId = true)
	private User user;

	@NonNull
	@Column(length = 255)
	private String title;

	@NonNull
	@Column(length = 4095)
	private String overview;

	@NonNull
	@Column(length = 4095)
	private String thumbnail;

	@NonNull
	@Column(columnDefinition = "MEDIUMTEXT", nullable = false)
	private String content;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "article")
	private List<Comment> comments;

	@Column(updatable = false, columnDefinition = "DATETIME(0)", nullable = false)
	@CreationTimestamp
	private OffsetDateTime createdAt;

	@Column(columnDefinition = "DATETIME(0)", nullable = false)
	@UpdateTimestamp
	private OffsetDateTime updatedAt;
}
