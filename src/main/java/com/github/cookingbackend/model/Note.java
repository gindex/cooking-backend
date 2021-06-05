package com.github.cookingbackend.model;

import com.yahoo.elide.annotation.Include;
import javax.persistence.Entity;
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
	private String note;
}
