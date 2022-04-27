package com.korilin.kkcp

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.korilin.kkcp.databinding.AccountFragmentBinding
import com.korilin.kkcp.databinding.ItemAccountBinding

class AccountFragment : Fragment() {

    companion object {
        fun newInstance() = AccountFragment()
    }

    private lateinit var viewModel: AccountViewModel
    private lateinit var binding: AccountFragmentBinding
    private val mAdapter = RecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AccountFragmentBinding.inflate(inflater, container, false)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        binding.actionButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            val view = inflater.inflate(R.layout.dialog_account, null)
            builder.setView(view)
                // Add action buttons
                .setPositiveButton("Create") { dialog, id ->
                    val radioGroup = view.findViewById<RadioGroup>(R.id.radio_group)
                    val level = when (radioGroup.checkedRadioButtonId) {
                        R.id.radio_lv3 -> 3
                        R.id.radio_lv2 -> 2
                        else -> 1
                    }
                    val name = view.findViewById<TextInputEditText>(R.id.name).text.toString()
                    val email = view.findViewById<TextInputEditText>(R.id.email).text.toString()
                    viewModel.newAccount(email, name, level)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create().show()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        viewModel.onAccountsChange = { position, _ ->
            mAdapter.notifyItemInserted(position)
        }
        viewModel.initAccounts()
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerAdapterHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterHolder {
            val view = ItemAccountBinding.inflate(requireActivity().layoutInflater, parent, false)
            return RecyclerAdapterHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerAdapterHolder, position: Int) {
            val account = viewModel.accounts[position]
            holder.binding.apply {
                name.text = account.name
                email.text = account.email
                level.text = "Lv${account.level}"
                deleteBtn.setOnClickListener {
                    val builder = AlertDialog.Builder(requireActivity())
                    builder.setMessage("确定删除该管理员账户吗？")
                        .setPositiveButton("Yes") { dialog, id ->
                            viewModel.deleteAccount(position) {
                                this@RecyclerViewAdapter.notifyItemRemoved(position)
                                requireContext().showToast("删除成功")
                            }
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.cancel()
                        }
                    // Create the AlertDialog object and return it
                    builder.create().show()
                }
            }
        }

        override fun getItemCount(): Int = viewModel.accounts.size
    }

    inner class RecyclerAdapterHolder(val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root)
}