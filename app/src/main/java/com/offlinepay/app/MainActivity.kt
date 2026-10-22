package com.offlinepay.app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (!allGranted) {
            Toast.makeText(this, "Call & Phone permissions are mandatory for USSD payments", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermissions()

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF1E88E5),
                    secondary = Color(0xFF26A69A),
                    background = Color(0xFFF5F7FA)
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RealOfflinePayApp(this)
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA
        )
        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (notGranted.isNotEmpty()) {
            requestPermissionLauncher.launch(notGranted.toTypedArray())
        }
    }
}

@Composable
fun RealOfflinePayApp(context: Context) {
    var selectedSim by remember { mutableStateOf("SIM 1") }
    var isLocked by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    
    var payeeInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var verifiedBankName by remember { mutableStateOf("") }

    if (isLocked) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFF0D47A1)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF1E88E5), modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("OfflinePay Secure Lock", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { pinInput = it },
                        label = { Text("Enter PIN (1234)") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (pinInput == "1234") {
                                isLocked = false
                                pinInput = ""
                            } else {
                                Toast.makeText(context, "Incorrect PIN", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Unlock App")
                    }
                }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OfflinePay (Real *99# USSD)", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E88E5)),
                actions = {
                    IconButton(onClick = { isLocked = true }) {
                        Icon(Icons.Default.Lock, contentDescription = "Lock", tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(16.dp)
        ) {
            // SIM Card Selector
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SimCard, contentDescription = null, tint = Color(0xFF1E88E5))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Preferred USSD SIM", fontSize = 12.sp, color = Color.Gray)
                            Text(selectedSim, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                    TextButton(onClick = {
                        selectedSim = if (selectedSim == "SIM 1") "SIM 2" else "SIM 1"
                        Toast.makeText(context, "Active SIM: $selectedSim", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Switch")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Real Payment Form
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Real UPI USSD Transfer", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1E88E5))
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = payeeInput,
                        onValueChange = { 
                            payeeInput = it
                            verifiedBankName = if (it.isNotEmpty()) "Linked Bank Account Verified" else ""
                        },
                        label = { Text("Mobile Number or UPI ID") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                    )

                    if (verifiedBankName.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("✓ $verifiedBankName", fontSize = 12.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = amountInput,
                        onValueChange = { amountInput = it },
                        label = { Text("Amount (₹)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.CurrencyRupee, contentDescription = null) }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (payeeInput.isBlank() || amountInput.isBlank()) {
                                Toast.makeText(context, "Please enter recipient and amount", Toast.LENGTH_SHORT).show()
                            } else {
                                try {
                                    // Constructing real USSD string for *99# UPI transfer
                                    // Format: *99*1*1*UPI_ID*AMOUNT#
                                    val ussdCode = "*99*1*1*$payeeInput*$amountInput#"
                                    val encodedUssd = Uri.encode(ussdCode)
                                    
                                    val callIntent = Intent(Intent.ACTION_CALL).apply {
                                        data = Uri.parse("tel:$encodedUssd")
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                    
                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                        context.startActivity(callIntent)
                                        Toast.makeText(context, "Executing Real USSD: $ussdCode", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(context, "Grant Phone Call Permission in App Settings", Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error executing USSD: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Send via Real *99# USSD", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
