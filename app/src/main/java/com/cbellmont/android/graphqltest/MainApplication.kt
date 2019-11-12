package com.cbellmont.android.graphqltest

import android.app.Application
import com.apollographql.apollo.ApolloClient
import com.facebook.stetho.Stetho
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class MainApplication : Application() {

    private val url = "https://countries.trevorblades.com/"

    companion object {
        lateinit var httpClient: OkHttpClient
        lateinit var myApolloClient: ApolloClient
    }

    override fun onCreate() {
        super.onCreate()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

        myApolloClient = ApolloClient.builder()
            .serverUrl(url)
            .okHttpClient(httpClient)
            .build()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}