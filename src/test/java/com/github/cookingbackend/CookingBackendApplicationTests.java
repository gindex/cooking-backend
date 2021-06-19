package com.github.cookingbackend;

import com.github.cookingbackend.model.Ingredient;
import com.github.cookingbackend.model.Note;
import com.github.cookingbackend.model.Step;
import com.github.cookingbackend.model.Tag;
import com.github.cookingbackend.repositories.IngredientRepository;
import com.github.cookingbackend.repositories.NoteRepository;
import com.github.cookingbackend.repositories.RecipeRepository;
import com.github.cookingbackend.repositories.StepRepository;
import com.github.cookingbackend.repositories.TagRepository;
import com.yahoo.elide.spring.controllers.JsonApiController;
import io.restassured.RestAssured;
import java.time.Duration;
import java.util.List;
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

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(scripts = {"classpath:sql/cleanup.sql", "classpath:sql/init.sql"})
class CookingBackendApplicationTests {

	@LocalServerPort
	int port;

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private IngredientRepository ingredientRepository;

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private StepRepository stepRepository;

	@Autowired
	private TagRepository tagRepository;

	@BeforeAll
	void setUp() {
		RestAssured.port = port;
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.config().getEncoderConfig().defaultContentCharset("UTF-8");
	}

	@Test
	void badPath_ClientError() {
		given()
			.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
			.accept(JsonApiController.JSON_API_CONTENT_TYPE)
			.body("""
				{
				  "data": [
				    {
				      "type": "recipe",
				      "attributes": {
				        "title": "My unique recipe"
				      }
				    }
				  ]
				}
				""")
			.when().post("/api/unknown")
			.then()
			.statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	void malformedJson_ClientError() {
		given()
			.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
			.accept(JsonApiController.JSON_API_CONTENT_TYPE)
			.body("not a json")
			.when().post("/api/recipe")
			.then()
			.statusCode(HttpStatus.SC_LOCKED);
	}

	@Nested
	class RecipeTest {

		@Test
		void getRecipe_Success() {
			when()
				.get("/api/recipe/2")
				.then()
				.statusCode(HttpStatus.SC_OK)
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

		@Test
		void createRecipe_Success() {
			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
					{
					  "data": [
					    {
					      "type": "recipe",
					      "attributes": {
					        "title": "My unique recipe",
					        "duration": "PT15M",
					        "servings": "3"
					      }
					    }
					  ]
					}
					""")
				.when().post("/api/recipe")
				.then()
				.statusCode(HttpStatus.SC_CREATED);

			var createdRecipe = recipeRepository.findAll().stream()
				.filter(r -> r.getTitle().equalsIgnoreCase("My unique recipe"))
				.findAny().orElseThrow();

			assertEquals("My unique recipe", createdRecipe.getTitle());
			assertEquals(Duration.ofMinutes(15), createdRecipe.getDuration());
			assertEquals(3, createdRecipe.getServings());
		}

		@Test
		void updateRecipe_Success() {
			var updatedRecipe = recipeRepository.findById(2L).orElseThrow();
			assertEquals("Spaghetti", updatedRecipe.getTitle());

			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
					{
					  "data": {
						  "type": "recipe",
						  "id": "2",
						  "attributes": {
							"title": "Best Spaghetti"
						  }
						}
					}
					""")
				.when().patch("/api/recipe/2")
				.then()
				.statusCode(HttpStatus.SC_NO_CONTENT);

			updatedRecipe = recipeRepository.findById(2L).orElseThrow();
			assertEquals("Best Spaghetti", updatedRecipe.getTitle());
		}

		@Test
		void createRecipe_ClientError() {
			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
					{
					  "data": [
					    {
					      "type": "recipe",
					      "attributes": {
					        "title": "My unique recipe",
					        "unknown_field": "my field"
					      }
					    }
					  ]
					}
					""")
				.when().post("/api/recipe")
				.then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
		}
	}

	@Nested
	class IngredientTest {

		@Test
		void getIngredient_Success() {
			when()
				.get("/api/recipe/1/ingredients/1")
				.then()
				.statusCode(HttpStatus.SC_OK)
				.body("data.id", equalTo("1"))
				.body("data.attributes.description", equalTo("500g flour"));
		}

		@Test
		void getIngredientRootLevel_ClientError() {
			when()
				.get("/api/ingredient/1")
				.then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
		}

		@Test
		void createIngredient_Success() {
			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
						{
						  "data": [
							{
							  "type": "ingredient",
							  "attributes": {
								"description": "1 Mozzarella"
							  }
							}
						  ]
						}
					""")
				.when().post("/api/recipe/1/ingredients")
				.then()
				.statusCode(HttpStatus.SC_CREATED);

			var ingredients = ingredientRepository.findAll().stream()
				.filter(ingredient -> ingredient.getRecipe().getTitle().equalsIgnoreCase("Pizza"))
				.toList();

			assertEquals(4, ingredients.size());
			List<String> descriptions = ingredients.stream().map(Ingredient::getDescription).toList();
			assertThat(descriptions, hasItem("1 Mozzarella"));
		}

		@Test
		void updateIngredient_Success() {
			var updatedIngredient = ingredientRepository.findById(1L).orElseThrow();
			assertEquals("500g flour", updatedIngredient.getDescription());

			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
					{
					  "data": {
						  "type": "ingredient",
						  "id": "1",
						  "attributes": {
							"description": "555g flour"
						  }
						}
					}
					""")
				.when().patch("/api/recipe/1/ingredients/1")
				.then()
				.statusCode(HttpStatus.SC_NO_CONTENT);

			updatedIngredient = ingredientRepository.findById(1L).orElseThrow();
			assertEquals("555g flour", updatedIngredient.getDescription());
		}

		@Test
		void moveIngredientToOtherRecipeClientError() {
			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
						{
						  "data": {
							"type": "ingredient",
							"id": "1",
							"relationships": {
							  "recipe": {
								"data": {
								  "type": "recipe",
								  "id": "2"
								}
							  }
							}
						  }
						}
					""")
				.when().patch("/api/recipe/1/ingredients/1")
				.then()
				.statusCode(HttpStatus.SC_FORBIDDEN);
		}
	}

	@Nested
	class NoteTest {

		@Test
		void getNote_Success() {
			when()
				.get("/api/recipe/1/notes/1")
				.then()
				.statusCode(HttpStatus.SC_OK)
				.body("data.id", equalTo("1"))
				.body("data.attributes.note", equalTo("It is a very nice recipe."));
		}

		@Test
		void getNoteRootLevel_ClientError() {
			when()
				.get("/api/note/1")
				.then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
		}

		@Test
		void createNote_Success() {
			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
						{
						  "data": [
							{
							  "type": "note",
							  "attributes": {
								"note": "My new note."
							  }
							}
						  ]
						}
					""")
				.when().post("/api/recipe/1/notes")
				.then()
				.statusCode(HttpStatus.SC_CREATED);

			var notes = noteRepository.findAll().stream()
				.filter(note -> note.getRecipe().getTitle().equalsIgnoreCase("Pizza"))
				.toList();

			assertEquals(2, notes.size());
			List<String> noteTexts = notes.stream().map(Note::getNote).toList();
			assertThat(noteTexts, hasItem("My new note."));
		}

		@Test
		void updateNote_Success() {
			var updatedNote = noteRepository.findById(1L).orElseThrow();
			assertEquals("It is a very nice recipe.", updatedNote.getNote());

			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
					{
					  "data": {
						  "type": "note",
						  "id": "1",
						  "attributes": {
							"note": "It's NOT that nice recipe."
						  }
						}
					}
					""")
				.when().patch("/api/recipe/1/notes/1")
				.then()
				.statusCode(HttpStatus.SC_NO_CONTENT);

			updatedNote = noteRepository.findById(1L).orElseThrow();
			assertEquals("It's NOT that nice recipe.", updatedNote.getNote());
		}

		@Test
		void moveNoteToOtherRecipeClientError() {
			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
						{
						  "data": {
							"type": "note",
							"id": "1",
							"relationships": {
							  "recipe": {
								"data": {
								  "type": "recipe",
								  "id": "2"
								}
							  }
							}
						  }
						}
					""")
				.when().patch("/api/recipe/1/notes/1")
				.then()
				.statusCode(HttpStatus.SC_FORBIDDEN);
		}
	}

	@Nested
	class StepTest {

		@Test
		void getStep_Success() {
			when()
				.get("/api/recipe/1/steps/1")
				.then()
				.statusCode(HttpStatus.SC_OK)
				.body("data.id", equalTo("1"))
				.body("data.attributes.title", equalTo("1 Step"))
				.body("data.attributes.description", equalTo("Prepare"));
		}

		@Test
		void getStepRootLevel_ClientError() {
			when()
				.get("/api/step/1")
				.then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
		}

		@Test
		void createStep_Success() {
			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
						{
						  "data": [
							{
							  "type": "step",
							  "attributes": {
								"title": "3 Step",
								"description": "Enjoy"
							  }
							}
						  ]
						}
					""")
				.when().post("/api/recipe/1/steps")
				.then()
				.statusCode(HttpStatus.SC_CREATED);

			var steps = stepRepository.findAll().stream()
				.filter(note -> note.getRecipe().getTitle().equalsIgnoreCase("Pizza"))
				.toList();

			assertEquals(3, steps.size());

			List<String> titles = steps.stream().map(Step::getTitle).toList();
			assertThat(titles, hasItem("3 Step"));

			List<String> descriptions = steps.stream().map(Step::getDescription).toList();
			assertThat(descriptions, hasItem("Enjoy"));
		}

		@Test
		void updateStep_Success() {
			var step = stepRepository.findById(1L).orElseThrow();
			assertEquals("Prepare", step.getDescription());

			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
					{
					  "data": {
						  "type": "step",
						  "id": "1",
						  "attributes": {
							"description": "Wash vegetables"
						  }
						}
					}
					""")
				.when().patch("/api/recipe/1/steps/1")
				.then()
				.statusCode(HttpStatus.SC_NO_CONTENT);

			step = stepRepository.findById(1L).orElseThrow();
			assertEquals("Wash vegetables", step.getDescription());
		}

		@Test
		void moveStepToOtherRecipeClientError() {
			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
						{
						  "data": {
							"type": "step",
							"id": "1",
							"relationships": {
							  "recipe": {
								"data": {
								  "type": "recipe",
								  "id": "2"
								}
							  }
							}
						  }
						}
					""")
				.when().patch("/api/recipe/1/steps/1")
				.then()
				.statusCode(HttpStatus.SC_FORBIDDEN);
		}
	}

	@Nested
	class TagTest {

		@Test
		void getTag_Success() {
			when()
				.get("/api/recipe/1/tags/1")
				.then()
				.statusCode(HttpStatus.SC_OK)
				.body("data.id", equalTo("1"))
				.body("data.attributes.tag", equalTo("italian"));
		}

		@Test
		void getTagRootLevel_ClientError() {
			when()
				.get("/api/tag/1")
				.then()
				.statusCode(HttpStatus.SC_NOT_FOUND);
		}

		@Test
		void createTag_Success() {
			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
						{
						  "data": [
							{
							  "type": "tag",
							  "attributes": {
								"tag": "family"
							  }
							}
						  ]
						}
					""")
				.when().post("/api/recipe/1/tags")
				.then()
				.statusCode(HttpStatus.SC_CREATED);

			var tags = tagRepository.findAll().stream()
				.filter(note -> note.getRecipe().getTitle().equalsIgnoreCase("Pizza"))
				.toList();

			assertEquals(3, tags.size());

			List<String> tagStrings = tags.stream().map(Tag::getTag).toList();
			assertThat(tagStrings, hasItem("family"));
		}

		@Test
		void updateTag_Success() {
			var tag = tagRepository.findById(1L).orElseThrow();
			assertEquals("italian", tag.getTag());

			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
					{
					  "data": {
						  "type": "tag",
						  "id": "1",
						  "attributes": {
							"tag": "american"
						  }
						}
					}
					""")
				.when().patch("/api/recipe/1/tags/1")
				.then()
				.statusCode(HttpStatus.SC_NO_CONTENT);

			tag = tagRepository.findById(1L).orElseThrow();
			assertEquals("american", tag.getTag());
		}

		@Test
		void moveTagToOtherRecipeClientError() {
			given()
				.contentType(JsonApiController.JSON_API_CONTENT_TYPE)
				.accept(JsonApiController.JSON_API_CONTENT_TYPE)
				.body("""
						{
						  "data": {
							"type": "tag",
							"id": "1",
							"relationships": {
							  "recipe": {
								"data": {
								  "type": "recipe",
								  "id": "2"
								}
							  }
							}
						  }
						}
					""")
				.when().patch("/api/recipe/1/tags/1")
				.then()
				.statusCode(HttpStatus.SC_FORBIDDEN);
		}
	}
}
