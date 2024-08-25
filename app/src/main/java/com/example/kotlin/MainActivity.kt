package com.example.kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                CalculatorScreen()
            }
        }
    }
}

@Composable
fun CalculatorTheme(content: @Composable () -> Unit) {
    var darkTheme by remember { mutableStateOf(true) }

    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme(),
    ) {
        Surface {
            content()
        }
    }
}

@Composable
fun CalculatorScreen() {
    var display by remember { mutableStateOf("0") }
    var operand1 by remember { mutableStateOf("") }
    var operand2 by remember { mutableStateOf("") }
    var operator by remember { mutableStateOf<Char?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var darkTheme by remember { mutableStateOf(true) }

    fun onNumberClick(number: String) {
        if (operator == null) {
            operand1 += number
            display = operand1
        } else {
            operand2 += number
            display = "$operand1 $operator $operand2"
        }
    }

    fun onOperatorClick(newOperator: Char) {
        if (operator == null) {
            operator = newOperator
            display = "$operand1 $operator"
        } else {
            operator = newOperator
            display = "$operand1 $operator"
        }
    }

    fun onClearClick() {
        operand1 = ""
        operand2 = ""
        operator = null
        display = "0"
    }

    fun onEqualClick() {
        if (operator != null && operand2.isNotEmpty()) {
            val result = when (operator) {
                '+' -> operand1.toInt() + operand2.toInt()
                '-' -> operand1.toInt() - operand2.toInt()
                '*' -> operand1.toInt() * operand2.toInt()
                '/' -> operand1.toInt() / operand2.toInt()
                else -> 0
            }
            display = "$operand1 $operator $operand2 = $result"
            operand1 = result.toString()
            operand2 = ""
            operator = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) 
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .background(MaterialTheme.colorScheme.surface) 
                .padding(16.dp)
                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        ) {
            // Theme toggle button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { darkTheme = !darkTheme }) {
                    Icon(
                        painter = painterResource(id = if (darkTheme) R.drawable.ic_sun else R.drawable.ic_moon),
                        contentDescription = if (darkTheme) "Switch to Light Mode" else "Switch to Dark Mode",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Display screen
            BasicTextField(
                value = display,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(1.dp, MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                textStyle = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Number buttons
            Column {
                for (row in listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("0", "C", "=")
                )) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (number in row) {
                            NumberButton(number, darkTheme) {
                                when (number) {
                                    "C" -> onClearClick()
                                    "=" -> onEqualClick()
                                    else -> onNumberClick(number)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Operator buttons
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (op in listOf("+", "-", "*", "/")) {
                    OperatorButton(op, darkTheme) { onOperatorClick(op[0]) }
                }
            }
        }

        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        .shadow(10.dp, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Popup", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("This is a simple popup!", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = { showDialog = false }) {
                                Text("OK")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NumberButton(number: String, darkTheme: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(64.dp)
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (darkTheme) Color.Gray else Color.White,
            contentColor = if (darkTheme) Color.White else Color.Black
        )
    ) {
        Text(text = number)
    }
}

@Composable
fun OperatorButton(operator: String, darkTheme: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(64.dp)
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (darkTheme) Color.DarkGray else Color.LightGray,
            contentColor = if (darkTheme) Color.White else Color.Black
        )
    ) {
        Text(text = operator)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorTheme {
        CalculatorScreen()
    }
}
