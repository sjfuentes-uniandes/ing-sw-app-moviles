package com.example.vinilos.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vinilos.models.Album
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

    private fun getRequest(path:String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL+path, responseListener,errorListener)
    }
}