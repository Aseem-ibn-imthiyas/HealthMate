
package com.healthmate.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme



enum class HealthMateDestination(
    val label: String,
    val icon: ImageVector
) {
    DASHBOARD("Home", Icons.Default.Home),
    HEALTH_RECORDS("Records", Icons.Default.Description),
    COMMUNITIES("Group", Icons.Default.Groups),
    PROFILE("Profile", Icons.Default.Person)
}

@Composable
fun AnimatedBottomNavigationBar(
    selectedDestination: HealthMateDestination,
    onDestinationSelected: (HealthMateDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(78.dp)
            .shadow(
                elevation = 12.dp,
                shape = CircleShape,
                ambientColor = Color(0xFF9DB4D5).copy(alpha = 0.20f),
                spotColor = Color(0xFF9DB4D5).copy(alpha = 0.20f)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = CircleShape
            )
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HealthMateDestination.entries.forEach { destination ->

            val isSelected = selectedDestination == destination

            val animatedWeight = animateFloatAsState(
                targetValue = if (isSelected) 1.45f else 1f,
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                ),
                label = "Navigation item width"
            )

            NavigationItem(
                destination = destination,
                selected = isSelected,
                modifier = Modifier
                    .weight(animatedWeight.value)
                    .fillMaxHeight()
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 350,
                            easing = FastOutSlowInEasing
                        )
                    ),
                onClick = {
                    onDestinationSelected(destination)
                }
            )
        }
    }
}

@Composable
private fun NavigationItem(
    destination: HealthMateDestination,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val iconScale = animateFloatAsState(
        targetValue = if (selected) 1.08f else 1f,
        animationSpec = tween(250),
        label = "Navigation icon scale"
    )

    Row(
        modifier = modifier
            .background(
                color = if (selected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    Color.Transparent
                },
                shape = CircleShape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = destination.icon,
            contentDescription = destination.label,
            tint = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        )

        AnimatedVisibility(
            visible = selected,
            enter = fadeIn(
                animationSpec = tween(200, delayMillis = 80)
            ) + expandHorizontally(
                animationSpec = tween(250)
            ),
            exit = fadeOut(
                animationSpec = tween(120)
            ) + shrinkHorizontally(
                animationSpec = tween(200)
            )
        ) {
            Row {
                Spacer(modifier = Modifier.width(7.dp))

                Text(
                    text = destination.label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
            }
        }
    }
}
