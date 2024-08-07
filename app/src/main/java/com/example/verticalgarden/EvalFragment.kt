package com.example.verticalgarden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EvalFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_eval, container, false)

        // Referensi Firestore
        val db = FirebaseFirestore.getInstance()

        // Referensi Firestore
        val docRef = db.collection("Sensor").document("Data")

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                e.printStackTrace()
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val humidity = snapshot.getString("HUMIDITY")?.toDoubleOrNull() ?: 0.0
                val tds = snapshot.getString("TDS_ppm")?.toDoubleOrNull() ?: 0.0
                val temperature = snapshot.getString("TEMPERATURE_c")?.toDoubleOrNull() ?: 0.0
                val pH = snapshot.getString("pH")?.toDoubleOrNull() ?: 0.0

                // Retrofit RESTful API Endpoint
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://vertikalgardenapp-46mr5uinbq-et.a.run.app")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                // Inisialisasi ApiService
                val service = retrofit.create(ApiService::class.java)

                // POST API
                val requestData = SensorReadingRequest(listOf(tds, temperature, humidity, pH))

                // POST API Retrofit
                service.getPrediction(requestData).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseData = response.body()?.string()

                            // Referensi Textview
                            val textView = view.findViewById<TextView>(R.id.evalharvesttimeest)
                            activity?.runOnUiThread {
                                textView.text = responseData
                            }
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            } else {
                println("Current data: null")
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EvalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
