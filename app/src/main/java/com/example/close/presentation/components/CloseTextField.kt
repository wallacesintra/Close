package com.example.close.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.close.R

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CloseTextField(){
    TextField(
        value = "",
        onValueChange = {},
        placeholder =  { Text(text = "")},
        shape = RoundedCornerShape(40.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(
                    id = R.string.search_user
                )
            )
        },
        trailingIcon = {
            Icon(imageVector = Icons.Default.Done, contentDescription = "")
        },
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(0.99f)
    )
}