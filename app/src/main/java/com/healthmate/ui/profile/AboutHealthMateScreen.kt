
package com.healthmate.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AboutBackground = Color(0xFFF7F8FF)
private val AboutBlue = Color(0xFF2563EB)
private val AboutDark = Color(0xFF102641)
private val AboutSecondary = Color(0xFF596780)
private val AboutPaleBlue = Color(0xFFE5EEFF)

@Composable
fun AboutHealthMateScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AboutBackground)
            .statusBarsPadding()
    ) {

        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(34.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to Settings",
                    tint = AboutDark,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "Settings",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = AboutDark
            )
        }

        // Main Layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            // Application Logo
            Box(
                modifier = Modifier.size(86.dp),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(17.dp))
                        .background(AboutBlue),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonitorHeart,
                            contentDescription = "HealthMate logo",
                            tint = Color.White,
                            modifier = Modifier.size(29.dp)
                        )
                    }
                }

                // Verified Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-1).dp, y = 1.dp)
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified application",
                        tint = AboutBlue,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            // App Name
            Text(
                text = "HealthMate",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = AboutDark
            )

            Spacer(modifier = Modifier.height(3.dp))

            // Tagline
            Text(
                text = "Your Health, Your Journey",
                fontSize = 14.sp,
                color = AboutSecondary
            )

            Spacer(modifier = Modifier.height(9.dp))

            // Version Information
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                AboutInfoBadge(
                    text = "Version 1.0.0"
                )

                Spacer(modifier = Modifier.width(7.dp))

                AboutInfoBadge(
                    text = "Build 1"
                )
            }

            Spacer(modifier = Modifier.height(9.dp))

            // Development Information
            Text(
                text = "Developed as part of Mobile Application Development",
                fontSize = 12.sp,
                color = AboutSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(3.dp))

            // Copyright
            Text(
                text = "Copyright © 2026 HealthMate",
                fontSize = 11.sp,
                color = AboutSecondary
            )

            Spacer(modifier = Modifier.weight(1f))

            // Made With Care Footer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 72.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(AboutPaleBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Made with care",
                        tint = AboutBlue,
                        modifier = Modifier.size(19.dp)
                    )
                }

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = "Made with Care",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = AboutDark
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Helping build healthier habits, one step at a time.",
                    fontSize = 13.sp,
                    color = AboutSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun AboutInfoBadge(
    text: String
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFFDCE8FA))
            .padding(
                horizontal = 11.dp,
                vertical = 4.dp
            )
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = AboutDark
        )
    }
}
