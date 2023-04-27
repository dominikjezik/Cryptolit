package sk.dominikjezik.cryptolit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import sk.dominikjezik.cryptolit.api.CoinGeckoService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit() = Retrofit.Builder()
        .baseUrl("https://api.coingecko.com/api/v3/")
        .build()

    @Provides
    @Singleton
    fun provideCoinGeckoService(retrofit: Retrofit) = retrofit.create(CoinGeckoService::class.java);

}