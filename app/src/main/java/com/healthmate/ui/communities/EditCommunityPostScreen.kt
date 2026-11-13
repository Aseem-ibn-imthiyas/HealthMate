
package com.healthmate.ui.communities

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage

private val EditPrimary = Color(0xFF4035AC)
private val EditHeading = Color(0xFF17243B)
private val EditSecondary = Color(0xFF64748B)
private val EditBorder = Color(0xFFDCE4F0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCommunityPostScreen(
    initialContent: String,
    initialImageUri: String?,
    onDismiss: () -> Unit,
    onSavePost: (String, String?) -> Unit
) {
    val context = LocalContext.current

    var postText by remember(initialContent) {
        mutableStateOf(initialContent)
    }

    var selectedImageUri by remember(initialImageUri) {
        mutableStateOf(initialImageUri)
    }

    var validationMessage by remember {
        mutableStateOf<String?>(null)
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                selectedImageUri = uri.toString()
                validationMessage = null

            } catch (e: SecurityException) {
                validationMessage = "Unable to access the selected image."
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        contentColor = EditHeading,
        shape = RoundedCornerShape(
            topStart = 30.dp,
            topEnd = 30.dp
        ),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 15.dp)
                    .size(width = 40.dp, height = 5.dp)
                    .background(
                        Color(0xFFD5DDE9),
                        CircleShape
                    )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.91f)
                .imePadding()
        ) {

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            Color(0xFFF0EEFF),
                            RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        tint = EditPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Edit Post",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = EditHeading
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Update your community post",
                        fontSize = 11.sp,
                        color = EditSecondary
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            Color(0xFFF1F4F8),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = EditSecondary
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFEDF0F5))

            // Form content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "POST CONTENT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF34445D)
                )

                Spacer(modifier = Modifier.height(9.dp))

                BasicTextField(
                    value = postText,
                    onValueChange = {
                        postText = it
                        validationMessage = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 130.dp)
                        .clip(RoundedCornerShape(17.dp))
                        .background(Color(0xFFEEF3FF))
                        .padding(15.dp),
                    textStyle = TextStyle(
                        fontSize = 13.sp,
                        lineHeight = 21.sp,
                        color = EditHeading
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            if (postText.isEmpty()) {
                                Text(
                                    text = "What would you like to share?",
                                    fontSize = 13.sp,
                                    color = EditSecondary
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "ATTACHED PHOTO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF34445D)
                )

                Spacer(modifier = Modifier.height(9.dp))

                if (!selectedImageUri.isNullOrBlank()) {
                    Box {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Post image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                                .clip(RoundedCornerShape(17.dp)),
                            contentScale = ContentScale.Crop
                        )

                        IconButton(
                            onClick = {
                                selectedImageUri = null
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(9.dp)
                                .size(34.dp)
                                .background(
                                    Color.White,
                                    CircleShape
                                )
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Remove image",
                                tint = Color(0xFFE04444)
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(17.dp))
                            .background(Color(0xFFF7F8FC))
                            .border(
                                1.dp,
                                EditBorder,
                                RoundedCornerShape(17.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No photo attached",
                            fontSize = 12.sp,
                            color = EditSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        photoPicker.launch(arrayOf("image/*"))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        EditBorder
                    )
                ) {
                    Icon(
                        Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        tint = EditPrimary,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = if (selectedImageUri == null)
                            "Add Photo"
                        else
                            "Replace Photo",
                        color = EditPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                validationMessage?.let { message ->
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Fixed bottom actions
            HorizontalDivider(color = Color(0xFFEDF0F5))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 12.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(0xFFC9D5E7)
                    )
                ) {
                    Text(
                        "Cancel",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EditHeading
                    )
                }

                Button(
                    onClick = {
                        if (
                            postText.isBlank() &&
                            selectedImageUri.isNullOrBlank()
                        ) {
                            validationMessage =
                                "Please add text or a photo."
                        } else {
                            onSavePost(
                                postText.trim(),
                                selectedImageUri
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EditPrimary
                    )
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        "Save Changes",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
