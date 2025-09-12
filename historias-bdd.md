# Projeto SOS Escola - Histórias de Usuário (HUs)

---

## HU 01 - Cadastro de Denúncia Confidencial

**História de Usuário**  
Como aluno,  
Quero registrar uma denúncia de forma confidencial,  
Para que eu possa relatar casos de bullying sem medo de represálias, sabendo que minha identidade não será revelada aos gestores.

### Cenários

**Cenário 1 (positivo) – Denúncia registrada com sucesso**  
Dado que estou logado como aluno  
E selecionei a opção de denúncia confidencial  
Quando preencho todos os campos obrigatórios (tipo de violência, descrição, data/local)  
Então o sistema deve cadastrar a denúncia vinculada ao meu usuário internamente  
E não deve exibir nenhuma informação de identificação pessoal minha para a Equipe Pedagógica

**Cenário 2 (negativo) – Falha por campos obrigatórios não preenchidos**  
Dado que estou logado como aluno  
Quando tento enviar uma denúncia sem preencher todos os campos obrigatórios  
Então o sistema deve exibir a mensagem “Por favor, preencha todos os campos obrigatórios”  
E não deve permitir o envio até a correção

---

## HU 02 - Visualização de Denúncias pela Equipe Pedagógica

**História de Usuário**  
Como um membro da Equipe Pedagógica,  
Quero acessar e visualizar as denúncias confidenciais registradas,  
Para que eu possa analisá-las, identificar padrões e tomar as devidas providências.

### Cenários

**Cenário 1 (positivo) – Lista de denúncias exibida**  
Dado que estou logado como membro da Equipe Pedagógica  
E existe pelo menos uma denúncia registrada no sistema  
Quando acesso a área de "Denúncias Recentes"  
Então o sistema deve exibir uma lista com as denúncias  
E cada denúncia deve mostrar o tipo, a descrição, a data e o status de confidencialidade

**Cenário 2 (negativo) – Nenhuma denúncia registrada**  
Dado que estou logado como membro da Equipe Pedagógica  
E não há nenhuma denúncia registrada no sistema  
Quando acesso a área de "Denúncias Recentes"  
Então o sistema deve exibir uma mensagem informando que não há denúncias a serem exibidas

---

## HU 03 - Geração de Protocolo de Denúncia

**História de Usuário**  
Como aluno,  
Quero receber um número de protocolo único ao registrar uma denúncia,  
Para que eu tenha um registro formal e um identificador para minha solicitação.

### Cenários

**Cenário 1 (positivo) – Protocolo gerado com sucesso**  
Dado que estou logado como aluno  
E preenchi todos os campos obrigatórios da denúncia  
Quando envio a denúncia com sucesso  
Então o sistema deve gerar um número único de protocolo  
E exibir este protocolo na tela de confirmação

**Cenário 2 (negativo) – Falha ao gerar protocolo**  
Dado que estou logado como aluno  
E ocorre um erro no envio da denúncia  
Quando o sistema tenta registrar a denúncia  
Então o sistema deve exibir uma mensagem de erro no envio  
E não deve gerar protocolo até que a denúncia seja enviada com sucesso

---

## HU 04 - Login Seguro para a Equipe Pedagógica

**História de Usuário**  
Como um membro da Equipe Pedagógica,  
Quero acessar o sistema com login e senha seguros,  
Para que eu possa visualizar as denúncias garantindo a confidencialidade dos dados.

### Cenários

**Cenário 1 (positivo) – Login bem-sucedido**  
Dado que sou um membro da Equipe Pedagógica cadastrado no sistema  
Quando insiro meu email e senha corretos  
Então o sistema deve autenticar o acesso  
E redirecionar para o painel administrativo

**Cenário 2 (negativo) – Credenciais inválidas**  
Dado que sou um membro da Equipe Pedagógica cadastrado no sistema  
Quando insiro credenciais incorretas  
Então o sistema deve exibir uma mensagem de erro

---

## HU 05 - Feedback do Denunciante sobre o Atendimento

**História de Usuário**  
Como aluno,  
Quero avaliar o atendimento da escola após a finalização da minha denúncia,  
Para que a escola possa receber feedback sobre o processo e eu possa expressar se minha voz foi ouvida.

### Cenários

**Cenário 1 (positivo) – Feedback enviado com sucesso**  
Dado que tenho uma denúncia com status "Finalizado" há mais de 7 dias  
Quando acesso os detalhes dessa denúncia no meu histórico  
Então o sistema deve exibir um botão “Avaliar Atendimento”  
E ao clicar, devo ser direcionado para um formulário de satisfação confidencial

**Cenário 2 (negativo) – Feedback fora do prazo**  
Dado que tenho uma denúncia com status "Finalizado" há menos de 7 dias  
Quando acesso os detalhes dessa denúncia no meu histórico  
Então o sistema deve exibir a mensagem “A avaliação estará disponível após 7 dias da finalização do caso”  
E não deve exibir o link para o formulário de feedback

---

## HU 06 - Notificação de Encerramento de Caso

**História de Usuário**  
Como denunciante,  
Quero receber uma notificação quando a análise da minha denúncia for concluída,  
Para que eu saiba que o caso foi finalizado.

### Cenários

**Cenário 1 (positivo) – Mensagem de encerramento enviada**  
Dado que minha denúncia teve o status alterado para "Finalizado"  
Quando o sistema processa essa atualização  
Então uma notificação deve ser gerada na minha área de notificações  
E a notificação deve informar o número de protocolo e o status final

**Cenário 2 (negativo) – Falha no envio da notificação**  
Dado que minha denúncia foi classificada como "Finalizado"  
E ocorre um erro no sistema de notificações  
Quando o sistema tenta enviar a notificação  
Então o sistema deve registrar a tentativa em logs  
E tentar reenviar a notificação na próxima oportunidade

---

## HU 07 - Visualização de Detalhes da Denúncia

**História de Usuário**  
Como um membro da Equipe Pedagógica,  
Quero visualizar os detalhes completos de uma denúncia,  
Para que eu possa analisar todas as informações antes de tomar providências.

### Cenários

**Cenário 1 (positivo) – Detalhes exibidos corretamente**  
Dado que estou logado como membro da Equipe Pedagógica  
E existe uma denúncia registrada no sistema  
Quando clico em uma denúncia da lista  
Então o sistema deve exibir todas as informações da denúncia (tipo, descrição, data, local, status, confidencialidade)

**Cenário 2 (negativo) – Falha ao carregar detalhes**  
Dado que estou logado como membro da Equipe Pedagógica  
E ocorre um erro ao buscar os detalhes da denúncia  
Quando tento abrir a denúncia  
Então o sistema deve exibir a mensagem “Não foi possível carregar os detalhes da denúncia”

---

## HU 08 - Atualização do Status da Denúncia

**História de Usuário**  
Como um membro da Equipe Pedagógica,  
Quero atualizar o status de uma denúncia (em andamento, finalizado, etc.),  
Para que o andamento do caso seja registrado corretamente no sistema.

### Cenários

**Cenário 1 (positivo) – Status atualizado com sucesso**  
Dado que estou logado como membro da Equipe Pedagógica  
E a denúncia está registrada no sistema  
Quando altero o status da denúncia  
Então o sistema deve salvar a alteração  
E exibir a nova informação no painel e no histórico da denúncia

**Cenário 2 (negativo) – Falha ao atualizar status**  
Dado que estou logado como membro da Equipe Pedagógica  
E ocorre um erro ao salvar a alteração  
Quando tento atualizar o status  
Então o sistema deve exibir uma mensagem de erro  
E manter o status anterior registrado

---

## HU 09 - Filtro e Pesquisa de Denúncias

**História de Usuário**  
Como um membro da Equipe Pedagógica,  
Quero filtrar e pesquisar denúncias por tipo, data ou status,  
Para que eu possa encontrar rapidamente os casos relevantes.

### Cenários

**Cenário 1 (positivo) – Filtragem realizada com sucesso**  
Dado que estou logado como membro da Equipe Pedagógica  
E existem várias denúncias registradas  
Quando aplico um filtro ou faço uma pesquisa  
Então o sistema deve exibir apenas as denúncias correspondentes aos critérios

**Cenário 2 (negativo) – Nenhuma denúncia encontrada**  
Dado que estou logado como membro da Equipe Pedagógica  
E não existem denúncias que correspondam aos critérios  
Quando aplico um filtro ou pesquisa  
Então o sistema deve exibir a mensagem “Nenhum resultado encontrado”

---

## HU 10 - Histórico de Denúncias do Aluno

**História de Usuário**  
Como aluno,  
Quero visualizar o histórico das minhas denúncias registradas,  
Para que eu possa acompanhar o status de todas as denúncias realizadas.

### Cenários

**Cenário 1 (positivo) – Histórico exibido corretamente**  
Dado que estou logado como aluno  
E tenho denúncias registradas  
Quando acesso a tela "Minhas Denúncias"  
Então o sistema deve exibir uma lista com todas as minhas denúncias (tipo, data, protocolo e status)

**Cenário 2 (negativo) – Nenhuma denúncia registrada**  
Dado que estou logado como aluno  
E não tenho nenhuma denúncia registrada  
Quando acesso a tela "Minhas Denúncias"  
Então o sistema deve exibir a mensagem “Você ainda não possui denúncias registradas”
