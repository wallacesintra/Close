package com.example.close.presentation.friends.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.close.R
import com.example.close.presentation.components.Loading
import com.example.close.presentation.components.MediumFriendProfileContainer
import com.example.close.presentation.components.MediumText
import com.example.close.presentation.friends.viewmodels.SearchUserViewModel
import com.example.close.presentation.models.profileImagesMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUser(
    currentUserUid: String,
    searchUserViewModel: SearchUserViewModel,
    friendsUIDList: List<String>,
    sendFriendRequestAction: (String) -> Unit,
    goToFriendProfile: (String) -> Unit
){

    val searchText by searchUserViewModel.searchText.collectAsState()
    val closeUsers by searchUserViewModel.closeUsers.collectAsState()
    val isSearching by searchUserViewModel.isSearching.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    var friendUID by remember {
        mutableStateOf("")
    }


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
                cursorColor = MaterialTheme.colorScheme.secondary,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                           Icon(
                               imageVector = Icons.Default.Search,
                               tint = MaterialTheme.colorScheme.secondary,
                               contentDescription = stringResource(
                                   id = R.string.search_user
                               )
                           )
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

            LazyColumn(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                items(closeUsers) { user ->
                    MediumFriendProfileContainer(
                        userUid = user.uid,
                        username = user.username,
                        imgResId = profileImagesMap[user.profileImg]!!.imgResId,
                        goToFriendProfile = {
                            friendUID = user.uid
                            showBottomSheet = true
//                            goToFriendProfile(user.uid)
                                            },
                    )
                }
            }
        }

        if (showBottomSheet){
            ModalBottomSheet(
                onDismissRequest = {showBottomSheet = false},
                sheetState = sheetState,
                modifier = Modifier
                    .weight(1.0f)
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                FriendProfileScreen(friendUid = friendUID , currentUserUid = currentUserUid, friendsUIDList = friendsUIDList ) {

                    sendFriendRequestAction(friendUID)
                }

            }
        }

        

    }
}