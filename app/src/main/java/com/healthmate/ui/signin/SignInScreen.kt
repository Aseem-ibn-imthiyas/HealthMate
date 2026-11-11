package com.healthmate.ui.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BackgroundColor = Color(0xFFF7F8FF)
private val PrimaryBlue = Color(0xFF2563EB)
private val HeadingColor = Color(0xFF0B1F38)
private val SecondaryColor = Color(0xFF52627A)
private val InputBorderColor = Color(0xFFF6E8E8)

@Composable
fun SignInScreen(
    onSignInClick: (String, String) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 35.dp)
    ) {

        // HealthMate Branding
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        Color(0xFFDCE8FC),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(PrimaryBlue, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "HealthMate logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text(
                    text = "Health",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = HeadingColor
                )

                Text(
                    text = "Mate",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }
        }

        // Sign In Form
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 72.dp)
        ) {

            Text(
                text = "Sign In",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = HeadingColor
            )

            Text(
                text = "Sign in to securely access health metrics.",
                fontSize = 16.sp,
                color = SecondaryColor
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Email Address
            Text(
                text = "Email Address",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = HeadingColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            SignInInputField(
                value = email,
                onValueChange = { email = it },
                placeholder = "name@example.com",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = SecondaryColor,
                        modifier = Modifier.size(20.dp)
                    )
                },
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password
            Text(
                text = "Password",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = HeadingColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            SignInInputField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Enter your password",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = SecondaryColor,
                        modifier = Modifier.size(20.dp)
                    )
                },
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = "Toggle password visibility",
                        tint = SecondaryColor,
                        modifier = Modifier
                            .size(22.dp)
                            .clickable {
                                passwordVisible = !passwordVisible
                            }
                    )
                }
            )

            // Forgot Password
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Forgot Password?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryBlue,
                    modifier = Modifier.clickable {
                        onForgotPasswordClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In Button
            Button(
                onClick = {
                    onSignInClick(email.trim(), password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Sign Up Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = 14.sp,
                    color = SecondaryColor
                )

                Text(
                    text = "Sign Up",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryBlue,
                    modifier = Modifier.clickable {
                        onSignUpClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun SignInInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable () -> Unit,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = InputBorderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        leadingIcon()

        Spacer(modifier = Modifier.width(13.dp))

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    fontSize = 14.sp,
                    color = Color(0xFF73788A)
                )
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = HeadingColor
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType
                ),
                visualTransformation = visualTransformation
            )
        }

        if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(10.dp))
            trailingIcon()
        }
    }
}
