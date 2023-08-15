package com.codeenemy.memorywhisper

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.codeenemy.memorywhisper.databinding.ActivityAddHappyPlaceBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddHappyPlaceActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityAddHappyPlaceBinding? = null
    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarAddPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolbarAddPlace?.setNavigationOnClickListener {
            onBackPressed()
        }
        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        binding?.editTextDate?.setOnClickListener(this)
        binding?.tvAddImage?.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.edit_text_date -> {
                DatePickerDialog(
                    this@AddHappyPlaceActivity,
                    dateSetListener, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            R.id.tv_add_image -> {
                val pickerDialog = AlertDialog.Builder(this)
                pickerDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf(
                    "Select photo from Gallery", "Capture photo from" +
                            " Camera"
                )
                pickerDialog.setItems(pictureDialogItems) { dialog, which ->
                    when (which) {
                        0 -> choosePhotoFromGallery()
                        1 -> Toast.makeText(
                            this@AddHappyPlaceActivity,
                            "Camera Selection Coming soon...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                pickerDialog.show()
            }
        }
    }

    private fun choosePhotoFromGallery() {
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ).withListener(object: MultiplePermissionsListener {
            override fun onPermissionsChecked( report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {
                    Toast.makeText(this@AddHappyPlaceActivity,
                        "Storage READ/WRITE permission are granted. Now you can select an image " +
                                "from GALLERY",
                        Toast.LENGTH_SHORT).show()
                }
            }
            override fun onPermissionRationaleShouldBeShown( permissions: MutableList<PermissionRequest>, token: PermissionToken
            ) {
                showRationalDialogForPermissions()
            }
        }).onSameThread().check();
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this).setMessage("It look like you have turned off permission " +
                "required for this feature. It can be enabled under the Applications Settings.")
            .setPositiveButton("GO TO SETTINGS"){
                _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName , null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel") {
                dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun updateDateInView() {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding?.editTextDate?.setText(sdf.format(cal.time).toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}