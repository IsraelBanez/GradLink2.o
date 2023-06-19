package com.example.myapplication.ui.theme.pages.messages

import android.content.ContentValues
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.ui.theme.LightGrey
import com.example.myapplication.ui.theme.White
import com.example.myapplication.ui.theme.pages.Routes
import com.example.myapplication.ui.theme.pages.login.LoginViewModel
import com.example.myapplication.ui.theme.pages.profile.ProfileViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.myapplication.Messenger
import com.example.myapplication.ui.theme.Green
import com.example.myapplication.ui.theme.LastSentColor
import com.example.myapplication.ui.theme.interBold
import com.example.myapplication.ui.theme.interRegular
import com.example.myapplication.ui.theme.interSemiBold
import com.example.myapplication.ui.theme.ptSans
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


var dataListM by mutableStateOf<List<Messenger>>(emptyList())
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(onNavToHomePage: () -> Unit,
                  onNavToMessengersTwoPage: () -> Unit,
                  loginViewModel: LoginViewModel,
                  profileViewModel: ProfileViewModel,
                  messageViewModel: MessageViewModel
)
{
    val messengerUiState by messageViewModel.muiState.collectAsState()
    val local = messageViewModel.getLocation("location") ?: GeoPoint(0.0, 0.0)

    var userLocal  = LatLng(local.latitude, local.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocal, 15f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {

        Marker(
            state = MarkerState(position = userLocal),
            title = local.longitude.toString(),
            snippet = local.latitude.toString()
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            IconButton(onClick = {onNavToHomePage.invoke() }) {
                Image(painter = painterResource(id = R.drawable.up_arrow),
                    contentDescription = "Return Home",
                    modifier = Modifier.rotate(270f))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 50.dp,
                            topEnd = 50.dp
                        )
                    )
                    .background(color = White),
                contentAlignment = Alignment.Center
            )
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                )
                {
                    Text(text = "Messages", fontFamily = interBold, fontSize = 30.sp, color = Green)

                    Spacer(modifier = Modifier.padding(10.dp))

                    // Handle Stories
                    MessengerStoryList()

                    Spacer(modifier = Modifier.padding(10.dp))

                    // Handle Search Query
                    TextField(
                        value = messageViewModel.userSearch,
                        onValueChange = {messageViewModel.updateUserSearch(it)},
                        leadingIcon = { Image(painter = painterResource(id = R.drawable.search),
                            contentDescription = "Search bar icon")},
                        textStyle = TextStyle(fontFamily = interRegular, fontSize = 12.sp, color = White),
                        modifier = Modifier
                            .width(310.dp)
                            .height(45.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = TextFieldDefaults.textFieldColors(containerColor = LightGrey,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done),)

                    Spacer(modifier = Modifier.padding(10.dp))
                    FriendList { updatedDataList ->

                        dataListM = updatedDataList
                    }
                    MessengerHistoryList( onNavToMessengersTwoPage, messageViewModel)
                }
            }
        }
    }
}


@Composable
fun MessengerStoryList(){
    LazyRow(contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.fillMaxWidth()){
        items(dataListM) { messenger ->
            if (messenger.uid != Firebase.auth.currentUser!!.uid) {
                MessengerStories(messenger = messenger)
            }
        }
    }
}

@Composable
fun MessengerStories(messenger: Messenger){
    Box(modifier = Modifier
        .width(80.dp)
        .height(80.dp)
        .clip(shape = RoundedCornerShape(40.dp))
        .background(color = LightGrey),
        contentAlignment = Alignment.Center
    ){
        Image(painter = painterResource(id = R.drawable.profile_2),
            contentDescription = "Story Profiles",
            modifier = Modifier
                .size(50.dp)
                .clickable { }, // Story feature
            contentScale = ContentScale.Crop
        )

    }
}

@Composable
fun FriendList(callback: (List<Messenger>) -> Unit){
    val db = Firebase.firestore
    val collectionRef = db.collection("users")
    var dataList =  mutableListOf<Messenger>()
    var data: Messenger
    collectionRef.get()
        .addOnSuccessListener { querySnapshot ->
            val updatedDataList = mutableListOf<Messenger>()
            for (document in querySnapshot.documents) {

                data = document.toObject<Messenger>()!!
                dataList.add(document.toObject<Messenger>()!!)
                updatedDataList.add(data)

            }
            Log.d(ContentValues.TAG, "DocumentSnapshot datalist: $dataList")

            callback(dataList)

        }
        .addOnFailureListener {
            Log.d(ContentValues.TAG, "DocumentSnapshot data: Error")
        }

}


@Composable
fun MessengerHistoryList( onNavToMessengersTwoPage: () -> Unit, messageViewModel: MessageViewModel){
    LazyColumn(contentPadding = PaddingValues(10.dp)){
        items(dataListM) { messenger ->
            if (messenger.uid != Firebase.auth.currentUser!!.uid) {
                MessengerHistory(
                    messenger = messenger,
                    onNavToMessengersTwoPage = onNavToMessengersTwoPage,
                    messageViewModel = messageViewModel
                )
            }
        }
    }
}

@Composable
fun MessengerHistory(messenger: Messenger, onNavToMessengersTwoPage: () -> Unit, messageViewModel: MessageViewModel){
    var context = LocalContext.current
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .clickable {
            messageViewModel.updateTargetMessenger(messenger.uid)
            // Just to make sure that I am connecting to the right users
            /*Toast
                .makeText(context, messageViewModel.userTargetedMessenger, Toast.LENGTH_SHORT)
                .show()*/

            onNavToMessengersTwoPage.invoke()
        }) {// Go to MessengerScreenTwo
        Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .clip(shape = RoundedCornerShape(40.dp))
                .background(color = LightGrey),
                contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.profile_2),
                    contentDescription = "Past messages",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { }, // Story feature
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Column(verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start)
            {
                var name = messenger.userFirst
                if (name.isEmpty()){
                    name = "UserName"
                }
                Text(text = name,
                    fontFamily = interSemiBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(text = "Most Recent chat",
                fontFamily = interRegular,
                fontSize = 14.sp,
                color = LastSentColor
                )
            }

        }
    }
}