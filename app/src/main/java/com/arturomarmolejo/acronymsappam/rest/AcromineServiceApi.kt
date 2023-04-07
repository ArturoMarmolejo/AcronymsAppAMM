package com.arturomarmolejo.acronymsappam.rest

import com.arturomarmolejo.acronymsappam.model.AcromineItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AcromineServiceApi {
    @GET("software/acromine/dictionary.py")
    suspend fun getAcronymList(
        @Query("sf") sf: String?,
        @Query("lf") lf: String?,
    ): Response<MutableList<AcromineItem>>

    companion object {
        //http://www.nactem.ac.uk/software/acromine/dictionary.py?sf=hmm
        //http://www.nactem.ac.uk/software/acromine/dictionary.py?lf=heavy%20meromyosin
        const val BASE_URL = "http://www.nactem.ac.uk/"
    }
}

