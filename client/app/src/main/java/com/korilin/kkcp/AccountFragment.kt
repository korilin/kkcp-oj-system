package com.korilin.kkcp

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        viewModel.onAccountsChange = { position, length ->
            mAdapter.notifyItemRangeChanged(position, length)
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
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("确定删除该管理员账户吗？")
                        .setPositiveButton("Yes") { dialog, id ->
                            viewModel.deleteAccount(account.email) {
                                this@RecyclerViewAdapter.notifyItemRemoved(position)
                            }
                            requireContext().showToast("删除成功")
                        }
                        .setNegativeButton("Cancel") { _, _ ->
                            // User cancelled the dialog
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