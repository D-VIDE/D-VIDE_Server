drop table if exists post CASCADE;
create table post
(
    postId bigint generated by default as identity,
    title varchar(255),
    content varchar(255),
    primary key (postId )
);