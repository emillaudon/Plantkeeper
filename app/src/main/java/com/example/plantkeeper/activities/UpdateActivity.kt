package com.example.plantkeeper.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.plantkeeper.R
import com.example.plantkeeper.models.NetworkHandler
import com.example.plantkeeper.models.Plant
import com.example.plantkeeper.models.PlantUpdate

class UpdateActivity : AppCompatActivity() {

    lateinit var image: ImageView

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var RESULT_LOAD_IMAGE: Int = 1

    lateinit var img: Bitmap
    lateinit var imgPath: String

    lateinit var heightTextView: TextView

    var oldHeight = 0.0

    lateinit var plant: Plant


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        plant = intent.getSerializableExtra("plant") as Plant

        oldHeight = plant.height

        var name = findViewById<TextView>(R.id.namePlantUpdatge)
        name.text = plant.name

        image = findViewById(R.id.imageChosen)

        heightTextView = findViewById(R.id.heightTextUpdate)
        heightTextView.text = plant.height.toString() + " CM"

        val updateButton = findViewById<Button>(R.id.saveButtonUpdate)

        heightTextView.setOnClickListener {
            showDialogForHeight()
        }

        image.setOnClickListener {
            verifyStoragePermissions(this)
            val i = Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }

        updateButton.setOnClickListener {
            var noteEditText = findViewById<EditText>(R.id.noteUpdate)

            var networkHandler = NetworkHandler()
            var newHeight = plant.height
            var note = noteEditText.text.toString()
            val currentTime = System.currentTimeMillis() / 1000
            networkHandler.newUpdate(imgPath, plant, PlantUpdate(plant.height, "placeHolder", note, currentTime.toInt() ))
            this.finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            val selectedImage: Uri? = data.data
            val filePathColumn =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver?.query(
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
            Toast.makeText(this, "Try Again!!", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun showDialogForHeight() {
        val d = Dialog(this)
        d.setTitle("Height in MM")
        var titleText = "Height in MM"
        d.setContentView(R.layout.dialog)
        val b1 = d.findViewById(R.id.button1) as Button
        val b2 = d.findViewById(R.id.button2) as Button
        val title = d.findViewById(R.id.dialogTitle) as TextView
        val np = d.findViewById(R.id.numberPicker1) as NumberPicker

        title.text = titleText

        oldHeight = 15.1
        np.maxValue = 400
        np.minValue = (oldHeight * 10).toInt()
        np.wrapSelectorWheel = false

        b1.setOnClickListener {
            plant.height = np.value.toDouble() / 10.0
            heightTextView.text = "${plant.height} CM"
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
                this,
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
}