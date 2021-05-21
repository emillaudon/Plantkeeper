package com.example.plantkeeper.fragments

import ProfilePostAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.plantkeeper.R
import com.example.plantkeeper.activities.MainActivity
import com.example.plantkeeper.activities.SpecificPostActivity
import com.example.plantkeeper.models.NetworkHandler
import com.example.plantkeeper.models.Plant
import com.example.plantkeeper.models.PlantHandler
import com.example.plantkeeper.models.PlantSorter
import com.example.plantkeeper.models.PlantSorter.Companion.bubbleSort
import kotlin.concurrent.thread


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    var plantHandler = PlantHandler()

    lateinit var gridView: GridView
    lateinit var adapter: ProfilePostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        gridView = rootView.findViewById(R.id.profileGrid)

        if(plantHandler.plantList.count() > 0) {
            gridView.adapter = ProfilePostAdapter(requireContext(), plantHandler.plantList)
        }

        gridView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                var intent = Intent(context, SpecificPostActivity::class.java)
                var plant = plantHandler.plantList[position]
                intent.putExtra("plant", plant)
                startActivity(intent)
            }

        plantHandler.getUserPlants() {
            update()
        }

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                println("now2")
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun update() {
        requireActivity().runOnUiThread {
            gridView.adapter = ProfilePostAdapter(requireContext(), plantHandler.plantList)

        }
    }
}
