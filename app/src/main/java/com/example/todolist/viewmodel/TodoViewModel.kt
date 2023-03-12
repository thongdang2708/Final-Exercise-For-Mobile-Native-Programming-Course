package com.example.todolist.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.todolist.model.Todo
import com.example.todolist.model.TodosApi
import kotlinx.coroutines.launch


sealed interface TodoUIState {
    data class Success (val todos: List<Todo>): TodoUIState
    data class SecondSuccess (val todosByUserId : List<Todo>): TodoUIState
    object Error: TodoUIState
    object Loading : TodoUIState
}

class TodoViewModel : ViewModel() {
    var todoUIState: TodoUIState by mutableStateOf<TodoUIState>(TodoUIState.Loading)
        private set

    var userId = mutableStateOf(1);
    var todosByUserId = mutableStateListOf<Todo>();

    fun setUserId (value : Int) {
        userId.value = value;
    }

    private var idOfUser : Int = 1
    get() {
        return userId.value
    }

    init {
        getTodosList();
        getToDoListsByUserId()
    }

    private fun getToDoListsByUserId () {
        viewModelScope.launch {
            var todosApi: TodosApi? = null

            try {

                todosApi = TodosApi!!.getInstance()
                todosByUserId.clear()
                todosByUserId.addAll(todosApi.getTodosByUserId(userId.value))

            } catch (e: Exception) {
                Log.d("TODOVIEWMODEL", e.message.toString())
                todoUIState = TodoUIState.Error
            }
        }
    }

    private fun getTodosList () {

        viewModelScope.launch {
            var todosApi: TodosApi? = null

            try {

                todosApi = TodosApi!!.getInstance()
                todoUIState = TodoUIState.Success(todosApi.getTodos())

            } catch (e: Exception) {
                Log.d("TODOVIEWMODEL", e.message.toString())
                todoUIState = TodoUIState.Error
            }
        }
    }
}



