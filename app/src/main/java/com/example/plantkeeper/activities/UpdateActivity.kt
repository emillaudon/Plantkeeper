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
import android.os.Environment
import android.provider.MediaStore
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.plantkeeper.BuildConfig
import com.example.plantkeeper.R
import com.example.plantkeeper.models.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UpdateActivity : AppCompatActivity() {

    lateinit var image: ImageView

    val REQUEST_IMAGE_CAPTURE = 1

    lateinit var img: Bitmap
    lateinit var currentPhotoPath: String

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
            StorageHandler.verifyStoragePermissions(this)
            StorageHandler.verifyStoragePermissions(this)
            var cameraPermissionOk = CameraPermissionHandler.verifyCameraPermission(this)
            while (!cameraPermissionOk) {
                cameraPermissionOk = CameraPermissionHandler.verifyCameraPermission(this)
            }
            dispatchTakePictureIntent()
        }

        updateButton.setOnClickListener {
            finishUpdate()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun finishUpdate() {
        var noteEditText = findViewById<EditText>(R.id.noteUpdate)
        var note = noteEditText.text.toString()
        var updateHandler = UpdateHandler()

        updateHandler.newUpdate(plant, currentPhotoPath, note) {newUpdate ->

            var intent = Intent()
            intent.putExtra("result", newUpdate)

            setResult(Activity.RESULT_OK, intent)

            this.finish()
        }
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
                this,
                BuildConfig.APPLICATION_ID +".fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            val f = File(currentPhotoPath)

            image.setImageURI(Uri.fromFile(f))
            image.scaleType = ImageView.ScaleType.CENTER_CROP

            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            this.sendBroadcast(mediaScanIntent)
        }  else {
            Toast.makeText(this, "Try Again!!", Toast.LENGTH_SHORT).show()
        }
    }

    fun showDialogForHeight() {
        val d = Dialog(this)
        d.setTitle("Height in MM")
        var titleText = "Height in MM"
        d.setContentView(R.layout.dialog)
        val setButton = d.findViewById(R.id.setButton) as Button
        val cancelButton = d.findViewById(R.id.cancelButton) as Button
        val title = d.findViewById(R.id.dialogTitle) as TextView
        val np = d.findViewById(R.id.numberPicker1) as NumberPicker

        title.text = titleText

        np.maxValue = 400
        np.minValue = (oldHeight * 10).toInt()
        np.wrapSelectorWheel = false

        setButton.setOnClickListener {
            plant.height = np.value.toDouble() / 10.0
            heightTextView.text = "${plant.height} CM"
            d.dismiss()
        }
        cancelButton.setOnClickListener{
            d.dismiss()
        }
        d.show()
    }
}