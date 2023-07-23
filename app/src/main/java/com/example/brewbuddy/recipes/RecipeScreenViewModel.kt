package com.example.brewbuddy.recipes

import RecipeResultsState
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.domain.use_case.get_recipes.GetRecipeResultsUseCase
import com.example.brewbuddy.getUser
import com.example.brewbuddy.marketplace.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecipeScreenViewModel  @Inject constructor(
    private val getRecipeResultsUseCase: GetRecipeResultsUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val _filters = mutableStateListOf<Filter>()
    val filters: SnapshotStateList<Filter> = _filters

    private val _search = mutableStateOf(String())
    val search: State<String> = _search

    private val _state = mutableStateOf(RecipeResultsState())
    val state: State<RecipeResultsState> = _state

    init {
        getResults("")
    }
    fun setKeywords(str: String) {
        _search.value = str
    }

    fun removeFilter(filter: Filter) {
        _filters.remove(filter)

    }
    fun addFilter(filter: Filter) {
        _filters.add(filter)

    }

    private val _query = mutableStateOf(String())
    val query: State<String> = _query
    fun search() {
        _query.value = createQueryString(_search.value, _filters)

        getResults(_query.value)
    }
    private fun filterToString(list: List<Filter>): String {
        var string = ""
        list.forEach { el ->
            if(!string.isEmpty()) {
                string += "&"
            }
            if(el.name.contains("Asce") || el.name.contains("Desc")) {
                string += "sort=${el.name}"
            } else {
                string += "${el.name}=${el.enabled}"
            }
        }
        Log.d("filterToString", string)
        return string
    }

    private fun keywordsToString(str: String): String {
        if(str.isNotEmpty()) {
            return "keywords=${str}"
        }
        return ""
    }
    private fun createQueryString(keywords: String, filters: List<Filter>): String {
        val keywordString = keywordsToString(keywords)
        val filterString = filterToString(filters)

        if (filterString.isEmpty() || keywordString.isEmpty()) {
            Log.d("createQueryString", keywordString + filterString)

            return keywordString + filterString
        }
        Log.d("createQueryString", "${keywordString}&${filterString}")
        return "${keywordString}&${filterString}"
    }

    private fun getResults(query: String) {
        getRecipeResultsUseCase(query).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = RecipeResultsState(results = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = RecipeResultsState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _state.value = RecipeResultsState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}