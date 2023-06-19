package com.example.myapplication.ui.theme.pages.messages

import android.content.ContentValues
import android.os.Message
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.Messenger
import com.example.myapplication.R
import com.example.myapplication.ui.theme.Green
import com.example.myapplication.ui.theme.LastSentColor
import com.example.myapplication.ui.theme.LightGrey
import com.example.myapplication.ui.theme.MessageGrey
import com.example.myapplication.ui.theme.PageDividerColor
import com.example.myapplication.ui.theme.PlaceHolderGrey
import com.example.myapplication.ui.theme.White
import com.example.myapplication.ui.theme.interRegular
import com.example.myapplication.ui.theme.interSemiBold
import com.example.myapplication.ui.theme.pages.Routes
import com.example.myapplication.ui.theme.pages.home.FriendRequestCard
import com.example.myapplication.ui.theme.pages.login.LoginViewModel
import com.example.myapplication.ui.theme.pages.profile.ProfileViewModel
import com.example.myapplication.ui.theme.pages.messages.MessageViewModel
import com.example.myapplication.ui.theme.ptSans
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.livedata.observeAsState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

var dataList by mutableStateOf<List<com.example.myapplication.ui.theme.pages.messages.Message>>(emptyList())

@Composable
fun MessageScreenTwo(onNavToMessengerPage: () -> Unit,
                     loginViewModel: LoginViewModel,
                     profileViewModel: ProfileViewModel,
                     messageViewModel: MessageViewModel
)
{
    val message: String by messageViewModel.message.observeAsState(initial = "")

    val messages: List<Map<String, Any>> by messageViewModel.messages.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableList()
    )
    messageViewModel.getMessagesStored()

    /*messageViewModel.getMessages(targetId){ updatedDataList ->

        dataList = updatedDataList
    }*/
    val messengerUiState by messageViewModel.muiState.collectAsState()

    val context = LocalContext.current

    if (messageViewModel.hasUser){
        messageViewModel.getDocument()
    } else {
        Toast.makeText(context, "Error: No user currently logged on.", Toast.LENGTH_SHORT).show()

    }
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
            IconButton(onClick = { onNavToMessengerPage.invoke()}) {
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
                contentAlignment = Alignment.TopCenter
            )
            {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 0.dp, 10.dp, 0.dp, 0.dp)) {
                    Header(messengers = Messengers(R.drawable.profile, "Name"),
                        onNavToMessengerPage = onNavToMessengerPage,
                        messageViewModel = messageViewModel
                    )
                    MessageBody(onNavToMessengerPage = onNavToMessengerPage,
                        messengers = Messengers(R.drawable.profile, "Name"),
                        messageViewModel = messageViewModel,
                        messages = messages
                    )
                    MessagingTool(messageViewModel = messageViewModel,
                        message = message

                        )
                }



            }
        }
    }
}

@Composable
fun Header(messengers: Messengers,
           onNavToMessengerPage: () -> Unit,
           messageViewModel: MessageViewModel){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, 0.dp, 0.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(75.dp)
                    .height(75.dp)
                    .clip(shape = RoundedCornerShape(40.dp))
                    .background(color = LightGrey)
                    .clickable { }, //  Story or Profile feature,
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Past messages",
                    modifier = Modifier
                        .size(50.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            )
            {
                var name = messageViewModel.getField("userFirst") ?:
                "UserName"
                if (name.isEmpty()){
                    name = "UserName"
                }
                Text(
                    text = name,
                    fontFamily = interSemiBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = "Active Status",
                    fontFamily = interRegular,
                    fontSize = 14.sp,
                    color = LastSentColor
                )
            }

        }
    }
    Divider(color = PageDividerColor, thickness = 1.dp)
}


@Composable
fun MessageBody(onNavToMessengerPage: () -> Unit,
                messengers: Messengers,
                messageViewModel: MessageViewModel,
                messages: List<Map<String, Any>>
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .background(color = White)
    ){

        Log.d(ContentValues.TAG, "Message list2: $dataList")
        LazyColumn(contentPadding = PaddingValues(10.dp),
            reverseLayout = true,
            modifier = Modifier.fillMaxSize()) {
            items(messages) { item ->
                MessageCard(item, messageViewModel)

            }
        }


    }
}

@Composable
fun MessageCard(messages: Map<String, Any>,
    messageViewModel: MessageViewModel
){
    var arr:Arrangement.Horizontal = Arrangement.Start
    if (messages["sender"].toString() == Firebase.auth.currentUser!!.uid){
        arr = Arrangement.End
    }
    if (messages["sender"].toString() != Firebase.auth.currentUser!!.uid) {
        messageViewModel.getTargetDocument(messages["sender"].toString())
    }
    Row(modifier = Modifier.padding(all = 8.dp).fillMaxWidth(),
    horizontalArrangement = arr) {
        if (messages["sender"].toString() != Firebase.auth.currentUser!!.uid) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .clip(shape = RoundedCornerShape(40.dp))
                    .background(color = LightGrey)
                    .clickable { }, //  Story or Profile feature,
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, color = Color.Transparent, CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            if (messages["sender"].toString() != Firebase.auth.currentUser!!.uid) {
                var name = messageViewModel.getTargetField("userFirst") ?:
                "UserName"
                if (name.isEmpty()){
                    name = "FriendName"
                }
                Text(
                    text = name,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = ptSans,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            var isExpanded by remember { mutableStateOf(false) }

            Card(elevation =  CardDefaults.cardElevation(defaultElevation =(3.dp)),
                colors = CardDefaults.cardColors(containerColor = Green),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.clickable { isExpanded = !isExpanded }
            ) {
                    Text(
                        text = messages["message"].toString(),
                        fontFamily = ptSans,
                        fontSize = 16.sp,
                        color = White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(all = 4.dp),
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1
                    )
                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingTool(messageViewModel: MessageViewModel,
                  message: String,


){
    Divider(color = PageDividerColor, thickness = 1.dp)
    Box(modifier = Modifier.fillMaxSize()
        .background(MessageGrey),
    contentAlignment = Alignment.Center
    )
    {
        TextField(singleLine = true,
            value = message,
            onValueChange = {messageViewModel.updateMessageValue(it)},
            placeholder = {Text(text = "Message", color = PlaceHolderGrey)},
            textStyle = TextStyle(fontFamily = ptSans, fontSize = 15.sp ),
            modifier = Modifier
                .width(350.dp)
                .height(54.dp),
            shape = RoundedCornerShape(30.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {messageViewModel.addMessages(messageViewModel.userTargetedMessenger) }
        ))
    }
}