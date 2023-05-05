package com.example.phonebook.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.routing.Screen
import com.example.phonebook.ui.components.AppDrawer
import com.example.phonebook.ui.components.Contact
import com.example.phonebook.ui.theme.Comfortaa
import com.example.phonebook.util.fromHex
import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Composable
fun ContactScreen(viewModel: MainViewModel) {
    val contacts by viewModel.contactsNotInTrash.observeAsState(listOf())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Contacts",
                        color = Color.fromHex("#EDE9E3"),
                        fontFamily = Comfortaa,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Drawer Button"
                        )
                    }
                }
            )
        },
        drawerContent = {
            AppDrawer(
                currentScreen = Screen.Contacts,
                closeDrawerAction = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onCreateNewContactClick() },
                contentColor = MaterialTheme.colors.background,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Contact Button"
                    )
                }
            )
        }
    ) {
        if (contacts.isNotEmpty()) {
            ContactsList(
                contacts = contacts.sortedBy { it.name },
                onContactCheckedChange = {
                    viewModel.onContactCheckedChange(it)
                },
                onContactClick = { viewModel.onContactClick(it) }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun ContactsList(
    contacts: List<ContactModel>,
    onContactCheckedChange: (ContactModel) -> Unit,
    onContactClick: (ContactModel) -> Unit
) {
    LazyColumn {
        items(count = contacts.size) { noteIndex ->
            val contact = contacts[noteIndex]
            Contact(
                contact = contact,
                onContactClick = onContactClick,
                onContactCheckedChange = onContactCheckedChange,
                isSelected = false
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun ContactsListPreview() {

    ContactsList(
        contacts = listOf(
            ContactModel(1, "B", "Content 1", "",false, ),
            ContactModel(2, "A", "Content 2", "",false, ),
            ContactModel(3, "F", "Content 3", "",true, )
        ).sortedBy { it.name },
        onContactCheckedChange = {},
        onContactClick = {}
    )
}