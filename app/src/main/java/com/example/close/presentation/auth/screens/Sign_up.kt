package com.example.close.presentation.auth.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.close.R
import com.example.close.presentation.auth.viewmodel.AuthViewModel
import com.example.close.presentation.components.GoBack


@Composable
fun SignUp(
    goBackEvent: () -> Unit,
    authViewModel: AuthViewModel
){

    var username by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {

        Box(
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            GoBack(goBackEvent = goBackEvent)
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.register),
                fontSize = 30.sp,
                fontWeight = FontWeight.W700,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(id = R.string.create_account),
                fontWeight = FontWeight.W300
            )
            Spacer(modifier = Modifier.height(25.dp))


            TextField(
                value = username,
                onValueChange = { username = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.icon_person),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                label = {
                    Text(text = stringResource(id = R.string.sign_up_username))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = stringResource(id = R.string.icon_email),
                        tint = MaterialTheme.colorScheme.primary

                    )
                },
                placeholder = { Text(text = stringResource(id = R.string.email_placeholder)) },
                label = {
                    Text(text = stringResource(id = R.string.sign_up_email))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)

            )


            TextField(
                value = password,
                onValueChange = { password = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(id = R.string.icon_password),
                        tint = MaterialTheme.colorScheme.primary

                    )
                },
                label = {
                    Text(text = stringResource(id = R.string.sign_up_password))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)

            )

            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(id = R.string.icon_password),
                        tint = MaterialTheme.colorScheme.primary

                    )
                },
                label = {
                    Text(text = stringResource(id = R.string.sign_up_confirm_password))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)

            )

            Button(
                onClick = { authViewModel.createNewAccountWithEmailAndPassword(username, email, password)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Text(text = stringResource(id = R.string.sign_up))

            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(10.dp)
        ) {
            Row {
                Text(
                    text = stringResource(id = R.string.have_account),
                    modifier = Modifier.padding(horizontal = 6.dp),
                    fontWeight = FontWeight.W300

                )
                Text(
                    text = stringResource(id = R.string.sign_in),
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(
                        onClick = {}
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignupPreview(){
//    SignUp {
//        {}
//    }
}