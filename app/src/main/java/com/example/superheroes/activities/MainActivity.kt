package com.example.superheroes.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superheroes.R
import com.example.superheroes.adapters.SuperheroAdapter
import com.example.superheroes.data.Superhero
import com.example.superheroes.data.SuperheroService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: SuperheroAdapter

    var superheroList: List<Superhero> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.recyclerView)

        adapter = SuperheroAdapter(superheroList)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this,2)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuItem = menu?.findItem(R.id.main)
        val searchView = menuItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
            Log.i("MENU", "He pulsado Enter")
            return false
        }

        override fun onQueryTextChange(query: String): Boolean {
            horoscopeList = Horoscope.horoscopeList.filter {
                getString(it.name).contains(query, true)
            }
            adapter.updateItems(horoscopeList)
            return false
        }
    })

    return true
}



    fun getRetrofit(): SuperheroService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.superheroapi.com/api/22310ddf1c5519469f933c1a1b401ef7/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return val service = retrofit.create(SuperheroService::class.java) // creamos un objeto a tarves del interface
    }

    fun searchSuperheroesByName(query: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = service.findSuperheroesByName(query)
                val service =

                superheroList = result.results

                CoroutineScope(Dispatchers.Main).launch {
                    adapter.items = superheroList
                    adapter.notifyDataSetChanged()
                }
            } catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}