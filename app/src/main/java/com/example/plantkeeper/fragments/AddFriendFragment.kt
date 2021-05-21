package com.example.plantkeeper.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentTransaction
import com.example.plantkeeper.R
import com.example.plantkeeper.models.NetworkHandler
import com.example.plantkeeper.models.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFriendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFriendFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_add_friend, container, false)

        var addButton = rootView.findViewById<Button>(R.id.addFriendButton2)

        addButton.setOnClickListener {
            var emailEditText = rootView.findViewById<EditText>(R.id.emailFriendEditText)
            var emailToAdd = emailEditText.text.toString()

            var networkHandler = NetworkHandler()
            networkHandler.addFriend(emailToAdd) {
                User.friendCount = User.friendCount + 1
                val newFragment: Fragment = HomeFragment()
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

                transaction.replace(R.id.flFragment, newFragment)
                transaction.addToBackStack(null)

                transaction.commit()
            }
        }


        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFriendFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}