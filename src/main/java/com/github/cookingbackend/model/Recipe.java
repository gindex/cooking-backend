package com.github.cookingbackend.model;

import com.yahoo.elide.annotation.Include;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Include(name = "recipe")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Recipe extends BaseEntity {

	@NotEmpty
	private String title;

}
