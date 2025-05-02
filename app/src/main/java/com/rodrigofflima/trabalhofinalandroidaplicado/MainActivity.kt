package com.rodrigofflima.trabalhofinalandroidaplicado

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.rodrigofflima.trabalhofinalandroidaplicado.database.TransactionRepository
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerType: Spinner
    private lateinit var spinnerDetail: Spinner
    private lateinit var editAmount: EditText
    private lateinit var textDate: TextView
    private lateinit var transactionRepository: TransactionRepository

    private val creditDetails = listOf("Salário", "Extras")
    private val debitDetails = listOf("Alimentação", "Transporte", "Saúde", "Moradia")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transactionRepository = TransactionRepository(this)

        spinnerType = findViewById(R.id.spinnerType)
        spinnerDetail = findViewById(R.id.spinnerDetail)
        editAmount = findViewById(R.id.editAmount)
        textDate = findViewById(R.id.textDate)

        val types = listOf("Crédito", "Débito")
        spinnerType.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)

        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val details = if (types[position] == "Crédito") creditDetails else debitDetails
                spinnerDetail.adapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    details
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val btnDate = findViewById<Button>(R.id.btnDate)
        btnDate.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker().build()
            picker.addOnPositiveButtonClickListener {
                val formatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                textDate.text = formatted
            }
            picker.show(supportFragmentManager, "DATE_PICKER")
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveTransaction()
        }

        findViewById<Button>(R.id.btnViewTransactions).setOnClickListener {
            startActivity(Intent(this, TransactionListActivity::class.java))
        }

        findViewById<Button>(R.id.btnBalance).setOnClickListener {
            showBalance()
        }
    }

    private fun saveTransaction() {
        if (spinnerType.selectedItem == null || spinnerDetail.selectedItem == null) {
            Toast.makeText(this, "Selecione um tipo e um detalhe.", Toast.LENGTH_SHORT).show()
            return
        }

        val amountText = editAmount.text.toString()
        if (amountText.isBlank()) {
            Toast.makeText(this, "Informe um valor válido.", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Valor deve ser maior que zero.", Toast.LENGTH_SHORT).show()
            return
        }

        val date = textDate.text.toString()
        if (date.isBlank()) {
            Toast.makeText(this, "Selecione uma data.", Toast.LENGTH_SHORT).show()
            return
        }

        val type = if (spinnerType.selectedItem == "Crédito") "C" else "D"
        val detail = spinnerDetail.selectedItem.toString()

        transactionRepository.insertTransaction(type, detail, amount, date)
        Toast.makeText(this, "Transação salva!", Toast.LENGTH_SHORT).show()

        cleanFields()
    }

    private fun showBalance() {
        val credit = transactionRepository.getTotalCredit()
        val debit = transactionRepository.getTotalDebit()
        val balance = credit - debit

        AlertDialog.Builder(this)
            .setTitle("Saldo")
            .setMessage("R$ %.2f".format(balance))
            .setPositiveButton("OK", null)
            .show()
    }

    private fun cleanFields() {
        spinnerType.setSelection(0)
        val details = if (spinnerType.selectedItem == "Crédito") creditDetails else debitDetails
        spinnerDetail.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, details)
        spinnerDetail.setSelection(0)

        editAmount.setText("")
        textDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }
}