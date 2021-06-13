insert into recipe (id, created_at, updated_at, servings, duration, title)
values (1, '2021-01-01T00:00:01Z', '2020-01-01T00:00:01Z', 2, '1 hour', 'Pizza'),
       (2, '2020-01-01T00:00:01Z', '2020-01-01T00:00:01Z', 4, '30 minute', 'Spaghetti');

insert into note (id, created_at, updated_at, note, recipe_id)
values (1, '2020-01-01T00:00:01Z', '2020-01-01T00:00:01Z', 'It is a very nice recipe.', 1),
       (2, '2020-01-01T00:00:01Z', '2020-01-01T00:00:01Z', 'Best spaghetti ever!', 2);

insert into ingredient (id, created_at, updated_at, description, recipe_id)
values (1, '2020-01-01T00:00:01Z', '2020-01-01T00:00:01Z', '500g flour', 1),
       (2, '2020-01-01T00:00:01Z', '2020-01-01T00:00:01Z', '100g salami', 1),
       (3, '2020-01-01T00:00:01Z', '2020-01-01T00:00:01Z', '100g cheese', 1),
       (4, '2020-01-01T00:00:01Z', '2020-01-01T00:00:01Z', '500g spaghetti', 2),
       (5, '2020-01-01T00:00:01Z', '2020-01-01T00:00:01Z', '1 liter water', 2);

insert into step (id, created_at, updated_at, description, title, recipe_id)
values (1, '2020-01-01T00:00:01Z', '2020-01-01T00:00:01Z', 'Prepare', '1 Step', 1),
       (2, '2020-01-01T00:00:01Z', '2020-01-01T00:00:01Z', 'Cook', '2 Step', 1),
       (3, '2020-01-01T00:00:01Z', '2020-01-01T00:00:01Z', 'Cook', '1 Step', 2);

insert into tag (id, created_at, updated_at, tag, recipe_id)
values (1, '2020-01-01T00:00:01Z', '2020-01-01T00:00:01Z', 'italian', 1),
       (2, '2020-01-01T00:00:01Z', '2020-01-01T00:00:01Z', 'quick', 1);
