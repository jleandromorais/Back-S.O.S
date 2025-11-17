# 🛡️ Back-S.O.S: API de Denúncias Anônimas

Repositório da API do projeto S.O.S Escola - uma plataforma de denúncias anônimas para escolas, promovendo um ambiente escolar mais seguro e acolhedor.
---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Entregas](#-entregas)
  - [Entrega 01](#-entrega-01-0909)
  - [Entrega 02](#-entrega-02)
  - [Entrega 03](#-entrega-03)
  - [Entrega 04](#-entrega-04)
- [Equipe](#-equipe)

---

## 💡 Sobre o Projeto

O **S.O.S Escola** é uma plataforma de denúncias anônimas desenvolvida para promover um ambiente escolar mais seguro. O sistema permite que alunos, professores e responsáveis relatem situações de bullying, violência, assédio e outros problemas de forma confidencial, garantindo a proteção da identidade do denunciante e facilitando a intervenção da gestão escolar.

---
## 🚀 Como Rodar o Projeto

Esta seção detalha os passos necessários para configurar e executar a API **Back-S.O.S** em seu ambiente local, seja utilizando o Maven diretamente ou através de contêineres Docker.

### Pré-requisitos

Para rodar o projeto, você precisará ter instalado em sua máquina:

*   **Java Development Kit (JDK)**: Versão 17 ou superior.
*   **Apache Maven**: Versão 3.6.3 ou superior (necessário apenas se não for usar o Maven Wrapper `mvnw`).
*   **Docker e Docker Compose** (Opcional, para execução containerizada).

---

### Opção 1: Execução Local com Maven

Siga os passos abaixo para clonar o repositório e executar a aplicação diretamente em sua máquina.

#### 1. Clonar o Repositório

Abra o terminal e clone o projeto:

```bash
git clone https://github.com/jleandromorais/Back-S.O.S.git
cd Back-S.O.S
```

#### 2. Compilar o Projeto

Utilize o Maven Wrapper (`mvnw`) para compilar o projeto e baixar todas as dependências necessárias.

```bash
# No Linux/macOS
./mvnw clean install

# No Windows (usando o prompt de comando)
mvnw clean install
```

#### 3. Executar a Aplicação

Após a compilação bem-sucedida, você pode executar a aplicação a partir do arquivo JAR gerado ou usando o comando `spring-boot:run`.

**Método A: Executar via Spring Boot Plugin**

```bash
# No Linux/macOS
./mvnw spring-boot:run

# No Windows
mvnw spring-boot:run
```

**Método B: Executar o JAR Gerado**

O arquivo JAR executável será gerado no diretório `target/`.

```bash
java -jar target/back-sos-0.0.1-SNAPSHOT.jar
```

A API estará acessível em `http://localhost:8080` (porta padrão do Spring Boot, a menos que configurada de forma diferente).

---

### Opção 2: Execução Containerizada com Docker

Para uma execução mais isolada e padronizada, você pode utilizar o `Dockerfile` fornecido.

#### 1. Clonar o Repositório

```bash
git clone https://github.com/jleandromorais/Back-S.O.S.git
cd Back-S.O.S
```

#### 2. Construir a Imagem Docker

Utilize o `Dockerfile` para construir a imagem da aplicação. O nome da imagem será `back-sos`.

```bash
docker build -t back-sos .
```

#### 3. Executar o Contêiner

Execute a imagem construída, mapeando a porta 8080 do contêiner para a porta 8080 da sua máquina.

```bash
docker run -p 8080:8080 back-sos
```

A API estará acessível em `http://localhost:8080`.

---

### Teste de Funcionamento

Para verificar se a API está rodando corretamente, você pode tentar acessar um endpoint de teste (se houver) ou a documentação Swagger/OpenAPI (se configurada).

**Exemplo de Acesso (se o Swagger estiver configurado):**

```
http://localhost:8080/swagger-ui.html
```

**Nota:** O projeto utiliza persistência em memória (conforme mencionado na **Entrega 02**), o que significa que os dados serão perdidos a cada reinicialização da aplicação. Para persistência de dados, seria necessário configurar um banco de dados externo.

---

## 📦 Entregas

### 📌 Entrega 01

#### ✅ Histórias de Usuário
- **Documentação:** [Histórias em BDD](historias-bdd.md)
- Especificações completas das funcionalidades utilizando o formato Behavior-Driven Development

#### 🎨 Protótipo (Lo-Fi)
- **Design:** [Protótipo no Figma](https://www.figma.com/design/LttrqgGPeTN1Wa9hu6Iidk/S.O.S-Escola?node-id=0-1)
- **Apresentação:** [Vídeo demonstrativo no Drive](https://drive.google.com/file/d/1OaMrHyjbxgaxI05TlfGhUkSIQZyMPNBp/view)

---

### 📌 Entrega 02

#### ✅ Histórias Implementadas
- HU 01 Cadastro de Denúncia Confidencial
- HU 02 Visualização de Denúncias pela Equipe Pedagógica
- Persistência realizada em memória

#### 🔄 Ambiente de Versionamento
- **Commits frequentes** (no mínimo semanais)
- Commits realizados direto no main
- [Repositório GitHub](https://github.com/jleandromorais/Back-S.O.S/tree/leandro-branch)

#### 🐛 Issue/Bug Tracker
- **Issue tracker atualizado** e usado em todas as semanas da entrega
- <img width="1476" height="394" alt="image" src="https://github.com/user-attachments/assets/26216e2a-f6e5-4571-9e67-c00a4ec89bca" />


#### 🎬 Screencast do Sistema
- **Vídeo demonstrativo:** [Vídeo no YouTube](https://youtu.be/1msdiDh64T8?si=UcwZ7tIZOldYyBha)
---

### 📌 Entrega 03

#### ✅ Histórias Implementadas
- HU 03 Geração de Protocolo de Denúnica
- HU 04 Registro e Login Seguro para Usuários

#### 🔄 Ambiente de Versionamento
- **Commits frequentes**
- [Repositório GitHub](https://github.com/jleandromorais/Back-S.O.S/tree/leandro-branch)

#### 🏗️ Arquitetura
- **Refatoração do código**
<img width="313" height="835" alt="image" src="https://github.com/user-attachments/assets/d2fe5698-83cf-43de-9c8f-f091c6e565fa" />


#### 🎬 Screencast do Sistema
- **Vídeo demonstrativo** do sistema funcionando
- Disponível no YouTube com áudio ou legenda
- [Link do screencast](https://youtu.be/upnG4-8L0J0)

#### 🐛 Issue/Bug Tracker
- **Issue tracker atualizado**
- [Ver issues no GitHub](https://github.com/jleandromorais/Back-S.O.S/issues)

#### 🧪 Testes Automatizados
- **Testes implementados**
- Relatório de Testes (https://github.com/jleandromorais/Back-S.O.S/blob/leandro-branch/%F0%9F%A7%AA%20Relat%C3%B3rio%20de%20Testes%20Automatizados%20-%20S.O.S%20Escola.md)
---

### 📌 Entrega 04

### ✅ Histórias Implementadas
- HU 07 Visualização de Detalhes da Denúncia pela Equipe Pedagógica
- HU 08 Atualização do Status da Denúncia pela Equipe Pedagógica
- HU 09 Filtro e Pesquisa de Denúncias por tipo, data e status

### 🔄 Ambiente de Versionamento
- **Commits frequentes**
- [Repositório GitHub](https://github.com/jleandromorais/Back-S.O.S/tree/main)

#### 🎬 Screencast do Sistema
- **Vídeo demonstrativo** do sistema funcionando
- Disponível no YouTube com legenda
- [Link do screencast](https://youtu.be/4mKWjx4NqUQ)
  
#### 🐛 Issue/Bug Tracker
- **Issue tracker atualizado**
- [Ver issues no GitHub](https://github.com/jleandromorais/Back-S.O.S/issues)
- <img width="1213" height="195" alt="image" src="https://github.com/user-attachments/assets/ac09ce2b-f98d-40cc-8f9c-a82608818bcd" />

  
#### 🧪 Testes Automatizados
- **Testes implementados**
- Relatório de Testes (https://github.com/jleandromorais/Back-S.O.S/blob/leandro-branch/%F0%9F%A7%AA%20Relat%C3%B3rio%20de%20Testes%20Automatizados%20-%20S.O.S%20Escola.md)
---

## 👥 Equipe

- **Enzo Antuna**
- **Gabriel Souza**
- **Levi Moraes**
- **Leandro Morais**
- **Kayky Dias**

---

**Desenvolvido com 💙 pela equipe S.O.S Escola**
