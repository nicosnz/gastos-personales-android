package com.example.gastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gastos.ui.theme.GastosTheme


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
// COLORES DEL DISEÑO
// ===============================

private val VerdeHeader = Color(0xFFB9F7CE)
private val VerdeBoton = Color(0xFFB9F7CE)
private val VerdeIngreso = Color(0xFF16A34A)
private val RojoEgreso = Color(0xFFDC2626)
private val GrisTexto = Color(0xFF64748B)
private val GrisBorde = Color(0xFFE2E8F0)


@OptIn(ExperimentalMaterial3Api::class)
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

    // Estados solo de UI para los menús desplegables
    var categoriaExpandida by remember { mutableStateOf(false) }
    var tipoExpandido by remember { mutableStateOf(false) }
    var tipoSeleccionado by remember { mutableStateOf(false) }
    var saldoVisible by remember { mutableStateOf(true) }

    // ===============================
    // LISTA DE MOVIMIENTOS
    // ===============================

    val movimientos = remember {
        mutableStateListOf<Movimiento>()
    }

    val categorias = listOf(
        "Comida",
        "Transporte",
        "Otros"
    )

    val tiposMovimiento = listOf(
        TipoMovimiento.INGRESO,
        TipoMovimiento.EGRESO
    )


    // ===============================
    // CÁLCULOS
    // ===============================


    val totalSaldo = calcularSaldo(movimientos)

    // ===============================
    // INTERFAZ
    // ===============================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {

        // ===============================
        // HEADER VERDE
        // ===============================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = VerdeHeader,
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {

            Text(
                text = "Control de Gastos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Saldo Disponible",
                    style = MaterialTheme.typography.bodyMedium
                )

                IconButton(onClick = { saldoVisible = !saldoVisible }) {
                    Text(
                        text = if (saldoVisible) "\uD83D\uDC41" else "\uD83D\uDE48",
                        fontSize = 18.sp
                    )
                }
            }

            Text(
                text = if (saldoVisible) "Bs %.2f".format(totalSaldo) else "Bs ***",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            // ===============================
            // TÍTULO DEL FORMULARIO
            // ===============================

            Text(
                text = "Ingresar nuevo movimiento",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ===============================
            // MONTO
            // ===============================

            Text(
                text = "Monto",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

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
                placeholder = {
                    Text("Ingresa el monto...")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = GrisBorde,
                    focusedBorderColor = VerdeIngreso
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ===============================
            // CATEGORÍA (LISTA DESPLEGABLE)
            // ===============================

            Text(
                text = "Categoría",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            ExposedDropdownMenuBox(
                expanded = categoriaExpandida,
                onExpandedChange = { categoriaExpandida = it }
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text("Selecciona la categoría correcta")
                    },
                    trailingIcon = {
                        Text("\u25BE", fontSize = 18.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = GrisBorde,
                        focusedBorderColor = VerdeIngreso
                    )
                )

                ExposedDropdownMenu(
                    expanded = categoriaExpandida,
                    onDismissRequest = { categoriaExpandida = false }
                ) {
                    categorias.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                categoria = opcion
                                categoriaExpandida = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===============================
            // TIPO DE MOVIMIENTO (LISTA DESPLEGABLE)
            // ===============================

            Text(
                text = "Tipo de Movimiento",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            ExposedDropdownMenuBox(
                expanded = tipoExpandido,
                onExpandedChange = { tipoExpandido = it }
            ) {
                OutlinedTextField(
                    value = if (tipoSeleccionado) {
                        if (tipoMovimiento == TipoMovimiento.INGRESO) "Ingreso" else "Egreso"
                    } else {
                        ""
                    },
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text("Selecciona el movimiento")
                    },
                    trailingIcon = {
                        Text("\u25BE", fontSize = 18.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = GrisBorde,
                        focusedBorderColor = VerdeIngreso
                    )
                )

                ExposedDropdownMenu(
                    expanded = tipoExpandido,
                    onDismissRequest = { tipoExpandido = false }
                ) {
                    tiposMovimiento.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(if (opcion == TipoMovimiento.INGRESO) "Ingreso" else "Egreso") },
                            onClick = {
                                tipoMovimiento = opcion
                                tipoSeleccionado = true
                                tipoExpandido = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===============================
            // DESCRIPCIÓN
            // ===============================

            Text(
                text = "Descripción",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                },
                placeholder = {
                    Text("¿Qué hiciste con tu plata?")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = GrisBorde,
                    focusedBorderColor = VerdeIngreso
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

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
                        tipoSeleccionado = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeBoton,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Registrar",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ===============================
            // MENSAJE
            // ===============================

            if (mensaje.isNotBlank()) {

                Text(
                    text = mensaje,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(color = GrisBorde)

            Spacer(modifier = Modifier.height(20.dp))

            // ===============================
            // MOVIMIENTOS
            // ===============================

            Text(
                text = "Movimientos Registrados",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (movimientos.isEmpty()) {
                Text(
                    text = "No se registraron transacciones",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto
                )
            } else {

                movimientos.forEach { movimiento ->

                    val esIngreso = movimiento.tipo == TipoMovimiento.INGRESO
                    val colorMonto = if (esIngreso) VerdeIngreso else RojoEgreso
                    val signo = if (esIngreso) "+" else "-"

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (esIngreso) "Ingreso" else "Egreso",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Text(
                                text = "$signo Bs %.2f".format(movimiento.monto),
                                color = colorMonto,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${movimiento.categoria} · ${movimiento.descripcion}",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrisTexto
                        )
                    }

                    HorizontalDivider(color = GrisBorde)
                }
            }
        }
    }
}