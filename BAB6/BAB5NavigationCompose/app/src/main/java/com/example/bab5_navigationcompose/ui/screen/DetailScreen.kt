package com.example.bab5_navigationcompose.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bab5_navigationcompose.ui.detail.DetailUiState

@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onOpenProfile: () -> Unit,
    onBack: () -> Unit,
    onBackToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Detail Student",
            style = MaterialTheme.typography.headlineMedium
        )

        when {
            // State: student tidak ditemukan -> tampilkan Card informatif
            uiState.errorMessage != null -> {
                StudentNotFoundCard(
                    message = uiState.errorMessage,
                    onBackToHome = onBackToHome
                )
            }

            // State: student ditemukan -> tampilkan detail
            uiState.student != null -> {
                val student = uiState.student
                Text(text = "ID: ${student.id}")
                Text(text = "Nama: ${student.name}")
                Text(text = "Program: ${student.program}")
                Text(text = "Angkatan: ${student.year}")

                OutlinedButton(
                    onClick = onOpenProfile,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Buka Profile")
                }

                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Kembali")
                }
            }
        }
    }
}

@Composable
private fun StudentNotFoundCard(
    message: String,
    onBackToHome: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Mahasiswa Tidak Ditemukan",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = "Silakan kembali ke Home dan pilih mahasiswa dari daftar yang tersedia.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Button(
                onClick = onBackToHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kembali ke Home")
            }
        }
    }
}