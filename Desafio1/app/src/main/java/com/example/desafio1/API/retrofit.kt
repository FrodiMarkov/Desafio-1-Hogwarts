package Api

import com.example.clienteapi_serverktor_25_26.Api.Parametros
import com.example.desafio1.API.asignaturasAPI
import com.example.desafio1.API.casaAPI
import com.example.desafio1.API.hechizosAPI
import com.example.desafio1.API.usuariosAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object retrofit {
    val usuariosRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Parametros.url + ":" + Parametros.puerto)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(usuariosAPI::class.java)
    }
    val asignaturasRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Parametros.url + ":" + Parametros.puerto)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(asignaturasAPI::class.java)
    }
    val hechizosRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Parametros.url + ":" + Parametros.puerto)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(hechizosAPI::class.java)
    }
    val casaRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Parametros.url + ":" + Parametros.puerto)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(casaAPI::class.java)
    }
}