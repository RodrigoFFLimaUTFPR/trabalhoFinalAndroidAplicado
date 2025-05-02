package com.rodrigofflima.trabalhofinalandroidaplicado.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.rodrigofflima.trabalhofinalandroidaplicado.entity.Transaction

class TransactionRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun insertTransaction(type: String, detail: String, amount: Double, date: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TYPE, type)
            put(DatabaseHelper.COLUMN_DETAIL, detail)
            put(DatabaseHelper.COLUMN_AMOUNT, amount)
            put(DatabaseHelper.COLUMN_DATE, date)
        }
        db.insert(DatabaseHelper.TABLE_NAME, null, values)
        db.close()
    }

    fun getAllTransactions(): List<Transaction> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            null,
            null, null, null, null, null
        )

        val transactions = mutableListOf<Transaction>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
            val type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TYPE))
            val detail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DETAIL))
            val amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE))

            transactions.add(Transaction(id, type, detail, amount, date))
        }
        cursor.close()
        db.close()
        return transactions
    }

    fun getTotalCredit(): Double {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(amount) FROM ${DatabaseHelper.TABLE_NAME} WHERE type = 'C'", null)
        cursor.moveToFirst()
        val totalCredit = cursor.getDouble(0)
        cursor.close()
        db.close()
        return totalCredit
    }

    fun getTotalDebit(): Double {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(amount) FROM ${DatabaseHelper.TABLE_NAME} WHERE type = 'D'", null)
        cursor.moveToFirst()
        val totalDebit = cursor.getDouble(0)
        cursor.close()
        db.close()
        return totalDebit
    }

    fun deleteTransaction(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete("transactions", "id = ?", arrayOf(id.toString()))
        db.close()
    }
}