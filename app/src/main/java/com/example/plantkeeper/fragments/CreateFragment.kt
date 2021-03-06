package com.example.plantkeeper.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.plantkeeper.BuildConfig
import com.example.plantkeeper.R
import com.example.plantkeeper.models.*
import kotlinx.android.synthetic.main.fragment_create.*
import java.io.File
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [CreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateFragment : Fragment() {
     val REQUEST_IMAGE_CAPTURE = 1

    lateinit var saveButton: Button

    lateinit var img: Bitmap
    lateinit var imgPath: String

    lateinit var image: ImageView

    lateinit var wateringBar: ProgressBar
    lateinit var sunlightBar: ProgressBar
    lateinit var temperatureBar: ProgressBar

    lateinit var heightTextView: TextView

    lateinit var noteEditText: EditText

    lateinit var currentPhotoPath: String

    lateinit var rootView: View

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
        rootView = inflater.inflate(R.layout.fragment_create, container, false)
        saveButton = rootView.findViewById(R.id.savebutton)
        image = rootView.findViewById(R.id.imageChosen)

        noteEditText = rootView.findViewById(R.id.noteUpdate)

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
            StorageHandler.verifyStoragePermissions(requireActivity())
            var cameraPermissionOk = CameraPermissionHandler.verifyCameraPermission(requireActivity())
            while (!cameraPermissionOk) {
                cameraPermissionOk = CameraPermissionHandler.verifyCameraPermission(requireActivity())
            }
            dispatchTakePictureIntent()
        }

        heightTextView.setOnClickListener {
            showDialogForHeight()
        }

        saveButton.setOnClickListener {
            savePlantAndGoBack()
        }

        return rootView
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun savePlantAndGoBack() {
        val text = rootView.findViewById<EditText>(R.id.updateNote).text.toString()

        val watering = wateringBar.progress
        val temperature = temperatureBar.progress
        val sunlight = sunlightBar.progress
        imgPath = currentPhotoPath

        var listOfUpdates = mutableListOf<PlantUpdate>()
        listOfUpdates.add(PlantUpdate(height, "placeHolder", "placeHolder", "100", 1, User.name))
        val newPost = Plant(imgPath, text, watering, temperature, sunlight, noteEditText.text.toString(), height, "placeHolder", listOfUpdates, 1)

        var plantHandler = PlantHandler()
        plantHandler.createNewPlant(newPost)

        val newFragment: Fragment = HomeFragment()
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

        transaction.replace(R.id.flFragment, newFragment)
        transaction.addToBackStack(null)

        resetFragment()
        transaction.commit()
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            var photoFile: File? = null
            try {
                photoFile = StorageHandler.createFile()
                currentPhotoPath = photoFile.getAbsolutePath()
            } catch (ex: IOException) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    BuildConfig.APPLICATION_ID +".fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            val f = File(currentPhotoPath)

            image.setImageURI(Uri.fromFile(f))
            println("4")
            image.scaleType = ImageView.ScaleType.CENTER_CROP

            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            requireContext().sendBroadcast(mediaScanIntent)
        } else {
            Toast.makeText(activity, "Try Again!!", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun showDialogFor(bar: ProgressBar) {
        val d = Dialog(requireContext())
        d.setTitle("NumberPicker")
        d.setContentView(R.layout.dialog)
        val setButton = d.findViewById(R.id.setButton) as Button
        val cancelButton = d.findViewById(R.id.cancelButton) as Button
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
        setButton.setOnClickListener {
                bar.progress = (np.value + 1) * 25
                d.dismiss()
        }
        cancelButton.setOnClickListener{
                d.dismiss()
        }
        d.show()
    }

    fun showDialogForHeight() {
        val d = Dialog(requireContext())
        d.setTitle("Height in MM")
        var titleText = "Height in MM"
        d.setContentView(R.layout.dialog)
        val setButton = d.findViewById(R.id.setButton) as Button
        val cancelButton = d.findViewById(R.id.cancelButton) as Button
        val title = d.findViewById(R.id.dialogTitle) as TextView
        val np = d.findViewById(R.id.numberPicker1) as NumberPicker
        if (height > 0.0) {
            np.value = (height * 10.0).toInt()
        }
        title.text = titleText

        np.maxValue = 400
        np.minValue = 0
        np.wrapSelectorWheel = false

        setButton.setOnClickListener {
            height = np.value.toDouble() / 10.0
            heightTextView.text = "$height CM"
            d.dismiss()
        }
        cancelButton.setOnClickListener{
            d.dismiss()
        }
        d.show()
    }

    fun resetFragment() {
        wateringBar.progress = 0
        sunlightBar.progress = 0
        temperatureBar.progress = 0

        heightTextView.text = "- CM"

        noteEditText.setText("")

        rootView.findViewById<EditText>(R.id.updateNote).setText("")
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
