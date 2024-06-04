package com.selegend.ecommercemobile.ui.profile

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.selegend.ecommercemobile.ui.profile.components.EditTextField
import com.selegend.ecommercemobile.ui.profile.components.NonEditTextField
import com.selegend.ecommercemobile.ui.user.UserViewState
import com.selegend.ecommercemobile.ui.utils.UIEvent

@Composable
fun ProfileScreen(
    sharedState: UserViewState,
    onPopBackStack: () -> Unit,
    onNavigate: (UIEvent.Navigate) -> Unit,
    context: Context,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val initialStateOfUri = null
    var firstName by remember { mutableStateOf("") }

    var lastName by remember { mutableStateOf("") }

    var address by remember { mutableStateOf("") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(initialStateOfUri) }

    val photoPickerLancher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImageUri = it
        }
    )

    val isCanSave = remember {
        derivedStateOf {
            firstName.isNotEmpty()
                    || lastName.isNotEmpty()
                    || address.isNotEmpty()
                    || selectedImageUri != null
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = {
            androidx.compose.material.TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                backgroundColor = Color.White,
                elevation = 0.dp,
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                onPopBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back"
                            )
                        }
                        Text(
                            text = "Edit Profile",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
                    }
                }
            }
        },
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = if (selectedImageUri != null) selectedImageUri else "${sharedState.user?.image}",
                contentDescription = null,
                modifier = Modifier
                    .height(100.dp)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(50))
                    .aspectRatio(1f)
                    .border(
                        1.dp,
                        Color.Gray.copy(alpha = 0.5f),
                        RoundedCornerShape(50)
                    ),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                onClick = {
                    photoPickerLancher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Pick a photo", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    EditTextField(
                        text = firstName!!,
                        label = "First Name",
                        placeholder = sharedState.user!!.firstName,
                        onValueChange = { firstName = it }
                    )
                }
                item {
                    EditTextField(
                        text = lastName!!,
                        label = "Last Name",
                        placeholder = sharedState.user!!.lastName,
                        onValueChange = { lastName = it }
                    )
                }
                item {
                    EditTextField(
                        text = address!!,
                        label = "Address",
                        placeholder = if (sharedState.user?.address == null) "Enter your address" else sharedState.user.address,
                        onValueChange = { address = it }
                    )
                }
                item {
                    NonEditTextField(label = "Username", text = sharedState.user!!.username)
                }
                item {
                    sharedState.user?.phoneNumber?.let { it ->
                        NonEditTextField(
                            label = "Phone Number",
                            text = it
                        )
                    }
                }
                item {
                    NonEditTextField(label = "Email", text = sharedState.user!!.email)
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        onClick = {
                            if (isCanSave.value) {
                                if (selectedImageUri == null) {
                                    val map = mapOf(
                                        "firstName" to firstName,
                                        "lastName" to lastName,
                                        "address" to address
                                    )
                                    viewModel.onEvent(
                                        ProfileEvent.OnProfileUpdateCredentials(
                                            map,
                                            context
                                        )
                                    )
                                } else {
                                    val map = mapOf(
                                        "firstName" to firstName,
                                        "lastName" to lastName,
                                        "address" to address,
                                    )
                                    viewModel.onEvent(
                                        ProfileEvent.OnProfileUpdate(
                                            selectedImageUri!!,
                                            "image",
                                            map,
                                            context
                                        )
                                    )
                                }
                            }
                        }) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Save changes", fontSize = 16.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}