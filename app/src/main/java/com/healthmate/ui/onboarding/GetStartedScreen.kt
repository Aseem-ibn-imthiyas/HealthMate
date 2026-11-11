package com.healthmate.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthmate.R

private val BackgroundColor = Color(0xFFFFFFFF)
private val PrimaryBlue = Color(0xFF2563EB)
private val HeadingColor = Color(0xFF0B1F38)
private val SecondaryColor = Color(0xFF52627A)

@Composable
fun GetStartedScreen(
    onGetStartedClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // HealthMate Logo
        Column(
            modifier = Modifier
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
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "HealthMate Logo",
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            PrimaryBlue,
                            CircleShape
                        )
                        .padding(9.dp)
                )
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

        Spacer(modifier = Modifier.weight(0.65f))

        // Main Illustration
        Image(
            painter = painterResource(
                id = R.drawable.onboarding_illustration
            ),
            contentDescription = "Person running illustration",
            modifier = Modifier
                .fillMaxWidth()
                .weight(2.4f),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Main Heading
        Text(
            text = "Your Health, Your Journey",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = HeadingColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        Text(
            text = "Track your daily activities and build healthier habits, one step at a time.",
            fontSize = 16.sp,
            lineHeight = 25.sp,
            color = SecondaryColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(0.45f))

        // Get Started Button
        Button(
            onClick = onGetStartedClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .shadow(
                    elevation = 7.dp,
                    shape = RoundedCornerShape(30.dp),
                    ambientColor = PrimaryBlue.copy(alpha = 0.15f),
                    spotColor = PrimaryBlue.copy(alpha = 0.20f)
                ),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Get Started",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Footer
        Text(
            text = "Small steps. Healthier days.",
            fontSize = 12.sp,
            color = SecondaryColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
