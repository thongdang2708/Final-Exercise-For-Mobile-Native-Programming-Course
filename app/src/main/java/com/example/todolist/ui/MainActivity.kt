package com.example.todolist.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytodoapp.R
import com.example.mytodoapp.ui.theme.MyTodoAppTheme
import com.example.todolist.model.Todo
import com.example.todolist.viewmodel.TodoUIState
import com.example.todolist.viewmodel.TodoViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import java.sql.RowId


import java.time.format.TextStyle


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTodoAppTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    WeatherApp()
                   
                }
            }
        }
    }
}

@Composable
fun WeatherApp (todoViewModel: TodoViewModel = viewModel()) {
    Column (modifier = Modifier.padding(2.dp)) {
        ToDoScreen(todoUIState = todoViewModel.todoUIState, todoViewModel = todoViewModel)
        
    }

}


@Composable
fun ToDoScreen (todoUIState: TodoUIState, todoViewModel: TodoViewModel) {

    when (todoUIState) {
        is TodoUIState.Loading -> LoadingScreen()
        is TodoUIState.Success -> ToDoScreen(todoUIState.todos, todoViewModel)
        is TodoUIState.Error -> ErrorScreen()
    }
}


@Composable
fun ToDoScreen (todos: List<Todo>, todoViewModel: TodoViewModel) {
    
    val array = todos.map {  todo -> todo.userId }.distinct().toIntArray();
    var selectedItem by remember { mutableStateOf(array[0]) }
    var expanded by remember { mutableStateOf(false) }
    
    var filteredArray = todos.filter { todo -> todo.userId === todoViewModel.userId.value }.toTypedArray();
    

    Column(modifier = Modifier.padding(2.dp)) {
        Text(text = stringResource(R.string.Notice, "${array.size}"), style = androidx.compose.ui.text.TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Blue))
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(R.string.Click), style = androidx.compose.ui.text.TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Blue))
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))) {
            Text(
                text = "${selectedItem}",
                modifier = Modifier
                    .clickable(onClick = { expanded = true })
                    .padding(16.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .padding(16.dp)
                    .widthIn(max = 200.dp)
            ) {
                array.forEach { item ->
                    DropdownMenuItem(onClick = {
                        selectedItem = item
                        todoViewModel.setUserId(item)
                        expanded = false
                    }) {
                        Text(text = "${item}")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(R.string.ListOfTasks, "${todoViewModel.userId.value}"), modifier = Modifier.padding(5.dp),color = Color.Green, style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp));
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyColumn(modifier = Modifier.padding(10.dp)) {
            items(filteredArray) {
                item -> 
                Row(modifier = Modifier.padding(5.dp)) {
                    Text(text = "${item.id}", style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "${item.title}", style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                }
                Divider(modifier = Modifier.border(width = 2.dp, color = Color.LightGray))
            }
        }
        
    }
    
    



}





@Composable
fun LoadingScreen () {
    Text(text = "Loading")
}

@Composable
fun ErrorScreen () {
    Text(text = "Error when retrieving data!")
}



@Composable
fun SearchBar () {


    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(2.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),


        ) {
            Text(text = "Please enter ", style = androidx.compose.ui.text.TextStyle(color = MaterialTheme.colors.onSecondary, fontSize = 24.sp, fontWeight = FontWeight.Bold))
            OutlinedTextField(value = "Hello", onValueChange = {},keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done), modifier = Modifier.fillMaxWidth(), keyboardActions = KeyboardActions(onDone = {
//                if (!validCityInputState) return@KeyboardActions
//                focusManager.clearFocus()
            }))

        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTodoAppTheme {
        WeatherApp()
    }
}