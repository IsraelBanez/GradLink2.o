package com.example.myapplication.ui.theme.pages.home

import android.app.PendingIntent.getActivity
import androidx.compose.foundation.lazy.items
import android.content.ContentValues
import android.util.Log
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.Messenger
import com.example.myapplication.R
import com.example.myapplication.data.MAIN_COLLECTION_REF
import com.example.myapplication.ui.theme.DropDownColor
import com.example.myapplication.ui.theme.FriendWhite
import com.example.myapplication.ui.theme.Green
import com.example.myapplication.ui.theme.LightGrey
import com.example.myapplication.ui.theme.LighterGrey
import com.example.myapplication.ui.theme.ProfileGrey
import com.example.myapplication.ui.theme.ProfileGreyInput
import com.example.myapplication.ui.theme.White
import com.example.myapplication.ui.theme.pages.login.LoginUIState
import com.example.myapplication.ui.theme.ptSans
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.tasks.await



var dataList by mutableStateOf<List<Messenger>>(emptyList())
@Composable
fun HomeScreen(homeScreenViewModel: HomeScreenViewModel,
               onNavToSettingPage: () -> Unit,
               onNavToPrivacyPage: () -> Unit,
               onNavToProfilePage: () -> Unit,
               onNavToMessengerPage: () -> Unit,
               currentUserLocation: LatLng)
{

    var expandedRight by remember { mutableStateOf(false) }
    var expandedLeft by remember { mutableStateOf(false) }
    var friendRequest by remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (homeScreenViewModel.hasUser){
        homeScreenViewModel.getDocument()
    } else {
        Toast.makeText(context, "Loading User", Toast.LENGTH_SHORT).show()

    }
    val local = homeScreenViewModel.getLocation("location") ?: GeoPoint(0.0, 0.0)

    var userLocal  = LatLng(local.latitude, local.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocal, 15f)
    }

// -120.660085 35.2977283


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
        /*Image(
            painter = painterResource(id = R.drawable.gradient_background),
            contentDescription = "Location of user",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )*/

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                 )
            {
                // Left menu bar
                Box(modifier = Modifier
                    .height(150.dp)
                    .width(100.dp)
                    .padding(start = 0.dp, 22.dp, 0.dp, 0.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        IconButton(
                            onClick = {expandedLeft = true },
                            modifier = Modifier
                                .height(55.dp)
                                .width(55.dp)
                                .clip(shape = RoundedCornerShape(35.dp))
                                .background(color = LightGrey)
                        )
                        {
                            Image(
                                painter = painterResource(id = R.drawable.menu_left),
                                contentDescription = "Menu left",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        MaterialTheme(
                            colorScheme = MaterialTheme.colorScheme.copy(surface = LightGrey),
                            shapes = MaterialTheme.shapes.copy( RoundedCornerShape(25.dp)),

                            ){
                        DropdownMenu(
                            expanded = expandedLeft,
                            onDismissRequest = { expandedLeft = false },
                            modifier = Modifier.width(55.dp),
                        ) {
                            DropdownMenuItem(
                                text = {},
                                onClick = { onNavToPrivacyPage.invoke() },
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.privacy_policy),
                                        contentDescription = "Privacy Policy",
                                        modifier = Modifier.size(32.dp)
                                    )
                                })
                            DropdownMenuItem(
                                text = {},
                                onClick = { onNavToSettingPage.invoke() },
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.settings),
                                        contentDescription = "Settings",
                                        modifier = Modifier.size(32.dp)
                                    )
                                })

                        }
                    }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Right menu bar
                Box(modifier = Modifier
                    .height(150.dp)
                    .width(100.dp)
                    .padding(start = 0.dp, 22.dp, 0.dp, 0.dp),
                contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier
                            .height(
                                if (!expandedRight) {
                                    105.dp
                                } else {
                                    55.dp //// fix later
                                }
                            )
                            .width(55.dp)
                            .clip(shape = RoundedCornerShape(50.dp))
                            .background(color = LightGrey),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        IconButton(
                            onClick = {onNavToProfilePage.invoke()},
                            modifier = Modifier
                                .height(55.dp)
                                .width(55.dp)
                                .clip(shape = RoundedCornerShape(35.dp))
                                .background(color = LightGrey)
                        )
                        {
                            Image(
                                painter = painterResource(id = R.drawable.profile),
                                contentDescription = "Profile",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        IconButton(
                            onClick = { expandedRight = true },
                            modifier = Modifier
                                .height(50.dp)
                                .width(50.dp)
                        )
                        {
                            if (!expandedRight) {
                                Image(
                                    painter = painterResource(id = R.drawable.application),
                                    contentDescription = "Applications",
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                        }
                        // Handle floating menu
                        var yRight = 0.dp

                        MaterialTheme(
                            colorScheme = MaterialTheme.colorScheme.copy(surface = LightGrey),
                            shapes = MaterialTheme.shapes.copy( RoundedCornerShape(25.dp)),

                        ){
                        DropdownMenu(
                            expanded = expandedRight,
                            onDismissRequest = { expandedRight = false },
                            modifier = Modifier.width(55.dp),
                            offset = DpOffset(x = 0.dp, y = -yRight)
                        ) {
                            DropdownMenuItem(
                                text = {},
                                onClick = { /* Handle edit! */ },
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.notifications),
                                        contentDescription = "Notifications",
                                        modifier = Modifier.size(32.dp)
                                    )
                                })
                            DropdownMenuItem(
                                text = {},
                                onClick = { onNavToMessengerPage.invoke() },
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.message_white),
                                        contentDescription = "Messages",
                                        modifier = Modifier.size(32.dp)
                                    )
                                })
                            DropdownMenuItem(
                                text = {},
                                onClick = { friendRequest = !friendRequest  },
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.friend_request),
                                        contentDescription = "Friend request",
                                        modifier = Modifier.size(32.dp)
                                    )
                                })
                            DropdownMenuItem(
                                text = {},
                                onClick = { /* Handle send feedback! */ },
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.add_stories),
                                        contentDescription = "Add stories",
                                        modifier = Modifier.size(32.dp)
                                    )
                                })
                            DropdownMenuItem(
                                text = {},
                                onClick = { expandedRight = false },
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.up_arrow),
                                        contentDescription = "close menu right",
                                        modifier = Modifier.size(32.dp)
                                    )
                                })
                        }
                    }
                    }
                }

            }

            Spacer(modifier = Modifier.weight(1f))


            Box(modifier = Modifier
                .fillMaxWidth()
                .height(
                    if (!friendRequest) {
                        150.dp
                    } else {
                        500.dp
                    }
                )
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp
                    )
                )

                .background(color = White),
                contentAlignment = Alignment.TopCenter,
                ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = { friendRequest = !friendRequest },
                        modifier = Modifier
                            .padding(start = 0.dp, 10.dp, 0.dp, 0.dp)
                            .height(45.dp)
                            .width(45.dp)
                            .rotate(90f)) {
                        Image(painter = painterResource(id = R.drawable.main_request),
                            contentDescription = "Friend Request Icon")
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    if (friendRequest){




                        FriendRequestApp { updatedDataList ->
                            // Update the dataList with the received data
                            dataList = updatedDataList
                        }
                        Log.d(ContentValues.TAG, "DocumentSnapshot list2: $dataList")
                        LazyRow(contentPadding = PaddingValues(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            modifier = Modifier.width(388.dp)) {
                            items(dataList) { item ->

                                if (item.uid != Firebase.auth.currentUser!!.uid) {
                                    FriendRequestCard(item)
                                }
                            }
                        }

                    }
                }

            }



        }
    }

}

@Composable
fun FriendRequestApp(callback: (List<Messenger>) -> Unit){
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

                // Process the retrieved data
                Log.d(ContentValues.TAG, "DocumentSnapshot data: $data")
            }
            Log.d(ContentValues.TAG, "DocumentSnapshot datalist: $dataList")

            callback(dataList)

        }
        .addOnFailureListener { exception ->
            // Handle any errors that occurred while retrieving the data
            Log.d(ContentValues.TAG, "DocumentSnapshot data: Error")
        }

    //FriendRequestList(dataList)
}


@Composable
fun FriendRequestCard(messenger: Messenger){
    Box(
        modifier = Modifier
            .width(250.dp)
            .height(400.dp)
            .clip(
                shape = RoundedCornerShape(
                    20.dp
                )
            )
            .border(width = 1.dp, color = ProfileGrey, shape = RoundedCornerShape(20.dp))
        ,
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(color = ProfileGrey)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.profile_2),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Profile Pictures",
                    modifier = Modifier
                        .size(150.dp),

                    )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Bottom
                )
                {
                    var name = messenger.userFirst
                    if (name.isEmpty()){
                        name = "UserName"
                    }
                    Text(text = name,
                        color = White,
                        fontFamily = ptSans,
                        fontSize = 25.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = LighterGrey))

                    // university
                    var uni = messenger.userUniversity
                    if (uni.isEmpty()){
                        uni = "University"
                    }
                    Text(text = uni,
                        color = White,
                        fontFamily = ptSans,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = LighterGrey))
                }

            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(color = White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Age: ${messenger.userAge}",
                            fontFamily = ptSans, fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(90.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .padding(start = 0.dp, 0.dp, 1.dp, 0.dp)
                            .background(color = ProfileGreyInput)
                            )
                        Spacer(modifier = Modifier.padding(15.dp))
                        Text(text = "Sex: ${messenger.userSex}",
                            fontFamily = ptSans, fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(90.dp)
                                .clip(RoundedCornerShape(40.dp))
                                .padding(start = 0.dp, 0.dp, 1.dp, 0.dp)
                                .background(color = ProfileGreyInput)
                        )
                    }
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    Text(text = "Hometown: ${messenger.userHomeTown}",
                        fontFamily = ptSans, fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(210.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .padding(start = 0.dp, 0.dp, 1.dp, 0.dp)
                            .background(color = ProfileGreyInput)
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    Text(text = "Interest: ${messenger.userInterest}",
                        fontFamily = ptSans, fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(210.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .padding(start = 0.dp, 0.dp, 1.dp, 0.dp)
                            .background(color = ProfileGreyInput)
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    Text(text = "Bio: ${messenger.userBio}",
                        fontFamily = ptSans, fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(210.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .padding(start = 0.dp, 0.dp, 1.dp, 0.dp)
                            .background(color = ProfileGreyInput) ,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis

                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Button(onClick = { /*TODO*/ },
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Green),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .width(100.dp)
                            .height(30.dp)
                    ) {
                        Text(text = "Request",
                            color = White,
                            fontFamily = ptSans,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,)
                    }
                }
            }
        }
    }
}