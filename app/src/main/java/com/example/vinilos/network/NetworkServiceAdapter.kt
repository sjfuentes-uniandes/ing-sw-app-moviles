package com.example.vinilos.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import android.util.Log
import com.example.vinilos.models.Album
import com.example.vinilos.models.Artist
import com.example.vinilos.models.Collector
import org.json.JSONArray
import org.json.JSONObject
import java.nio.file.Path

class NetworkServiceAdapter constructor(context: Context){
    companion object{
        const val  BASE_URL = "https://vinilos-backend-5f9h.onrender.com/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this){
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun getAlbums(onComplete:(resp:List<Album>)->Unit, onError: (error:VolleyError)->Unit){
        requestQueue.add(getRequest("albums",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Album>()
                for (i in 0 until resp.length()){
                    val item = resp.getJSONObject(i)
                    list.add(i, Album(albumId = item.getInt("id"),
                        name = item.getString("name"),
                        cover = item.getString("cover"),
                        recordLabel = item.getString("recordLabel"),
                        releaseDate = item.getString("releaseDate"),
                        genre = item.getString("genre"),
                        description = item.getString("description")))
                }
                onComplete(list)
            },
            {
                onError(it)
            }))
    }

    fun getCollectors(onComplete:(resp:List<Collector>)->Unit, onError: (error:VolleyError)->Unit){
        requestQueue.add(getRequest("collectors",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Collector>()
                for (i in 0 until resp.length()){
                    val item = resp.getJSONObject(i)
                    list.add(i, Collector(collectorId = item.getInt("id"),
                        name = item.getString("name"),
                        telephone = item.getString("telephone"),
                        email = item.getString("email")))
                }
                onComplete(list)
            },
            {
                onError(it)
            }))
    }

    fun getArtists(
        onComplete: (resp: List<Artist>) -> Unit,
        onError: (error: VolleyError) -> Unit
    ) {
        requestQueue.add(
            getRequest(
            "bands", { response ->
                    val resp = JSONArray(response)
                    val list = mutableListOf<Artist>()
                    val artists = (0 until resp.length()).map { a ->
                        val item = resp.getJSONObject(a)
                        Artist(
                            artistId = item.getInt("id"),
                            image = item.getString("image"),
                            name = item.getString("name"),
                            description = item.getString("description"),
                            creationDate = item.getString("creationDate")
                        )
                    }

                    list.addAll(artists)

                    onComplete(list)
            },
            {
                onError(it)
            }
        ))
    }


    fun createAlbum(
        name: String,
        cover: String,
        releaseDate: String,
        description: String,
        genre: String,
        recordLabel: String,
        tracks: List<Map<String, String>>,
        onComplete: (album: Album) -> Unit,
        onError: (error: VolleyError) -> Unit
    ) {

        val jsonBody = JSONObject().apply {
            put("name", name)
            put("cover", cover)
            put("releaseDate", releaseDate)
            put("description", description)
            put("genre", genre)
            put("recordLabel", recordLabel)
        }

        val requestBody = jsonBody.toString()
        
        val request = object : StringRequest(
            Request.Method.POST,
            BASE_URL + "albums",
            { response ->
                try {
                    Log.d("NetworkServiceAdapter", "Raw response received: $response")
                    val jsonResponse = JSONObject(response)
                    Log.d("NetworkServiceAdapter", "Parsed JSON response: ${jsonResponse.toString()}")
                    val album = Album(
                        albumId = jsonResponse.getInt("id"),
                        name = jsonResponse.getString("name"),
                        cover = jsonResponse.getString("cover"),
                        recordLabel = jsonResponse.getString("recordLabel"),
                        releaseDate = jsonResponse.getString("releaseDate"),
                        genre = jsonResponse.getString("genre"),
                        description = jsonResponse.getString("description")
                    )
                    onComplete(album)
                } catch (e: Exception) {
                    Log.e("NetworkServiceAdapter", "Error parsing response: ${e.message}", e)
                    Log.e("NetworkServiceAdapter", "Response was: $response")
                    onError(VolleyError("Error parsing response: ${e.message}", e))
                }
            },
            { error ->
                val errorMessage = when {
                    error.networkResponse != null -> {
                        val statusCode = error.networkResponse.statusCode
                        val errorBody = try {
                            String(error.networkResponse.data, Charsets.UTF_8)
                        } catch (e: Exception) {
                            "Could not parse error body"
                        }
                        Log.e("NetworkServiceAdapter", "Network error: Status $statusCode, Body: $errorBody")
                        "HTTP $statusCode: $errorBody"
                    }
                    error.message != null -> {
                        Log.e("NetworkServiceAdapter", "Volley error: ${error.message}")
                        error.message ?: "Unknown error"
                    }
                    else -> {
                        Log.e("NetworkServiceAdapter", "Unknown error: ${error.javaClass.simpleName}")
                        "Could not retrieve response code from HttpUrlConnection"
                    }
                }
                onError(VolleyError(errorMessage, error))
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json; charset=utf-8"
                headers["Accept"] = "application/json"
                return headers
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray(Charsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }

        // Increase timeout significantly for Render.com which can be slow
        request.retryPolicy = com.android.volley.DefaultRetryPolicy(
            30000, // 30 seconds timeout (Render.com can be slow)
            2, // 2 retries
            com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        Log.d("NetworkServiceAdapter", "Sending POST request to: ${BASE_URL}albums")
        Log.d("NetworkServiceAdapter", "Request body: ${jsonBody.toString()}")

        requestQueue.add(request)
    }

    private fun getRequest(path:String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL+path, responseListener,errorListener)
    }
}