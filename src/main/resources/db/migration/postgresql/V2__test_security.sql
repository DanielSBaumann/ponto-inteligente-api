--Tabela para testes
CREATE TABLE usuario (
  id bigint NOT NULL,
  email varchar(255) NOT NULL,
  senha varchar(255) NOT NULL,
  perfil varchar(255) NOT NULL
);

ALTER TABLE usuario
  ADD PRIMARY KEY (id);

--Adcionando registros basicos
INSERT INTO empresa (id,razao_social,cnpj)
    VALUES(1,'dsb system','13703006000177');

--INSERT INTO funcionario(id,nome,email,senha,cpf,perfil,empresa_id)
--    VALUES(1,'usuario usuario','usuario@email.com','usuario','18036181401','ROLE_USUARIO',1),
--         (2,'admin admin','admin@email.com','admin','24802933606','ROLE_ADMIN',1);
