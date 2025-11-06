# 🧪 Guia de Testes - S.O.S Escola

Este documento fornece instruções detalhadas sobre como executar e entender os testes automatizados do projeto S.O.S Escola.

---

## 📋 Pré-requisitos

- **Java 21** ou superior
- **Maven** (incluído no projeto via `mvnw`)
- **Git** para clonar o repositório

---

## 🚀 Execução Rápida

### 1. Clonar o repositório
```bash
git clone https://github.com/jleandromorais/Back-S.O.S.git
cd Back-S.O.S
```

### 2. Fazer checkout da branch com os testes
```bash
git checkout origin/leandro-branch
```

### 3. Executar todos os testes
```bash
./mvnw test
```

**Resultado esperado:**
```
[INFO] Tests run: 28, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 📊 Tipos de Testes Implementados

### 🔹 Testes de Unidade (9 testes)
**Localização:** `src/test/java/br/com/sosescolar/service/DenunciaServiceTest.java`

Testam a lógica de negócio isoladamente, sem dependências externas.

**Executar apenas testes de unidade:**
```bash
./mvnw test -Dtest=DenunciaServiceTest
```

**Exemplos de testes:**
- ✅ Criação de denúncia confidencial
- ✅ Criação de denúncia identificada
- ✅ Validação de campos obrigatórios
- ✅ Geração de protocolo único
- ✅ Busca por protocolo

---

### 🔹 Testes de Integração (9 testes)
**Localização:** `src/test/java/br/com/sosescolar/controller/DenunciaControllerIntegrationTest.java`

Testam os endpoints da API com o contexto Spring completo.

**Executar apenas testes de integração:**
```bash
./mvnw test -Dtest=DenunciaControllerIntegrationTest
```

**Exemplos de testes:**
- ✅ POST /api/denuncias retorna 201 CREATED
- ✅ GET /api/denuncias/{protocolo} retorna 200 OK
- ✅ GET retorna 404 NOT_FOUND para protocolo inexistente
- ✅ POST retorna 400 BAD_REQUEST para dados inválidos
- ✅ Validação de todos os tipos de denúncia

---

### 🔹 Testes End-to-End (7 testes)
**Localização:** `src/test/java/br/com/sosescolar/e2e/SistemaE2ETest.java`

Testam fluxos completos do sistema, simulando o uso real.

**Executar apenas testes E2E:**
```bash
./mvnw test -Dtest=SistemaE2ETest
```

**Exemplos de testes:**
- ✅ Fluxo completo: aluno cria denúncia e recebe protocolo
- ✅ Fluxo completo: denúncia identificada com nome
- ✅ Fluxo de erro: validação de campos obrigatórios
- ✅ Fluxo: múltiplas denúncias simultâneas
- ✅ Fluxo: validação do formato do protocolo

---

## 🎯 Comandos Úteis

### Executar todos os testes
```bash
./mvnw test
```

### Executar testes com saída detalhada
```bash
./mvnw test -X
```

### Executar testes sem logs do Spring
```bash
./mvnw test -q
```

### Executar um teste específico
```bash
./mvnw test -Dtest=DenunciaServiceTest#deveCriarDenunciaConfidencialComSucesso
```

### Executar testes e gerar relatório
```bash
./mvnw test
cat target/surefire-reports/*.txt
```

### Limpar e executar testes
```bash
./mvnw clean test
```

---

## 📁 Estrutura de Testes

```
src/test/java/
├── S/O/S/Escola/CESAR/
│   ├── CesarApplicationTests.java          # Teste de contexto Spring
│   └── DenunciaE2ETest.java                # Testes E2E existentes (2 testes)
│
├── br/com/sosescolar/
│   ├── service/
│   │   └── DenunciaServiceTest.java        # Testes de unidade (9 testes)
│   │
│   ├── controller/
│   │   └── DenunciaControllerIntegrationTest.java  # Testes de integração (9 testes)
│   │
│   └── e2e/
│       └── SistemaE2ETest.java             # Testes E2E novos (7 testes)
```

---

## 🧩 Cobertura de Funcionalidades

### ✅ História de Usuário 01 - Cadastro de Denúncia Confidencial
**Cenários testados:**
- ✅ Denúncia registrada com sucesso
- ✅ Falha por campos obrigatórios não preenchidos
- ✅ Preservação da confidencialidade

**Testes relacionados:**
- `deveCriarDenunciaConfidencialComSucesso`
- `fluxoCompletoDenunciaConfidencial`
- `devePreservarConfidencialidadeEmDenunciasAnonimas`

---

### ✅ História de Usuário 03 - Geração de Protocolo de Denúncia
**Cenários testados:**
- ✅ Protocolo gerado com sucesso
- ✅ Protocolo único para cada denúncia
- ✅ Formato correto do protocolo (YYYYMMDD-XXXXXXXX)

**Testes relacionados:**
- `deveGerarProtocoloUnicoParaCadaDenuncia`
- `fluxoVerificacaoFormatoProtocolo`
- `fluxoMultiplasDenunciasComProtocolosUnicos`

---

### ✅ História de Usuário 07 - Visualização de Detalhes da Denúncia
**Cenários testados:**
- ✅ Detalhes exibidos corretamente
- ✅ Falha ao carregar detalhes (protocolo inexistente)

**Testes relacionados:**
- `deveBuscarDenunciaPorProtocoloERetornar200`
- `deveRetornar404QuandoProtocoloNaoExiste`
- `fluxoErroBuscaProtocoloInexistente`

---

## 🔍 Interpretando os Resultados

### ✅ Teste Passou
```
[INFO] Tests run: 28, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### ❌ Teste Falhou
```
[ERROR] Tests run: 28, Failures: 1, Errors: 0, Skipped: 0
[ERROR] Failures: 
[ERROR]   DenunciaServiceTest.deveCriarDenuncia:45 expected: <201> but was: <400>
```

### ⚠️ Teste com Erro
```
[ERROR] Tests run: 28, Failures: 0, Errors: 1, Skipped: 0
[ERROR] Errors: 
[ERROR]   DenunciaServiceTest.deveCriarDenuncia:45 » NullPointer
```

---

## 🐛 Troubleshooting

### Problema: "Permission denied" ao executar mvnw
**Solução:**
```bash
chmod +x mvnw
./mvnw test
```

### Problema: Versão do Java incorreta
**Verificar versão:**
```bash
java -version
```

**Deve ser Java 21 ou superior.**

### Problema: Testes falhando por timeout
**Aumentar timeout:**
```bash
./mvnw test -Dmaven.surefire.timeout=600
```

### Problema: Porta já em uso
**Solução:** Os testes usam portas aleatórias (`RANDOM_PORT`), então esse problema não deve ocorrer.

---

## 📊 Relatórios de Teste

### Relatório em texto
```bash
cat target/surefire-reports/*.txt
```

### Relatório detalhado
```bash
./mvnw test
cat RELATORIO_TESTES.md
```

### Log completo da execução
```bash
./mvnw test 2>&1 | tee test-execution.log
```

---

## 🎓 Boas Práticas Implementadas

### 1. **Nomenclatura Clara**
```java
@Test
@DisplayName("Deve criar denúncia confidencial com sucesso")
void deveCriarDenunciaConfidencialComSucesso() {
    // ...
}
```

### 2. **Padrão AAA (Arrange-Act-Assert)**
```java
// Arrange - Preparar dados
DenunciaDTO denuncia = new DenunciaDTO();
denuncia.setTipoDenuncia(TipoDeDenun.BULLYING);

// Act - Executar ação
DenunciaDTO resultado = service.criarDenuncia(denuncia);

// Assert - Verificar resultado
assertNotNull(resultado.getProtocolo());
```

### 3. **Isolamento de Testes**
```java
@BeforeEach
void setUp() {
    denunciaRepository.deleteAll(); // Limpar dados entre testes
}
```

### 4. **Uso de Mocks**
```java
@Mock
private DenunciaRepository denunciaRepository;

@InjectMocks
private DenunciaService denunciaService;
```

---

## 📚 Recursos Adicionais

- **Relatório Completo:** `RELATORIO_TESTES.md`
- **Log de Execução:** `execucao-testes-completa.log`
- **Documentação JUnit 5:** https://junit.org/junit5/docs/current/user-guide/
- **Spring Boot Testing:** https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing

---

## 🎯 Checklist de Validação

Antes de fazer commit, verifique:

- [ ] Todos os testes estão passando (`./mvnw test`)
- [ ] Nenhum teste foi comentado ou desabilitado
- [ ] Novos testes foram adicionados para novas funcionalidades
- [ ] Testes cobrem cenários positivos e negativos
- [ ] Nomenclatura dos testes está clara e descritiva
- [ ] Código de teste segue os padrões do projeto

---

## 💡 Dicas

### Executar testes em modo watch (reexecutar ao salvar)
```bash
./mvnw test -Dspring-boot.run.fork=false
```

### Executar apenas testes que falharam
```bash
./mvnw test -Dsurefire.rerunFailingTestsCount=2
```

### Executar testes em paralelo (mais rápido)
```bash
./mvnw test -T 4
```

---

## 📞 Suporte

Em caso de dúvidas ou problemas:

1. Verifique o arquivo `RELATORIO_TESTES.md`
2. Consulte os logs em `target/surefire-reports/`
3. Entre em contato com a equipe de desenvolvimento

---

**Desenvolvido com 💙 pela equipe S.O.S Escola**
