package com.korilin.kkcp

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.korilin.kkcp.databinding.AccountFragmentBinding
import com.korilin.kkcp.databinding.ItemAccountBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
            }
        }

        override fun getItemCount(): Int = viewModel.accounts.size
    }

    inner class RecyclerAdapterHolder(val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}