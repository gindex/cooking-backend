package com.github.cookingbackend.repositories;

import com.github.cookingbackend.model.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface StepRepository extends JpaRepository<Step, Long> {
}
