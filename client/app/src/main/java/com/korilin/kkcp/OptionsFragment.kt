package com.korilin.kkcp

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.korilin.kkcp.databinding.ItemAccountBinding
import com.korilin.kkcp.databinding.OptionsFragmentBinding

class OptionsFragment : Fragment() {

    companion object {
        fun newInstance() = OptionsFragment()
    }

    private lateinit var viewModel: OptionsViewModel
    private lateinit var binding: OptionsFragmentBinding
    private val mAdapter = RecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OptionsFragmentBinding.inflate(inflater, container, false)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OptionsViewModel::class.java)
        viewModel.onUsersInsert = { position, size ->
            mAdapter.notifyItemRangeInserted(position, size)
        }
        viewModel.initOptions()
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerAdapterHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterHolder {
            val view = ItemAccountBinding.inflate(requireActivity().layoutInflater, parent, false)
            return RecyclerAdapterHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerAdapterHolder, position: Int) {
            val option = viewModel.options[position]
            holder.binding.apply {
                title.text = option.email
                content.text = option.option
                tag.text = option.time
                deleteBtn.visibility = View.GONE
            }
        }

        override fun getItemCount(): Int = viewModel.options.size
    }

    inner class RecyclerAdapterHolder(val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root)
}