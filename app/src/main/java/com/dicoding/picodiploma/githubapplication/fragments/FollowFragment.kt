package com.dicoding.picodiploma.githubapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.picodiploma.githubapplication.R
import kotlinx.android.synthetic.main.fragment_follow.*

class FollowFragment : Fragment() {

    companion object{
        private const val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(index: Int): FollowFragment{
            val fragment = FollowFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var index = 1
        if(arguments != null){
            index = arguments?.getInt(ARG_SECTION_NUMBER, 0) as Int
        }

        section_label.text = index.toString()
    }
}