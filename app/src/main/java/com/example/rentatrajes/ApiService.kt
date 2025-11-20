package com.example.rentatrajes

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("servicio.php?proveedores")
    suspend fun getProveedores(): List<ModeloProveedor>

    @POST("servicio.php?agregarProveedor")
    @FormUrlEncoded
    suspend fun agregarProveedor(
        @Field("nombre") nombre: String,
        @Field("telefono") telefono: String,
        @Field("direccion") direccion: String
    ): Response<String>

    @POST("servicio.php?modificarProveedor")
    @FormUrlEncoded
    suspend fun modificarProveedor(
        @Field("id") id: String,
        @Field("nombre") nombre: String,
        @Field("telefono") telefono: String,
        @Field("direccion") direccion: String
    ): Response<String>

    @POST("servicio.php?eliminarProveedor")
    @FormUrlEncoded
    suspend fun eliminarProveedor(
        @Field("id") id: Int
    ): Response<String>

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // TRAJES
    @GET("servicio.php?trajes")
    suspend fun getTrajes(): List<ModeloTraje>

    @POST("servicio.php?agregarTraje")
    @FormUrlEncoded
    suspend fun agregarTraje(
        @Field("nombre_traje") nombre: String,
        @Field("descripcion") descripcion: String,
        @Field("precio") precio: Float
    ): Response<String>

    @POST("servicio.php?modificarTraje")
    @FormUrlEncoded
    suspend fun modificarTraje(
        @Field("id_traje") id: String,
        @Field("nombre_traje") nombre: String,
        @Field("descripcion") descripcion: String,
        @Field("precio") precio: Float
    ): Response<String>

    @POST("servicio.php?eliminarTraje")
    @FormUrlEncoded
    suspend fun eliminarTraje(
        @Field("id_traje") id: Int
    ): Response<String>


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@POST("servicio.php?IniciarSesion")
@FormUrlEncoded
suspend fun IniciarSesion(
    @Field("usuario") usuario: String,
    @Field("contrasena") contrasena: String,
): Response<LoginResponse>

    // CLIENTES
    @GET("servicio.php?clientes")
    suspend fun getClientes(): List<ModeloCliente>

    @POST("servicio.php?agregarCliente")
    @FormUrlEncoded
    suspend fun agregarCliente(
        @Field("nombre") nombre: String,
        @Field("telefono") telefono: String,
        @Field("correo") correo: String
    ): Response<String>

    @POST("servicio.php?modificarCliente")
    @FormUrlEncoded
    suspend fun modificarCliente(
        @Field("id") id: String,
        @Field("nombre") nombre: String,
        @Field("telefono") telefono: String,
        @Field("correo") correo: String
    ): Response<String>

    @POST("servicio.php?eliminarCliente")
    @FormUrlEncoded
    suspend fun eliminarCliente(
        @Field("id") id: Int
    ): Response<String>

//////////////////////////////////////////////////////////////////////////////////////
 ///   Rentas
////////////////////////////////////////////////////////////////////////////////////

    @FormUrlEncoded
    @POST("servicio.php")
    suspend fun insertarRenta(
        @Field("accion") accion: String = "insertarRenta",
        @Field("id_cliente") idCliente: Int
    ): Response<Respuesta>

    @GET("servicio.php?rentas")
    suspend fun mostrarRentas(): Response<List<ModeloRenta>>

    @FormUrlEncoded
    @POST("servicio.php?eliminar")
    suspend fun eliminar(
        @Field("id_renta") id: Int
    ): Response<RespuestaSimple>

    @FormUrlEncoded
    @POST("servicio.php")
    suspend fun editarRenta(
        @Field("accion") accion: String = "editarRenta",
        @Field("id_renta") idRenta: Int,
        @Field("id_cliente") idCliente: Int
    ): Response<Respuesta>

    @GET("servicio.php?clientesRentas")
    suspend fun mostrarClientes(): Response<List<ModeloClienteRenta>>



}

