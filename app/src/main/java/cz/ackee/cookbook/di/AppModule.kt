package cz.ackee.cookbook.di

import android.app.Application
import androidx.room.Room
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cz.ackee.cookbook.AppConfig
import cz.ackee.cookbook.BuildConfig
import cz.ackee.cookbook.api.ApiService
import cz.ackee.cookbook.db.CookbookDb
import cz.ackee.cookbook.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideGson() = GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create()

    @Singleton
    @Provides
    fun provideOkHttpClient(gson: Gson): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                    .addNetworkInterceptor(StethoInterceptor())
                    .build()
        } else {
            OkHttpClient.Builder().build()
        }
    }

    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient, gson: Gson): ApiService =
            Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(AppConfig.REST_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(LiveDataCallAdapterFactory())
                    .build()
                    .create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideDb(app: Application) =
            Room.databaseBuilder(app, CookbookDb::class.java, "cookbook.db")
                    .fallbackToDestructiveMigration()
                    .build()

    @Singleton
    @Provides
    fun provideRecipeDao(db: CookbookDb) = db.recipeDao()
}