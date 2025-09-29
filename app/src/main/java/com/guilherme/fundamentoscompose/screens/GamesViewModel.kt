package com.guilherme.fundamentoscompose.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.fundamentoscompose.data.sampleGames
import com.guilherme.fundamentoscompose.model.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.SharingStarted

data class UiState(
    val gamesFiltrados: List<Game> = emptyList(),
    val estudios: List<String> = emptyList(),
    val query: String = "",
    val estudioSelecionado: String? = null
) {
    val filtroAtivo: Boolean get() = query.isNotBlank() || estudioSelecionado != null
}

class GamesViewModel : ViewModel() {

    private val _todos = MutableStateFlow(sampleGames)
    private val _query = MutableStateFlow("")
    private val _estudioSelecionado = MutableStateFlow<String?>(null)

    val uiState: StateFlow<UiState> =
        combine(_todos, _query, _estudioSelecionado) { games, q, studio ->
            val qn = q.trim().lowercase()

            val filtrados = games.filter { g ->
                val matchQuery =
                    qn.isBlank() ||
                            g.titulo.lowercase().contains(qn) ||
                            g.genero.lowercase().contains(qn) ||
                            g.estudio.lowercase().contains(qn)

                val matchStudio = studio == null || g.estudio == studio
                matchQuery && matchStudio
            }

            UiState(
                gamesFiltrados = filtrados,
                estudios = games.map { it.estudio }.distinct(),
                query = q,
                estudioSelecionado = studio
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UiState()
        )

    fun onQueryChange(novo: String) { _query.value = novo }

    fun onStudioClick(estudio: String) {
        _estudioSelecionado.update { atual ->
            if (atual == estudio) null else estudio // clique novamente desmarca
        }
    }

    fun limparFiltro() {
        _query.value = ""
        _estudioSelecionado.value = null
    }
}
