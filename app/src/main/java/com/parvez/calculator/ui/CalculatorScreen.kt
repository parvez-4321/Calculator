package com.parvez.calculator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.parvez.calculator.data.CalculationHistoryEntity
import com.parvez.calculator.data.CalculatorDatabase
import com.parvez.calculator.math.ExpressionEngine
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(onOpenTools: () -> Unit = {}) {
    var expression by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("0") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    var scientific by rememberSaveable { mutableStateOf(true) }
    var radians by rememberSaveable { mutableStateOf(false) }
    var lastAnswer by rememberSaveable { mutableStateOf(0.0) }
    var showHistory by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val database = remember { CalculatorDatabase.getInstance(context) }
    val history by database.historyDao().observeAll().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current
    val engine = remember(radians) {
        ExpressionEngine(if (radians) ExpressionEngine.AngleMode.RADIANS else ExpressionEngine.AngleMode.DEGREES)
    }

    fun press(key: String) {
        error = null
        when (key) {
            "AC" -> { expression = ""; result = "0" }
            "DEL" -> if (expression.isNotEmpty()) expression = expression.dropLast(1)
            "=" -> {
                if (expression.isNotBlank()) {
                    try {
                        val sourceExpression = expression
                        val value = engine.evaluate(sourceExpression, lastAnswer)
                        lastAnswer = value
                        result = formatResult(value)
                        scope.launch {
                            database.historyDao().insert(
                                CalculationHistoryEntity(
                                    expression = sourceExpression,
                                    result = result,
                                    rawResult = value,
                                    angleMode = if (radians) "RADIANS" else "DEGREES"
                                )
                            )
                        }
                        expression = ""
                    } catch (e: Exception) {
                        error = e.message ?: "Invalid expression"
                    }
                }
            }
            "Ans" -> expression += "ans"
            "×" -> expression += "*"
            "÷" -> expression += "/"
            "−" -> expression += "-"
            "√" -> expression += "sqrt("
            "x²" -> expression += "^2"
            "xʸ" -> expression += "^"
            "π" -> expression += "pi"
            "e" -> expression += "e"
            "sin", "cos", "tan", "asin", "acos", "atan", "ln", "log", "sqrt", "abs", "floor", "ceil", "sinh", "cosh", "tanh" -> expression += "$key("
            else -> expression += key
        }
        if (key != "=" && key != "AC" && expression.isNotBlank()) {
            try { result = formatResult(engine.evaluate(expression, lastAnswer)) }
            catch (_: Exception) { /* Partial expressions are expected while typing. */ }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Calculator", fontWeight = FontWeight.SemiBold)
                        Text("Scientific • Offline", style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    IconButton(onClick = onOpenTools) {
                        Icon(Icons.Default.Build, contentDescription = "Open math tools")
                    }
                    IconButton(onClick = { showHistory = !showHistory }) {
                        Icon(Icons.Default.History, contentDescription = "Calculation history")
                    }
                    IconButton(onClick = { scientific = !scientific }) {
                        Icon(Icons.Default.Settings, contentDescription = "Toggle scientific keypad")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                FilterChip(selected = scientific, onClick = { scientific = true }, label = { Text("Scientific") })
                FilterChip(selected = !scientific, onClick = { scientific = false }, label = { Text("Standard") })
                Spacer(Modifier.weight(1f))
                FilterChip(selected = radians, onClick = { radians = !radians }, label = { Text(if (radians) "RAD" else "DEG") })
            }
            Card(
                modifier = Modifier.fillMaxWidth().weight(if (showHistory) 0.42f else 0.34f),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(expression.ifBlank { " " }, style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.End, maxLines = 3)
                    Spacer(Modifier.height(12.dp))
                    Text(result, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End, maxLines = 2)
                    error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        IconButton(onClick = { clipboard.setText(AnnotatedString(result)) }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy result")
                        }
                    }
                }
            }
            if (showHistory) {
                Card(modifier = Modifier.fillMaxWidth().weight(0.35f)) {
                    Column(Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Calculation history", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.weight(1f))
                            TextButton(onClick = { scope.launch { database.historyDao().clearAll() } }) { Text("Clear all") }
                        }
                        LazyColumn {
                            items(history, key = { it.id }) { item ->
                                TextButton(
                                    onClick = { expression = item.expression; result = item.result; showHistory = false },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(Modifier.fillMaxWidth()) {
                                        Text(item.expression, maxLines = 1)
                                        Text(item.result, color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            val keys = if (scientific) listOf(
                listOf("sin", "cos", "tan", "ln", "log"),
                listOf("asin", "acos", "atan", "(", ")", "DEL"),
                listOf("7", "8", "9", "÷", "√", "x²"),
                listOf("4", "5", "6", "×", "xʸ", "!"),
                listOf("1", "2", "3", "−", "π", "e"),
                listOf("0", ".", "%", "+", "Ans", "="),
                listOf("AC", "sinh", "cosh", "tanh", "abs", "floor")
            ) else listOf(
                listOf("7", "8", "9", "÷"),
                listOf("4", "5", "6", "×"),
                listOf("1", "2", "3", "−"),
                listOf("0", ".", "%", "+"),
                listOf("AC", "DEL", "(", ")", "=")
            )
            Column(Modifier.fillMaxWidth().weight(0.66f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                keys.forEach { row ->
                    Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { key ->
                            val isEquals = key == "="
                            val isUtility = key in listOf("AC", "DEL", "÷", "×", "−", "+", "x²", "xʸ")
                            FilledTonalButton(
                                onClick = { press(key) },
                                modifier = Modifier.weight(1f).fillMaxHeight(),
                                shape = RoundedCornerShape(18.dp),
                                contentPadding = PaddingValues(2.dp),
                                colors = if (isEquals) ButtonDefaults.filledTonalButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) else if (isUtility) ButtonDefaults.filledTonalButtonColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                ) else ButtonDefaults.filledTonalButtonColors()
                            ) {
                                Text(key, fontSize = if (key.length > 4) 12.sp else 16.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatResult(value: Double): String {
    if (!value.isFinite()) return "Error"
    if (value == 0.0) return "0"
    if (abs(value) >= 1e12 || abs(value) < 1e-8) return String.format(Locale.US, "%.8e", value)
    return DecimalFormat("#,##0.##########", DecimalFormatSymbols(Locale.US)).format(value)
}
