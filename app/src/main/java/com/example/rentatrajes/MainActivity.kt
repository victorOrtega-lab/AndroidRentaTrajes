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
import androidx.compose.foundation.layout.Box
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

import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import com.example.rentatrajes.RetrofitClient
import com.example.rentatrajes.ApiService

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.example.rentatrajes.RetrofitClient.api
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

class DetalleRentaViewModel : ViewModel() {

    private val _detalles = MutableStateFlow<List<ModeloDetalleRenta>>(emptyList())
    val detalles: StateFlow<List<ModeloDetalleRenta>> = _detalles

    fun cargarDetalles(idRenta: Int? = null) {
        viewModelScope.launch {
            try {
                val resp = RetrofitClient.api.getDetalleRenta(idRenta)
                if (resp.isSuccessful) {
                    _detalles.value = resp.body() ?: emptyList()
                } else {
                    _detalles.value = emptyList()
                }
            } catch (e: Exception) {
                _detalles.value = emptyList()
            }
        }
    }

    fun insertarDetalle(
        idRenta: Int,
        idTraje: Int,
        precio: Double?,
        descripcion: String,
        fechaInicio: String?,
        fechaFin: String?,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // --- LOG para debug ---
                Log.d("DBG_INSERTAR_DETALLE", "ENVIANDO -> idRenta=$idRenta, idTraje=$idTraje, precio=$precio, descripcion='$descripcion', fechaInicio='$fechaInicio', fechaFin='$fechaFin'")

                val resp = RetrofitClient.api.insertarDetalle(
                    idRenta = idRenta,
                    idTraje = idTraje,
                    precio = precio?.toString(),
                    descripcion = descripcion,
                    fechaHoraInicio = fechaInicio, // nombres exactos del ApiService
                    fechaHoraFin = fechaFin
                )

                if (resp.isSuccessful) {
                    val body = resp.body()
                    val ok = body?.status == "ok"
                    if (ok) cargarDetalles(idRenta)
                    onComplete(ok, body?.mensaje ?: if (ok) "correcto" else "error")
                } else {
                    onComplete(false, "Error servidor")
                }
            } catch (e: Exception) {
                onComplete(false, e.message)
            }
        }
    }

    fun editarDetalle(
        idDetalle: Int,
        idRenta: Int?,
        idTraje: Int?,
        precio: Double?,
        descripcion: String,
        fechaInicio: String?,
        fechaFin: String?,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val resp = RetrofitClient.api.editarDetalle(
                    idDetalle = idDetalle,
                    idRenta = idRenta,
                    idTraje = idTraje,
                    precio = precio?.toString(),
                    descripcion = descripcion,
                    fechaInicio = fechaInicio,  // Nombres exactos del ApiService
                    fechaFin = fechaFin
                )

                if (resp.isSuccessful) {
                    val body = resp.body()
                    val ok = body?.status == "ok"
                    if (ok) cargarDetalles(idRenta)
                    onComplete(ok, body?.mensaje)
                } else {
                    onComplete(false, "Error servidor")
                }
            } catch (e: Exception) {
                onComplete(false, e.message)
            }
        }
    }

    fun eliminarDetalle(
        idDetalle: Int,
        idRenta: Int?,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val resp = RetrofitClient.api.eliminarDetalle(idDetalle = idDetalle)
                if (resp.isSuccessful) {
                    val body = resp.body()
                    val ok = body?.status == "ok"
                    if (ok) cargarDetalles(idRenta)
                    onComplete(ok, body?.mensaje)
                } else {
                    onComplete(false, "Error servidor")
                }
            } catch (e: Exception) {
                onComplete(false, e.message)
            }
        }
    }
}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class MainActivity : ComponentActivity() {

    private val proveedoresVM: ProveedoresViewModel by viewModels()
    private val trajesVM: TrajesViewModel by viewModels()
    private val clientesVM: ClientesViewModel by viewModels()

    private val detalleRentaVM: DetalleRentaViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // si usas
        setContent {
            RentaTrajesTheme {
                AppContent(
                    proveedoresVM = proveedoresVM,
                    trajesVM = trajesVM,
                    clientesVM = clientesVM,
                    detalleRentaVM = detalleRentaVM   // <-- aquí lo envías
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
    clientesVM: ClientesViewModel,     // <-- ya estaba
    detalleRentaVM: DetalleRentaViewModel   // <-- FALTABA ESTE
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

        // CLIENTES
        composable("lstClientes") { LstClientesContent(navController, clientesVM) }
        composable("frmClientes") { FrmClientesContent(navController, clientesVM) }

        // DETALLE DE RENTA
        composable("lstDetalleVenta") { LstDetalleRentaContent(navController, detalleRentaVM) }
        composable("frmDetalleVenta") { FrmDetalleRentaContent(navController, detalleRentaVM) }


        // RENTAS
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
data class ModeloDetalleRenta(
    val id_detalle: Int,
    val id_renta: Int?,
    val idtraje: Int?,
    val precio: Double?,
    val descripcion: String?,
    val fechaHoraInicio: String?,
    val fechaHoraFin: String?,
    val nombre_traje: String?,        // <-- Nuevo
    val nombre_cliente: String?
)



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
val retrofit = Retrofit.Builder()
    .baseUrl("https://gibraltar-volumes-subaru-borough.trycloudflare.com/api2/")
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
fun LstDetalleRentaContent(
    navController: NavHostController,
    viewModel: DetalleRentaViewModel,
    modifier: Modifier = Modifier,
    idRentaFiltro: Int? = null
) {
    val detalles by viewModel.detalles.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var refreshTrigger by remember { mutableStateOf(0) } // contador para forzar recarga

    LaunchedEffect(idRentaFiltro, refreshTrigger) {
        viewModel.cargarDetalles(idRentaFiltro)
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Button(onClick = { navController.navigate("menu") }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue), modifier = Modifier.fillMaxWidth()) {
            Text("Menu")
        }
        Spacer(Modifier.height(12.dp))

        Button(onClick = {
            // pasar idRentaFiltro si quieres crear bajo una renta específica
            navController.currentBackStackEntry?.savedStateHandle?.set("idRentaSeleccionada", idRentaFiltro)
            navController.navigate("frmDetalleVenta")
        }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue), modifier = Modifier.fillMaxWidth()) {
            Text("Nuevo detalle")
        }
        Spacer(Modifier.height(16.dp))

        Text("Detalle de Renta", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        // tabla con scroll horizontal para evitar que se amontone
        Box(modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
        ) {
            Column {
                Row {
                    Text("ID Detalle", Modifier.width(100.dp), fontWeight = FontWeight.Bold)
                    Text("Cliente/Renta", Modifier.width(160.dp), fontWeight = FontWeight.Bold)
                    Text("Traje", Modifier.width(160.dp), fontWeight = FontWeight.Bold)
                    Text("Precio", Modifier.width(100.dp), fontWeight = FontWeight.Bold)
                    Text("Inicio", Modifier.width(180.dp), fontWeight = FontWeight.Bold)
                    Text("Fin", Modifier.width(180.dp), fontWeight = FontWeight.Bold)
                    Text("Eliminar", Modifier.width(120.dp), fontWeight = FontWeight.Bold)
                    Text("Editar", Modifier.width(120.dp), fontWeight = FontWeight.Bold)
                }
                Divider()

                detalles.forEach { d ->
                    Row(modifier = Modifier
                        .background(Color(0xFFF5F5F5))
                        .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${d.id_detalle}", Modifier.width(100.dp))
                        Text(d.nombre_cliente ?: (d.id_renta?.toString() ?: "-"), Modifier.width(160.dp))
                        Text(d.nombre_traje ?: (d.idtraje?.toString() ?: "-"), Modifier.width(160.dp))
                        Text((d.precio?.toString() ?: "-"), Modifier.width(100.dp))
                        Text(d.fechaHoraInicio ?: "-", Modifier.width(180.dp))
                        Text(d.fechaHoraFin ?: "-", Modifier.width(180.dp))

                        Button(onClick = {
                            scope.launch {
                                viewModel.eliminarDetalle(d.id_detalle, d.id_renta) { success, msg ->
                                    Toast.makeText(context, if (success) "Eliminado" else "Error: $msg", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }, modifier = Modifier.width(120.dp)) { Text("Eliminar") }

                        Spacer(Modifier.width(8.dp))

                        Button(onClick = {
                            // Enviar SOLO valores simples al formulario (no objetos)
                            navController.currentBackStackEntry?.savedStateHandle?.set("id_detalle_edit", d.id_detalle)
                            navController.currentBackStackEntry?.savedStateHandle?.set("id_renta_edit", d.id_renta)
                            navController.currentBackStackEntry?.savedStateHandle?.set("id_traje_edit", d.idtraje)
                            navController.currentBackStackEntry?.savedStateHandle?.set("precio_edit", d.precio?.toString())
                            navController.currentBackStackEntry?.savedStateHandle?.set("descripcion_edit", d.descripcion)
                            navController.currentBackStackEntry?.savedStateHandle?.set("fecha_inicio_edit", d.fechaHoraInicio)
                            navController.currentBackStackEntry?.savedStateHandle?.set("fecha_fin_edit", d.fechaHoraFin)

                            navController.navigate("frmDetalleVenta")
                        }, modifier = Modifier.width(120.dp)) { Text("Editar") }
                    }
                    Spacer(Modifier.height(6.dp))
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrmDetalleRentaContent(
    navController: NavHostController,
    detalleRentaVM: DetalleRentaViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // valores enviados desde la lista (si vienen)
    val idDetalleEditable = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("id_detalle_edit")
    val idRentaFromList = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("id_renta_edit")
    val idTrajeFromList = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("id_traje_edit")
    val precioFromList = navController.previousBackStackEntry?.savedStateHandle?.get<String>("precio_edit")
    val descripcionFromList = navController.previousBackStackEntry?.savedStateHandle?.get<String>("descripcion_edit")
    val fechaInicioFromList = navController.previousBackStackEntry?.savedStateHandle?.get<String>("fecha_inicio_edit")
    val fechaFinFromList = navController.previousBackStackEntry?.savedStateHandle?.get<String>("fecha_fin_edit")

    // estados locales
    var selectedRenta by remember { mutableStateOf<ModeloRenta?>(null) }
    var selectedTraje by remember { mutableStateOf<ModeloTraje?>(null) }
    var precio by remember { mutableStateOf(precioFromList ?: "") }
    var descripcion by remember { mutableStateOf(descripcionFromList ?: "") }
    var fechaInicio by remember { mutableStateOf(fechaInicioFromList ?: "") }
    var fechaFin by remember { mutableStateOf(fechaFinFromList ?: "") }

    val rentasList = remember { mutableStateListOf<ModeloRenta>() }
    val trajesList = remember { mutableStateListOf<ModeloTraje>() }

    // Cargar listas de rentas y trajes
    LaunchedEffect(Unit) {
        try {
            val r = RetrofitClient.api.mostrarRentas()
            if (r.isSuccessful) {
                rentasList.clear()
                rentasList.addAll(r.body() ?: emptyList())
                // si el formulario viene con un idRenta, selecciónalo
                if (idRentaFromList != null) {
                    selectedRenta = rentasList.find { it.id_renta == idRentaFromList }
                }
            }
        } catch (e: Exception) { /* ignorar error visual */ }

        try {
            val t = RetrofitClient.api.getTrajes()
            if (t.isNotEmpty()) {
                trajesList.clear()
                trajesList.addAll(t)
                if (idTrajeFromList != null) {
                    selectedTraje = trajesList.find { it.id_traje == idTrajeFromList }
                }
            }
        } catch (e: Exception) { /* ignorar error visual */ }
    }

    val isEditMode = idDetalleEditable != null

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp)) {
        Button(onClick = { navController.navigate("lstDetalleVenta") }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)) {
            Text("Regresar")
        }
        Spacer(Modifier.height(16.dp))
        Text(if (isEditMode) "Editar Detalle" else "Nuevo Detalle", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        // Renta selector
        Text("Renta:")
        var expandRenta by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expandRenta, onExpandedChange = { expandRenta = !expandRenta }) {
            TextField(value = selectedRenta?.nombre ?: "", onValueChange = {}, readOnly = true, label = { Text("Selecciona renta") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandRenta) }, modifier = Modifier.menuAnchor().fillMaxWidth())
            ExposedDropdownMenu(expanded = expandRenta, onDismissRequest = { expandRenta = false }) {
                rentasList.forEach { r ->
                    DropdownMenuItem(text = { Text("ID ${r.id_renta} - ${r.nombre}") }, onClick = { selectedRenta = r; expandRenta = false })
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        // Traje selector
        Text("Traje:")
        var expandTraje by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expandTraje, onExpandedChange = { expandTraje = !expandTraje }) {
            TextField(value = selectedTraje?.nombre_traje ?: "", onValueChange = {}, readOnly = true, label = { Text("Selecciona traje") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandTraje) }, modifier = Modifier.menuAnchor().fillMaxWidth())
            ExposedDropdownMenu(expanded = expandTraje, onDismissRequest = { expandTraje = false }) {
                trajesList.forEach { t ->
                    DropdownMenuItem(text = { Text(t.nombre_traje) }, onClick = { selectedTraje = t; expandTraje = false })
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        Text("Precio:")
        TextField(value = precio, onValueChange = { precio = it }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))

        Text("Descripción:")
        TextField(value = descripcion, onValueChange = { descripcion = it }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))

        Text("Fecha/Hora Inicio:")
        OutlinedTextField(value = fechaInicio, onValueChange = {}, modifier = Modifier.fillMaxWidth().clickable {
            // selector de fecha/hora
            val cal = Calendar.getInstance()
            DatePickerDialog(context, { _, y, m, d ->
                TimePickerDialog(context, { _, h, min ->
                    fechaInicio = "%04d-%02d-%02d %02d:%02d".format(y, m + 1, d, h, min)
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }, enabled = false)
        Spacer(Modifier.height(12.dp))

        Text("Fecha/Hora Fin:")
        OutlinedTextField(value = fechaFin, onValueChange = {}, modifier = Modifier.fillMaxWidth().clickable {
            val cal = Calendar.getInstance()
            DatePickerDialog(context, { _, y, m, d ->
                TimePickerDialog(context, { _, h, min ->
                    fechaFin = "%04d-%02d-%02d %02d:%02d".format(y, m + 1, d, h, min)
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }, enabled = false)
        Spacer(Modifier.height(18.dp))

        Button(onClick = {
            if (selectedRenta == null || selectedTraje == null) {
                Toast.makeText(context, "Selecciona renta y traje", Toast.LENGTH_SHORT).show()
                return@Button
            }

            val precioVal = precio.toDoubleOrNull()

            if (isEditMode) {
                detalleRentaVM.editarDetalle(
                    idDetalle = idDetalleEditable!!,
                    idRenta = selectedRenta!!.id_renta,
                    idTraje = selectedTraje!!.id_traje,
                    precio = precioVal,
                    descripcion = descripcion,
                    fechaInicio = fechaInicio,
                    fechaFin = fechaFin
                ) { ok, msg ->
                    Toast.makeText(context, msg ?: "", Toast.LENGTH_SHORT).show()
                    if (ok) { navController.navigate("lstDetalleVenta") }
                }
            } else {
                detalleRentaVM.insertarDetalle(
                    idRenta = selectedRenta!!.id_renta,
                    idTraje = selectedTraje!!.id_traje,
                    precio = precioVal,
                    descripcion = descripcion,
                    fechaInicio = fechaInicio,
                    fechaFin = fechaFin
                ) { ok, msg ->
                    Toast.makeText(context, msg ?: "", Toast.LENGTH_SHORT).show()
                    if (ok) { navController.navigate("lstDetalleVenta") }
                }
            }
        }, modifier = Modifier.align(Alignment.End)) {
            Text(if (isEditMode) "Guardar cambios" else "Agregar detalle")
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

