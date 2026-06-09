package com.example.tugas7khairulrijalsyauqi.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugas7khairulrijalsyauqi.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = remember { ProfileViewModel() }
) {
    val profile = viewModel.profile
    var editedName by remember(profile) { mutableStateOf(profile.name) }
    var editedEmail by remember(profile) { mutableStateOf(profile.email) }
    var editedPhone by remember(profile) { mutableStateOf(profile.phone) }
    var editedInstitution by remember(profile) { mutableStateOf(profile.institution) }

    val isEditing = viewModel.isEditing

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Profil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            if (!isEditing) {
                IconButton(onClick = { viewModel.startEditing() }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isEditing) {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Nama") },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        singleLine = true
                    )
                } else {
                    Text(
                        text = profile.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Pengguna Aplikasi Catatan",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                if (isEditing) {
                    EditableProfileField(
                        icon = Icons.Default.Person,
                        label = "Nama",
                        value = editedName,
                        onValueChange = { editedName = it }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    EditableProfileField(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = editedEmail,
                        onValueChange = { editedEmail = it }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    EditableProfileField(
                        icon = Icons.Default.Phone,
                        label = "Telepon",
                        value = editedPhone,
                        onValueChange = { editedPhone = it }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    EditableProfileField(
                        icon = Icons.Default.School,
                        label = "Institusi",
                        value = editedInstitution,
                        onValueChange = { editedInstitution = it }
                    )
                } else {
                    ProfileMenuItem(
                        icon = Icons.Default.Person,
                        title = "Nama",
                        subtitle = profile.name
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    ProfileMenuItem(
                        icon = Icons.Default.Email,
                        title = "Email",
                        subtitle = profile.email
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    ProfileMenuItem(
                        icon = Icons.Default.Phone,
                        title = "Telepon",
                        subtitle = profile.phone
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    ProfileMenuItem(
                        icon = Icons.Default.School,
                        title = "Institusi",
                        subtitle = profile.institution
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Tentang Aplikasi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Notes App v1.0",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Aplikasi catatan sederhana dengan Jetpack Compose untuk tugas mobile development.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (isEditing) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        editedName = profile.name
                        editedEmail = profile.email
                        editedPhone = profile.phone
                        editedInstitution = profile.institution
                        viewModel.cancelEditing()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Batal", fontWeight = FontWeight.Medium)
                }

                Button(
                    onClick = {
                        viewModel.updateName(editedName)
                        viewModel.updateEmail(editedEmail)
                        viewModel.updatePhone(editedPhone)
                        viewModel.updateInstitution(editedInstitution)
                        viewModel.saveProfile()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Simpan", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EditableProfileField(
    icon: ImageVector,
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
