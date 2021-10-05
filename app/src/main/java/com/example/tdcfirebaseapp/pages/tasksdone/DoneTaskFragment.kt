package com.example.tdcfirebaseapp.pages.tasksdone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tdcfirebaseapp.databinding.FragmentDoneTaskBinding

class DoneTaskFragment : Fragment() {

    private lateinit var binding: FragmentDoneTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoneTaskBinding.inflate(inflater, container, false)

        return binding.root
    }
}