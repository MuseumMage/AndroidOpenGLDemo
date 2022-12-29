package com.example.androidlearnopengl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.androidlearnopengl.databinding.FragmentChooseBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChooseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChooseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentChooseBinding>(
            inflater,
            R.layout.fragment_choose,
            container,
            false
        )
        binding.androidDocButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_chooseFragment_to_androidDocFragment)
        }
        binding.helloTriangleButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_chooseFragment_to_helloTriangleFragment)
        }
        return binding.root
    }
}