package com.example.singletonpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.view.View
import android.widget.*

import retrofit2.Callback
import retrofit2.Response
import android.widget.ArrayAdapter as ArrayAdapter

class MainActivity : AppCompatActivity() {
    private var currencyDetailsm: Datum? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(Constants.TAG_MAIN_ACTIVITY, "onCreate: ")

        val inputText = findViewById<View>(R.id.userEnterId) as EditText
        val convertButton = findViewById<View>(R.id.buttonid) as Button
        val spinner = findViewById<View>(R.id.spinner) as Spinner

        val cur = Constants.CURRENCY_ARRAY

        var select:Int = 0
        if(spinner != null){
            val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item , cur)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id:Long) {
                    select = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }
        convertButton.setOnClickListener {
            var newInt = inputText.text.toString()
            var currency: Double = newInt.toDouble()

            getCurrency(onResult = {
                currencyDetailsm = it

                when (select){
                    0 -> display(calculate(currencyDetailsm?.eur?.inr?.toDouble(), currency));
                    1 -> display(calculate(currencyDetailsm?.eur?.usd?.toDouble(), currency));
                    2 -> display(calculate(currencyDetailsm?.eur?.aud?.toDouble(), currency));
                    3 -> display(calculate(currencyDetailsm?.eur?.sar?.toDouble(), currency));
                    4 -> display(calculate(currencyDetailsm?.eur?.cny?.toDouble(), currency));
                    5 -> display(calculate(currencyDetailsm?.eur?.jpy?.toDouble(), currency));
                    6 -> display(calculate(currencyDetailsm?.eur?.egp?.toDouble(), currency))
                }
            })
        }
    }
    fun display(calculate: Double){
        val resultText = findViewById<View>(R.id.resultId) as TextView
        resultText.text = "The Result is: "+ calculate
    }

    fun calculate(i : Double? , sel: Double):Double{
        var int = 0.0
        if(i != null){
            int = (i * sel)
        }
        return int
    }
    fun getCurrency(onResult: (Datum?) -> Unit){
        val apiInterface = APIClient.getClient()?.create(APIInterface::class.java)

        if(apiInterface != null){
            apiInterface.getCurrency()?.enqueue(object : Callback<Datum> {
                override fun onResponse(call: retrofit2.Call<Datum>, response: Response<Datum>) {
                    onResult(response.body())
                }

                override fun onFailure(call: retrofit2.Call<Datum>, t: Throwable) {
                    onResult(null)
                    call.cancel()
                }
            })
        }
    }
}

private fun <T> retrofit2.Call<T>?.enqueue(callback: Callback<Datum>) {

}

