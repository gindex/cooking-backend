insert into recipe (id, created_at, updated_at, servings, time, title)
values (1, '2021-01-01 00:00:01', '2021-01-01 00:00:01', 2, '1 hour', 'Pizza'),
       (2, '2020-01-01 00:00:01', null, 4, '30 minute', 'Spaghetti');

insert into note (id, created_at, updated_at, note, recipe_id)
values (1, '2021-01-01 00:00:01', '2021-01-01 00:00:01', 'It is a very nice recipe.', 1),
       (1, '2021-01-01 00:00:01', null, 'Best spaghetti ever!', 2);

insert into ingredient (id, created_at, updated_at, description, recipe_id)
values (1, '2021-01-01 00:00:01', '2021-01-01 00:00:01', '500g flour', 1),
       (2, '2021-01-01 00:00:01', null, '100g salami', 1),
       (3, '2021-01-01 00:00:01', null, '100g cheese', 1),
       (1, '2021-01-01 00:00:01', null, '500g spaghetti', 2),
       (2, '2021-01-01 00:00:01', null, '1 liter water', 2);

insert into step (id, created_at, updated_at, description, title, recipe_id)
values (1, '2021-01-01 00:00:01', '2021-01-01 00:00:01', 'Prepare', '1 Step', 1),
       (2, '2021-01-01 00:00:01', null, 'Cook', '2 Step', 1),
       (1, '2021-01-01 00:00:01', null, 'Cook', '1 Step', 2);

insert into tag (id, created_at, updated_at, tag, recipe_id)
values (1, '2021-01-01 00:00:01', '2021-01-01 00:00:01', 'italian', 1),
       (2, '2021-01-01 00:00:01', '2021-01-01 00:00:01', 'quick', 1);
