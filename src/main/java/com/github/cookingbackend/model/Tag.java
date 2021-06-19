package com.github.cookingbackend.model;

import com.github.cookingbackend.elide.Prefab;
import com.yahoo.elide.annotation.Include;
import com.yahoo.elide.annotation.UpdatePermission;
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
@Include(name = "tag", rootLevel = false)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tag extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipe_id")
	@UpdatePermission(expression = Prefab.NONE)
	private Recipe recipe;

	@NotEmpty
	private String tag;

}
