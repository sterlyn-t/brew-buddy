package com.example.brewbuddy

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brewbuddy.ui.theme.InterFont
import com.example.brewbuddy.ui.theme.OrangeBrownMedium
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.brewbuddy.profile.CurrentUserViewModel
import com.example.brewbuddy.ui.theme.GreenMedium
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

sealed class AccessScreens(val route: String, @StringRes val resourceId: Int) {
    object Login : AccessScreens("Profile", R.string.login_route)
    object Register : AccessScreens("Register", R.string.register_route)
}

@Composable
fun FormWrapper(content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        content = content
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {

    val currentUserViewModel: CurrentUserViewModel = viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    val nonWhitespaceFilter = remember { Regex("^[^\n ]*\$")}
    val alphanumericFilter = remember { Regex("[a-zA-Z0-9]*")}

    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    val errorMsg = remember {mutableStateOf("")}

    val focusRequester = remember {FocusRequester()}
    val passwordFocusRequester = remember {FocusRequester()}
    var isLoginEnabled by remember {mutableStateOf(false)}

    Surface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        FormWrapper() {
            Title()
            TextField(
                value = username,
                onValueChange = {
                    if(it.text. matches(alphanumericFilter)){
                        username = it
                    }
                },
                placeholder = { Text(text = "Username")},
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        passwordFocusRequester.requestFocus()
                    }
                ),
                modifier = Modifier.focusRequester(focusRequester)
            )
            TextField(
                value = password,
                onValueChange = {
                    if(it.text. matches(nonWhitespaceFilter)){
                        password = it
                    }

                    isLoginEnabled = username.text.isNotBlank() && password.text.isNotBlank()
                },
                placeholder = { Text(text = "Password")},
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isLoginEnabled) {
                            loginUser(username.text, password.text, isLoginEnabled, errorMsg, currentUserViewModel)
                        }
                    }
                ),
                modifier = Modifier.focusRequester(passwordFocusRequester)
            )
            Button(
                onClick = { loginUser(username.text, password.text, isLoginEnabled, errorMsg, currentUserViewModel) },
                colors = ButtonDefaults.buttonColors(containerColor = GreenMedium),
                modifier= Modifier.size(width=280.dp, height=40.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("LOGIN")
            }
            ErrorMessage(errorMsg.value)
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick={navController.navigate(AccessScreens.Register.route)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor=MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "Sign up")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val currentUserViewModel: CurrentUserViewModel = viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    val nonWhitespaceFilter = remember { Regex("^[^\n]*\$")}
    val alphanumericFilter = remember { Regex("[a-zA-Z0-9]*")}

    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }

    val errorMsg = remember {mutableStateOf("")}
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.navigate(AccessScreens.Login.route)}) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back")
            }
        }

        FormWrapper() {
            Title()
            TextField(
                value = username,
                onValueChange = {
                    if(it.text. matches(alphanumericFilter)){
                        username = it
                    }
                },
                placeholder = { Text(text = "Username")},
            )
            TextField(
                value = password,
                onValueChange = {
                    if(it.text. matches(nonWhitespaceFilter)){
                        password = it
                    }
                },
                placeholder = { Text(text = "Password")},
                visualTransformation = PasswordVisualTransformation()
            )
            TextField(
                value = confirmPassword,
                onValueChange = {
                    if(it.text. matches(nonWhitespaceFilter)){
                        confirmPassword = it
                    }
                },
                placeholder = { Text(text = "Confirm Password")},
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = {
                    Log.d("REGISTER_USER", username.text)

                    Log.d("REGISTER_PWD", password.text)
                    Log.d("REGISTER_CONF_PWD", confirmPassword.text)

                    errorMsg.value = if (password.text != confirmPassword.text) {
                        "Passwords do not match."
                    } else if(
                        !currentUserViewModel.registerUser(
                            username.text,
                            password.text)
                    ) {
                        "Username is already taken"
                    } else {
                        ""
                    }
                    Log.d("REGISTER", errorMsg.value)
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenMedium),
                modifier= Modifier.size(width=280.dp, height=40.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("REGISTER")
            }
            ErrorMessage(errorMsg.value)
        }
    }
}

@Composable
fun ErrorMessage(text: String) {
    Text(text=text, color=Color.Red)
}
@Composable
fun Title(color: Color = OrangeBrownMedium) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(text="brew", style=MaterialTheme.typography.titleLarge, color= color)
        Text(text="buddy", style=TextStyle(
            fontFamily = InterFont,
            fontSize = 32.sp,
            letterSpacing = 0.sp
        ), color= color)
    }
}
@Composable
fun AccessScreen() {
    val navController = rememberNavController()
    val vmStoreOwner = rememberViewModelStoreOwner()

    Surface() {
        CompositionLocalProvider(
            LocalNavGraphViewModelStoreOwner provides vmStoreOwner
        ) {
            NavHost(navController, startDestination = AccessScreens.Login.route) {
                composable(AccessScreens.Login.route) {
                    LoginScreen(navController)
                }
                composable(AccessScreens.Register.route) {
                    RegisterScreen(navController)
                }

            }

        }
    }
}

private fun loginUser(username: String, password: String, isLoginEnabled: Boolean, errorMsg: MutableState<String>, currentUserViewModel: CurrentUserViewModel) {
    if (isLoginEnabled) {
        errorMsg.value = if (!currentUserViewModel.loginUser(username, password)) {
            "Incorrect password or username."
        } else {
            ""
        }
    }
}