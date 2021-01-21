package com.example.firebasestorage

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private val TAG = "HomeActivity"
    private lateinit var db: FirebaseFirestore
    private lateinit var collectionReference: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        configureDatabaseAndCollectionReference()

        btnSave.setOnClickListener {
            val product = getDataEditText()
            sendProductToDatabase(product)
        }

        btnDelete.setOnClickListener {
            deleteProductFromDataBase()
        }

        btnUpdate.setOnClickListener {
            val product = getDataEditText()
            updateProductOfDatabase(product)
        }

        readProductsFromDatabase()
    }

    fun configureDatabaseAndCollectionReference() {
        db = FirebaseFirestore.getInstance()
        collectionReference = db.collection("products")
    }

    fun getDataEditText(): MutableMap<String, Any> {
        val product: MutableMap<String, Any> = HashMap()

        product["name"] = edNameProd.text.toString()
        product["amount"] = edAmountProd.text.toString()
        product["price"] = edPriceProd.text.toString()

        return product
    }

    fun sendProductToDatabase(product: MutableMap<String, Any>) {
        val name = edNameProd.text.toString()

        collectionReference.document(name).set(product).addOnSuccessListener {
            Toast.makeText(this, "Produto enviado com sucesso!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Erro ao enviar o produto!", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteProductFromDataBase() {
        collectionReference.document("Caneta Azul").delete().addOnSuccessListener {
            Toast.makeText(this, "Produto deletado com sucesso!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Erro ao deletar produto!", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateProductOfDatabase(product: MutableMap<String, Any>) {
        collectionReference
            .document("Caneta Azul")
            .update(product)
    }

    fun readProductsFromDatabase() {
        collectionReference
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(TAG, document.id + " => " + document.data)
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            })
    }
}