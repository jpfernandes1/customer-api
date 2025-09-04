# 📡 API PARA CADASTRO DE CLIENTES

API RESTful para gestão de clientes pessoa física com operações CRUD completas, autenticação JWT, documentação Swagger e testes abrangentes.

## 🧠 Visão Técnica
Esta API foi desenvolvida seguindo os princípios RESTful e as melhores práticas do Spring Boot. A arquitetura em camadas (Controller-Service-Repository) foi escolhida para garantir separação de concerns, facilitando a manutenção e escalabilidade.

## 🤔 Por Que Esta Arquitetura?
- **Spring Data JPA**: Para abstração da camada de dados, aumentando produtividade e reduzindo boilerplate code
- **Spring Security + JWT**: Para autenticação stateless e segura, seguindo padrões industry-standard
- **DTO Pattern**: Para desacoplar a entidade de banco da representação da API
- **Swagger/OpenAPI**: Para documentação automática e interativa da API
- **Testes em Camadas**: Para garantir qualidade e facilitar refatoração

## ✨ Funcionalidades
- ✅ CRUD completo de clientes pessoa física
- ✅ Autenticação JWT com Spring Security
- ✅ Paginação e busca por atributos
- ✅ Cálculo automático de idade a partir da data de nascimento
- ✅ Validação de dados de entrada
- ✅ Documentação interativa com Swagger
- ✅ Cobertura abrangente de testes

## 🛠 Tecnologias e Dependências
- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.5** - Framework principal
- **Maven 3.9.9**  - automação e gestão de projetos
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização
- **JJWT** - Tokens JWT
- **PostgreSQL** - Banco de dados principal
- **H2 Database** - Banco para testes
- **Lombok** - Redução de boilerplate code
- **Springdoc OpenAPI** - Documentação Swagger
- **JUnit 5 & Mockito** - Testes unitários e de integração


## 🚀 Instalação e Execução

### 1. Clone o repositório
```bash
git clone https://github.com/jpfernandes1/customer-api.git
cd customer-api
```


### A. Com Docker

Pré-requisito: Docker Desktop instalado e configurado.

```bash
# Subir toda a stack (app + PostgreSQL)
docker-compose up -d

# Parar a stack
docker-compose down
```

##  B. Sem Docker

Pré-requisito: PgAdmin instalado e configurado


### 1. Crie um banco de dados

No postgres local, crie um banco de dados conforme configurações em `application-local.yml`.


### 2. Execute a API com o Maven

Executar aplicação:

```bash
# Executar aplicação
mvn spring-boot:run

# Executar com perfil de desenvolvimento
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```


## 🧪 Testes
```bash
# Executar todos os testes
mvn test

# Executar testes com cobertura
mvn test jacoco:report

# Executar apenas testes de integração
mvn test -Dtest=*IntegrationTest

# Executar apenas testes unitários
mvn test -Dtest=*Test
```

## 🌐Swagger
Para acessa o swagger clique no link:
[Swagger](http://localhost:8080/swagger-ui/index.html#)

Em seguida clique em `Auth` para visualizar o endpoint para fazer login.
Ao rodar o projeto pela primeira vez são automaticamente cadastrados
dois usuários, um com role ADMIN e outro com USER;

Use uma das credenciais abaixo para fazer login:

USER:  
email: user@email.com  
password: user123

ADMIN:  
email: admin@email.com  
password:admin123

nota: você também pode fazer seu proprio registro em Users/register

Feita a requisição de login, irá gerar um token.
Copie esse token, retorne ao inicio da pagina e clique na caixinha do
lado direito `Authorize`. Cole o token na caixa com a tag Value e clique em
Authorize.

Pronto, autenticação feita. Agora você já pode testar os endpoints à vontade.

## 🎨Processo criativo

Adotei o padrão de projeto MVC (Model-View-Controller) por ser uma abordagem 
que oferece separação clara de concerns, tem boa mantenabilidade e
escalabilidade, tem ótima integração com o spring boot e possibilida o teste isolado de cada camada.

### 🗂️ Entidades: 

* User (dados de autenticação)
* Customer (dados do cliente - comerciais)
* Address (dados de endereço do cliente)


### obs.: alguns foram os motivos de separar User de Customer:

Separação de concerns - Autenticação vs Dados comerciais
Segurança reforçada - Dados sensíveis isolados
Flexibilidade - Podem haver Users que não são Customers e vice-versa
Manutenibilidade - Alterações em uma entidade não afetam a outra
Performance - Consultas mais específicas por tabela

### obs 2.: uma das exigências do desafio é o calculo da idade a partir da data de nascimento.
Utilizei um `@Transient` para gerar o valor dinâmico e não persistido.

### 📋 DTOs

Utilizamos Records em vez de Class pela sua imutabilidade após criados,
garantindo segurança contra modificações acidentais.


### 📊 Repository

Boa parte dos métodos dos repositórios contam com "IgnoreCase", o que 
torna a query case-insensitive (ignora maiúsculas e minúsculas)

Foram criados métodos que retornam a lista paginada mas também corrida (caso eventual necessidade);

### 🔍 Mappers

Foram criados separados dos services para mante-los limpos e focados nas regras do negócio.

### 📖 Banco de dados

O versionamento do banco está sendo realizado via `Flyway`, que conta com a V1
para criação das tabelas e relacionamentos e V2 para popular essas tabelas.
Exceto a tabela user que precisa ser populada pela própria API para que seja
gerado o password criptografado. Para isto, foi criada a classe `UserDataLoader`
que recebe a anotação `@Component` (criando uma instância bean gerenciada) 
e implementa `CommandLineRunner`.
Qualquer bean que implementa CommandLineRunner tem seu método run() 
executado automaticamente após a aplicação estar totalmente inicializada.



Nota: Este projeto foi desenvolvido como parte de um desafio técnico, 
demonstrando boas práticas de desenvolvimento API RESTful com Spring Boot.