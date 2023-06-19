package com.example.myapplication.ui.theme.pages.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.ui.theme.LighterGrey
import com.example.myapplication.ui.theme.ProfileGrey
import com.example.myapplication.ui.theme.ProfileGreyInput
import com.example.myapplication.ui.theme.White
import com.example.myapplication.ui.theme.pages.Routes
import com.example.myapplication.ui.theme.pages.login.LoginUIState
import com.example.myapplication.ui.theme.ptSans
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(User: LoginUIState,
                  onNavToHomePage: () -> Unit,
                  profileViewModel: ProfileViewModel,
                  currentUserLocation: LatLng) {

    val profileUiState by profileViewModel.puiState.collectAsState()
    val context = LocalContext.current
    if (profileViewModel.hasUser){
        profileViewModel.getDocument()
    } else {
        Toast.makeText(context, "Error: No user currently logged on.", Toast.LENGTH_SHORT).show()

    }

    val local = profileViewModel.getLocation("location") ?: GeoPoint(0.0, 0.0)

    var userLocal  = LatLng(local.latitude, local.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocal, 15f)
    }


    Text(text = currentUserLocation.longitude.toString(), color = Color.Black)
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {

        Marker(
            state = MarkerState(position = userLocal),
            title = currentUserLocation.longitude.toString(),
            snippet = currentUserLocation.latitude.toString()
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        // Update with location

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            IconButton(onClick = {
                profileViewModel.resetProfile()
                onNavToHomePage.invoke() }) {
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
                    ),
                contentAlignment = Alignment.Center
            )
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                )
                {

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
                                .size(250.dp),

                            )
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Bottom
                        )
                        {
                            // User name
                            BasicTextField(value = profileViewModel.userName,
                                onValueChange = { profileViewModel.updateUserName(it) },
                                textStyle = TextStyle(
                                    color = White,
                                    fontFamily = ptSans,
                                    fontSize = 55.sp,
                                    fontWeight = FontWeight.ExtraBold
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = LighterGrey),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier.padding(start = 15.dp, 0.dp, 0.dp, 0.dp)
                                    ) {

                                        if (profileUiState.userFullName.isEmpty()) {
                                            var name = profileViewModel.getField("userFirst") ?:
                                            "UserName"
                                            if (name.isEmpty()){
                                                name = "UserName"
                                            }
                                            Text(
                                                text = name
                                                         ,
                                                color = White,
                                                fontSize = 55.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontFamily = ptSans,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                        }
                                        innerTextField()
                                    }
                                },
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        profileViewModel.updateSingle("userFirst",
                                            profileUiState.userFullName)}
                                ))

                            // User University
                            BasicTextField(value = profileViewModel.userUniversity,
                                onValueChange = { profileViewModel.updateUserUniversity(it) },
                                textStyle = TextStyle(
                                    color = White,
                                    fontFamily = ptSans,
                                    fontSize = 45.sp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = LighterGrey),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier.padding(start = 15.dp, 0.dp, 0.dp, 5.dp)
                                    ) {

                                        if (profileUiState.userUniversity.isEmpty()) {

                                            var uni = profileViewModel.getField("userUniversity") ?:
                                            "University"
                                            if (uni.isEmpty()){
                                                uni = "University"
                                            }
                                            Text(
                                                text = uni,
                                                color = White,
                                                fontSize = 45.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = ptSans,
                                                overflow = TextOverflow.Ellipsis
                                            )

                                        }
                                        innerTextField()
                                    }
                                },
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        profileViewModel.updateSingle("userUniversity",
                                            profileUiState.userUniversity)}
                                ))
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(color = White)
                            .verticalScroll(rememberScrollState()),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(modifier = Modifier.padding(vertical = 15.dp))
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically) {

                                // Handle user age input
                                TextField(
                                    leadingIcon = { Text(
                                        text = "Age:",
                                        fontFamily = ptSans, fontSize = 25.sp,
                                        modifier = Modifier.padding(start = 15.dp, 0.dp, 0.dp, 0.dp),
                                        color = Color.Black
                                    ) },
                                    singleLine = true,
                                    value = profileViewModel.userAge,
                                    onValueChange = { profileViewModel.updateUserAge(it) },
                                    textStyle = TextStyle(fontFamily = ptSans, fontSize = 25.sp),
                                    modifier = Modifier
                                        .width(150.dp)
                                        .height(60.dp),
                                    shape = RoundedCornerShape(30.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = ProfileGreyInput,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        errorIndicatorColor = Color.Transparent
                                    ),
                                    placeholder = {if (
                                        profileUiState.userAge.isEmpty()) {
                                        Text(text = profileViewModel.getField("userAge") ?:
                                        ""
                                            ,fontFamily = ptSans, fontSize = 25.sp)
                                    }},
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            profileViewModel.updateSingle("userAge",
                                            profileUiState.userAge)}
                                    )
                                )

                                Spacer(modifier = Modifier.padding(30.dp))

                                // Handle user sex input
                                TextField(
                                    leadingIcon = { Text(
                                        text = "Sex:",
                                        fontFamily = ptSans, fontSize = 25.sp,
                                        modifier = Modifier.padding(start = 15.dp, 0.dp, 0.dp, 0.dp),
                                        color = Color.Black
                                    ) },
                                    singleLine = true,
                                    value = profileViewModel.userSex,
                                    onValueChange = {profileViewModel.updateUserSex(it)},
                                    textStyle = TextStyle(fontFamily = ptSans, fontSize = 25.sp),
                                    modifier = Modifier
                                        .width(150.dp)
                                        .height(60.dp),
                                    shape = RoundedCornerShape(30.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = ProfileGreyInput,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        errorIndicatorColor = Color.Transparent
                                    ),
                                    placeholder = {if (
                                        profileUiState.userSex.isEmpty()) {
                                        Text(text = profileViewModel.getField("userSex") ?:
                                        ""
                                            ,fontFamily = ptSans, fontSize = 25.sp)
                                    }},
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            profileViewModel.updateSingle("userSex",
                                                profileUiState.userSex)}
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.padding(vertical = 5.dp))

                            // Handle user hometown input
                            TextField( leadingIcon = { Text(
                                text = "Hometown:",
                                fontFamily = ptSans, fontSize = 25.sp,
                                modifier = Modifier.padding(start = 15.dp, 0.dp, 0.dp, 0.dp),
                                color = Color.Black
                            ) },
                                singleLine = true,
                                value = profileViewModel.userHomeTown,
                                onValueChange = {profileViewModel.updateUserHomeTown(it)},
                                textStyle = TextStyle(fontFamily = ptSans, fontSize = 25.sp),
                                modifier = Modifier
                                    .width(360.dp)
                                    .height(60.dp),
                                shape = RoundedCornerShape(30.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = ProfileGreyInput,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    errorIndicatorColor = Color.Transparent
                                ),
                                placeholder = {if (
                                    profileUiState.userHomeTown.isEmpty()) {
                                    Text(text = profileViewModel.getField("userHomeTown") ?:
                                    ""
                                        ,fontFamily = ptSans, fontSize = 25.sp)
                                }},
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        profileViewModel.updateSingle("userHomeTown",
                                            profileUiState.userHomeTown)}
                                )
                            )

                            Spacer(modifier = Modifier.padding(vertical = 5.dp))

                            // Handle user interest input
                            TextField( leadingIcon = { Text(
                                text = "Interest:",
                                fontFamily = ptSans, fontSize = 25.sp,
                                modifier = Modifier.padding(start = 15.dp, 0.dp, 0.dp, 0.dp),
                                color = Color.Black
                            ) },
                                singleLine = true,
                                value = profileViewModel.userInterest,
                                onValueChange = {profileViewModel.updateUserInterest(it)},
                                textStyle = TextStyle(fontFamily = ptSans, fontSize = 25.sp),
                                modifier = Modifier
                                    .width(360.dp)
                                    .height(60.dp),
                                shape = RoundedCornerShape(30.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = ProfileGreyInput,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    errorIndicatorColor = Color.Transparent
                                ),
                                placeholder = {if (
                                    profileUiState.userInterest.isEmpty()) {
                                    Text(text = profileViewModel.getField("userInterest") ?:
                                    ""
                                        ,fontFamily = ptSans, fontSize = 25.sp,
                                        overflow = TextOverflow.Ellipsis)
                                }},
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        profileViewModel.updateSingle("userInterest",
                                            profileUiState.userInterest)}
                                )
                            )

                            Spacer(modifier = Modifier.padding(vertical = 5.dp))

                            // Handle user bio input
                            TextField( leadingIcon = { Text(
                                text = "Bio:",
                                fontFamily = ptSans, fontSize = 25.sp,
                                modifier = Modifier.padding(start = 15.dp, 0.dp, 0.dp, 0.dp),
                                color = Color.Black
                            ) },
                                value = profileViewModel.userBio,
                                onValueChange = {profileViewModel.updateUserBio(it)},
                                textStyle = TextStyle(fontFamily = ptSans, fontSize = 25.sp),
                                modifier = Modifier
                                    .width(360.dp)
                                    ,
                                shape = RoundedCornerShape(30.dp),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = ProfileGreyInput,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    errorIndicatorColor = Color.Transparent
                                ),
                                placeholder = {if (
                                    profileUiState.userBio.isEmpty()) {
                                    Text(text = profileViewModel.getField("userBio") ?:
                                    ""
                                        ,fontFamily = ptSans, fontSize = 25.sp,
                                        overflow = TextOverflow.Ellipsis,)
                                }},
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        profileViewModel.updateSingle("userBio",
                                            profileUiState.userBio)}
                                )
                            )
                        }

                    }
                }
            }
        }


    }

}

