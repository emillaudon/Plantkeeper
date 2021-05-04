package com.example.plantkeeper.fragments

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.plantkeeper.R
import com.example.plantkeeper.models.NetworkHandler
import com.example.plantkeeper.models.Plant
import kotlinx.android.synthetic.main.fragment_create.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val REQUEST_EXTERNAL_STORAGE = 1
private val PERMISSIONS_STORAGE = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)


/**
 * A simple [Fragment] subclass.
 * Use the [CreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var RESULT_LOAD_IMAGE: Int = 1

    lateinit var saveButton: Button

    lateinit var img: Bitmap
    lateinit var imgPath: String

    lateinit var image: ImageView

    lateinit var wateringBar: ProgressBar
    lateinit var sunlightBar: ProgressBar
    lateinit var temperatureBar: ProgressBar

    lateinit var heightTextView: TextView

    lateinit var noteEditText: EditText

    var height: Double = -10.0

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
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_create, container, false)
        saveButton = rootView.findViewById(R.id.savebutton)
        image = rootView.findViewById(R.id.imageChosen)

        noteEditText = rootView.findViewById(R.id.note)

        wateringBar = rootView.findViewById(R.id.progressBarWatering)
        sunlightBar = rootView.findViewById(R.id.progressBarSunlight)
        temperatureBar = rootView.findViewById(R.id.progressBarTemperature)

        heightTextView = rootView.findViewById(R.id.heightText)
        heightTextView.text = "- CM"

        val barList = listOf<ProgressBar>(wateringBar, sunlightBar, temperatureBar)

        barList.forEach {bar ->
            bar.setOnClickListener {
                showDialogFor(bar)
            }
        }


        image.setOnClickListener {
            verifyStoragePermissions(activity)
            val i = Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }

        heightTextView.setOnClickListener {
            showDialogForHeight()
        }



        saveButton.setOnClickListener {
            val text = rootView.findViewById<EditText>(R.id.postName).text.toString()

            val watering = wateringBar.progress
            val temperature = temperatureBar.progress
            val sunlight = sunlightBar.progress

            val newPost = Plant(imgPath, text, watering, temperature, sunlight, noteEditText.text.toString(), height)
            val handler = NetworkHandler()

            handler.newPlant(newPost, imgPath)

            val newFragment: Fragment = HomeFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

            transaction.replace(R.id.flFragment, newFragment)
            transaction.addToBackStack(null)

            transaction.commit()
        }

        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            val selectedImage: Uri? = data.data
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor = activity?.contentResolver?.query(
                selectedImage!!,
                filePathColumn, null, null, null
            )
            cursor?.moveToFirst()

            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
            val picturePath = cursor?.getString(columnIndex!!)
            cursor?.close()
            imgPath = picturePath!!
            img = BitmapFactory.decodeFile(picturePath)
            image.setImageBitmap(img)
            image.scaleType = ImageView.ScaleType.CENTER_CROP;

        } else {
            Toast.makeText(activity, "Try Again!!", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun showDialogFor(bar: ProgressBar) {
        val d = Dialog(requireContext())
        d.setTitle("NumberPicker")
        d.setContentView(R.layout.dialog)
        val b1 = d.findViewById(R.id.button1) as Button
        val b2 = d.findViewById(R.id.button2) as Button
        val title = d.findViewById(R.id.dialogTitle) as TextView
        val np = d.findViewById(R.id.numberPicker1) as NumberPicker

        np.maxValue = 3
        np.minValue = 0
        np.wrapSelectorWheel = false
        var titleText = ""

        when (bar) {
            progressBarWatering -> {
                titleText = "Watering Frequency"
                np.setDisplayedValues(arrayOf("Rarely", "Sometimes", "Often", "Very Often"))
            }
            progressBarSunlight -> {
                titleText = "Sunlight Needed"
                np.setDisplayedValues(arrayOf("Barely Any", "Some", "Moderate", "Alot"))
            }
            progressBarTemperature -> {
                titleText = "Temperature"
                np.setDisplayedValues(arrayOf("Resistant to cold", "Room temperature", "Above Room Temperature", "Tropical"))
            }
        }
        title.text = titleText
        b1.setOnClickListener {
                bar.progress = (np.value + 1) * 25
                d.dismiss()
        }
        b2.setOnClickListener{
                d.dismiss()
        }
        d.show()
    }

    fun showDialogForHeight() {
        val d = Dialog(requireContext())
        d.setTitle("Height in MM")
        var titleText = "Height in MM"
        d.setContentView(R.layout.dialog)
        val b1 = d.findViewById(R.id.button1) as Button
        val b2 = d.findViewById(R.id.button2) as Button
        val title = d.findViewById(R.id.dialogTitle) as TextView
        val np = d.findViewById(R.id.numberPicker1) as NumberPicker
        if (height > 0.0) {
            np.value = (height * 10.0).toInt()
        }
        title.text = titleText

        np.maxValue = 400
        np.minValue = 0
        np.wrapSelectorWheel = false

        b1.setOnClickListener {
            height = np.value.toDouble() / 10.0
            heightTextView.text = "$height CM"
            d.dismiss()
        }
        b2.setOnClickListener{
            d.dismiss()
        }
        d.show()
    }

    fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission =
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            if (activity != null) {
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
