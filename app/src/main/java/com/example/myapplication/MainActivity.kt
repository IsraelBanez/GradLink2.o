package com.example.myapplication

import android.Manifest
import android.content.ContentProviderClient
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.AuthRepository
import com.example.myapplication.data.AuthRepositoryImpl
import com.example.myapplication.data.StorageRepositoryImpl
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.pages.login.LoginViewModel
import com.example.myapplication.ui.theme.pages.Routing
import com.example.myapplication.ui.theme.pages.home.HomeScreenViewModel
import com.example.myapplication.ui.theme.pages.messages.MessageViewModel
import com.example.myapplication.ui.theme.pages.profile.ProfileViewModel
import com.example.myapplication.ui.theme.pages.settings.SettingViewModel
import com.example.myapplication.ui.theme.pages.signup.SignUpViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.proto.TargetGlobal


class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var auth: FirebaseAuth

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val db = Firebase.firestore
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    // Ask Permission for location and get long and lat
                    var currentUserLocation by remember { mutableStateOf(LatLng(0.0,0.0)) }
                    var geoPoint by remember { mutableStateOf(GeoPoint(0.0,0.0)) }
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


                    if (ActivityCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat
                            .checkSelfPermission(
                                this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ),
                            101
                        )
                    }

                    if (auth.currentUser == null) {
                        Toast.makeText(this, "Hit", Toast.LENGTH_SHORT).show()

                    }
                    fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                        val location: Location? = task.result
                        if (location == null) {
                            Toast.makeText(this, "Location Error", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Location Successful", Toast.LENGTH_SHORT)
                                .show()
                            val lat = location.latitude
                            val long = location.longitude
                            // Update data class with location data
                            currentUserLocation = LatLng(lat, long)
                            geoPoint = GeoPoint(location.latitude, location.longitude)
                        }
                    }

                    Routing(
                        loginViewModel = LoginViewModel(authRepositoryImpl = AuthRepositoryImpl(auth)),
                        signUpViewModel = SignUpViewModel(authRepositoryImpl = AuthRepositoryImpl(auth),
                        storageRepositoryImpl = StorageRepositoryImpl(fireStore = db, auth = auth)
                        ),
                        profileViewModel = ProfileViewModel(authRepositoryImpl = AuthRepositoryImpl(auth),
                            storageRepositoryImpl = StorageRepositoryImpl(fireStore = db, auth = auth)),
                        settingViewModel = SettingViewModel(authRepositoryImpl = AuthRepositoryImpl(auth)),
                        messageViewModel = MessageViewModel(authRepositoryImpl = AuthRepositoryImpl(auth),
                            storageRepositoryImpl = StorageRepositoryImpl(fireStore = db, auth = auth)),
                        homeScreenViewModel = HomeScreenViewModel(authRepositoryImpl = AuthRepositoryImpl(auth),
                            storageRepositoryImpl = StorageRepositoryImpl(fireStore = db, auth = auth)),
                        currentUserLocation = currentUserLocation,
                     geoPoint = geoPoint
                    )
                }
            }
        }

    }
}

// Ignore these starter composables

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}