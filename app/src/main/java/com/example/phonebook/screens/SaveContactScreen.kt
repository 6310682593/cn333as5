package com.example.phonebook.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phonebook.R
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.domain.model.NEW_CONTACT_ID
import com.example.phonebook.domain.model.TagModel
import com.example.phonebook.routing.PhoneNumberRouter
import com.example.phonebook.routing.Screen
import com.example.phonebook.ui.components.ContactColor
import com.example.phonebook.ui.theme.Comfortaa
import com.example.phonebook.util.fromHex
import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.time.format.TextStyle

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Composable
fun SaveContactScreen(viewModel: MainViewModel) {
    val contactEntry by viewModel.contactEntry.observeAsState(ContactModel())

    val tags: List<TagModel> by viewModel.tags.observeAsState(listOf())

    val bottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val moveContactToTrashDialogShownState = rememberSaveable { mutableStateOf(false) }

    BackHandler {
        if (bottomDrawerState.isOpen) {
            coroutineScope.launch { bottomDrawerState.close() }
        } else {
            PhoneNumberRouter.navigateTo(Screen.Contacts)
        }
    }

    Scaffold(
        topBar = {
            val isEditingMode: Boolean = contactEntry.id != NEW_CONTACT_ID
            SaveContactTopAppBar(
                isEditingMode = isEditingMode,
                onBackClick = { PhoneNumberRouter.navigateTo(Screen.Contacts) },
                onSaveContactClick = { viewModel.saveContact(contactEntry) },
                onOpenTagPickerClick = {
                    coroutineScope.launch { bottomDrawerState.open() }
                },
                onDeleteContactClick = {
                    moveContactToTrashDialogShownState.value = true
                }
            )
        }
    ) {
        BottomDrawer(
            drawerState = bottomDrawerState,
            drawerContent = {
                TagPicker(
                    tags = tags,
                    onTagSelect = { tag ->
                        viewModel.onContactEntryChange(contactEntry.copy(tag = tag))
                    }
                )
            }
        ) {
            SaveContactContent(
                contact = contactEntry,
                onContactChange = { updateContactEntry ->
                    viewModel.onContactEntryChange(updateContactEntry)
                }
            )
        }

        if (moveContactToTrashDialogShownState.value) {
            AlertDialog(
                onDismissRequest = {
                    moveContactToTrashDialogShownState.value = false
                },
                title = {
                    Text("Move to the trash?")
                },
                text = {
                    Text(
                        "Are you sure you want to " +
                                "delete this contact?"
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.moveContactToTrash(contactEntry)
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        moveContactToTrashDialogShownState.value = false
                    }) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }
}

@Composable
fun SaveContactTopAppBar(
    isEditingMode: Boolean,
    onBackClick: () -> Unit,
    onSaveContactClick: () -> Unit,
    onOpenTagPickerClick: () -> Unit,
    onDeleteContactClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Save Contact",
                color = MaterialTheme.colors.onPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        actions = {
            IconButton(onClick = onSaveContactClick) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save Note Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            IconButton(onClick = onOpenTagPickerClick) {
                Icon(
                    painter = painterResource(id = R.drawable.bookmark ),
                    contentDescription = "Open Color Picker Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            if (isEditingMode) {
                IconButton(onClick = onDeleteContactClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Contact Button",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    )
}

@Composable
private fun SaveContactContent(
    contact: ContactModel,
    onContactChange: (ContactModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val isChecked: Boolean = contact.isFriend

        FavoriteCheckOption(
            isChecked = isChecked,
            onCheckedChange = { FavoriteNewValue ->
                val isCheckedOff: Boolean = if (FavoriteNewValue) true else false

                onContactChange.invoke(contact.copy(isFriend = isCheckedOff))
            }
        )
        ContentTextField(
            label = "Name",
            text = contact.name,
            onTextChange = { newTitle ->
                onContactChange.invoke(contact.copy(name = newTitle))
            }
        )

        ContentTextField(
            modifier = Modifier
                .heightIn(max = 240.dp)
                .padding(top = 16.dp),
            label = "PhoneNumber",
            text = contact.phoneNumber,
            onTextChange = { newContent ->
                onContactChange.invoke(contact.copy(phoneNumber = newContent))
            },
        )

        ContentTextField(
            modifier = Modifier
                .heightIn(max = 240.dp)
                .padding(top = 16.dp),
            label = "Notes",
            text = contact.notes,
            onTextChange = { newContent ->
                onContactChange.invoke(contact.copy(phoneNumber = newContent))
            }
        )

        PickedTag(color = contact.tag)
    }
}

@Composable
private fun ContentTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        textStyle = androidx.compose.ui.text.TextStyle(
            fontFamily = Comfortaa,
            fontWeight = FontWeight.Normal,
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
    )
}
@Composable
private fun FavoriteCheckOption(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .padding(8.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Add to your favorites",
            modifier = Modifier.weight(1f),
            fontFamily = Comfortaa,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .align(alignment = Alignment.CenterVertically),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.DarkGray,
                uncheckedThumbColor = Color.DarkGray,
                checkedTrackColor = Color(0x789CCE),
                uncheckedTrackColor = Color(0x789CCE),
            )
        )
    }
}

@Composable
private fun PickedTag(color: TagModel) {
    Row(
        Modifier
            .padding(8.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Picked Tag : " + color.name,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            fontFamily = Comfortaa,
            fontWeight = FontWeight.Normal
        )
        ContactColor(
            color = Color.fromHex(color.hex),
            size = 40.dp,
            border = 1.dp,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
private fun TagPicker(
    tags: List<TagModel>,
    onTagSelect: (TagModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Tag picker",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp),
            fontFamily = Comfortaa,
            color = Color.fromHex("#341D17")
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(tags.size) { itemIndex ->
                val color = tags[itemIndex]
                TagItem(
                    color = color,
                    onColorSelect = onTagSelect
                )
            }
        }
    }
}

@Composable
fun TagItem(
    color: TagModel,
    onColorSelect: (TagModel) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onColorSelect(color)
                }
            )
    ) {
        ContactColor(
            modifier = Modifier.padding(10.dp),
            color = Color.fromHex(color.hex),
            size = 80.dp,
            border = 2.dp
        )
        Text(
            text = color.name,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterVertically),
            fontFamily = Comfortaa,
            fontWeight = FontWeight.Normal,
            color = Color.fromHex("#341D17")
        )
    }
}

@Preview
@Composable
fun TagItemPreview() {
    TagItem(TagModel.DEFAULT) {}
}

@Preview
@Composable
fun TagPickerPreview() {
    TagPicker(
        tags = listOf(
            TagModel.DEFAULT,
            TagModel.DEFAULT,
            TagModel.DEFAULT
        )
    ) { }
}

@Preview
@Composable
fun PickedTagPreview() {
    PickedTag(TagModel.DEFAULT)
}