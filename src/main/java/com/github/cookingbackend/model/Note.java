package com.github.cookingbackend.model;

import com.yahoo.elide.annotation.Include;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Include(name = "note", rootLevel = false)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Note extends BaseEntity{

	@NotEmpty
	@Column(nullable = false)
	private String note;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipe_id")
	private Recipe recipe;
}
