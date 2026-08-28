
package com.example.gastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gastos.ui.theme.GastosTheme

// ===============================
// DATOS
// ===============================

enum class TipoMovimiento {
    INGRESO,
    EGRESO
}

data class Movimiento(
    val descripcion: String,
    val monto: Double,
    val categoria: String,
    val tipo: TipoMovimiento
)

// ===============================
// FUNCIONES
// ===============================

fun calcularIngresos(movimientos: List<Movimiento>): Double {
    return movimientos
        .filter { it.tipo == TipoMovimiento.INGRESO }
        .sumOf { it.monto }
}

fun calcularEgresos(movimientos: List<Movimiento>): Double {
    return movimientos
        .filter { it.tipo == TipoMovimiento.EGRESO }
        .sumOf { it.monto }
}

fun calcularSaldo(movimientos: List<Movimiento>): Double {
    return calcularIngresos(movimientos) - calcularEgresos(movimientos)
}

// ===============================
// MAIN ACTIVITY
// ===============================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            GastosTheme {
                AplicacionGastos()
            }
        }
    }
}

// ===============================
// APLICACIÓN
// ===============================

@Composable
fun AplicacionGastos() {

    // ===============================
    // ESTADOS DEL FORMULARIO
    // ===============================

    var descripcion by remember {
        mutableStateOf("")
    }

    var monto by remember {
        mutableStateOf("")
    }

    var categoria by remember {
        mutableStateOf("")
    }

    var tipoMovimiento by remember {
        mutableStateOf(TipoMovimiento.INGRESO)
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    // ===============================
    // LISTA DE MOVIMIENTOS
    // ===============================

    val movimientos = remember {
        mutableStateListOf<Movimiento>()
    }

    // ===============================
    // CÁLCULOS
    // ===============================

    val totalIngresos = calcularIngresos(movimientos)
    val totalEgresos = calcularEgresos(movimientos)
    val totalSaldo = calcularSaldo(movimientos)

    // ===============================
    // INTERFAZ
    // ===============================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // TÍTULO

        Text(
            text = "Control de Gastos",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // ===============================
        // DESCRIPCIÓN
        // ===============================

        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                descripcion = it
            },
            label = {
                Text("Descripción")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // ===============================
        // MONTO
        // ===============================

        OutlinedTextField(
            value = monto,
            onValueChange = { valor ->

                // Permite números y un punto/coma decimal
                if (
                    valor.isEmpty() ||
                    valor.matches(Regex("^\\d*([.,]\\d*)?$"))
                ) {
                    monto = valor
                }
            },
            label = {
                Text("Monto")
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // ===============================
        // CATEGORÍA
        // ===============================

        OutlinedTextField(
            value = categoria,
            onValueChange = {
                categoria = it
            },
            label = {
                Text("Categoría")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // ===============================
        // TIPO DE MOVIMIENTO
        // ===============================

        Text(
            text = "Tipo de Movimiento",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = {
                    tipoMovimiento = TipoMovimiento.INGRESO
                }
            ) {
                Text("INGRESO")
            }

            Button(
                onClick = {
                    tipoMovimiento = TipoMovimiento.EGRESO
                }
            ) {
                Text("EGRESO")
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Seleccionado: $tipoMovimiento"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // ===============================
        // BOTÓN REGISTRAR
        // ===============================

        Button(
            onClick = {

                val montoNumero = monto
                    .replace(",", ".")
                    .toDoubleOrNull()

                if (
                    descripcion.isBlank() ||
                    categoria.isBlank() ||
                    montoNumero == null ||
                    montoNumero <= 0
                ) {

                    mensaje = "Completa los campos correctamente"

                } else {

                    val nuevoMovimiento = Movimiento(
                        descripcion = descripcion,
                        monto = montoNumero,
                        categoria = categoria,
                        tipo = tipoMovimiento
                    )

                    movimientos.add(nuevoMovimiento)

                    mensaje = "Movimiento creado correctamente"

                    // Limpiar formulario

                    descripcion = ""
                    monto = ""
                    categoria = ""

                    tipoMovimiento = TipoMovimiento.INGRESO
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // ===============================
        // MENSAJE
        // ===============================

        if (mensaje.isNotBlank()) {

            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // ===============================
        // RESUMEN
        // ===============================

        Text(
            text = "RESUMEN",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "Ingresos: Bs %.2f".format(totalIngresos)
            )

            Text(
                text = "Egresos: Bs %.2f".format(totalEgresos)
            )

            Text(
                text = "Saldo: Bs %.2f".format(totalSaldo)
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // ===============================
        // MOVIMIENTOS
        // ===============================

        Text(
            text = "MOVIMIENTOS REALIZADOS",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // ENCABEZADO

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Descripción",
                modifier = Modifier.weight(2f)
            )

            Text(
                text = "Categoría",
                modifier = Modifier.weight(2f)
            )

            Text(
                text = "Monto",
                modifier = Modifier.weight(2f)
            )

            Text(
                text = "Tipo",
                modifier = Modifier.weight(2f)
            )
        }

        // LISTA DE MOVIMIENTOS

        movimientos.forEach { movimiento ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = movimiento.descripcion,
                    modifier = Modifier.weight(2f)
                )

                Text(
                    text = movimiento.categoria,
                    modifier = Modifier.weight(2f)
                )

                Text(
                    text = "Bs %.2f".format(movimiento.monto),
                    modifier = Modifier.weight(2f)
                )

                Text(
                    text = movimiento.tipo.name,
                    modifier = Modifier.weight(2f)
                )
            }
        }
    }
}


