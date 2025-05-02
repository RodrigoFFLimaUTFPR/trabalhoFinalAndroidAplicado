package com.rodrigofflima.trabalhofinalandroidaplicado

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rodrigofflima.trabalhofinalandroidaplicado.adapter.TransactionAdapter
import com.rodrigofflima.trabalhofinalandroidaplicado.database.TransactionRepository
import com.rodrigofflima.trabalhofinalandroidaplicado.entity.Transaction

class TransactionListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private lateinit var transactionRepository: TransactionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_list)

        val btnVoltar = findViewById<Button>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish()
        }

        transactionRepository = TransactionRepository(this)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = TransactionAdapter { transaction ->
            deleteTransaction(transaction)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val transactions = transactionRepository.getAllTransactions()
        adapter.submitList(transactions)
    }

    private fun deleteTransaction(transaction: Transaction) {
        transactionRepository.deleteTransaction(transaction.id)
        val updatedList = transactionRepository.getAllTransactions()
        adapter.submitList(updatedList)
    }
}
