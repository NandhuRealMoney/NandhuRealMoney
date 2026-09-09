package com.nandhurealmoney.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*

private val Navy = Color(0xFF20264D)
private val Blue = Color(0xFF087FF5)
private val Orange = Color(0xFFFF7A18)
private val Page = Color(0xFFF7F9FC)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { NandhuApp() }
    }
}

@Composable
fun NandhuApp() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Blue,
            secondary = Orange,
            background = Page,
            surface = Color.White
        )
    ) {
        val nav = rememberNavController()
        Scaffold(
            bottomBar = {
                NavigationBar {
                    listOf(
                        "home" to Icons.Default.Home,
                        "orders" to Icons.Default.ReceiptLong,
                        "buy" to Icons.Default.CurrencyRupee,
                        "upi" to Icons.Default.AccountBalanceWallet,
                        "profile" to Icons.Default.Person
                    ).forEach { (route, icon) ->
                        NavigationBarItem(
                            selected = nav.currentBackStackEntryAsState().value?.destination?.route == route,
                            onClick = { nav.navigate(route) { launchSingleTop = true } },
                            icon = { Icon(icon, null) },
                            label = { Text(route.replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }
        ) { pad ->
            NavHost(navController = nav, startDestination = "home", modifier = Modifier.padding(pad)) {
                composable("home") { Home(nav) }
                composable("orders") { Orders() }
                composable("buy") { BuySell(isBuy = true) }
                composable("upi") { Upi() }
                composable("profile") { Profile(nav) }
                composable("introduction") { Introduction(nav) }
                composable("activity") { ActivityScreen() }
            }
        }
    }
}

@Composable
fun Header() {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Nandhu", color = Orange, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("REAL", color = Blue, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(" Money", color = Navy, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun Home(nav: NavHostController) {
    LazyColumn(Modifier.fillMaxSize().background(Page)) {
        item { Header() }
        item {
            Card(
                Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Blue)
            ) {
                Column(Modifier.padding(26.dp)) {
                    Text("Start your earning journey", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text("with NandhuReal Money!", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(18.dp))
                    Button(onClick = { nav.navigate("introduction") }, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                        Text("Learn more >", color = Blue, fontSize = 17.sp)
                    }
                }
            }
        }
        item {
            Column(Modifier.padding(36.dp, 30.dp, 36.dp, 10.dp)) {
                Text("My Balance", color = Navy, fontSize = 20.sp)
                Text("₹2,013.95", color = Navy, fontSize = 40.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(12.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))) {
                    Row(Modifier.fillMaxWidth().padding(18.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("In Sell UPI Count: 5", fontSize = 17.sp, color = Color.DarkGray)
                        Text("›", fontSize = 28.sp, color = Color.Gray)
                    }
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth().padding(horizontal = 26.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                Quick("Buy", Icons.Default.AddCard) { nav.navigate("buy") }
                Quick("Sell", Icons.Default.Upload) { nav.navigate("buy") }
                Quick("+UPI", Icons.Default.CreditCard) { nav.navigate("upi") }
                Quick("Activity", Icons.Default.CardGiftcard) { nav.navigate("activity") }
            }
        }
        item {
            Card(Modifier.padding(26.dp).fillMaxWidth(), colors = CardDefaults.cardColors(Color.White)) {
                Column(Modifier.padding(20.dp)) {
                    Text("Standard Member", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Navy)
                    Text("Commission rate: 3.5%", fontSize = 17.sp, color = Color.Gray)
                    Spacer(Modifier.height(20.dp))
                    LinearProgressIndicator(progress = { 0.08f }, Modifier.fillMaxWidth())
                    Spacer(Modifier.height(18.dp))
                    Text("Buy ₹29,73,744 more to unlock Premium Member", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun Quick(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }.padding(5.dp)) {
        Surface(shape = RoundedCornerShape(18.dp), color = Color(0xFFEAF3FF), modifier = Modifier.size(64.dp)) {
            Icon(icon, null, tint = Blue, modifier = Modifier.padding(18.dp))
        }
        Spacer(Modifier.height(7.dp))
        Text(label, color = Navy, fontSize = 15.sp)
    }
}

@Composable
fun BuySell(isBuy: Boolean) {
    var amount by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val profit = 0.035
    Column(Modifier.fillMaxSize().background(Page).padding(24.dp)) {
        Text(if (isBuy) "Buy PCoin" else "Sell PCoin", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Navy)
        Text("Demo mode — no real-money transfer is performed.", color = Color.Gray)
        Spacer(Modifier.height(24.dp))
        OutlinedTextField(
            value = amount, onValueChange = { amount = it },
            label = { Text("Amount in ₹") }, singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        if (isBuy) Text("Example: ₹1,000 → 1,035 demo PCoin", color = Blue)
        Spacer(Modifier.height(20.dp))
        Button(onClick = {
            val a = amount.toDoubleOrNull() ?: 0.0
            message = if (a > 0) "Demo transaction: ₹%.2f → %.2f PCoin".format(a, a * (1 + profit))
                       else "Enter a valid amount."
        }, modifier = Modifier.fillMaxWidth()) { Text(if (isBuy) "Buy (Demo)" else "Sell (Demo)") }
        if (message.isNotEmpty()) {
            Spacer(Modifier.height(18.dp))
            Text(message, color = Navy, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun Orders() {
    SimplePage("Orders") {
        Text("No demo orders yet.", color = Color.Gray, fontSize = 18.sp)
    }
}

@Composable
fun Upi() {
    SimplePage("+UPI Account") {
        Text("Demo wallet linking", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Navy)
        Text("Supported demo wallets: Mobikwik, Freecharge, Navi", color = Color.Gray)
        Spacer(Modifier.height(16.dp))
        Button(onClick = {}) { Text("Add demo UPI wallet") }
    }
}

@Composable
fun ActivityScreen() {
    SimplePage("Activity") {
        Text("No activity yet.", color = Color.Gray, fontSize = 18.sp)
    }
}

@Composable
fun Profile(nav: NavHostController) {
    SimplePage("My Profile") {
        Text("NandhuReal Money", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Navy)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { nav.navigate("introduction") }) { Text("Introduction") }
    }
}

@Composable
fun SimplePage(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().background(Page).padding(28.dp)) {
        Text(title, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Navy)
        Spacer(Modifier.height(22.dp))
        content()
    }
}

@Composable
fun Introduction(nav: NavHostController) {
    LazyColumn(Modifier.fillMaxSize().background(Color.White).padding(34.dp)) {
        item {
            Text("NandhuReal Money Introduction", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Navy, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            Text("Demo product information", color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(35.dp))
        }
        item { Section("What is PCoin?", "PCoin is a demo token used inside NandhuReal Money. In this demo application it has no cash value and cannot be withdrawn.") }
        item { Section("Buy means?", "The Buy screen simulates purchasing demo PCoin with rupees. A sample 3.5% promotional calculation is shown for demonstration only.") }
        item { Section("Sell means?", "The Sell screen simulates converting demo PCoin back to a displayed rupee amount. No bank or UPI transfer is made.") }
        item { Section("How you earn?", "The app demonstrates how a percentage-based calculation could be displayed. It does not promise income or returns.") }
        item { Section(" +UPI Account", "This demo contains a UPI-style interface only. It does not connect to, collect, or transfer funds through real UPI wallets.") }
        item { Section("Advantages of Wallet Working Mode", "• No unnecessary phone permissions\n• No bank credentials required\n• No real-money transfers\n• Demo activity and transaction screens\n• Simple mobile-first interface") }
    }
}

@Composable
fun Section(title: String, body: String) {
    Column(Modifier.padding(bottom = 30.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.width(6.dp).height(34.dp).background(Blue))
            Spacer(Modifier.width(12.dp))
            Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF31445A))
        }
        Spacer(Modifier.height(14.dp))
        Text(body, fontSize = 18.sp, lineHeight = 29.sp, color = Navy)
        Divider(Modifier.padding(top = 25.dp))
    }
}
