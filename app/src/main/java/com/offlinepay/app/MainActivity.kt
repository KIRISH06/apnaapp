```kotlin
package com.offlinepay.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Primary = Color(0xFF1769FF)
private val DarkBlue = Color(0xFF102A43)
private val Background = Color(0xFFF4F7FC)
private val Green = Color(0xFF14804A)
private val Gray = Color(0xFF64748B)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Primary,
                    background = Background,
                    surface = Color.White
                )
            ) {
                OfflinePayApp()
            }
        }
    }
}

private data class Transaction(
    val title: String,
    val subtitle: String,
    val amount: String,
    val received: Boolean
)

@Composable
fun OfflinePayApp() {
    var screen by remember { mutableStateOf("Home") }
    var recipient by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }

    val transactions = remember {
        mutableStateListOf(
            Transaction(
                "Grocery Store",
                "Demo transaction",
                "₹450",
                false
            ),
            Transaction(
                "Rahul Kumar",
                "Demo transaction",
                "₹250",
                true
            ),
            Transaction(
                "Mobile Recharge",
                "Demo transaction",
                "₹199",
                false
            )
        )
    }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            if (screen == "Home" ||
                screen == "History" ||
                screen == "Settings"
            ) {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    NavigationBarItem(
                        selected = screen == "Home",
                        onClick = { screen = "Home" },
                        icon = { Text("⌂", fontSize = 22.sp) },
                        label = { Text("Home") }
                    )

                    NavigationBarItem(
                        selected = screen == "History",
                        onClick = { screen = "History" },
                        icon = { Text("↻", fontSize = 22.sp) },
                        label = { Text("History") }
                    )

                    NavigationBarItem(
                        selected = screen == "Settings",
                        onClick = { screen = "Settings" },
                        icon = { Text("⚙", fontSize = 22.sp) },
                        label = { Text("Settings") }
                    )
                }
            }
        },
        snackbarHost = {
            if (showMessage) {
                Snackbar(
                    action = {
                        TextButton(
                            onClick = { showMessage = false }
                        ) {
                            Text("OK")
                        }
                    }
                ) {
                    Text(message)
                }
            }
        }
    ) { padding ->

        when (screen) {
            "Home" -> HomeScreen(
                modifier = Modifier.padding(padding),
                onPay = { screen = "Pay" },
                onScan = { screen = "Scan" },
                onOffline = { screen = "Offline" }
            )

            "Pay" -> PayScreen(
                modifier = Modifier.padding(padding),
                recipient = recipient,
                onRecipientChange = { recipient = it },
                amount = amount,
                onAmountChange = { amount = it },
                note = note,
                onNoteChange = { note = it },
                onBack = { screen = "Home" },
                onContinue = {
                    message =
                        "Demo only. No money was transferred."
                    showMessage = true
                }
            )

            "Scan" -> SimpleScreen(
                modifier = Modifier.padding(padding),
                title = "Scan QR Code",
                description =
                    "Camera QR scanning is not connected yet.",
                onBack = { screen = "Home" },
                buttonText = "Enter UPI ID",
                onAction = { screen = "Pay" }
            )

            "Offline" -> SimpleScreen(
                modifier = Modifier.padding(padding),
                title = "Offline Mode",
                description =
                    "You can view demo screens without internet. Real UPI transfers and bank verification require supported payment infrastructure and connectivity.",
                onBack = { screen = "Home" },
                buttonText = "Back to Home",
                onAction = { screen = "Home" }
            )

            "History" -> HistoryScreen(
                modifier = Modifier.padding(padding),
                transactions = transactions
            )

            "Settings" -> SettingsScreen(
                modifier = Modifier.padding(padding),
                onAbout = {
                    message =
                        "OfflinePay v2.0 is a demo application."
                    showMessage = true
                }
            )
        }
    }
}

@Composable
private fun HomeScreen(
    modifier: Modifier,
    onPay: () -> Unit,
    onScan: () -> Unit,
    onOffline: () -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Primary,
                            RoundedCornerShape(15.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "O",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        "OfflinePay",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )
                    Text(
                        "Simple. Secure. Convenient.",
                        color = Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DarkBlue
                )
            ) {
                Column(
                    modifier = Modifier.padding(22.dp)
                ) {
                    Text(
                        "DEMO WALLET BALANCE",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        "₹ 12,450.00",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Sample balance • Not real money",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }

        item {
            Text(
                "Quick Actions",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )

            Spacer(Modifier.height(10.dp))

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {
                ActionCard(
                    modifier = Modifier.weight(1f),
                    icon = "↗",
                    title = "Pay",
                    onClick = onPay
                )

                ActionCard(
                    modifier = Modifier.weight(1f),
                    icon = "▦",
                    title = "Scan QR",
                    onClick = onScan
                )

                ActionCard(
                    modifier = Modifier.weight(1f),
                    icon = "⇄",
                    title = "Offline",
                    onClick = onOffline
                )
            }
        }

        item {
            Text(
                "Recent Activity",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )
        }

        item {
            TransactionCard(
                Transaction(
                    "Grocery Store",
                    "Demo transaction",
                    "₹450",
                    false
                )
            )
        }

        item {
            TransactionCard(
                Transaction(
                    "Rahul Kumar",
                    "Demo transaction",
                    "₹250",
                    true
                )
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEAF2FF)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "This is a prototype. No actual payments are processed.",
                    modifier = Modifier.padding(16.dp),
                    color = DarkBlue,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun ActionCard(
    modifier: Modifier,
    icon: String,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(105.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        Color(0xFFEAF2FF),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    icon,
                    color = Primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(7.dp))

            Text(
                title,
                color = DarkBlue,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun PayScreen(
    modifier: Modifier,
    recipient: String,
    onRecipientChange: (String) -> Unit,
    amount: String,
    onAmountChange: (String) -> Unit,
    note: String,
    onNoteChange: (String) -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Header("Pay Someone", onBack)

        Spacer(Modifier.height(22.dp))

        Text("Recipient UPI ID")

        OutlinedTextField(
            value = recipient,
            onValueChange = onRecipientChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("name@bank") },
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        Text("Amount (₹)")

        OutlinedTextField(
            value = amount,
            onValueChange = {
                if (it.all { c ->
                        c.isDigit() || c == '.'
                    }
                ) {
                    onAmountChange(it)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("0.00") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        Text("Note (optional)")

        OutlinedTextField(
            value = note,
            onValueChange = onNoteChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Payment note") },
            singleLine = true
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onContinue,
            enabled = recipient.contains("@") &&
                (amount.toDoubleOrNull() ?: 0.0) > 0,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text("Continue (Demo)")
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "This form does not transfer money. Never enter your UPI PIN in this demo.",
            color = Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun HistoryScreen(
    modifier: Modifier,
    transactions: List<Transaction>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            "Transaction History",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Text(
            "Sample transactions",
            color = Gray,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(18.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(transactions) { transaction ->
                TransactionCard(transaction)
            }
        }
    }
}

@Composable
private fun TransactionCard(transaction: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (transaction.received)
                            Color(0xFFE2F8EC)
                        else
                            Color(0xFFEAF2FF),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (transaction.received) "↓" else "↗",
                    color = if (transaction.received)
                        Green else Primary,
                    fontSize = 22.sp
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    transaction.title,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkBlue
                )

                Text(
                    transaction.subtitle,
                    fontSize = 12.sp,
                    color = Gray
                )
            }

            Text(
                (if (transaction.received) "+" else "-") +
                    transaction.amount,
                fontWeight = FontWeight.Bold,
                color = if (transaction.received)
                    Green else DarkBlue
            )
        }
    }
}

@Composable
private fun SettingsScreen(
    modifier: Modifier,
    onAbout: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            "Settings",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Text(
            "Manage your preferences",
            color = Gray
        )

        Spacer(Modifier.height(20.dp))

        SettingsItem(
            "Profile",
            "Demo user"
        )

        SettingsItem(
            "Security",
            "Never share your UPI PIN"
        )

        SettingsItem(
            "Notifications",
            "Demo notifications"
        )

        SettingsItem(
            "App Version",
            "OfflinePay v2.0"
        )

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onAbout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("About OfflinePay")
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            modifier = Modifier.padding(17.dp)
        ) {
            Text(
                title,
                fontWeight = FontWeight.SemiBold,
                color = DarkBlue
            )

            Spacer(Modifier.height(4.dp))

            Text(
                description,
                fontSize = 12.sp,
                color = Gray
            )
        }
    }
}

@Composable
private fun SimpleScreen(
    modifier: Modifier,
    title: String,
    description: String,
    onBack: () -> Unit,
    buttonText: String,
    onAction: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Header(title, onBack)

        Spacer(Modifier.height(25.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Text(
                description,
                modifier = Modifier.padding(20.dp),
                color = DarkBlue,
                fontSize = 15.sp
            )
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onAction,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(buttonText)
        }
    }
}

@Composable
private fun Header(
    title: String,
    onBack: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "‹",
            fontSize = 34.sp,
            color = Primary,
            modifier = Modifier
                .clickable { onBack() }
                .padding(end = 12.dp)
        )

        Text(
            title,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )
    }
}
