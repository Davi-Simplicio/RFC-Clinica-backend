# SaúdeFem — Backend (RFCC Jaraguá do Sul)

API REST e camada de persistência do **SaúdeFem**, sistema de gestão clínica e prontuário ginecológico/oncológico da Rede Feminina de Combate ao Câncer (RFCC) de Jaraguá do Sul / SC.

> Etapa 1 — Concepção, requisitos, modelagem de dados e camada de persistência (JPA).

## Documentação

- [Documento de Concepção e Requisitos (Etapa 1)](docs/DOCUMENTACAO_ETAPA1.md)
- Diagrama de classes UML: [PNG](docs/diagrama_classes.png) · [PlantUML](docs/diagrama_classes.puml) · [Mermaid](docs/diagrama_classes.mmd)

## Stack

- Java 21, Spring Boot 3.3 (Spring Web, Spring Data JPA / Hibernate, Bean Validation)
- BCrypt (`spring-security-crypto`) para hash de senhas
- H2 (perfil `dev` e testes) e PostgreSQL 16 (perfil `prod`)
- Lombok, Maven Wrapper

## Estrutura

```
src/main/java/br/org/rfcc/saudefem
├── domain/entity   # 8 entidades JPA: Usuario, Paciente, Endereco, TelefoneContato,
│                   # DadosRecepcao, Anamnese, Procedimento, Agendamento
├── domain/enums    # RolePerfil, StatusAgendamento, PeriodoTurno, TipoTelefone
└── repository      # 8 interfaces JpaRepository
src/test/java/.../repository/PersistenciaJpaTest.java   # @DataJpaTest do mapeamento
```

## Como executar

Pré-requisito: JDK 21 (o Maven é baixado automaticamente pelo wrapper).

```bash
# compilar e rodar os testes
./mvnw clean verify

# subir a aplicação com H2 em memória (console em http://localhost:8080/h2-console)
./mvnw spring-boot:run

# subir com PostgreSQL
DB_URL=jdbc:postgresql://localhost:5432/saudefem_rfcc DB_USERNAME=postgres DB_PASSWORD=postgres \
  ./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

No Windows use `mvnw.cmd` no lugar de `./mvnw`.
