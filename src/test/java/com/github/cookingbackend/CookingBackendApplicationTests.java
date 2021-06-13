package com.github.cookingbackend;

import com.github.cookingbackend.repositories.RecipeRepository;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(scripts = {"classpath:sql/cleanup.sql", "classpath:sql/init.sql"})
class CookingBackendApplicationTests {

	@LocalServerPort
	int port;

	@Autowired
	private RecipeRepository repo;

	@BeforeAll
	void setUp() {
		RestAssured.port = port;
	}

	@Nested
	class Recipe {

		@Test
		void getRecipe_Success() {
			when()
				.get("/api/recipe/2")
				.then()
				.statusCode(HttpStatus.SC_OK)
				.log().all()
				.body("data.id", equalTo("2"))
				.body("data.type", equalTo("recipe"))
				.body("data.attributes.servings", equalTo(4))
				.body("data.attributes.duration", equalTo("PT30M"))
				.body("data.attributes.title", equalTo("Spaghetti"))
				.body("data.relationships.ingredients.data.id", hasItems("4", "5"))
				.body("data.relationships.notes.data.id", hasItem("2"))
				.body("data.relationships.steps.data.id", hasItem("3"))
				.body("data.relationships.tags.data.id", empty());
		}

		@Test
		void getRecipe_NotFound() {
			when()
				.get("/api/recipe/3")
				.then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
		}



	}



}
