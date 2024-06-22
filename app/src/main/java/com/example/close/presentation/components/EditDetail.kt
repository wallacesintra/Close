package com.example.close.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.close.R

@Composable
fun EditDetail(
    modifier: Modifier = Modifier,
    detailToEdit: String,
    detailValue: String,
    confirmDetail: (String) -> Unit = {},
    painter: Painter,
){

    var detailToEditValue by remember {
        mutableStateOf(detailValue)
    }

    var enableEdit by remember {
        mutableStateOf(false)
    }

    var confirmClick by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(confirmClick) {
        if (confirmClick) {
            confirmDetail(detailToEditValue)
            confirmClick = false
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                Icon(
                    painter = painter,
                    contentDescription = "edit $detailToEdit",
                    modifier = modifier
                        .padding(10.dp)
                        .size(30.dp)
                )

                Column {


                    Text(
                        text = detailToEdit,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                        modifier = Modifier.padding(horizontal = 14.dp )
                    )

                    TextField(
                        value = detailToEditValue,
                        onValueChange = { detailToEditValue = it },
                        enabled = enableEdit,
                        maxLines = 2,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            disabledTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                }

            }

            Box(
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                IconButton(
                    onClick = { enableEdit = !enableEdit },
                    modifier = modifier.padding(6.dp)
                ) {

                    if (!enableEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "edit $detailToEdit",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "confirm new $detailToEdit",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = modifier
                                .clickable(
                                    onClick = {
                                        confirmClick = true
                                        enableEdit = false
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun EditDetailPreview(){
    EditDetail(
        detailToEdit = "username",
        detailValue = "wallacesintra",
        painter = painterResource(id = R.drawable.person)
    )
}


