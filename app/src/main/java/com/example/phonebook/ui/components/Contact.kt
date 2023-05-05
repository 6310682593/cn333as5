package com.example.phonebook.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.ui.theme.Comfortaa
import com.example.phonebook.util.fromHex

@ExperimentalMaterialApi
@Composable
fun Contact(
    modifier: Modifier = Modifier,
    contact: ContactModel,
    onContactClick: (ContactModel) -> Unit = {},
    onContactCheckedChange: (ContactModel) -> Unit = {},
    isSelected: Boolean
) {
    val background = if (isSelected)
        Color.LightGray
    else
        MaterialTheme.colors.surface

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = Color.fromHex("#EDE9E8")
    ) {
        ListItem(
            text = { Text(text = contact.name,
                maxLines = 1,
                fontFamily = Comfortaa,
                fontWeight = FontWeight.Bold,
                color = Color.fromHex("#341D17"))},
            secondaryText = {
                Text(text = contact.tag.name,
                    maxLines = 1,
                    fontFamily = Comfortaa,
                    fontWeight = FontWeight.Light,
                    color = Color.fromHex("#341D17"))
            },
            icon = {
                ContactColor(
                    color = Color.fromHex(contact.tag.hex),
                    size = 40.dp,
                    border = 1.dp
                )
            },
            modifier = Modifier.clickable {
                onContactClick.invoke(contact)
            }
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun ContactPreview() {
    Contact(contact = ContactModel(1, "Enzyme", "0123456789", "",false), isSelected = true)
}