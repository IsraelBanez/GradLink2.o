package com.example.myapplication.ui.theme.pages.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


import com.example.myapplication.R
import com.example.myapplication.ui.theme.CreateBlue
import com.example.myapplication.ui.theme.Green
import com.example.myapplication.ui.theme.ORGrey
import com.example.myapplication.ui.theme.PlaceHolderGrey
import com.example.myapplication.ui.theme.White
import com.example.myapplication.ui.theme.pages.Routes
import com.example.myapplication.ui.theme.ptSans
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(loginViewModel: LoginViewModel,
                onNavToHomePage: () -> Unit, onNavToSignUpPage: () -> Unit,
){

    val loginUiState by loginViewModel.uiState.collectAsState()
    val context = LocalContext.current


    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.gradient_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Text(
                text = "Login",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = ptSans,
                color = White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 50.dp)
            )

            // User's email
            TextField(
                singleLine = true,
                value = loginViewModel.userEmail,
                onValueChange = {loginViewModel.updateUserEmail(it)},
                isError = loginUiState.invalidEmail,
                placeholder = {
                    if (loginUiState.invalidEmail) {
                        Text(text = "Invalid Email", color = Color.Red)
                    } else {
                        Text(text = "Email", color = PlaceHolderGrey)
                    }
                } ,
                textStyle = TextStyle(fontFamily = ptSans, fontSize = 15.sp ),
                modifier = Modifier
                    .width(244.dp)
                    .height(54.dp),
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.textFieldColors(containerColor = White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next),
            )


            Spacer(modifier = Modifier.padding(start = 0.dp,0.dp,0.dp,30.dp))

            // User's password
            TextField(
                singleLine = true,
                value =  loginViewModel.userPassword,
                onValueChange = { loginViewModel.updateUserPassword(it)} ,
                isError = loginUiState.invalidPassword,
                placeholder = {
                    if (loginUiState.invalidPassword) {
                        Text(text = "Invalid Password", color = Color.Red)
                    } else {
                        Text(text = "Password", color = PlaceHolderGrey)
                    }
                } ,
                textStyle = TextStyle(fontFamily = ptSans, fontSize = 15.sp ),
                modifier = Modifier
                    .width(244.dp)
                    .height(54.dp),
                shape = RoundedCornerShape(30.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { if (loginViewModel.checkValidUser()){
                        loginViewModel.authenticateUser(context)
                        onNavToHomePage.invoke()
                    } }
                ),
                colors = TextFieldDefaults.textFieldColors(containerColor = White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent),
            )

            ClickableText(text = AnnotatedString("Forgot Password?"),
                onClick = {},
                style = TextStyle(fontSize = 15.sp,
                    fontFamily = ptSans,
                    color = White,
                    textAlign = TextAlign.Right),
                modifier = Modifier
                    .width(244.dp)
                    .padding(start = 0.dp, 0.dp, 10.dp, 0.dp)

            )

            Spacer(modifier = Modifier.padding(start = 0.dp,0.dp,0.dp,35.dp))
            Button(
                onClick = {if (loginViewModel.checkValidUser() ){
                    loginViewModel.authenticateUser(context)
                    onNavToHomePage.invoke()
                } },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .width(244.dp)
                    .height(54.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Green)
            ) {
                Text(text = "Login",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = ptSans,
                    color = White,
                    textAlign = TextAlign.Center,)
            }
            Spacer(modifier = Modifier.padding(start = 0.dp,0.dp,0.dp,50.dp))
            Box(modifier = Modifier
                .fillMaxSize()
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp
                    )
                )
                .background(color = White),

                )
            {
                Column() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 50.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(1.dp)
                                .padding(start = 0.dp, 0.dp, 5.dp, 0.dp)
                                .background(color = ORGrey)
                        )
                        Text(
                            text = "OR",
                            color = ORGrey,
                            fontSize = 12.sp,
                            fontFamily = ptSans,
                        )
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(1.dp)
                                .padding(start = 5.dp, 0.dp, 0.dp, 0.dp)
                                .background(color = ORGrey)
                        )
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center) {
                        Text(text = "New? ",
                            color = ORGrey,
                            fontSize = 15.sp,
                            fontFamily = ptSans,)
                        ClickableText(text = AnnotatedString("Create an account"),
                            onClick = {onNavToSignUpPage.invoke()},
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = ptSans,
                                color = CreateBlue,)
                        )
                    }
                }
            }
        }
        // Need to fix as I have to click twice to re-log in; loggin in once automatically
        // brings me to home page, but a second or third (etc), I need to click twice
        // Update: This was "fixed" bye invoking page change immediately after authentication above
        LaunchedEffect(key1 = loginViewModel.hasUser){
            if (loginViewModel.hasUser == true){
                onNavToHomePage.invoke()
            }
        }
    }
}
