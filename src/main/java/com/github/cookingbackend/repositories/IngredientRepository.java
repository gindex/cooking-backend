package com.github.cookingbackend.repositories;

import com.github.cookingbackend.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
