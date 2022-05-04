package com.alzu.android.newsroom.data.network

import com.alzu.android.newsroom.data.network.model.NewsResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = Country.USA.country,
        @Query("page")
        pageNum: Int = 1,
        @Query("pageSize")
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): Response<NewsResponseDto>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNum: Int = 1,
        @Query("pageSize")
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): Response<NewsResponseDto>

    companion object{

        private const val DEFAULT_PAGE_SIZE = 20

        enum class Country(val country: String) {
            ARAB_EMIRATES("ae"),
            ARGENTINA("ar"),
            AUSTRIA("at"),
            AUSTRALIA("au"),
            BELGIUM("be"),
            BULGARIA("bg"),
            BRAZIL("br"),
            CANADA("ca"),
            SWITZERLAND("ch"),
            CHINA("cn"),
            COLOMBIA("co"),
            CUBA("cu"),
            CZECH_REPUBLIC("cz"),
            GERMANY("de"),
            EGYPT("eg"),
            FRANCE("fr"),
            UNITED_KINGDOM("gb"),
            GREECE("gr"),
            HONG_KONG("hk"),
            HUNGARY("hu"),
            INDONESIA("id"),
            IRELAND("ie"),
            ISRAEL("il"),
            INDIA("in"),
            ITALY("it"),
            JAPAN("jp"),
            KOREA("kr"),
            LITHUANIA("lt"),
            LATVIA("lv"),
            MOROCCO("ma"),
            MEXICO("mx"),
            MALAYSIA("my"),
            NIGERIA("ng"),
            NETHERLANDS("nl"),
            NORWAY("no"),
            NEW_ZEALAND("nz"),
            PHILIPPINES("ph"),
            POLAND("pl"),
            PORTUGAL("pt"),
            ROMANIA("ro"),
            SERBIA("rs"),
            RUSSIA("ru"),
            SAUDI_ARABIA("sa"),
            SWEDEN("se"),
            SINGAPORE("sg"),
            SLOVENIA("si"),
            SLOVAKIA("sk"),
            THAILAND("th"),
            TURKEY("tr"),
            TAIWAN("tw"),
            UKRAINE("ua"),
            USA("us"),
            VENEZUELA("ve"),
            SOUTH_AFRICA("za"),
            EMPTY("")
        }
    }
}

/*
enum class Country(val country: String) {
    ARAB_EMIRATES("ae"),
    ARGENTINA("ar"),
    AUSTRIA("at"),
    AUSTRALIA("au"),
    BELGIUM("be"),
    BULGARIA("bg"),
    BRAZIL("br"),
    CANADA("ca"),
    SWITZERLAND("ch"),
    CHINA("cn"),
    COLOMBIA("co"),
    CUBA("cu"),
    CZECH_REPUBLIC("cz"),
    GERMANY("de"),
    EGYPT("eg"),
    FRANCE("fr"),
    UNITED_KINGDOM("gb"),
    GREECE("gr"),
    HONG_KONG("hk"),
    HUNGARY("hu"),
    INDONESIA("id"),
    IRELAND("ie"),
    ISRAEL("il"),
    INDIA("in"),
    ITALY("it"),
    JAPAN("jp"),
    KOREA("kr"),
    LITHUANIA("lt"),
    LATVIA("lv"),
    MOROCCO("ma"),
    MEXICO("mx"),
    MALAYSIA("my"),
    NIGERIA("ng"),
    NETHERLANDS("nl"),
    NORWAY("no"),
    NEW_ZEALAND("nz"),
    PHILIPPINES("ph"),
    POLAND("pl"),
    PORTUGAL("pt"),
    ROMANIA("ro"),
    SERBIA("rs"),
    RUSSIA("ru"),
    SAUDI_ARABIA("sa"),
    SWEDEN("se"),
    SINGAPORE("sg"),
    SLOVENIA("si"),
    SLOVAKIA("sk"),
    THAILAND("th"),
    TURKEY("tr"),
    TAIWAN("tw"),
    UKRAINE("ua"),
    USA("us"),
    VENEZUELA("ve"),
    SOUTH_AFRICA("za"),
    EMPTY("")
}*/
