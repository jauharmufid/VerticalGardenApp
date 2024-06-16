package com.example.verticalgarden

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.android.material.switchmaterial.SwitchMaterial
import android.widget.Toast



private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MonConFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_mon_con, container, false)

        // Mendapatkan referensi ke TextView
        val nilaiWater: TextView = view.findViewById(R.id.nilaiwater)
        val nilaiNutrisi: TextView = view.findViewById(R.id.nilainutrisi)
        val nilaipH: TextView = view.findViewById(R.id.nilaiph)
        val nilaiTemperature: TextView = view.findViewById(R.id.nilaintemp)

        // Mendapatkan referensi ke SwitchMaterial
        val switchMaterial = view.findViewById<SwitchMaterial>(R.id.switch1)
        val editTextWater = view.findViewById<EditText>(R.id.inputwater)
        val applyButtonWater = view.findViewById<Button>(R.id.applywater)
//        val editTextpH = view.findViewById<EditText>(R.id.inputph)
//        val applyButtonpH = view.findViewById<Button>(R.id.applyph)
        val editTextNutrisi = view.findViewById<EditText>(R.id.inputnutrisi)
        val applyButtonNutrisi = view.findViewById<Button>(R.id.applynutrisi)

        // Membaca data dari Firestore
        val db = FirebaseFirestore.getInstance()

        //Water Controlling
        val docRefSensorWater = db.collection("Sensor").document("Data")
        val docRefControlWater = db.collection("Control").document("settings")

        docRefSensorWater.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // Ambil nilai dari Firestore
                val dataWater = snapshot.getString("Distance")

                applyButtonWater.setOnClickListener {
                    val desiredVolume = editTextWater.text.toString()

                    docRefControlWater.get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                                val mode = document.getString("mode")
                                if (mode == "manual" && desiredVolume > dataWater.toString()) {
                                    docRefControlWater.update("desiredVolume", desiredVolume)
                                        .addOnSuccessListener {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!")
                                            Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(TAG, "Error updating document", e)
                                            Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show()
                                        }
                                } else {
                                    // Tampilkan pesan kesalahan jika mode bukan 'manual' atau nilai yang dimasukkan tidak valid atau lebih kecil dari dataWater
                                    Log.w(TAG, "Invalid input or input is not greater than current distance or mode is not manual")
                                    Toast.makeText(context, "Invalid input or input is not greater than current water or mode is not manual", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Log.d(TAG, "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "get failed with ", exception)
                        }
                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

        //////////////////////////////////////////pH Controlling/////////////////////////////////////////////////////

        //Nutrition Controlling
        val docRefSensorNutrisi = db.collection("Sensor").document("Data")
        val docRefControlNutrisi = db.collection("Control").document("settings")

        docRefSensorNutrisi.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // Ambil nilai dari Firestore
                val dataNutrition = snapshot.getString("TDS")

                applyButtonNutrisi.setOnClickListener {
                    val desiredPPM = editTextNutrisi.text.toString()

                    docRefControlNutrisi.get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                                val mode = document.getString("mode")
                                if (mode == "manual" && desiredPPM > dataNutrition.toString()) {
                                    docRefControlNutrisi.update("desiredPPM", desiredPPM)
                                        .addOnSuccessListener {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!")
                                            Toast.makeText(context, "Update Success", Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(TAG, "Error updating document", e)
                                            Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show()
                                        }
                                } else {
                                    // Tampilkan pesan kesalahan jika mode bukan 'manual' atau nilai yang dimasukkan tidak valid atau lebih kecil dari dataNutrition
                                    Log.w(TAG, "Invalid input or input is not greater than current Nutritions or mode is not manual")
                                    Toast.makeText(context, "Invalid input or input is not greater than current nutritions or mode is not manual", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Log.d(TAG, "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "get failed with ", exception)
                        }
                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

        // Atur listener untuk perubahan status switch
        switchMaterial.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Jika switch diaktifkan, ubah mode menjadi 'manual'
                val docRef = db.collection("Control").document("settings")
                docRef.update("mode", "manual")
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully updated!")
                        Toast.makeText(context, "Manual Mode", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error updating document", e)
                        Toast.makeText(context, "Failed Update Mode", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Jika switch dimatikan, ubah mode menjadi 'automatic'
                val docRef = db.collection("Control").document("settings")
                docRef.update("mode", "automatic")
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully updated!")
                        Toast.makeText(context, "Automatic Mode", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error updating document", e)
                        Toast.makeText(context, "Failed Update Mode", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        val docRef = db.collection("Sensor").document("Data")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // Ambil nilai dari Firestore
                val dataWater = snapshot.getString("Distance")
                // Atur teks TextView dengan data dari Firestore
                nilaiWater.text = dataWater
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

        val docRefNutritions = db.collection("Sensor").document("Data")
        docRefNutritions.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // Ambil nilai dari Firestore
                val dataNutrisi = snapshot.get("TDS").toString()
                // Atur teks TextView dengan data dari Firestore
                nilaiNutrisi.text = dataNutrisi
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

        val docRefpH = db.collection("Sensor").document("Data")
        docRefpH.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // Ambil nilai dari Firestore
                val datapH = snapshot.getString("pH")
                // Atur teks TextView dengan data dari Firestore
                nilaipH.text = datapH
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

        val docRefTemperature = db.collection("Sensor").document("Data")
        docRefTemperature.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // Ambil nilai dari Firestore
                val dataHumidity = snapshot.getString("Humidity")
                val dataTemperature = snapshot.getString("Temperature")

                // Atur teks TextView dengan data dari Firestore
                nilaiTemperature.text = "$dataHumidity\n$dataTemperature"
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

        return view
    }

    companion object {
        private const val TAG = "MonConFragment"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MonConFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
