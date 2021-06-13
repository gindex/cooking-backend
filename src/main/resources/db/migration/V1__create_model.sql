create table recipe
(
    id         bigint
        constraint recipe_pk primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,

    title      text                     not null,
    time       interval,
    servings   int
);

comment on table recipe is 'Describes the recipe entity.';

create table ingredient
(
    id          bigint
        constraint ingredient_pk primary key,
    created_at  timestamp with time zone not null,
    updated_at  timestamp with time zone not null,

    recipe_id   bigint
        constraint recipe_id_fk references recipe (id) on update cascade on delete cascade,

    description text                     not null
);

comment on table ingredient is 'Describes ingredient of the recipe.';

create table note
(
    id         bigint
        constraint note_pk primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,

    recipe_id  bigint
        constraint recipe_id_fk references recipe (id) on update cascade on delete cascade,

    note       text                     not null
);

comment on table note is 'Describes note of the recipe.';

create table step
(
    id          bigint
        constraint step_pk primary key,
    created_at  timestamp with time zone not null,
    updated_at  timestamp with time zone not null,

    recipe_id   bigint
        constraint recipe_id_fk references recipe (id) on update cascade on delete cascade,

    title       text                     not null,
    description text                     not null
);

comment on table step is 'Describes step of the recipe.';

create table tag
(
    id         bigint
        constraint tag_pk primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,

    recipe_id  bigint
        constraint recipe_id_fk references recipe (id) on update cascade on delete cascade,

    tag        text                     not null
);

comment on table tag is 'Describes tag of the recipe.';

create sequence hibernate_sequence;

alter sequence hibernate_sequence owner to cooking;
