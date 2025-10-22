package com.example.rentatrajes

import android.R
import android.R.attr.fontWeight
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rentatrajes.ui.theme.RentaTrajesTheme
import androidx.compose.material3.*
import androidx.core.view.WindowCompat.enableEdgeToEdge
import com.example.rentatrajes.LoginContent
import java.util.Calendar




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RentaTrajesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                        innerPadding -> AppContent(
                    modifier = Modifier.padding(innerPadding)
                )
                }
            }
        }
    }
}

@Composable
fun AppContent(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginContent(navController, modifier) }
        composable("menu") { MenuContent(navController, modifier) }

        composable("lstDetalleVenta") { LstDetalleRentaContent(navController, modifier) }
        composable("frmDetalleVenta") { FrmDetalleRentaContent(navController, modifier) }

        composable("lstProveedores") { LstProveedoresContent(navController, modifier) }
        composable("frmProveedores") { FrmProveedoresContent(navController, modifier) }

        composable("lstRentas") { LstRentasContent(navController, modifier) }
        composable("frmRentas") { FrmRentasContent(navController, modifier) }

        composable("lstClientes") { LstClientesContent(navController, modifier) }
        composable("frmClientes") { FrmClientesContent(navController, modifier) }

        composable ("lstTrajes") {LstTrajesContent(navController,modifier)}
        composable ("frmTrajes") {FrmTrajesContent(navController,modifier)}
    }
}

///////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun LoginContent(navController: NavHostController, modifier: Modifier) {

    val context = LocalContext.current

    var usuario: String by remember { mutableStateOf("") }
    var contrasena: String by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Inicio de Sesión",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Usuario:")
        TextField(
            value = usuario,
            onValueChange = { usuario = it },
            placeholder = { Text("Ingresa tu usuario") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Contraseña:")
        TextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            placeholder = { Text("Ingresa tu contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button (
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            onClick = {
                Toast.makeText(context, "Usuario: ${usuario}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Contraseña: ${contrasena}", Toast.LENGTH_SHORT).show()
                if (usuario == "UTNC" && contrasena == "123") {
                    navController.navigate("menu")
                } else {
                    Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    navController.navigate("login")
                }

            },
            modifier = Modifier.align(Alignment.End)


        ) {
            Text("Iniciar sesión")


        }

    }
}

@Composable
fun MenuContent(navController: NavHostController, modifier: Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("login")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Inicio",
                style = TextStyle(textDecoration = TextDecoration.None),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),

            )
        }
        Spacer(modifier = Modifier.height(100.dp))

        Text(
            text = "Menu",
            fontSize = 35.sp,
            fontWeight = FontWeight.ExtraLight,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Selecciona la opcion deseada",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("lstProveedores")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Proveedores",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("lstRentas")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Rentas",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                navController.navigate("lstClientes")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Clientes",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("lstTrajes")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Trajes",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("lstDetalleVenta")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Detalle De Renta",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))




    }
}
////////////////   Angel PROVEEDORES //////////////////////////////////////////////////////

@Composable
fun LstProveedoresContent(navController: NavHostController, modifier: Modifier) {
    data class Proveedores(val idprovvedor: Int, val nombre: String, val telefono: String, val direccion: String)
    val productos = remember {
        mutableStateListOf(
            Proveedores(1, "Polo", "5557478399", direccion="25 Mayo #256,col Roman Cepeda"),
            Proveedores(2, "Versace", "5559834721", direccion="Av. Hidalgo #103, col Centro"),
            Proveedores(3, "Armani", "5546729103", direccion="Calle Juárez #58, col Las Flores"),

            )
    }
// productos[index] = Producto(principe, 20, 5)

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("menu")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Menu",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                navController.navigate("frmProveedores")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Formulario",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))



        Button(
            onClick = {
                productos.add(Proveedores(4, "Louis Vuitton", "5589423765", direccion="Av. Presidente Masaryk #350, col Polanco"),
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()

        ) {
            Text(
                "Agregar IDs prueba",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Proveedores",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.Start),
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))



        Row {
            Text("ID Proveedor", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("Nombre de proveedor", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Telefono", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Direccion", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
        }
        Divider()
        productos.forEachIndexed { index, producto ->
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor)
            ) {
                Text("${producto.idprovvedor}", modifier = Modifier
                    .width(150.dp)
                )
                Text("${producto.nombre}", modifier = Modifier
                    .width(100.dp)
                )
                Text("${producto.telefono}", modifier = Modifier
                    .width(100.dp)
                )
                Text("${producto.direccion}", modifier = Modifier
                    .width(100.dp)
                )
                Button(onClick = {
                    productos.removeAt(index)
                }) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
fun FrmProveedoresContent(navController: NavHostController, modifier: Modifier) {

    val context = LocalContext.current

    var idproveedor: String by remember { mutableStateOf("") }
    var nombre: String by remember { mutableStateOf("") }
    var telefono: String by remember { mutableStateOf("") }
    var direccion: String by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("lstProveedores")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Proveedores",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Proveedores",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "ID del proveedor:")
        TextField(
            value = idproveedor,
            onValueChange = { idproveedor = it },
            placeholder = { Text("Ingresa el ID del proveedor") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Nombre del proveedor:")
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            placeholder = { Text("Ingresa el nombre del proveedor") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Telefono:")
        TextField(
            value = telefono    ,
            onValueChange = { telefono = it },
            placeholder = { Text("Ingresa el telefono del proveedor") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Direccion:")
        TextField(
            value = direccion    ,
            onValueChange = { direccion = it },
            placeholder = { Text("Ingresa la direccion del proveedor") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Toast.makeText(context, "Nombre: ${idproveedor}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Precio: ${nombre   }", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Existencias: ${telefono}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Existencias: ${direccion}", Toast.LENGTH_SHORT).show()

            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Enviar")
        }
    }
}


//////////////////////////////////////////////////////////////////

////////////////   Abdiel DETALLE RENTA  //////////////////////////////////////////////////////
@Composable
fun LstDetalleRentaContent(navController: NavHostController, modifier: Modifier) {


    data class Producto(val idRenta: Int, val idTraje: Int, val precio: Double, val descripcion: String, val fechaInicio: String, val fechaFin: String
    )


    val productos = remember {
        mutableStateListOf(
            Producto(1, 101, 850.00, "Renta de traje negro clásico", "2025-10-06 10:00", "2025-10-07 18:00"),
            Producto(2, 102, 950.00, "Renta de traje azul marino", "2025-10-08 09:30", "2025-10-09 16:00"),
            Producto(3, 103, 1200.00, "Renta de smoking para boda", "2025-10-10 14:00", "2025-10-12 20:00"),
            Producto(4, 104, 700.00, "Renta de traje gris juvenil", "2025-10-11 12:00", "2025-10-12 15:30"),
            Producto(5, 105, 1100.00, "Renta de traje blanco para gala", "2025-10-13 09:00", "2025-10-14 19:00")
        )
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {


        Button(
            onClick = { navController.navigate("Menu") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Menú",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = { navController.navigate("frmDetalleVenta") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Formulario",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                productos.add(Producto(8, 10, 750.00, "Renta de traje negro a modo de prueba", "2025-10-06 10:00", "2025-10-07 18:00"),
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Agregar IDs prueba",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Detalle De Renta",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.Start),
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("ID Renta", modifier = Modifier.width(90.dp), fontWeight = FontWeight.Bold)
            Text("ID Traje", modifier = Modifier.width(90.dp), fontWeight = FontWeight.Bold)
            Text("Precio", modifier = Modifier.width(90.dp), fontWeight = FontWeight.Bold)
            Text("Descripción", modifier = Modifier.width(200.dp), fontWeight = FontWeight.Bold)
            Text("Inicio", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("Fin", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
        }

        Divider()


        productos.forEachIndexed { index, producto ->
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row(modifier = Modifier.background(bgColor)) {
                Text("${producto.idRenta}", modifier = Modifier.width(90.dp))
                Text("${producto.idTraje}", modifier = Modifier.width(90.dp))
                Text("$${producto.precio}", modifier = Modifier.width(90.dp))
                Text(producto.descripcion, modifier = Modifier.width(200.dp))
                Text(producto.fechaInicio, modifier = Modifier.width(150.dp))
                Text(producto.fechaFin, modifier = Modifier.width(150.dp))
                Button(onClick = { productos.removeAt(index) }) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FrmDetalleRentaContent(navController: NavHostController, modifier: Modifier) {

    val context = LocalContext.current

    val listaIdRenta = listOf("Renta001", "Renta002", "Renta003", "Renta004")
    val listaIdTraje = listOf("TrajeA", "TrajeB", "TrajeC", "TrajeD")
    var idRenta by remember { mutableStateOf<String?>(null) }
    var idTraje by remember { mutableStateOf<String?>(null) }
    var precio by remember { mutableStateOf<Double?>(null) }
    var descripcion by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("lstDetalleVenta")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Detalle De Renta",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Detalle De Renta",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "ID Renta:")
        var expandedRenta by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expandedRenta,
            onExpandedChange = { expandedRenta = !expandedRenta },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                readOnly = true,
                value = idRenta ?: "",
                onValueChange = { },
                label = { Text("Seleccione ID Renta") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRenta) },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expandedRenta,
                onDismissRequest = { expandedRenta = false },
            ) {
                listaIdRenta.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            idRenta = item
                            expandedRenta = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "ID Traje:")
        var expandedTraje by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expandedTraje,
            onExpandedChange = { expandedTraje = !expandedTraje },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                readOnly = true,
                value = idTraje ?: "",
                onValueChange = { },
                label = { Text("Seleccione ID Traje") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTraje) },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expandedTraje,
                onDismissRequest = { expandedTraje = false },
            ) {
                listaIdTraje.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            idTraje = item
                            expandedTraje = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Precio:")
        TextField(
            value = precio?.toString() ?: "",
            onValueChange = { precio = it.toDoubleOrNull() },
            placeholder = { Text("Ingrese el precio") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Descripción:")
        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            placeholder = { Text("Ingrese la descripción") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Fecha/Hora Inicio
        Text(text = "Fecha/Hora Inicio:")
        OutlinedTextField(
            value = fechaInicio,
            onValueChange = { },
            placeholder = { Text("Seleccione fecha y hora") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val calendar = Calendar.getInstance()
                    val datePickerDialog = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            calendar.set(year, month, dayOfMonth)
                            val timePickerDialog = TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    calendar.set(Calendar.MINUTE, minute)
                                    fechaInicio = String.format(
                                        "%04d-%02d-%02d %02d:%02d",
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH) + 1,
                                        calendar.get(Calendar.DAY_OF_MONTH),
                                        calendar.get(Calendar.HOUR_OF_DAY),
                                        calendar.get(Calendar.MINUTE)
                                    )
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            )
                            timePickerDialog.show()
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    datePickerDialog.show()
                },
            enabled = false
        )
        Spacer(modifier = Modifier.height(16.dp))


        Text(text = "Fecha/Hora Fin:")
        OutlinedTextField(
            value = fechaFin,
            onValueChange = { },
            placeholder = { Text("Seleccione fecha y hora") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val calendar = Calendar.getInstance()
                    val datePickerDialog = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            calendar.set(year, month, dayOfMonth)
                            val timePickerDialog = TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    calendar.set(Calendar.MINUTE, minute)
                                    fechaFin = String.format(
                                        "%04d-%02d-%02d %02d:%02d",
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH) + 1,
                                        calendar.get(Calendar.DAY_OF_MONTH),
                                        calendar.get(Calendar.HOUR_OF_DAY),
                                        calendar.get(Calendar.MINUTE)
                                    )
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            )
                            timePickerDialog.show()
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )
                    datePickerDialog.show()
                },
            enabled = false
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Toast.makeText(context, "ID Renta: ${idRenta}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "ID Traje: ${idTraje}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Precio: ${precio}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Descripción: ${descripcion}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Fecha/Hora Inicio: ${fechaInicio}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Fecha/Hora Fin: ${fechaFin}", Toast.LENGTH_SHORT).show()


            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Enviar")
        }
    }
}
//////////////////////////////////////////////////////////////////

////////////////   Hector RENTAS  //////////////////////////////////////////////////////
@Composable
fun LstRentasContent(navController: NavHostController, modifier: Modifier) {
    data class Rentas(val idrenta: Int, val idcliente: String)
    val Rentas = remember {
        mutableStateListOf(
            Rentas(1, "Juan"),
            Rentas(2, "El Sorprendente Raul"),
            Rentas(3, "Zoe")
        )
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("menu")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Menu",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                navController.navigate("frmRentas")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Formulario",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Rentas.add(Rentas(4, "Sandra"))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Agregar IDs prueba",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Rentas",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("ID de la Renta", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("ID del cliente", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
        }
        Divider()
        Rentas.forEachIndexed { index, producto ->
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor)
            ) {
                Text("${producto.idrenta}", modifier = Modifier
                    .width(150.dp)
                )
                Text("${producto.idcliente}", modifier = Modifier
                    .width(100.dp)
                )
                Button(onClick = {
                    Rentas.removeAt(index)
                }) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrmRentasContent(navController: NavHostController, modifier: Modifier) {

    val context = LocalContext.current

    var idrenta by remember { mutableStateOf("") }
    var idcliente by remember { mutableStateOf("") }

    val rentasList = listOf("R001", "R002", "R003", "R004")
    val clientesList = listOf("El Sorprendente Raul", "Zoe", "Lara Horse")

    // Estados para desplegar los menús -----------------------------------------//
    var expandedRenta by remember { mutableStateOf(false) }
    var expandedCliente by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("lstRentas")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Rentas",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Rentas",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Text(text = "ID de la renta:")
        ExposedDropdownMenuBox(
            expanded = expandedRenta,
            onExpandedChange = { expandedRenta = !expandedRenta }
        ) {
            TextField(
                value = idrenta,
                onValueChange = {},
                readOnly = true,
                label = { Text("Selecciona el ID de la renta") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRenta) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedRenta,
                onDismissRequest = { expandedRenta = false }
            ) {
                rentasList.forEach { id ->
                    DropdownMenuItem(
                        text = { Text(id) },
                        onClick = {
                            idrenta = id
                            expandedRenta = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- ComboBox para ID del cliente ---

        Text(text = "ID del cliente:")

        ExposedDropdownMenuBox(
            expanded = expandedCliente,
            onExpandedChange = { expandedCliente = !expandedCliente }
        ) {
            TextField(
                value = idcliente,
                onValueChange = {},
                readOnly = true,
                label = { Text("Selecciona el ID del cliente") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCliente) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedCliente,
                onDismissRequest = { expandedCliente = false }
            ) {
                clientesList.forEach { id ->
                    DropdownMenuItem(
                        text = { Text(id) },
                        onClick = {
                            idcliente = id
                            expandedCliente = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        // --- Botón Enviar ---
        Button(
            onClick = {
                Toast.makeText(context, "ID de la Renta: $idrenta", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "ID del Cliente: $idcliente", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Enviar")
        }
    }
}


//////////////////////////////////////////////////////////////////

////////////////   Big-tour CLIENTES  //////////////////////////////////////////////////////
@Composable
fun LstClientesContent(navController: NavHostController, modifier: Modifier) {
    data class Producto(val idcliente: Int, val nombre: String, val telefono: String, val CorreoElectronico: String )
    val productos = remember {
        mutableStateListOf(
            Producto(1, "juan", "8783347354" , CorreoElectronico = "asd@qwer.com"),
            Producto(2, "el sorprendente raul", "8774324789", "qwer@zxc.com"),
            Producto(3, "zoe", "8673674783", "braulio@xpde.com")
        )
    }
// productos[index] = Producto(principe, 20, 5)

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("menu")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Menu",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                navController.navigate("frmClientes")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Formulario",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                productos.add(Producto(4, "pedro",  "8781258402", "fdsfs@gmail.com"))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Agregar Clientes  prueba",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Clientes",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.Start),
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text("ID Cliente", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("Nombre", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Telefono", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Correo Electroníco", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
        }
        Divider()
        productos.forEachIndexed { index, producto ->
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor)
            ) {
                Text("${producto.idcliente}", modifier = Modifier
                    .width(150.dp)
                )
                Text(producto.nombre, modifier = Modifier
                    .width(100.dp)
                )
                Text(producto.telefono, modifier = Modifier
                    .width(100.dp)
                )
                Text(producto.CorreoElectronico, modifier = Modifier
                    .width(100.dp)
                )
                Button(onClick = {
                    productos.removeAt(index)
                }) {
                    Text("Eliminar")
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrmClientesContent(navController: NavHostController, modifier: Modifier) {

    val context = LocalContext.current

    var idrenta by remember { mutableStateOf("") }
    var idcliente by remember { mutableStateOf("") }
    var idtraje by remember { mutableStateOf("") }

    // Lista de clientes (puedes obtenerla dinámicamente de una BD)
    val listaClientes = listOf("1 - Juan", "2 - El sorprendente raul", "3 - Zoe")

    var expanded by remember { mutableStateOf(false) }
    var selectedCliente by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("lstClientes")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Clientes",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Clientes",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔽 ID de la Renta
        Text(text = "ID de la Renta:")
        TextField(
            value = idrenta,
            onValueChange = { idrenta = it },
            placeholder = { Text("Ingresa el ID de la renta") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔽 ComboBox para ID del Cliente
        Text(text = "ID del Cliente:")
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedCliente,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Selecciona un cliente") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listaClientes.forEach { cliente ->
                    DropdownMenuItem(
                        text = { Text(cliente) },
                        onClick = {
                            selectedCliente = cliente
                            expanded = false
                            // Si quieres extraer el ID:
                            idcliente = cliente.substringBefore(" - ")
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔽 Teléfono
        Text(text = "Teléfono:")
        TextField(
            value = idtraje,
            onValueChange = { idtraje = it },
            placeholder = { Text("Ingresa el teléfono") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔽 Correo electrónico
        Text(text = "Correo Electrónico:")
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Ingresa el correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Toast.makeText(context, "ID Renta: $idrenta", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Cliente: $idcliente", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Teléfono: $idtraje", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Enviar")
        }
    }
}

//////////////////////////////////////////////////////////////////

////////////////   Sofi TRAJES  //////////////////////////////////////////////////////

@Composable
fun LstTrajesContent(navController: NavHostController, modifier: Modifier) {
    data class Producto(val idTraje: Int, val nombreTraje: String, val descripcion: String, val precio: Float)
    val productos = remember {
        mutableStateListOf(
            Producto(1, "Traje de gala", "Clásico corte slim fit, perfecto para bodas.", precio = 600.99f),
            Producto(2, "traje de lino",  "Ligero y fresco, ideal para verano.",520.99f),
            Producto(3, "Smoking negro", "Para eventos formales y alfombras rojas.", 650.99f)
        )
    }
// productos[index] = Producto(principe, 20, 5)

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            ///          .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("menu")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Menu",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                navController.navigate("frmTrajes")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Formulario",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                productos.add(Producto(4, "smoking azul", "para eventos formales",680.50f))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Agregar traje prueba",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Trajes",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.Start),
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row (modifier = Modifier.fillMaxWidth())   {
            Text("ID de traje", modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold)
            Text("Nombre", modifier = Modifier.weight(1.0f), fontWeight = FontWeight.Bold)
            Text("Descripcion", modifier = Modifier.weight(2.0f), fontWeight = FontWeight.Bold)
            Text("precio", modifier = Modifier.weight(1.0f), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.weight(0.7f), fontWeight = FontWeight.Bold)
        }
        Divider()
        productos.forEachIndexed { index, producto ->
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor)
            ) {
                Text("${producto.idTraje}", modifier = Modifier
                    .weight(0.8f)
                )
                Text(producto.nombreTraje, modifier = Modifier
                    .weight(1.0f)
                )
                Text(producto.descripcion, modifier = Modifier
                    .weight(2.0f)
                )
                Text(String.format("$%.2f", producto.precio), modifier = Modifier
                    .weight(1.0f))


                Button(onClick = {
                    productos.removeAt(index)
                },
                    // Aplica el peso al Button y una altura para controlarlo
                    modifier = Modifier.weight(0.7f).height(36.dp),
                    //    contentPadding = PaddingValues(horizontal = 4.dp) // Reduce el padding interno
                ) {
                    Text("Eliminar", fontSize = 10.sp)
                }
            }
        }
    }
}


@Composable
fun FrmTrajesContent(navController: NavHostController, modifier: Modifier) {

    val context = LocalContext.current

    var idTraje: String by remember { mutableStateOf("") }
    var nombreTraje: String by remember { mutableStateOf("") }
    var descripcion: String by remember { mutableStateOf("") }
    var precio: String by remember { mutableStateOf("") }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("lstTrajes")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "trajes",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Trajes",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "ID del traje:")
        TextField(
            value = idTraje,
            onValueChange = { idTraje = it },
            placeholder = { Text("Ingresa el ID del traje") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "nombre del traje:")
        TextField(
            value = nombreTraje,
            onValueChange = { nombreTraje = it },
            placeholder = { Text("Ingresa el nombre del traje") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )



        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Descripción:")
        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            placeholder = { Text("Ingresa la descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Precio:")
        TextField(
            value = precio,
            onValueChange = { precio = it },
            placeholder = { Text("Ingresa el precio") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )




        Button(
            onClick = {
                Toast.makeText(context, "ID: $idTraje", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Nombre: $nombreTraje", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Descripción: $descripcion", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Precio: $precio", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.End)
        )

        {
            Text("Enviar")
        }
    }
}

//////////////////////////////////////////////////////////////////

@Preview(showBackground = true)
@Composable
fun AppContentPreview() {
    RentaTrajesTheme {
        AppContent()
    }
}

@Composable
fun RentaTrajesTheme(content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}