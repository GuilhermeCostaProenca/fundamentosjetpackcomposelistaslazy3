package com.guilherme.fundamentoscompose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guilherme.fundamentoscompose.model.Game


@Composable
fun GamesScreen(vm: GamesViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Fundamentos Compose - Listas & Filtro") }) }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(16.dp)
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
                    // Use AssistChip para compatibilidade total:
                    AssistChip(
                        onClick = { vm.onStudioClick(est) },
                        label = { Text(est) }
                    )

                    // Se sua versão tiver FilterChip e quiser destacar o selecionado, troque por:
                    // FilterChip(
                    //     selected = state.estudioSelecionado == est,
                    //     onClick = { vm.onStudioClick(est) },
                    //     label = { Text(est) }
                    // )
                }
            }

            // 3) Botão “Limpar filtro” só quando houver filtro ativo
            if (state.filtroAtivo) {
                Row(Modifier.fillMaxWidth()) {
                    Spacer(Modifier.weight(1f))
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
                        .fillMaxSize()
                        .padding(top = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhum jogo encontrado.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
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
            Spacer(Modifier.height(2.dp))
            Text(text = "Estúdio: ${game.estudio}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Gênero: ${game.genero}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
