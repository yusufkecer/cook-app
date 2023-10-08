package com.example.food_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Food App"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInf = menuInflater
        menuInf.inflate(R.menu.food_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add) {
            println("Add button clicked")
            val action = RecipeListDirections.actionRecipeListToFoodDetail()

            Navigation.findNavController(this, R.id.recipeList).navigate(action)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setAppbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Food App"
    }


}