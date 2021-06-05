package com.github.cookingbackend.model;

import com.yahoo.elide.annotation.Include;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Include(name = "recipe")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Recipe {

	@Id
	@GeneratedValue
	@NotNull
	UUID id;

}
