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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.example.close.R
import com.example.close.presentation.auth.models.AuthState
import com.example.close.presentation.auth.viewmodel.AuthViewModel
import com.example.close.presentation.auth.viewmodel.SignInSignUpViewModel
import com.example.close.presentation.components.GoBack

@Composable
fun SignIn(
    authViewModel: AuthViewModel,
    goBackEvent: () -> Unit,
    goToSignUp: () -> Unit,
    signInSignUpViewModel: SignInSignUpViewModel,
    goToProfile: () -> Unit,
){
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var correctEmailFormat by remember {
        mutableStateOf(false)
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var signInButtonClicked by remember {
        mutableStateOf(false)
    }

    if (signInButtonClicked){
        LaunchedEffect(key1 = Unit) {

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            GoBack(goBackEvent = goBackEvent)
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.sign_in_welcome),
                fontSize = 30.sp,
                fontWeight = FontWeight.W700,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(id = R.string.sign_in_account),
                fontWeight = FontWeight.W300
            )
            Spacer(modifier = Modifier.height(25.dp))


            TextField(
                value = email,
                onValueChange = {
                    email = it
                    correctEmailFormat = signInSignUpViewModel.isValidEmail(email)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = stringResource(id = R.string.icon_email),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                label = {
                    if (!correctEmailFormat && email != ""){
                        Text(text = stringResource(id = R.string.email_placeholder))
                    }else{
                        Text(text = stringResource(id = R.string.sign_up_email))
                    }
                },
                isError = (!correctEmailFormat && email != ""),
                placeholder = { Text(text = stringResource(id = R.string.email_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
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
                trailingIcon = {
                    Icon(
                        painter = if (!passwordVisible) painterResource(id = R.drawable.visible) else painterResource(
                            id = R.drawable.not_visible
                        ),
                        contentDescription = stringResource(id = R.string.password_visible),
                        modifier = Modifier
                            .clickable(
                                onClick = { passwordVisible = !passwordVisible}
                            )
                    )
                },

                label = { Text(text = stringResource(id = R.string.password)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
            )




        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {

                        authViewModel.signInExistingAccountWithEmailAndPassword(email, password)
                        goToProfile()
                              },
//                    onClick = {signInButtonClicked = true},
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.sign_in))
                }

                Row(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.no_account),
                        modifier = Modifier.padding(horizontal = 6.dp),
                        fontWeight = FontWeight.W300

                    )
                    Text(
                        text = stringResource(id = R.string.sign_up),
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(
                            onClick = goToSignUp
                        )
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInPreview(){
//    SignIn({})
}