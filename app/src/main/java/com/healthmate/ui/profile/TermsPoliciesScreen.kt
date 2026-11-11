
package com.healthmate.ui.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TermsBackground = Color(0xFFF7F8FF)
private val TermsBlue = Color(0xFF0752CC)
private val TermsHeading = Color(0xFF31558F)
private val TermsDark = Color(0xFF102641)
private val TermsSecondary = Color(0xFF596780)
private val TermsCard = Color(0xFFEDF2FF)

@Composable
fun TermsPoliciesScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TermsBackground)
            .statusBarsPadding()
    ) {

        // Top Settings Header
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
                    tint = TermsDark,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(5.dp))

            Text(
                text = "Settings",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = TermsDark
            )
        }

        // Long Scrollable Terms Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
        ) {

            Spacer(Modifier.height(15.dp))

            // Terms Introduction Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(TermsCard)
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {

                Box(
                    modifier = Modifier
                        .size(43.dp)
                        .background(
                            Color(0xFFD8E7FF),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = TermsBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Terms of Services",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TermsDark
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "These terms outline your legal rights, responsibilities, key health disclaimers using HealthMate.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = TermsSecondary
                    )
                }
            }

            Spacer(Modifier.height(26.dp))

            // Section 01
            TermsSection(
                number = "01",
                title = "General Terms",
                content = """
                    By accessing and using HealthMate, you agree to comply with these Terms of Service. These terms govern your access to and use of the application's health tracking, record management, and community features. You agree to use HealthMate responsibly and provide accurate information when recording your daily health activities. You are responsible for maintaining the confidentiality of your account credentials and for all activities performed through your account. HealthMate is intended for personal health and wellness tracking purposes. Users must not misuse the application, interfere with its functionality, or use it to distribute harmful, misleading, or inappropriate content.
                """.trimIndent()
            )

            // Section 02
            TermsSection(
                number = "02",
                title = "Health Information Disclaimer",
                content = """
                    HealthMate is designed to help users monitor and manage general daily wellness activities, including steps walked, calories burned, and water intake. The application is not intended to provide medical advice, diagnosis, or treatment. The information displayed should not replace professional medical guidance. Users should consult qualified healthcare professionals when making decisions regarding their health.
                """.trimIndent()
            )

            // Section 03
            TermsSection(
                number = "03",
                title = "User Responsibilities",
                content = """
                    Provide accurate information when using the application, protect their account details, use the application appropriately, and respect other members when participating in community activities. Users must not attempt to access another person's account, upload harmful content, or misuse the application's features.
                """.trimIndent()
            )

            // Section 04
            TermsSection(
                number = "04",
                title = "Community Guidelines",
                content = """
                    When participating in HealthMate communities and group challenges, members are expected to maintain respectful communication, avoid harassment or discrimination, share constructive and appropriate health discussions, and strictly respect the privacy and health telemetry of fellow members. Any abusive, defamatory, or deceptive conduct will result in immediate suspension or termination of community privileges.
                """.trimIndent()
            )

            // Section 05
            TermsSection(
                number = "05",
                title = "Data and Privacy",
                content = """
                    HealthMate takes the security of your personal health telemetry seriously. Your account information, activity telemetry (steps, calories, hydration), and health logs are stored and processed in compliance with relevant data privacy standards. For exhaustive details regarding how your personal information is collected, stored, encrypted, and shared, please refer to our full Privacy Policy.
                """.trimIndent()
            )

            // Section 06
            TermsSection(
                number = "06",
                title = "Application Availability & Maintenance",
                content = """
                    HealthMate strives to provide continuous service availability; however, features, sync integrations, or server telemetry may occasionally be updated, modified, or temporarily unavailable due to scheduled maintenance, software updates, or unexpected outages. We reserve the right to modify or discontinue features at our discretion.
                """.trimIndent()
            )

            // Section 07
            TermsSection(
                number = "07",
                title = "Changes to Terms",
                content = """
                    HealthMate reserves the right to amend, update, or replace these Terms of Service at any time. When material changes are made, we will notify users through in-app notices or registered email alerts. Continued use of HealthMate following notice of revisions constitutes your agreement to the modified terms.
                """.trimIndent()
            )

            // Section 08
            Column(
                modifier = Modifier.padding(bottom = 25.dp)
            ) {

                Text(
                    text = "SECTION 08",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TermsBlue,
                    letterSpacing = 0.5.sp
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Contact Information & Support",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TermsHeading
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = buildAnnotatedString {
                        append(
                            "If you have any questions, feedback, or legal inquiries concerning these Terms of Service, please reach out to our dedicated compliance and support team at "
                        )

                        withStyle(
                            SpanStyle(
                                color = TermsBlue,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Medium
                            )
                        ) {
                            append("legal@healthmate-app.com")
                        }

                        append(" or via the in-app Help Center.")
                    },
                    modifier = Modifier.clickable {
                        val intent = Intent(
                            Intent.ACTION_SENDTO,
                            Uri.parse(
                                "mailto:legal@healthmate-app.com"
                            )
                        )

                        try {
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            // No compatible email app installed
                        }
                    },
                    fontSize = 12.sp,
                    lineHeight = 19.sp,
                    color = TermsSecondary
                )
            }

            // End of Terms Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(TermsCard)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 27.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "End of Terms of Service",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TermsHeading
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Last Updated: November 2026",
                    fontSize = 11.sp,
                    color = TermsSecondary
                )
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
private fun TermsSection(
    number: String,
    title: String,
    content: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp)
    ) {

        Text(
            text = "SECTION $number",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = TermsBlue,
            letterSpacing = 0.5.sp
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TermsHeading
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = content,
            fontSize = 12.sp,
            lineHeight = 19.sp,
            color = TermsSecondary
        )
    }
}
