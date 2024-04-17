package azizy.mahsa.datastoresample

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

import azizy.mahsa.datastoresample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    //binding
    private lateinit var binding: ActivityMainBinding

    //DataStore
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userInfo")

    private val userName = stringPreferencesKey("USERNAME")
    private val userAge = intPreferencesKey("AGE")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //InitViews
        binding.apply {
            saveBtn.setOnClickListener {
                CoroutineScope(IO).launch {
                    storeUserInfo(nameEdt.text.toString(), 21)
                }
            }
            //Get data
            lifecycle.coroutineScope.launchWhenCreated {
                getName().collect{
                    showDataTxt.text=it
                }
            }
        }

    }

    private suspend fun storeUserInfo(name: String, age: Int) {
        dataStore.edit {
            it[userName] = name
            it[userAge] = age
        }
    }

    private fun getName() = dataStore.data.map {
        it[userName] ?: ""
    }
}