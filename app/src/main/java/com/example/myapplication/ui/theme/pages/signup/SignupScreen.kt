package com.example.myapplication.ui.theme.pages.signup

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
import com.example.myapplication.ui.theme.pages.login.LoginViewModel
import com.example.myapplication.ui.theme.ptSans
import com.google.firebase.firestore.GeoPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(signupViewModel: SignUpViewModel,
                 onNavToHomePage: () -> Unit, onNavToLogInPage: () -> Unit,
                 geoPoint: GeoPoint
){

    val signupUiState by signupViewModel.uiState.collectAsState()
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
                text = "Signup",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = ptSans,
                color = White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 50.dp)
            )

            TextField(
                singleLine = true,
                value = signupViewModel.userFirst,
                onValueChange = {signupViewModel.updateUserFirst(it)},
                isError = signupUiState.emptyFirst,
                placeholder = {
                    if (signupUiState.emptyFirst) {
                        Text(text = "First is empty", color = Color.Red)
                    } else {
                        Text(text = "First", color = PlaceHolderGrey)
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
            )

            Spacer(modifier = Modifier.padding(start = 0.dp,0.dp,0.dp,30.dp))
            TextField(
                singleLine = true,
                value = signupViewModel.userLast,
                onValueChange = {signupViewModel.updateUserLast(it)},
                isError = signupUiState.emptyLast,
                placeholder = {
                    if (signupUiState.emptyLast) {
                        Text(text = "Last is empty", color = Color.Red)
                    } else {
                        Text(text = "Last", color = PlaceHolderGrey)
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
            )

            Spacer(modifier = Modifier.padding(start = 0.dp,0.dp,0.dp,30.dp))
            TextField(
                singleLine = true,
                value = signupViewModel.userEmail,
                onValueChange = {signupViewModel.updateUserEmail(it)},
                isError = signupUiState.invalidEmail,
                placeholder = {
                    if (signupUiState.invalidEmail) {
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
            TextField(
                singleLine = true,
                value =  signupViewModel.userPassword,
                onValueChange = { signupViewModel.updateUserPassword(it)} ,
                isError = signupUiState.invalidPassword,
                placeholder = {
                    if (signupUiState.invalidPassword) {
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
                    onDone = { if (signupViewModel.checkValidUser() && signupViewModel.checkValidName()){
                        signupViewModel.logUser(context)
                    } }
                ),
                colors = TextFieldDefaults.textFieldColors(containerColor = White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent),

                )

            Spacer(modifier = Modifier.padding(start = 0.dp,0.dp,0.dp,35.dp))

            Button(
                onClick = { if (signupViewModel.checkValidUser() && signupViewModel.checkValidName()){
                    signupViewModel.logUser(context)
                }

                 },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .width(244.dp)
                    .height(54.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Green)
            ) {
                Text(text = "Create account",
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
                        Text(text = "Already have an account? ",
                            color = ORGrey,
                            fontSize = 15.sp,
                            fontFamily = ptSans,)
                        ClickableText(text = AnnotatedString("Login"),
                            onClick = {onNavToLogInPage.invoke()},
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = ptSans,
                                color = CreateBlue,)
                        )
                    }
                }
            }
        }
        LaunchedEffect(key1 = signupViewModel.hasUser){
            if (signupViewModel.hasUser == true ){
                signupViewModel.addProfile(geoPoint)
                onNavToHomePage.invoke()
            }
        }
    }
}
