create table recipe
(
    id uuid
        constraint recipe_pk_constraint primary key
);

comment on table recipe is 'Contains all recipes.';
