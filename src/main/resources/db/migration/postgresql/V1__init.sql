--Initial Script
CREATE TABLE empresa (
  id bigint NOT NULL,
  cnpj varchar(255) NOT NULL UNIQUE,
  data_atualizacao date NOT NULL DEFAULT CURRENT_DATE,
  data_criacao date NOT NULL DEFAULT CURRENT_DATE,
  razao_social varchar(255) NOT NULL
);

CREATE TABLE funcionario (
  id bigint NOT NULL ,
  cpf varchar(255) NOT NULL UNIQUE,
  data_atualizacao date NOT NULL DEFAULT CURRENT_DATE,
  data_criacao date NOT NULL DEFAULT CURRENT_DATE,
  email varchar(255) NOT NULL UNIQUE,
  nome varchar(255) NOT NULL,
  perfil varchar(255) NOT NULL,
  qtd_horas_almoco float DEFAULT NULL,
  qtd_horas_trabalho_dia float DEFAULT NULL,
  senha varchar(255) NOT NULL,
  valor_hora decimal(19,2) DEFAULT NULL,
  empresa_id bigint DEFAULT NULL
);

CREATE TABLE lancamento (
  id bigint NOT NULL,
  data date NOT NULL DEFAULT CURRENT_DATE,
  data_atualizacao date NOT NULL DEFAULT CURRENT_DATE,
  data_criacao date NOT NULL DEFAULT CURRENT_DATE,
  descricao varchar(255) DEFAULT NULL,
  localizacao varchar(255) DEFAULT NULL,
  tipo varchar(255) NOT NULL,
  funcionario_id bigint DEFAULT NULL
);

--
-- Indexes for table `empresa`
--
ALTER TABLE empresa
  ADD PRIMARY KEY (id);

--
-- Indexes for table `funcionario`
--
ALTER TABLE funcionario
  ADD PRIMARY KEY (id);

--
-- Indexes for table `lancamento`
--
ALTER TABLE lancamento
  ADD PRIMARY KEY (id);

--
-- Constraints for table `funcionario`
--
ALTER TABLE funcionario
  ADD CONSTRAINT FK4cm1kg523jlopyexjbmi6y54j FOREIGN KEY (empresa_id) REFERENCES empresa (id);

--
-- Constraints for table `lancamento`
--
ALTER TABLE lancamento
  ADD CONSTRAINT FK46i4k5vl8wah7feutye9kbpi4 FOREIGN KEY (funcionario_id) REFERENCES funcionario (id);