package com.example.plantkeeper.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plantkeeper.R
import com.example.plantkeeper.adapters.PostAdapter
import com.example.plantkeeper.models.NetworkHandler
import com.example.plantkeeper.models.Plant
import com.example.plantkeeper.models.PlantUpdate
import com.example.plantkeeper.models.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment(val newPlant: Plant? = null) : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<PostAdapter.ViewHolder>

   var list = ArrayList<PlantUpdate>()


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
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerView)

        val activity = requireActivity()

        //list = ArrayList<PlantUpdate>()
        adapter = PostAdapter(list, activity)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        var yesButton = rootView.findViewById<Button>(R.id.addFriendButton)
        var addFriendText = rootView.findViewById<TextView>(R.id.noFriendsTextView)

        if (User.friendCount > 0) {
            requireActivity().runOnUiThread {
                addFriendText.isVisible = false
                yesButton.isVisible = false
            }

            var networkHandler = NetworkHandler()
            networkHandler.getFriendPosts() {
                if (it.count() > 0 && list != ArrayList(it)) {
                    list = ArrayList(it)

                    update()
                }
            }
        } else {
            yesButton.setOnClickListener {
                val newFragment: Fragment = AddFriendFragment()
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

                transaction.replace(R.id.flFragment, newFragment)
                transaction.addToBackStack(null)

                transaction.commit()
            }
        }



        // Inflate the layout for this fragment
        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun update() {
        requireActivity().runOnUiThread {
            recyclerView.adapter = PostAdapter(list, requireActivity())
            /*
            adapter.notifyDataSetChanged()
            adapter.notifyDataSetInvalidated()
            gridView.adapter = adapter
            adapter.notifyDataSetChanged()
            adapter.notifyDataSetInvalidated()
            gridView.invalidateViews()

             */
        }
    }
}
