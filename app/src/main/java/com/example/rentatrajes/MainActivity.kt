package com.example.rentatrajes
import android.content.Context
import android.content.SharedPreferences

import android.R
import android.R.attr.fontWeight
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.core.view.WindowCompat.enableEdgeToEdge
import com.example.rentatrajes.LoginContent
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.Calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
fun setSessionValue(context: Context, key: String, value: String) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val editor = sharedPrefs.edit()
    editor.putString(key, value)
    editor.apply()
}

fun getSessionValue(context: Context, key: String, defaultValue: String): String? {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    return sharedPrefs.getString(key, defaultValue)
}





class ProveedoresViewModel : ViewModel() {

    private val _proveedores = MutableStateFlow<List<ModeloProveedor>>(emptyList())
    val proveedores: StateFlow<List<ModeloProveedor>> = _proveedores

    private val _selectedProveedor = MutableStateFlow<ModeloProveedor?>(null)
    val selectedProveedor: StateFlow<ModeloProveedor?> = _selectedProveedor

    fun cargarProveedores() {
        viewModelScope.launch {
            try {
                val lista = RetrofitClient.api.getProveedores()
                _proveedores.value = lista
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun seleccionarProveedor(p: ModeloProveedor) {
        _selectedProveedor.value = p
    }

    fun limpiarSeleccion() {
        _selectedProveedor.value = null
    }

    fun agregarProveedor(nombre: String, telefono: String, direccion: String, onComplete: (Boolean) -> Unit) {

        // 🚨 PASO 1: DEBUG ANTES DE ENVIAR 🚨
        Log.d("DBG_AGREGAR", "ENVIANDO -> nombre='$nombre', telefono='$telefono', direccion='$direccion'")

        viewModelScope.launch {
            try {
                val resp = RetrofitClient.api.agregarProveedor(nombre, telefono, direccion)

                val success = resp.isSuccessful && resp.body()?.contains("correcto") == true

                onComplete(success)

                if (success) cargarProveedores()

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("DBG_AGREGAR", "ERROR -> ${e.message}")
                onComplete(false)
            }
        }
    }


    fun modificarProveedor(id: Int, nombre: String, telefono: String, direccion: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val resp = RetrofitClient.api.modificarProveedor(id.toString(), nombre, telefono, direccion)
                val success = resp.isSuccessful && resp.body()?.contains("correcto") == true
                onComplete(success)
                if (success) cargarProveedores()
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false)
            }
        }
    }

    fun eliminarProveedor(id: Int, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val resp = RetrofitClient.api.eliminarProveedor(id)
                val success = resp.isSuccessful && resp.body()?.contains("correcto") == true
                onComplete(success)
                if (success) cargarProveedores()
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false)
            }
        }
    }
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////77

class TrajesViewModel : ViewModel() {

    private val _trajes = MutableStateFlow<List<ModeloTraje>>(emptyList())
    val trajes: StateFlow<List<ModeloTraje>> = _trajes

    private val _selectedTraje = MutableStateFlow<ModeloTraje?>(null)
    val selectedTraje: StateFlow<ModeloTraje?> = _selectedTraje

    fun cargarTrajes() {
        viewModelScope.launch {
            try {
                val lista = RetrofitClient.api.getTrajes()
                _trajes.value = lista
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun seleccionarTraje(t: ModeloTraje) {
        _selectedTraje.value = t
    }

    fun limpiarSeleccion() {
        _selectedTraje.value = null
    }

    fun agregarTraje(nombre: String, descripcion: String, precio: Float, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val resp = RetrofitClient.api.agregarTraje(nombre, descripcion, precio)
                val success = resp.isSuccessful && resp.body()?.contains("correcto") == true
                onComplete(success)
                if (success) cargarTrajes()
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false)
            }
        }
    }

    fun modificarTraje(id: Int, nombre: String, descripcion: String, precio: Float, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val resp = RetrofitClient.api.modificarTraje(id.toString(), nombre, descripcion, precio)
                val success = resp.isSuccessful && resp.body()?.contains("correcto") == true
                onComplete(success)
                if (success) cargarTrajes()
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false)
            }
        }
    }

    fun eliminarTraje(id: Int, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val resp = RetrofitClient.api.eliminarTraje(id)
                val success = resp.isSuccessful && resp.body()?.contains("correcto") == true
                onComplete(success)
                if (success) cargarTrajes()
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false)
            }
        }
    }
}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


class ClientesViewModel : ViewModel() {

    private val _clientes = MutableStateFlow<List<ModeloCliente>>(emptyList())
    val clientes: StateFlow<List<ModeloCliente>> = _clientes

    private val _selectedCliente = MutableStateFlow<ModeloCliente?>(null)
    val selectedCliente: StateFlow<ModeloCliente?> = _selectedCliente

    // ------------------- CARGAR CLIENTES -------------------
    fun cargarClientes() {
        viewModelScope.launch {
            try {
                val lista = RetrofitClient.api.getClientes()
                _clientes.value = lista
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ClientesVM", "Error al cargar clientes: ${e.message}")
            }
        }
    }

    // ------------------- SELECCIONAR / LIMPIAR -------------------
    fun seleccionarCliente(cliente: ModeloCliente) {
        _selectedCliente.value = cliente
    }

    fun limpiarSeleccion() {
        _selectedCliente.value = null
    }

    // ------------------- AGREGAR CLIENTE -------------------
    fun agregarCliente(
        nombre: String,
        telefono: String,
        correo: String,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val resp: Response<String> = RetrofitClient.api.agregarCliente(nombre, telefono, correo)
                val success = resp.isSuccessful && resp.body()?.contains("correcto") == true
                onComplete(success)
                if (success) cargarClientes()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ClientesVM", "Error al agregar cliente: ${e.message}")
                onComplete(false)
            }
        }
    }

    // ------------------- MODIFICAR CLIENTE -------------------
    fun modificarCliente(
        id: Int,
        nombre: String,
        telefono: String,
        correo: String,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val resp: Response<String> = RetrofitClient.api.modificarCliente(id.toString(), nombre, telefono, correo)
                val success = resp.isSuccessful && resp.body()?.contains("correcto") == true
                onComplete(success)
                if (success) cargarClientes()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ClientesVM", "Error al modificar cliente: ${e.message}")
                onComplete(false)
            }
        }
    }

    // ------------------- ELIMINAR CLIENTE -------------------
    fun eliminarCliente(id: Int, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val resp: Response<String> = RetrofitClient.api.eliminarCliente(id)
                val success = resp.isSuccessful && resp.body()?.contains("correcto") == true
                onComplete(success)
                if (success) cargarClientes()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ClientesVM", "Error al eliminar cliente: ${e.message}")
                onComplete(false)
            }
        }
    }
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



class MainActivity : ComponentActivity() {

    private val proveedoresVM: ProveedoresViewModel by viewModels()
    private val trajesVM: TrajesViewModel by viewModels()
    private val clientesVM: ClientesViewModel by viewModels() // <-- Nuevo

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // si usas
        setContent {
            RentaTrajesTheme {
                AppContent(
                    proveedoresVM = proveedoresVM,
                    trajesVM = trajesVM,
                    clientesVM = clientesVM // <-- Nuevo
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppContent(
    modifier: Modifier = Modifier,
    proveedoresVM: ProveedoresViewModel,
    trajesVM: TrajesViewModel,
    clientesVM: ClientesViewModel // <-- Nuevo
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val sesionIniciada: String? = getSessionValue(context, "sesionIniciada", "no")
    val startDestination = if (sesionIniciada == "yes") "menu" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // LOGIN y MENU
        composable("login") { LoginContent(navController, modifier) }
        composable("menu") { MenuContent(navController, modifier) }

        // PROVEEDORES
        composable("lstProveedores") { LstProveedoresContent(navController, proveedoresVM) }
        composable("frmProveedores") { FrmProveedoresContent(navController, proveedoresVM) }

        // TRAJES
        composable("lstTrajes") { LstTrajesContent(navController, trajesVM) }
        composable("frmTrajes") { FrmTrajesContent(navController, trajesVM) }

        // CLIENTES <-- NUEVO
        composable("lstClientes") { LstClientesContent(navController, clientesVM) }
        composable("frmClientes") { FrmClientesContent(navController, clientesVM) }


        // RENTAS
        composable("lstDetalleVenta") { LstDetalleRentaContent(navController, modifier) }
        composable("frmDetalleVenta") { FrmDetalleRentaContent(navController, modifier) }

        composable("lstRentas") { LstRentasContent(navController, modifier) }
        composable("frmRentas") { FrmRentasContent(navController, modifier) }

        // COMENTARIOS
        composable("PantallaComentarios") { PantallaComentarios(api, navController) }
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ------- COMENTARIOS -------------//
data class ModeloComentario(
    val id_comentario: Int,
    val comentario: String
)

///////////////////////////////////////////////////////////////////////////////////////////////////
// ────── MODELOS RENTA──────
data class ModeloRenta(
    val id_renta: Int,
    val nombre: String
)
data class ModeloClienteRenta(
    val id_cliente: Int,
    val nombre: String
)
data class Respuesta(
    val status: String,
    val mensaje: String
)
data class RespuestaSimple(
    val status: String
)

//───────────────────────────────────
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
data class OpcionCliente(
    val value: String,
    val text: String
)
data class ModeloCliente(
    val id: Int,
    val nombre: String,
    val telefono: String,
    val correo: String
)


///////////////////////////////////////////////////////////////////////////////////////////

data class LoginResponse(
    val success: Boolean,
    val message: String
)
data class ModeloProveedor(
    val id: Int,
    val nombre: String,
    val telefono: String,
    val direccion: String
)


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

data class ModeloTraje(
    val id_traje: Int,
    val nombre_traje: String,
    val descripcion: String,
    val precio: Float
)


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

val retrofit = Retrofit.Builder()
    .baseUrl("https://sanyo-scanned-tahoe-capable.trycloudflare.com/api/")
    .addConverterFactory(ScalarsConverterFactory.create()) // <- primero
    .addConverterFactory(GsonConverterFactory.create())    // <- luego
    .build()

val api = retrofit.create(ApiService::class.java)
@Composable
fun LoginContent(navController: NavHostController, modifier: Modifier) {

    val context = LocalContext.current

    var usuario by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }

    val scope = rememberCoroutineScope()

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

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            onClick = {

                if (usuario.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(context, "Faltan datos", Toast.LENGTH_SHORT).show()
                    return@Button   // <--- CORREGIDO AQUÍ
                }

                scope.launch {
                    try {
                        val respuesta = api.IniciarSesion(usuario, contrasena)

                        if (respuesta.isSuccessful) {
                            val body = respuesta.body()

                            if (body != null && body.success) {
                                Toast.makeText(context, "Inicio correcto", Toast.LENGTH_SHORT).show()
                                setSessionValue(context, "sesionIniciada", "yes")
                                navController.navigate("menu")
                                setSessionValue(context, "sesionIniciada", "yes")
                            } else {
                                Toast.makeText(context, body?.message ?: "Datos incorrectos", Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            val error = respuesta.errorBody()?.string() ?: "Error del servidor"
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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

        Button(
            onClick = {
                navController.navigate("PantallaComentarios")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = modifier.fillMaxWidth( )
        ) {
            Text(
                "Comentarios",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


    }
}
////////////////   Angel PROVEEDORES //////////////////////////////////////////////////////

@Composable
fun LstProveedoresContent(navController: androidx.navigation.NavHostController, viewModel: ProveedoresViewModel) {

    val proveedores by viewModel.proveedores.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) { viewModel.cargarProveedores() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp)
    ) {
        Button(onClick = { navController.navigate("menu") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)) {
            Text("Menu")
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            viewModel.limpiarSeleccion()
            navController.navigate("frmProveedores")
        }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)) {
            Text("Agregar Proveedor")
        }

        Spacer(Modifier.height(16.dp))
        Text("Proveedores", fontSize = MaterialTheme.typography.titleLarge.fontSize, fontWeight = FontWeight.ExtraBold, color = Color.Red)
        Spacer(Modifier.height(16.dp))

        Row {
            Text("ID", Modifier.width(50.dp), fontWeight = FontWeight.Bold)
            Text("Nombre", Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("Teléfono", Modifier.width(120.dp), fontWeight = FontWeight.Bold)
            Text("Dirección", Modifier.width(200.dp), fontWeight = FontWeight.Bold)
            Text("Eliminar", Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Modificar", Modifier.width(100.dp), fontWeight = FontWeight.Bold)
        }
        Divider()

        proveedores.forEach { proveedor ->
            val index = proveedores.indexOf(proveedor)
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row(modifier = Modifier
                .background(bgColor)
                .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${proveedor.id}", Modifier.width(50.dp))
                Text("${proveedor.nombre}", Modifier.width(150.dp))
                Text("${proveedor.telefono}", Modifier.width(120.dp))
                Text("${proveedor.direccion}", Modifier.width(200.dp))

                Button(onClick = {
                    scope.launch {
                        viewModel.eliminarProveedor(proveedor.id) { success ->
                            android.widget.Toast.makeText(context, if (success) "Proveedor eliminado" else "Error al eliminar", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }
                }, modifier = Modifier.width(100.dp)) { Text("Eliminar") }

                Spacer(modifier = Modifier.width(4.dp))

                Button(onClick = {
                    viewModel.seleccionarProveedor(proveedor)
                    navController.navigate("frmProveedores")
                }, modifier = Modifier.width(100.dp)) { Text("Modificar") }
            }
        }
    }
}




@Composable
fun FrmProveedoresContent(navController: androidx.navigation.NavHostController, viewModel: ProveedoresViewModel) {

    val proveedorSeleccionado by viewModel.selectedProveedor.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf(proveedorSeleccionado?.nombre ?: "") }
    var telefono by remember { mutableStateOf(proveedorSeleccionado?.telefono ?: "") }
    var direccion by remember { mutableStateOf(proveedorSeleccionado?.direccion ?: "") }

    LaunchedEffect(proveedorSeleccionado) {
        nombre = proveedorSeleccionado?.nombre ?: ""
        telefono = proveedorSeleccionado?.telefono ?: ""
        direccion = proveedorSeleccionado?.direccion ?: ""
    }

    val isEditMode = proveedorSeleccionado != null

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Button(onClick = { navController.navigate("lstProveedores") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)) {
            Text("Lista Proveedores")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Formulario de Proveedores", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (isEditMode) {
            Text("ID del proveedor:")
            TextField(value = proveedorSeleccionado?.id.toString() ?: "", onValueChange = {}, readOnly = true, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text("Nombre:")
        TextField(value = nombre, onValueChange = { nombre = it }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Text("Teléfono:")
        TextField(value = telefono, onValueChange = { telefono = it }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
        Spacer(modifier = Modifier.height(16.dp))

        Text("Dirección:")
        TextField(value = direccion, onValueChange = { direccion = it }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            scope.launch {
                if (isEditMode) {
                    val id = proveedorSeleccionado!!.id
                    viewModel.modificarProveedor(id, nombre, telefono, direccion) { success ->
                        android.widget.Toast.makeText(context, if (success) "Proveedor modificado" else "Error al modificar", android.widget.Toast.LENGTH_SHORT).show()
                        if (success) {
                            viewModel.limpiarSeleccion()
                            navController.navigate("lstProveedores") { popUpTo("lstProveedores") { inclusive = true } }
                        }
                    }
                } else {
                    viewModel.agregarProveedor(nombre, telefono, direccion) { success ->
                        android.widget.Toast.makeText(context, if (success) "Proveedor agregado" else "Error al agregar", android.widget.Toast.LENGTH_SHORT).show()
                        if (success) {
                            viewModel.limpiarSeleccion()
                            navController.navigate("lstProveedores") { popUpTo("lstProveedores") { inclusive = true } }
                        }
                    }
                }
            }
        }, modifier = Modifier.align(Alignment.End)) {
            Text(if (isEditMode) "Guardar Cambios" else "Agregar Proveedor")
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
                productos.add(
                    Producto(
                        8,
                        10,
                        750.00,
                        "Renta de traje negro a modo de prueba",
                        "2025-10-06 10:00",
                        "2025-10-07 18:00"
                    ),
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
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
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
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
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


/////////////////////////////////////////////////////////////////////////////////////////
////////////////   Hector RENTAS  //////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
@Composable
fun LstRentasContent(navController: NavHostController, modifier: Modifier) {

    val rentas = remember { mutableStateListOf<ModeloRenta>() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Cargar rentas desde la API
    LaunchedEffect(Unit) {
        try {
            val respuesta = api.mostrarRentas()
            if (respuesta.isSuccessful) {
                val lista = respuesta.body() ?: emptyList()
                rentas.clear()
                rentas.addAll(lista)
            } else {
                Log.e("API", "Error del servidor: ${respuesta.code()}")
            }
        } catch (e: Exception) {
            Log.e("API", "Error al cargar rentas: ${e.message}")
        }
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .horizontalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        // Botón menú
        Button(
            onClick = { navController.navigate("menu") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Menu",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para ir al formulario
        Button(
            onClick = { navController.navigate("frmRentas") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Formulario",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Rentas",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Encabezado de tabla
        Row {
            Text("ID Renta", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Cliente", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("Editar", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
        }

        Divider()

        // Filas de rentas
        rentas.forEachIndexed { index, renta ->
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row(
                modifier = Modifier
                    .background(bgColor)
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${renta.id_renta}", modifier = Modifier.width(100.dp))
                Text(renta.nombre, modifier = Modifier.width(150.dp))

                // Botón Editar: envía el cliente al formulario
                Button(
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("idRenta", renta.id_renta)
                        navController.currentBackStackEntry?.savedStateHandle?.set("nombreCliente", renta.nombre)
                        navController.navigate("frmRentas")
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text("Editar")
                }


                // Botón Eliminar
                Button(onClick = {
                    scope.launch {
                        try {
                            val respuesta = api.eliminar(renta.id_renta)
                            if (respuesta.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Renta eliminada con éxito",
                                    Toast.LENGTH_SHORT
                                ).show()
                                rentas.remove(renta)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error al eliminar",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Error de conexión: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }, modifier = Modifier.width(100.dp)) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrmRentasContent(navController: NavController, modifier: Modifier) {

    val idRenta = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Int>("idRenta")

    val nombreClienteEdit = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("nombreCliente")

    val context = LocalContext.current

    val clientesList = remember { mutableStateListOf<ModeloClienteRenta>() }
    var selectedCliente by remember { mutableStateOf<ModeloClienteRenta?>(null) }
    var expandedCliente by remember { mutableStateOf(false) }

    // Cargar clientes
    LaunchedEffect(Unit) {
        val respuestaClientes = api.mostrarClientes()
        if (respuestaClientes.isSuccessful) {
            clientesList.clear()
            clientesList.addAll(respuestaClientes.body() ?: emptyList())

            if (nombreClienteEdit != null) {
                selectedCliente = clientesList.find { it.nombre == nombreClienteEdit }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Button(
            onClick = { navController.navigate("lstRentas") },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tabla", style = TextStyle(textDecoration = TextDecoration.Underline))
        }

        Spacer(Modifier.height(16.dp))

        Text(
            if (idRenta == null) "Crear Renta" else "Editar Renta",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )


        Spacer(Modifier.height(16.dp))

        Text("Nombre del cliente:")
        ExposedDropdownMenuBox(
            expanded = expandedCliente,
            onExpandedChange = { expandedCliente = !expandedCliente }
        ) {
            TextField(
                value = selectedCliente?.nombre ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Selecciona el cliente") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedCliente) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedCliente,
                onDismissRequest = { expandedCliente = false }
            ) {
                clientesList.forEach { cliente ->
                    DropdownMenuItem(
                        text = { Text(cliente.nombre) },
                        onClick = {
                            selectedCliente = cliente
                            expandedCliente = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        val scope = rememberCoroutineScope()

        Button(onClick = {
            val idCliente = selectedCliente?.id_cliente
            if (idCliente == null) {
                Toast.makeText(context, "Selecciona un cliente", Toast.LENGTH_SHORT).show()
                return@Button
            }

            scope.launch {
                val resp = if (idRenta == null) {
                    api.insertarRenta(idCliente = idCliente)
                } else {
                    api.editarRenta(idRenta = idRenta, idCliente = idCliente)
                }

                if (resp.isSuccessful) {
                    Toast.makeText(context, resp.body()?.mensaje ?: "OK", Toast.LENGTH_SHORT).show()
                    navController.navigate("lstRentas")
                } else {
                    Toast.makeText(context, "Error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text(if (idRenta == null) "Insertar" else "Guardar Cambios")
        }

    }
}
//////////////////////////////////////////////////////////////////

////////////////   Big-tour CLIENTES  //////////////////////////////////////////////////////
@Composable
fun LstClientesContent(navController: NavHostController, viewModel: ClientesViewModel) {

    val clientes by viewModel.clientes.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) { viewModel.cargarClientes() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Button(onClick = { navController.navigate("menu") }, modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)) {
            Text("Menu")
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            viewModel.limpiarSeleccion()
            navController.navigate("frmClientes")
        }, modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)) {
            Text("Agregar Cliente")
        }

        Spacer(Modifier.height(16.dp))
        Text("Clientes", fontSize = MaterialTheme.typography.titleLarge.fontSize, fontWeight = FontWeight.ExtraBold, color = Color.Red)
        Spacer(Modifier.height(16.dp))

        Row {
            Text("ID", Modifier.width(50.dp), fontWeight = FontWeight.Bold)
            Text("Nombre", Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("Teléfono", Modifier.width(120.dp), fontWeight = FontWeight.Bold)
            Text("Correo", Modifier.width(200.dp), fontWeight = FontWeight.Bold)
            Text("Eliminar", Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Modificar", Modifier.width(100.dp), fontWeight = FontWeight.Bold)
        }
        Divider()

        clientes.forEach { cliente ->
            val index = clientes.indexOf(cliente)
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row(modifier = Modifier
                .background(bgColor)
                .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${cliente.id}", Modifier.width(50.dp))
                Text(cliente.nombre, Modifier.width(150.dp))
                Text(cliente.telefono, Modifier.width(120.dp))
                Text(cliente.correo, Modifier.width(200.dp))

                Button(onClick = {
                    scope.launch {
                        viewModel.eliminarCliente(cliente.id) { success ->
                            Toast.makeText(context, if (success) "Cliente eliminado" else "Error al eliminar", Toast.LENGTH_SHORT).show()
                        }
                    }
                }, modifier = Modifier.width(100.dp)) { Text("Eliminar") }

                Spacer(modifier = Modifier.width(4.dp))

                Button(onClick = {
                    viewModel.seleccionarCliente(cliente)
                    navController.navigate("frmClientes")
                }, modifier = Modifier.width(100.dp)) { Text("Modificar") }
            }
        }
    }
}



@Composable
fun FrmClientesContent(navController: NavHostController, viewModel: ClientesViewModel) {

    val clienteSeleccionado by viewModel.selectedCliente.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf(clienteSeleccionado?.nombre ?: "") }
    var telefono by remember { mutableStateOf(clienteSeleccionado?.telefono ?: "") }
    var correo by remember { mutableStateOf(clienteSeleccionado?.correo ?: "") }

    LaunchedEffect(clienteSeleccionado) {
        nombre = clienteSeleccionado?.nombre ?: ""
        telefono = clienteSeleccionado?.telefono ?: ""
        correo = clienteSeleccionado?.correo ?: ""
    }

    val isEditMode = clienteSeleccionado != null

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Button(onClick = { navController.navigate("lstClientes") }, modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)) {
            Text("Lista Clientes")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(if (isEditMode) "Editar Cliente" else "Agregar Cliente", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (isEditMode) {
            Text("ID del cliente:")
            TextField(value = clienteSeleccionado?.id.toString() ?: "", onValueChange = {}, readOnly = true, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text("Nombre:")
        TextField(value = nombre, onValueChange = { nombre = it }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Text("Teléfono:")
        TextField(value = telefono, onValueChange = { telefono = it }, modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
        Spacer(modifier = Modifier.height(16.dp))

        Text("Correo:")
        TextField(value = correo, onValueChange = { correo = it }, modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            scope.launch {
                if (isEditMode) {
                    val id = clienteSeleccionado!!.id
                    viewModel.modificarCliente(id, nombre, telefono, correo) { success ->
                        Toast.makeText(context, if (success) "Cliente modificado" else "Error al modificar", Toast.LENGTH_SHORT).show()
                        if (success) {
                            viewModel.limpiarSeleccion()
                            navController.navigate("lstClientes") { popUpTo("lstClientes") { inclusive = true } }
                        }
                    }
                } else {
                    viewModel.agregarCliente(nombre, telefono, correo) { success ->
                        Toast.makeText(context, if (success) "Cliente agregado" else "Error al agregar", Toast.LENGTH_SHORT).show()
                        if (success) {
                            viewModel.limpiarSeleccion()
                            navController.navigate("lstClientes") { popUpTo("lstClientes") { inclusive = true } }
                        }
                    }
                }
            }
        }, modifier = Modifier.align(Alignment.End)) {
            Text(if (isEditMode) "Guardar Cambios" else "Agregar Cliente")
        }
    }
}


//////////////////////////////////////////////////////////////////

////////////////   Sofi TRAJES  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun LstTrajesContent(navController: NavHostController, viewModel: TrajesViewModel) {
    val trajes by viewModel.trajes.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { viewModel.cargarTrajes() }

    val scrollVertical = rememberScrollState()
    val scrollHorizontal = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollVertical)
            .padding(24.dp)
    ) {

        Button(
            onClick = { navController.navigate("menu") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)
        ) { Text("Menu") }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.limpiarSeleccion()
                navController.navigate("frmTrajes")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)
        ) { Text("Agregar Traje") }
        Spacer(Modifier.height(16.dp))

        Text(
            "Trajes",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Red
        )
        Spacer(Modifier.height(16.dp))

        // ✨ Scroll horizontal para la tabla
        Column(modifier = Modifier.horizontalScroll(scrollHorizontal)) {

            Row {
                Text("ID", Modifier.width(50.dp), fontWeight = FontWeight.Bold)
                Text("Nombre", Modifier.width(150.dp), fontWeight = FontWeight.Bold)
                Text("Descripción", Modifier.width(200.dp), fontWeight = FontWeight.Bold)
                Text("Precio", Modifier.width(100.dp), fontWeight = FontWeight.Bold)
                Text("Eliminar", Modifier.width(100.dp), fontWeight = FontWeight.Bold)
                Text("Modificar", Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            }
            Divider()

            trajes.forEach { traje ->
                val index = trajes.indexOf(traje)
                val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

                Row(
                    modifier = Modifier
                        .background(bgColor)
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${traje.id_traje}", Modifier.width(50.dp))
                    Text("${traje.nombre_traje}", Modifier.width(150.dp))
                    Text("${traje.descripcion}", Modifier.width(200.dp))
                    Text(String.format("$%.2f", traje.precio), Modifier.width(100.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.eliminarTraje(traje.id_traje) { success ->
                                    Toast.makeText(context, if (success) "Traje eliminado" else "Error al eliminar", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.width(100.dp)
                    ) { Text("Eliminar") }

                    Spacer(Modifier.width(4.dp))

                    Button(
                        onClick = {
                            viewModel.seleccionarTraje(traje)
                            navController.navigate("frmTrajes")
                        },
                        modifier = Modifier.width(100.dp)
                    ) { Text("Modificar") }
                }
            }
        }
    }
}



@Composable
fun FrmTrajesContent(navController: NavHostController, viewModel: TrajesViewModel) {
    val trajeSeleccionado by viewModel.selectedTraje.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf(trajeSeleccionado?.nombre_traje ?: "") }
    var descripcion by remember { mutableStateOf(trajeSeleccionado?.descripcion ?: "") }
    var precio by remember { mutableStateOf(trajeSeleccionado?.precio?.toString() ?: "") }

    LaunchedEffect(trajeSeleccionado) {
        nombre = trajeSeleccionado?.nombre_traje ?: ""
        descripcion = trajeSeleccionado?.descripcion ?: ""
        precio = trajeSeleccionado?.precio?.toString() ?: ""
    }

    val isEditMode = trajeSeleccionado != null

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Button(onClick = { navController.navigate("lstTrajes") }, modifier = Modifier.fillMaxWidth()) { Text("Lista Trajes") }
        Spacer(Modifier.height(16.dp))
        Text("Formulario de Trajes", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(16.dp))

        if (isEditMode) {
            Text("ID del traje:")
            TextField(value = trajeSeleccionado?.id_traje.toString(), onValueChange = {}, readOnly = true, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
        }

        Text("Nombre del traje:")
        TextField(value = nombre, onValueChange = { nombre = it }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))

        Text("Descripción:")
        TextField(value = descripcion, onValueChange = { descripcion = it }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))

        Text("Precio:")
        TextField(value = precio, onValueChange = { precio = it }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            scope.launch {
                val precioFloat = precio.toFloatOrNull() ?: 0f
                if (isEditMode) {
                    viewModel.modificarTraje(trajeSeleccionado!!.id_traje, nombre, descripcion, precioFloat) { success ->
                        Toast.makeText(context, if (success) "Traje modificado" else "Error al modificar", Toast.LENGTH_SHORT).show()
                        if (success) {
                            viewModel.limpiarSeleccion()
                            navController.navigate("lstTrajes") { popUpTo("lstTrajes") { inclusive = true } }
                        }
                    }
                } else {
                    viewModel.agregarTraje(nombre, descripcion, precioFloat) { success ->
                        Toast.makeText(context, if (success) "Traje agregado" else "Error al agregar", Toast.LENGTH_SHORT).show()
                        if (success) {
                            viewModel.limpiarSeleccion()
                            navController.navigate("lstTrajes") { popUpTo("lstTrajes") { inclusive = true } }
                        }
                    }
                }
            }
        }, modifier = Modifier.align(Alignment.End)) {
            Text(if (isEditMode) "Guardar Cambios" else "Agregar Traje")
        }
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun PantallaComentarios(api: ApiService, navController: NavHostController) {

    val listaComentarios = remember { mutableStateListOf<ModeloComentario>() }
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Campo del formulario
    var nuevoComentario by remember { mutableStateOf("") }

    // ---- FUNCIÓN PARA RECARGAR COMENTARIOS ----
    fun cargarComentarios() {
        scope.launch {
            try {
                val respuesta = api.mostrarComentarios()
                if (respuesta.isSuccessful) {
                    val datos = respuesta.body() ?: emptyList()
                    listaComentarios.clear()
                    listaComentarios.addAll(datos)
                } else {
                    error = "Error del servidor"
                }
            } catch (e: Exception) {
                error = "Error: ${e.message}"
            }
            cargando = false
        }
    }

    // Cargar al abrir
    LaunchedEffect(Unit) { cargarComentarios() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Button(
            onClick = { navController.navigate("menu") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)
        ) { Text("Menu") }
        Spacer(Modifier.height(16.dp))

        Text(
            "Comentarios",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // ------------------ FORMULARIO PARA AGREGAR ------------------
        OutlinedTextField(
            value = nuevoComentario,
            onValueChange = { nuevoComentario = it },
            label = { Text("Nuevo comentario") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (nuevoComentario.isNotBlank()) {
                    scope.launch {
                        try {
                            val resp = api.insertarComentario(nuevoComentario)
                            if (resp.isSuccessful && resp.body()?.status == "ok") {

                                Toast.makeText(
                                    contexto,
                                    "Comentario agregado",
                                    Toast.LENGTH_SHORT
                                ).show()

                                nuevoComentario = ""
                                cargarComentarios()

                            } else {
                                Toast.makeText(
                                    contexto,
                                    "Error al agregar",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                contexto,
                                "Error: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Text("Agregar comentario")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ------------------ TABLA ------------------
        when {
            cargando -> Text("Cargando comentarios...")

            error != null -> Text("Ocurrió un error: $error", color = Color.Red)

            else -> {
                // Encabezados
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("ID", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                    Text("Comentario", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text("Eliminar", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
                }

                Divider()

                // Filas
                listaComentarios.forEachIndexed { index, comentario ->

                    val bgColor =
                        if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(bgColor)
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text("${comentario.id_comentario}", modifier = Modifier.width(80.dp))

                        Text(
                            comentario.comentario,
                            modifier = Modifier.weight(1f)
                        )

                        // ------------ BOTÓN ELIMINAR ------------
                        Button(
                            onClick = {
                                scope.launch {
                                    try {
                                        val resp = api.eliminarComentario(comentario.id_comentario)
                                        if (resp.isSuccessful && resp.body()?.status == "ok") {

                                            Toast.makeText(
                                                contexto,
                                                "Un hater menos",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            listaComentarios.remove(comentario)

                                        } else {
                                            Toast.makeText(
                                                contexto,
                                                "No se pudo eliminar",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            contexto,
                                            "Error: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            modifier = Modifier.width(100.dp)
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun RentaTrajesTheme(content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}

