# Manual de Contribuição - Ideal Computer

Bem-vindos ao repositório do Ideal Computer! Para garantirmos que nossa documentação acadêmica e o rastreio de horas funcionem perfeitamente, todos devem seguir o fluxo abaixo rigorosamente.

## 🛠 Ferramentas Obrigatórias

GitHub: Onde nosso código vive.

Jira: Onde nossas tarefas ficam (Quadro Scrum). Usamos a chave unificada IC para o projeto todo.

Clockify (Extensão de Navegador): Obrigatório para contabilizar as horas trabalhadas em cada tarefa para facilitar o controle acadêmico.

## 🔄 Fluxo de Trabalho (Obrigatório)

### 1. Pegando uma tarefa

Acesse o Jira e escolha uma tarefa na coluna "A Fazer".

Observe o ID único da tarefa (ex: IC-1, IC-2) e a etiqueta (label) que indica a área do sistema (ex: Frontend ou Backend).

Abra o card da tarefa no Jira e clique no botão "Start Timer" (do Clockify) que aparece lá dentro ANTES de começar a codar.

### 2. Criando sua Branch

Nunca desenvolva fazendo push direto na main ou na develop. Toda tarefa deve ter sua própria branch de desenvolvimento, criada sempre a partir da develop.

O nome da branch DEVE seguir este padrão: tipo / ID-DO-JIRA - área - descrição-curta.

Exemplos de nomenclatura:

Para o Backend: feature/IC-1-backend-auth-login

Para o Frontend: feature/IC-2-frontend-tela-home

```bash
Garanta que está na branch develop e que ela está atualizada
git checkout develop
git pull origin develop

Crie a sua branch de trabalho (substitua pelo ID e nome corretos)
git checkout -b feature/IC-1-backend-auth-login
```

### 3. Commits Inteligentes

Para que o Jira mova os cards automaticamente sem precisarmos arrastar nada manualmente, a mensagem do seu commit precisa iniciar com o ID da tarefa e um comando de status:

```bash
Exemplo de commit que move a tarefa para "Em Andamento"
git commit -m "IC-1 #in-progress Adicionando dependencias do Spring Boot para auth"

Exemplo de commit que move a tarefa para "Concluído"
git commit -m "IC-1 #done Finalizando endpoints de login"
```

### 4. Finalizando a Tarefa

Pare o cronômetro do Clockify.

Envie sua branch pronta para o GitHub:

```bash
git push -u origin feature/IC-1-backend-auth-login
```

No site do GitHub, abra um Pull Request (PR) apontando a sua branch para a branch develop (NUNCA para a main).

Coloque o ID da tarefa no título do PR para manter o rastreio (ex: IC-1: Criação da API de Autenticação).

Peça para outro participante do grupo revisar o código e aprovar o PR (Merge).
