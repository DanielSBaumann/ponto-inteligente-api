CREATE TABLE usuario (
  id bigint NOT NULL,
  email varchar(255) NOT NULL,
  senha varchar(255) NOT NULL,
  perfil varchar(255) NOT NULL
);

ALTER TABLE usuario
  ADD PRIMARY KEY (id);