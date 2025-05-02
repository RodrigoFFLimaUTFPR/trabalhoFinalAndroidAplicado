package com.rodrigofflima.trabalhofinalandroidaplicado.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rodrigofflima.trabalhofinalandroidaplicado.entity.Transaction
import com.rodrigofflima.trabalhofinalandroidaplicado.R

class TransactionAdapter(private val onDeleteTransaction: (Transaction) -> Unit) :
    ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(transaction: Transaction) {
            val type = if (transaction.type == "C") "Crédito" else "Débito"
            itemView.findViewById<TextView>(R.id.textType).text = type
            itemView.findViewById<TextView>(R.id.textDate).text = transaction.date

            itemView.findViewById<TextView>(R.id.textDetail).text = transaction.detail
            itemView.findViewById<TextView>(R.id.textAmount).text = "R$ %.2f".format(transaction.amount)

            val amountTextView = itemView.findViewById<TextView>(R.id.textAmount)
            if (transaction.type == "C") {
                amountTextView.setTextColor(itemView.context.getColor(R.color.green))
            } else {
                amountTextView.setTextColor(itemView.context.getColor(R.color.red))
            }

            // Ícone de lixeira no botão
            val deleteButton = itemView.findViewById<ImageView>(R.id.btnDelete)
            deleteButton.setImageResource(R.drawable.ic_delete)  // Aponte para o seu ícone de lixeira
            deleteButton.setOnClickListener {
                showDeleteConfirmationDialog(transaction)
            }
        }

        // Função para mostrar o diálogo de confirmação de exclusão
        private fun showDeleteConfirmationDialog(transaction: Transaction) {
            val context = itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Deseja excluir esse registro?")
                .setCancelable(false)
                .setPositiveButton("Sim") { dialog, id ->
                    onDeleteTransaction(transaction)
                }
                .setNegativeButton("Não") { dialog, id ->
                    dialog.dismiss()  // Apenas fecha o diálogo
                }
            val alert = builder.create()
            alert.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}