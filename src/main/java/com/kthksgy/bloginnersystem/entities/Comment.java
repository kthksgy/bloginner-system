package com.kthksgy.bloginnersystem.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
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
public class Comment {
	@Id
	@GeneratedValue
	@Type(type = "uuid-char")
	@Column(length = 36)
	private UUID id;

	@NonNull
	@ManyToOne
	@JsonProperty("articleId")
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true)
	private Article article;

	@JsonProperty("articleTitle")
	public String getArticleTitle() {
		if (article == null) {
			return "";
		}
		return article.getTitle();
	}

	@NonNull
	@NotEmpty
	private String handlename;

	@NonNull
	@NotEmpty
	@Column(columnDefinition = "TEXT")
	private String content;

	private Boolean isPublished = false;

	@Column(updatable = false, columnDefinition = "DATETIME(0)", nullable = false)
	@CreationTimestamp
	private OffsetDateTime createdAt;

	@Column(columnDefinition = "DATETIME(0)", nullable = false)
	@UpdateTimestamp
	private OffsetDateTime updatedAt;

}
