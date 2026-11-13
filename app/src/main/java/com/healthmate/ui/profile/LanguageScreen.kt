
package com.healthmate.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthmate.ui.profile.model.LanguageUiModel
import androidx.compose.ui.res.stringResource
import com.healthmate.R


private val LanguageBlue = Color(0xFF0752CC)
private val LanguageText = Color(0xFF102641)
private val LanguageSecondary = Color(0xFF505A70)
private val LanguageBackground = Color(0xFFF7F8FF)
private val LanguagePaleBlue = Color(0xFFEDF2FF)

private val availableLanguages = listOf(
    LanguageUiModel(
        code = "en",
        nameResId = R.string.language_english,
        nativeNameResId = R.string.native_english,
        flag = "🇬🇧"
    ),
    LanguageUiModel(
        code = "ta",
        nameResId = R.string.language_tamil,
        nativeNameResId = R.string.native_tamil,
        flag = "🇱🇰"
    )
)

@Composable
fun LanguageScreen(
    onBackClick: () -> Unit,
    onSaveLanguage: (String) -> Unit,
    currentLanguage: String = "en",
    modifier: Modifier = Modifier
) {
    var selectedLanguage by remember(currentLanguage) {
        mutableStateOf(currentLanguage)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {

        // Settings Header
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
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(Modifier.width(5.dp))

            Text(
                text = stringResource(R.string.settings),
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
        ) {
            Spacer(Modifier.height(15.dp))

            // Language Introduction Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(19.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(25.dp)
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column {
                    Text(
                        text = stringResource(R.string.choose_language),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = stringResource(R.string.language_description),
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            // Available Languages Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.available_languages),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = stringResource(R.string.select_language_instruction),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(13.dp))

            // Language Selection Cards
            availableLanguages.forEach { language ->

                LanguageOptionCard(
                    language = language,
                    isSelected = selectedLanguage == language.code,
                    onClick = {
                        selectedLanguage = language.code
                    }
                )

                Spacer(Modifier.height(11.dp))
            }

            Spacer(Modifier.height(14.dp))

            // Save Language Button
            Button(
                onClick = {
                    onSaveLanguage(selectedLanguage)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Text(
                    text = stringResource(R.string.save_language),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp)
                )
            }

            Spacer(Modifier.height(13.dp))

            Text(
                text = stringResource(R.string.language_change_hint),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun LanguageOptionCard(
    language: LanguageUiModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .clip(RoundedCornerShape(38.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Flag Circle
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = language.flag,
                fontSize = 20.sp
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(language.nameResId),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(
                            horizontal = 9.dp,
                            vertical = 3.dp
                        )
                ) {
                    Text(
                        text = language.code.uppercase(),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(3.dp))

            Text(
                text = stringResource(language.nativeNameResId),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Selection Indicator
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outlineVariant,
                    CircleShape
                )
        )
    }
}
