package com.parvez.calculator.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.parvez.calculator.math.ExpressionEngine
import com.parvez.calculator.tools.*

@Composable
fun ToolsScreen(onBack: () -> Unit) {
    var section by remember { mutableStateOf("Statistics") }
    var values by remember { mutableStateOf("1, 2, 3, 4, 5") }
    var result by remember { mutableStateOf("") }
    var a by remember { mutableStateOf("1") }
    var b by remember { mutableStateOf("-3") }
    var c by remember { mutableStateOf("2") }
    var number by remember { mutableStateOf("100") }
    var rate by remember { mutableStateOf("5") }
    var years by remember { mutableStateOf("2") }
    var radius by remember { mutableStateOf("5") }
    var fromUnit by remember { mutableStateOf(LengthUnit.METER) }
    var toUnit by remember { mutableStateOf(LengthUnit.FOOT) }
    var baseInput by remember { mutableStateOf("255") }
    var constantQuery by remember { mutableStateOf("") }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Math Tools") }, navigationIcon = {
            TextButton(onClick = onBack) { Text("Back") }
        })
    }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Choose a tool", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Statistics", "Equation", "Finance").forEach {
                    FilterChip(selected = section == it, onClick = { section = it; result = "" }, label = { Text(it) })
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Geometry", "Units", "Programmer", "Constants").forEach {
                    FilterChip(selected = section == it, onClick = { section = it; result = "" }, label = { Text(it) })
                }
            }
            when (section) {
                "Statistics" -> {
                    Text("Descriptive statistics", style = MaterialTheme.typography.titleLarge)
                    OutlinedTextField(values, { values = it }, label = { Text("Numbers separated by commas") }, modifier = Modifier.fillMaxWidth())
                    Button(onClick = {
                        result = runCatching {
                            val data = values.split(',', ';', ' ').filter { it.isNotBlank() }.map { it.toDouble() }
                            val s = StatisticsEngine.describe(data)
                            "Count: ${s.count}\nMean: ${s.mean}\nMedian: ${s.median}\nMode is not inferred automatically\nMin: ${s.minimum}\nMax: ${s.maximum}\nRange: ${s.range}\nSample variance: ${s.sampleVariance}\nSample SD: ${s.sampleStandardDeviation}\nQ1: ${s.q1}\nQ3: ${s.q3}\nIQR: ${s.interquartileRange}"
                        }.getOrElse { "Error: ${it.message}" }
                    }) { Text("Calculate statistics") }
                }
                "Equation" -> {
                    Text("Quadratic equation: ax² + bx + c = 0", style = MaterialTheme.typography.titleLarge)
                    OutlinedTextField(a, { a = it }, label = { Text("a") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(b, { b = it }, label = { Text("b") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(c, { c = it }, label = { Text("c") }, modifier = Modifier.fillMaxWidth())
                    Button(onClick = {
                        result = runCatching {
                            when (val solution = EquationSolver.solveQuadratic(a.toDouble(), b.toDouble(), c.toDouble())) {
                                is QuadraticSolutions.Real -> "Real root 1: ${solution.first}\nReal root 2: ${solution.second ?: "Repeated / linear root"}"
                                is QuadraticSolutions.Complex -> "Complex roots: ${solution.realPart} ± ${solution.imaginaryMagnitude}i"
                                QuadraticSolutions.NoSolution -> "No unique solution"
                            }
                        }.getOrElse { "Error: ${it.message}" }
                    }) { Text("Solve equation") }
                }
                "Finance" -> {
                    Text("Simple interest", style = MaterialTheme.typography.titleLarge)
                    OutlinedTextField(number, { number = it }, label = { Text("Principal") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(rate, { rate = it }, label = { Text("Annual rate (%)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(years, { years = it }, label = { Text("Years") }, modifier = Modifier.fillMaxWidth())
                    Button(onClick = {
                        result = runCatching {
                            val p = number.toDouble(); val r = rate.toDouble(); val y = years.toDouble()
                            val interest = FinancialCalculator.simpleInterest(p, r, y)
                            "Interest: $interest\nTotal amount: ${p + interest}"
                        }.getOrElse { "Error: ${it.message}" }
                    }) { Text("Calculate interest") }
                }
                "Geometry" -> {
                    Text("Circle calculator", style = MaterialTheme.typography.titleLarge)
                    OutlinedTextField(radius, { radius = it }, label = { Text("Radius") }, modifier = Modifier.fillMaxWidth())
                    Button(onClick = {
                        result = runCatching {
                            val r = radius.toDouble()
                            "Area: ${GeometryCalculator.circleArea(r)}\nCircumference: ${GeometryCalculator.circleCircumference(r)}"
                        }.getOrElse { "Error: ${it.message}" }
                    }) { Text("Calculate circle") }
                }
                "Units" -> {
                    Text("Length converter", style = MaterialTheme.typography.titleLarge)
                    OutlinedTextField(number, { number = it }, label = { Text("Value") }, modifier = Modifier.fillMaxWidth())
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column(Modifier.weight(1f)) {
                            Text("From")
                            LengthUnit.entries.forEach { unit ->
                                FilterChip(selected = fromUnit == unit, onClick = { fromUnit = unit }, label = { Text(unit.name) })
                            }
                        }
                        Column(Modifier.weight(1f)) {
                            Text("To")
                            LengthUnit.entries.forEach { unit ->
                                FilterChip(selected = toUnit == unit, onClick = { toUnit = unit }, label = { Text(unit.name) })
                            }
                        }
                    }
                    Button(onClick = {
                        result = runCatching { UnitConverter.length(number.toDouble(), fromUnit, toUnit).toString() }
                            .getOrElse { "Error: ${it.message}" }
                    }) { Text("Convert length") }
                }
                "Constants" -> {
                    Text("Scientific constants", style = MaterialTheme.typography.titleLarge)
                    OutlinedTextField(constantQuery, { constantQuery = it }, label = { Text("Search name, symbol or unit") }, modifier = Modifier.fillMaxWidth())
                    ScientificConstants.search(constantQuery).forEach { constant ->
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("${constant.name} (${constant.symbol})", style = MaterialTheme.typography.titleMedium)
                                Text("${constant.value} ${constant.unit}", style = MaterialTheme.typography.bodyLarge)
                                Text(constant.description, style = MaterialTheme.typography.bodySmall)
                                Text(if (constant.exact) "Exact / defined value" else "Approximate: ${constant.source}",
                                    style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
                "Programmer" -> {
                    Text("Number base converter", style = MaterialTheme.typography.titleLarge)
                    OutlinedTextField(baseInput, { baseInput = it }, label = { Text("Decimal integer") }, modifier = Modifier.fillMaxWidth())
                    Button(onClick = {
                        result = runCatching {
                            "Binary: ${ProgrammerCalculator.convert(baseInput, NumberBase.DECIMAL, NumberBase.BINARY)}\nOctal: ${ProgrammerCalculator.convert(baseInput, NumberBase.DECIMAL, NumberBase.OCTAL)}\nHex: ${ProgrammerCalculator.convert(baseInput, NumberBase.DECIMAL, NumberBase.HEXADECIMAL)}"
                        }.getOrElse { "Error: ${it.message}" }
                    }) { Text("Convert bases") }
                }
            }
            if (result.isNotBlank()) {
                Card(Modifier.fillMaxWidth()) {
                    Text(result, Modifier.padding(16.dp), style = MaterialTheme.typography.bodyLarge)
                }
            }
            HorizontalDivider()
            Text("Additional calculation engines are available in the source for matrix operations and complex arithmetic. Their dedicated screens are still pending.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
