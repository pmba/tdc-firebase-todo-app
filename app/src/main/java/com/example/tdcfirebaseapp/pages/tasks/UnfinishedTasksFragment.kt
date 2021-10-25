package com.example.tdcfirebaseapp.pages.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tdcfirebaseapp.R
import com.example.tdcfirebaseapp.databinding.FragmentUnfinishedTasksBinding

class UnfinishedTasksFragment : Fragment() {

    private lateinit var binding: FragmentUnfinishedTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnfinishedTasksBinding.inflate(inflater, container, false)

        return binding.root
    }
}