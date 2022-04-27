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
import com.korilin.kkcp.databinding.ItemAccountBinding
import com.korilin.kkcp.databinding.UserFragmentBinding

class UserFragment : Fragment() {

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel
    private lateinit var binding: UserFragmentBinding
    private val mAdapter = RecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserFragmentBinding.inflate(inflater, container, false)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel.onUsersInsert = { position, size ->
            mAdapter.notifyItemRangeInserted(position, size)
        }
        viewModel.onUsersUpdate = {
            mAdapter.notifyItemChanged(it)
        }
        viewModel.initUsers()
    }


    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerAdapterHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterHolder {
            val view = ItemAccountBinding.inflate(requireActivity().layoutInflater, parent, false)
            return RecyclerAdapterHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerAdapterHolder, position: Int) {
            val user = viewModel.users[position]
            holder.binding.apply {
                title.text = user.name
                content.text = user.email
                tag.text = "ID:${user.id}"
                if (user.block) {
                    deleteBtn.text = "UnBlock"
                    deleteBtn.setBackgroundColor(
                        requireContext().getColor(R.color.github_dark)
                    )
                } else {
                    deleteBtn.text = "Block"
                }
                deleteBtn.setOnClickListener {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage(if (user.block) "是否解禁该用户？" else "确定封禁该用户？")
                        .setPositiveButton("Yes") { _, _ ->
                            viewModel.updateUserState(position, user.block.not())
                        }
                        .setNegativeButton("Cancel") { _, _ ->
                        }
                    // Create the AlertDialog object and return it
                    builder.create().show()
                }
            }
        }

        override fun getItemCount(): Int = viewModel.users.size
    }

    inner class RecyclerAdapterHolder(val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root)
}