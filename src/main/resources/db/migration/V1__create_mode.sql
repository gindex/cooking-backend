create table recipe
(
    id uuid constraint recipe_pk_constraint primary key,
    title text not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
);

comment on table recipe is 'Contains all recipes.';
