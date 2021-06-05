package com.github.cookingbackend.repositories;

import com.github.cookingbackend.model.Recipe;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RecipeRepository extends JpaRepository<Recipe, UUID> {
}
