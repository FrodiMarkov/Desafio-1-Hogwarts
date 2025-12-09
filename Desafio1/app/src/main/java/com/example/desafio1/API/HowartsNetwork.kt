package Api

import com.example.clienteapi_serverktor_25_26.Api.Parametros
import com.example.desafio1.API.asignaturasAPI
import com.example.desafio1.API.usuariosAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HowartsNetwork {
    val usuariosRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Parametros.url + ":" + Parametros.puerto)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(usuariosAPI::class.java)
    }
    val asignaturasRetrofit: asignaturasAPI by lazy {
        Retrofit.Builder()
            .baseUrl(Parametros.url + ":" + Parametros.puerto)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(asignaturasAPI::class.java)
    }
}