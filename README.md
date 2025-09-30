# Fundamentos Jetpack Compose – Listas Lazy

## Integrantes

* Guilherme Costa Proença — RM 98937
* Eduarda Shiratzu — RM 551188
* Hugo Antonio — RM 550153

---

## Descrição do Projeto

Aplicativo Android em **Kotlin** utilizando **Jetpack Compose** e **Material 3** para exibir uma lista de jogos com:

* **Filtro por texto** (título, estúdio ou gênero)
* **Filtro por estúdio** (faixa horizontal com chips)
* **Botão “Limpar filtro”** que remove qualquer filtro ativo e restaura a lista completa

O foco do checkpoint é praticar:

* Construção de UI **declarativa** com Compose
* Listas performáticas com **LazyColumn** e **LazyRow**
* **Gerenciamento de estado reativo** com **ViewModel** + **StateFlow**
* Organização de projeto por camadas (dados, lógica, interface)

---

## Ambiente e Requisitos

* Android Studio (Giraffe+ ou mais recente)
* JDK 11
* Min SDK 24+
* Compose Compiler Extension compatível (ex.: 1.5.x)
* Emulador Android configurado **ou** dispositivo físico com Depuração USB

Dependências principais (via BOM do Compose):

* `androidx.activity:activity-compose`
* `androidx.compose.material3:material3`
* `androidx.compose.ui:*`
* `androidx.lifecycle:lifecycle-viewmodel-compose` (necessária para `viewModel()` no Compose)

> Observação: alguns componentes do Material 3, como `AssistChip`/`FilterChip`, podem estar marcados como **experimental**. O projeto utiliza `@OptIn(ExperimentalMaterial3Api::class)` quando necessário.

---

## Como abrir e executar

### 1) Clonar o repositório

```bash
git clone <URL-do-seu-repo>
```

### 2) Abrir no Android Studio

* File → Open → selecione a pasta do projeto
* Aguarde o **Sync** do Gradle

### 3) Executar

* **Emulador**: crie/seleciona um AVD (Device Manager) e clique **Run ▶**
* **Dispositivo físico**:

  1. Ativar “Opções do desenvolvedor” e **Depuração USB** no Android
  2. Conectar via USB e autorizar o computador no telefone
  3. Selecionar o dispositivo no Android Studio e **Run ▶**

> Caso o emulador acuse problemas de hipervisor (Windows), veja “Troubleshooting”.

---

## Estrutura do Código

```
com.guilherme.fundamentoscompose
 ├─ model
 │   └─ Game.kt
 ├─ data
 │   └─ SampleData.kt
 ├─ screens
 │   ├─ GamesViewModel.kt
 │   └─ GamesScreen.kt
 └─ MainActivity.kt
```

### Arquivos principais

**model/Game.kt**
`data class Game(id, titulo, estudio, genero)` — modelo simples para os itens da lista.

**data/SampleData.kt**
Lista estática de jogos para alimentar a interface durante o checkpoint.

**screens/GamesViewModel.kt**

* Expõe um `StateFlow<UiState>` com:

  * `gamesFiltrados: List<Game>`
  * `estudios: List<String>` (distintos)
  * `query: String`
  * `estudioSelecionado: String?`
  * `filtroAtivo: Boolean` (derivado)
* Regras de negócio:

  * `onQueryChange(novo: String)` — atualiza o filtro de texto
  * `onStudioClick(estudio: String)` — aplica/alternar filtro de estúdio
  * `limparFiltro()` — limpa todos os filtros
* Implementa a filtragem combinando `query` e `estudioSelecionado` em tempo real.

**screens/GamesScreen.kt**

* `Scaffold` com `TopAppBar`
* `OutlinedTextField` para o filtro por texto
* `LazyRow` com chips de estúdio (seleção por clique)
* `TextButton` “Limpar filtro” visível somente quando `filtroAtivo == true`
* `LazyColumn` para exibir a lista filtrada
* `GameItem` (Card) para renderizar cada jogo

**MainActivity.kt**
Chama `GamesScreen()` dentro do tema Material.

---

## Funcionamento da Filtragem (detalhes técnicos)

* O `ViewModel` mantém o **estado único** da tela (`UiState`) e emite atualizações via **StateFlow**.
* A UI coleta esse estado com `collectAsState()`; cada mudança em `query` ou `estudioSelecionado` **recompõe** a tela automaticamente.
* Critérios de filtro:

  * `query` faz `contains` case-insensitive em **título**, **gênero** e **estúdio**
  * Filtro por estúdio é aplicado quando `estudioSelecionado != null`
  * Ambos os filtros **se combinam** (E lógico)
* O botão “Limpar filtro” aparece quando `query.isNotBlank()` **ou** `estudioSelecionado != null`.

---

## Passos de Validação (testes manuais)

1. **Lista completa** ao abrir o app
2. Digitar “RPG” no campo → somente jogos de gênero RPG
3. Clicar no estúdio “FromSoftware” → apenas jogos desse estúdio
4. Clicar novamente no mesmo estúdio → desmarca (remove esse filtro)
5. Com qualquer filtro ativo, o botão **“Limpar filtro”** aparece
6. Clicar **“Limpar filtro”** → lista restaurada integralmente
7. Estado “vazio”: digitar algo que não exista → aparecer “Nenhum jogo encontrado.”

---

## Prints da Aplicação

Crie a pasta `images/` na raiz e adicione os arquivos. Referencie assim:

```
![Lista completa](images/print1.png)
![Filtro por texto](images/print2.png)
![Filtro por estúdio](images/print3.png)
![Limpar filtro](images/print4.png)
```

---

## Decisões de Implementação

* **Compose + Material 3** para UI declarativa e consistente
* **LazyColumn/LazyRow** pela eficiência em listas
* **ViewModel + StateFlow** para estado reativo e desacoplado da Activity
* **Separação em pacotes (model/data/screens)** para manutenibilidade e evolução futura
* **Opt-in ExperimentalMaterial3Api** quando necessário (chips)

---

## Troubleshooting (erros comuns e soluções)

### “Unresolved reference: viewModel”

Adicionar a dependência:

```kotlin
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
```

Sincronizar o Gradle.

### “Unresolved reference: compose” ou imports estranhos

Confirme imports no arquivo:

```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
```

### `FilterChip` indisponível

Use `AssistChip` (Material 3) e, se necessário:

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
```

### `Modifier.padding(paddingValues = innerPadding)` não compila

Use:

```kotlin
Modifier.padding(innerPadding)
```

### `Arrangement.spacedBy(space = 8.dp)` não compila

Use:

```kotlin
Arrangement.spacedBy(8.dp)
```

### Emulador não inicia (Windows, erro de hipervisor/AEHD)

* Se **Hyper-V/WSL2** estiver ativo: use o emulador com **WHPX/Hyper-V** (sem AEHD).
* Se quiser o **driver do Android (AEHD)**: desative Hyper-V, “Windows Hypervisor Platform” e “Virtual Machine Platform”, desligue “Core Isolation / Memory Integrity” e rode `silent_install.bat` do AEHD como Administrador.
* Alternativa rápida: rodar em **dispositivo físico** com Depuração USB.

---

## Possíveis Extensões

* Fonte de dados real (API com Retrofit)
* Paginação (Paging 3)
* Estados de carregamento/erro
* Testes de UI com `compose-ui-test`

---

## Licença

Uso acadêmico para o Checkpoint da disciplina Android Kotlin Developer.

