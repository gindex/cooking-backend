package com.github.cookingbackend.model;

import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import com.yahoo.elide.annotation.Include;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.TypeDef;

@EqualsAndHashCode(callSuper = true)
@Include(name = "recipe")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@TypeDef(
	typeClass = PostgreSQLIntervalType.class,
	defaultForType = Duration.class
)
public class Recipe extends BaseEntity {

	@NotEmpty
	@Column(nullable = false)
	private String title;

	@Column(
		name = "time",
		columnDefinition = "interval"
	)
	private Duration time;

	private int servings;

	@OneToMany(
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	@JoinColumn(name = "recipe_id")
	@Fetch(FetchMode.SUBSELECT)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<Step> steps;

	@OneToMany(
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	@JoinColumn(name = "recipe_id")
	@Fetch(FetchMode.SUBSELECT)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<Ingredient> ingredients;

	@OneToMany(
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	@JoinColumn(name = "recipe_id")
	@Fetch(FetchMode.SUBSELECT)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<Tag> tags;

	@OneToMany(
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	@JoinColumn(name = "recipe_id")
	@Fetch(FetchMode.SUBSELECT)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<Note> notes;
}
