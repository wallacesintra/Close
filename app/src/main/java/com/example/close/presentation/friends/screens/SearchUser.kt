package com.example.close.presentation.friends.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.close.R
import com.example.close.presentation.components.Loading
import com.example.close.presentation.components.MediumFriendProfileContainer
import com.example.close.presentation.components.MediumText
import com.example.close.presentation.profile.viewmodels.SearchUserViewModel

@Composable
fun SearchUser(
    currentUserUid: String,
    searchUserViewModel: SearchUserViewModel,
    goToFriendProfile: (String) -> Unit
){

    val searchText by searchUserViewModel.searchText.collectAsState()
    val closeUsers by searchUserViewModel.closeUsers.collectAsState()
    val isSearching by searchUserViewModel.isSearching.collectAsState()


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxSize()
    ) {
        TextField(
            value = searchText,
            placeholder = { Text(text = stringResource(id = R.string.search_user))},
            onValueChange = searchUserViewModel::onSearchTextChange,
            shape = RoundedCornerShape(40.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                           Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(
                               id = R.string.search_user
                           ))
            },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(0.99f)
        )


        if (isSearching){
            Loading()
        }else {
            if (closeUsers.isEmpty() && searchText.isNotEmpty()){
                MediumText(text = stringResource(id = R.string.no_user_found), modifier = Modifier.padding(10.dp))
            }

            LazyColumn {
                items(closeUsers) { user ->
                    MediumFriendProfileContainer(
                        userUid = user.uid,
                        username = user.username,
                        goToFriendProfile = goToFriendProfile
                    )
                }
            }
        }

        

    }
}