@file:OptIn(ExperimentalMaterial3Api::class) // Necessário para componentes do Material 3

package com.guilherme.fundamentoscompose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api // Importação explícita
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guilherme.fundamentoscompose.model.Game
// Se GamesViewModel estiver em outro pacote, adicione a importação:
// import com.guilherme.fundamentoscompose.viewmodel.GamesViewModel // Exemplo de caminho

@Composable
fun GamesScreen(vm: GamesViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Fundamentos Compose - Listas & Filtro") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding) // Aplica o padding da Scaffold para a TopAppBar
                .padding(16.dp)     // Seu padding customizado
                .fillMaxSize()
        ) {
            // 1) Filtro por texto
            OutlinedTextField(
                value = state.query,
                onValueChange = vm::onQueryChange,
                label = { Text("Filtrar por título, estúdio ou gênero") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            // 2) Filtro por estúdio (LazyRow de chips)
            Text("Filtrar por estúdio:", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.estudios) { est ->
                    AssistChip(
                        onClick = { vm.onStudioClick(est) },
                        label = { Text(est) }
                    )
                    // Alternativa com FilterChip se preferir e sua versão do Material 3 suportar bem:
                    // FilterChip(
                    //     selected = state.estudioSelecionado == est,
                    //     onClick = { vm.onStudioClick(est) },
                    //     label = { Text(est) }
                    // )
                }
            }

            // 3) Botão “Limpar filtro” só quando houver filtro ativo
            if (state.filtroAtivo) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End // Alinha o botão à direita
                ) {
                    TextButton(onClick = vm::limparFiltro) {
                        Text("Limpar filtro")
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // 4) Lista principal (LazyColumn)
            if (state.gamesFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize() // Para centralizar no espaço restante
                        .padding(top = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhum jogo encontrado.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize() // LazyColumn preenche o espaço restante
                ) {
                    items(state.gamesFiltrados, key = { it.id }) { game ->
                        GameItem(game)
                    }
                }
            }
        }
    }
}

@Composable
private fun GameItem(game: Game) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(text = game.titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp)) // Aumentei um pouco o espaço para melhor leitura
            Text(
                text = "Estúdio: ${game.estudio}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Gênero: ${game.genero}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
