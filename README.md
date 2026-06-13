# cli_calcorias_clojure — Interface de Linha de Comando (CLI)

CLI desenvolvida em **Clojure** para interagir com a API `calcorias_clojure`. Permite que o usuário gerencie seu perfil, registre alimentos consumidos e exercícios realizados, e consulte o histórico de calorias e o balanço energético diretamente pelo terminal.

---

## Sobre o Projeto

Este projeto é a camada de interação do usuário do sistema de contagem de calorias. Ele se comunica com a API REST (`calcorias_clojure`) através de requisições HTTP e persiste o perfil do usuário localmente em um arquivo `perfil.json`.

---

## Tecnologias Utilizadas

| Tecnologia | Finalidade |
|---|---|
| Clojure 1.10 | Linguagem principal |
| tools.cli | Parsing de argumentos e opções da CLI |
| clj-http | Requisições HTTP para a API |
| Cheshire | Serialização/deserialização JSON |

---

## Estrutura do Projeto

```
cli_calcorias_clojure/
├── project.clj
└── src/
    ├── cli_calcorias/
    │   ├── core.clj              # Ponto de entrada (-main)
    │   └── cli.clj               # Definição dos comandos e roteamento
    └── commands/
        ├── config/
        │   ├── add_user.clj      # Comando: config set
        │   └── update_user.clj   # Comando: config update
        ├── save/
        │   ├── food.clj          # Comando: add food
        │   └── exercise.clj      # Comando: add exercise (estrutura base)
        └── get/
            ├── calories.clj      # Comando: get calories
            └── balance.clj       # Comando: get balance
```

---

## Pré-requisitos

- [Java JDK 8+](https://adoptium.net/)
- [Leiningen](https://leiningen.org/) (`lein`)
- A API `calcorias_clojure` em execução em `http://0.0.0.0:3000`

---

## Como Executar

Clone o repositório e, na pasta `cli_calcorias_clojure`, execute os comandos com `lein run`:

```bash
lein run -- <comando> <subcomando> [argumentos] [opções]
```

Você também pode gerar um uberjar para executar a CLI diretamente:

```bash
lein uberjar
java -jar target/uberjar/cli_calcorias-0.1.0-SNAPSHOT-standalone.jar <comando> <subcomando> [argumentos] [opções]
```

---

## Comandos Disponíveis

### `config set` — Cadastrar Usuário

Cria um novo perfil de usuário e salva as informações no arquivo `perfil.json` na pasta atual.

**Sintaxe:**
```bash
lein run -- config set <nome> <peso> [opções]
```

**Argumentos obrigatórios:**
- `<nome>` — Nome do usuário (string)
- `<peso>` — Peso em kg (número)

**Opções:**
| Opção | Descrição |
|---|---|
| `-a AGE`, `--age AGE` | Idade do usuário (opcional) |
| `-s SEX`, `--sex SEX` | Sexo do usuário: `M` ou `F` (opcional) |

**Exemplos:**
```bash
lein run -- config set João 80
lein run -- config set João 80 -a 25 -s M
```

**Saída esperada:**
```
User sucessfully created.
```

O perfil é salvo em `perfil.json` com a estrutura:
```json
{
  "name": "João",
  "weight": "80",
  "age": "25",
  "sex": "M"
}
```

---

### `config update` — Atualizar Usuário

Atualiza os dados do perfil existente em `perfil.json`. Apenas os campos informados são alterados — os demais mantêm o valor anterior.

**Sintaxe:**
```bash
lein run -- config update [<nome>] [<peso>] [opções]
```

Todos os argumentos e opções são opcionais. Se omitidos, o valor atual é preservado.

**Opções:**
| Opção | Descrição |
|---|---|
| `-a AGE`, `--age AGE` | Nova idade (opcional) |
| `-s SEX`, `--sex SEX` | Novo sexo: `M` ou `F` (opcional) |

**Exemplos:**
```bash
# Atualiza apenas o peso
lein run -- config update João 85

# Atualiza apenas a idade
lein run -- config update -a 26

# Atualiza nome, peso, idade e sexo
lein run -- config update Maria 65 -a 30 -s F
```

---

### `add food` — Registrar Alimento

Registra um alimento consumido. A CLI envia uma requisição `POST /api/food` para a API, que consulta o banco de dados do USDA para obter as informações calóricas.

**Sintaxe:**
```bash
lein run -- add food <nome> <gramas>
```

**Argumentos obrigatórios:**
- `<nome>` — Nome do alimento em inglês (string)
- `<gramas>` — Quantidade consumida em gramas (inteiro)

**Exemplos:**
```bash
lein run -- add food apple 150
lein run -- add food "brown rice" 200
```

**Saída esperada (sucesso):**
```
Food successfully added.
```

**Saída quando há múltiplos resultados:**
```
Multiple matches found. Please type the full name of one:

APPLE, RAW.
APPLE JUICE, UNSWEETENED.
APPLE PIE
```

Nesse caso, execute o comando novamente com o nome completo e exato do alimento desejado.

---

### `add exercise` *(em desenvolvimento)*

Registra um exercício realizado. A estrutura do comando está definida, mas a implementação completa ainda está em desenvolvimento.

**Sintaxe planejada:**
```bash
lein run -- add exercise <nome> <minutos>
```

**Argumentos previstos:**
- `<nome>` — Nome da atividade física em inglês (ex.: `running`, `swimming`)
- `<minutos>` — Duração da atividade em minutos (inteiro)

> Requer um usuário cadastrado via `config set` para calcular as calorias queimadas com base no peso.

---

### `get calories` — Consultar Histórico de Calorias

Exibe a lista de todos os registros de alimentos consumidos e exercícios realizados armazenados na API.

**Sintaxe:**
```bash
lein run -- get calories [opções]
```

**Opções:**
| Opção | Descrição |
|---|---|
| `-p day`, `--period day` | Filtra pelo dia atual ou mês (padrão: mês) |

**Exemplos:**
```bash
lein run -- get calories
lein run -- get calories -p day
```

**Saída esperada:**
```
Results:

type, name, kcal, date

intake, "APPLE, RAW", 78.0, 2025-06-13.
burned, "Running", 320.5, 2025-06-13
```

---

### `get balance` — Consultar Balanço Energético

Exibe o saldo calórico atual — a diferença entre as calorias consumidas (intake) e as calorias queimadas (burned).

**Sintaxe:**
```bash
lein run -- get balance [opções]
```

**Opções:**
| Opção | Descrição |
|---|---|
| `-p day`, `--period day` | Filtra pelo dia atual ou mês |

**Exemplos:**
```bash
lein run -- get balance
lein run -- get balance -p day
```

**Saída esperada:**
```
Energy Balance: -242.5
```

Um valor negativo indica **déficit calórico** (mais calorias queimadas do que consumidas). Um valor positivo indica **superávit calórico**.

---

## Fluxo de Uso Típico

```bash
# 1. Cadastrar o usuário
lein run -- config set João 80 -a 25 -s M

# 2. Registrar o café da manhã
lein run -- add food oatmeal 100
lein run -- add food banana 120

# 3. Registrar o exercício do dia
lein run -- add exercise running 30

# 4. Consultar o histórico do dia
lein run -- get calories -p day

# 5. Verificar o balanço energético
lein run -- get balance -p day
```

---

## Arquivo de Perfil Local

O comando `config set` cria um arquivo `perfil.json` na pasta onde a CLI é executada. Este arquivo é lido e atualizado pelo comando `config update`.

**Estrutura do arquivo:**
```json
{
  "name": "João",
  "weight": "80",
  "age": "25",
  "sex": "M"
}
```

> Se o arquivo `perfil.json` não existir ao executar `config update`, um erro será exibido: `Failed: User not create`. Execute `config set` primeiro.

---

## Tratamento de Erros

A CLI valida os argumentos antes de fazer qualquer requisição à API e exibe mensagens de erro claras no terminal:

| Situação | Mensagem |
|---|---|
| Argumentos insuficientes em `config set` | `Failed: Invalid arguments. Use: config set <nome> <peso>` |
| Peso não é um número | `Failed: weight is not a number` |
| Gramas não é um número em `add food` | `Failed: grams is not a number` |
| Argumentos insuficientes em `add food` | `Failed: Invalid arguments. Use: config set <name> <grams>` |
| Perfil não encontrado em `config update` | `Failed: User not create` |
| Comando desconhecido | `Comando desconhecido: <cmd>` |
| Subcomando inválido | `Subcomando inválido para <cmd>: <subcmd>` |

---

## Dependência

Esta CLI depende da API `calcorias_clojure` rodando em `http://0.0.0.0:3000`. Certifique-se de iniciar a API antes de executar qualquer comando que faça requisições remotas (`add food`, `get calories`, `get balance`).

```bash
# No diretório calcorias_clojure:
lein ring server
```