package Api

import com.example.clienteapi_serverktor_25_26.Api.Parametros
import com.example.desafio1.API.usuariosAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object EncuestaNetwork {
    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Parametros.url + ":" + Parametros.puerto)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(usuariosAPI::class.java)
    }
}