package com.aman.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aman.calculator.ui.theme.CalculatorTheme
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                CalculatorApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var memory by remember { mutableStateOf(0.0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Calculator") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = input,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = result,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.End
                )
            }

            Divider(
                color = MaterialTheme.colorScheme.onBackground,
                thickness = 2.dp
            )

            Column(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth()
            ) {
                val mainButtons = listOf(
                    listOf("7", "8", "9", "/"),
                    listOf("4", "5", "6", "*"),
                    listOf("1", "2", "3", "-"),
                    listOf("0", ".", "=", "+")
                )
                val sciButtons = listOf(
                    listOf("sin", "cos", "tan", "√", "%")
                )
                val memButtons = listOf(
                    listOf("MR", "MC", "M+", "M-")
                )
                val topButtons = listOf(
                    listOf("AC", "C", "(", ")")
                )

                // Top Buttons
                topButtons.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { button ->
                            CalculatorButton(button) {
                                when (button) {
                                    "AC" -> {
                                        input = ""
                                        result = ""
                                    }
                                    "C" -> {
                                        if (input.isNotEmpty()) {
                                            input = input.dropLast(1)
                                        }
                                    }
                                    else -> {
                                        input += button
                                    }
                                }
                            }
                        }
                    }
                }

                // Memory Buttons
                memButtons.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { button ->
                            CalculatorButton(button) {
                                when (button) {
                                    "MR" -> input += memory.toString()
                                    "MC" -> memory = 0.0
                                    "M+" -> {
                                        try {
                                            val expr = ExpressionBuilder(input).build()
                                            memory += expr.evaluate()
                                            input = ""
                                        } catch (e: Exception) {
                                            result = "Error"
                                        }
                                    }
                                    "M-" -> {
                                        try {
                                            val expr = ExpressionBuilder(input).build()
                                            memory -= expr.evaluate()
                                            input = ""
                                        } catch (e: Exception) {
                                            result = "Error"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Scientific Buttons
                sciButtons.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { button ->
                            CalculatorButton(button) {
                                when (button) {
                                    "sin", "cos", "tan", "√", "%" -> input += button + "("
                                }
                            }
                        }
                    }
                }

                // Main Buttons
                mainButtons.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { button ->
                            CalculatorButton(button) {
                                when (button) {
                                    "=" -> {
                                        try {
                                            val expr = ExpressionBuilder(input).build()
                                            val evalResult = expr.evaluate()
                                            result = evalResult.toString()
                                        } catch (e: Exception) {
                                            result = "Error"
                                        }
                                    }
                                    else -> {
                                        input += button
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(symbol: String, onClick: () -> Unit) {
    val backgroundColor = MaterialTheme.colorScheme.secondaryContainer
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(backgroundColor, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        when (symbol) {
            "AC" -> {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clear_all),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            "C" -> {
                Icon(
                    painter = painterResource(id = R.drawable.ic_backspace),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            else -> {
                Text(text = symbol, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorAppPreview() {
    CalculatorTheme {
        CalculatorApp()
    }
}
