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

        // Inisialisasi Firestore
        val db = FirebaseFirestore.getInstance()

        // Dapatkan referensi ke dokumen yang berisi nilai sensor
        val docRef = db.collection("Sensor").document("Data")

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Tangani kasus ketika terjadi kesalahan saat mendengarkan perubahan
                e.printStackTrace()
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // Ambil nilai sensor dari dokumen
                val humidity = snapshot.getString("HUMIDITY")?.toDoubleOrNull() ?: 0.0
                val tds = snapshot.getString("TDS_ppm")?.toDoubleOrNull() ?: 0.0
                val temperature = snapshot.getString("TEMPERATURE_c")?.toDoubleOrNull() ?: 0.0
                val pH = snapshot.getString("pH")?.toDoubleOrNull() ?: 0.0

                // Inisialisasi Retrofit
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://vertikalgardenapp-46mr5uinbq-as.a.run.app")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                // Inisialisasi ApiService
                val service = retrofit.create(ApiService::class.java)

                // Buat objek SensorReadingRequest untuk dikirim ke API
                val requestData = SensorReadingRequest(listOf(tds, temperature, humidity, pH))

                // Kirim permintaan POST ke API menggunakan Retrofit
                service.getPrediction(requestData).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responseData = response.body()?.string()

                            // Dapatkan referensi ke TextView di layout fragment_eval.xml
                            val textView = view.findViewById<TextView>(R.id.evalharvesttimeest)

                            // Pastikan Anda berada di thread utama saat memperbarui UI
                            activity?.runOnUiThread {
                                // Tampilkan respons JSON pada TextView
                                textView.text = responseData
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Tangani kasus ketika permintaan gagal
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
