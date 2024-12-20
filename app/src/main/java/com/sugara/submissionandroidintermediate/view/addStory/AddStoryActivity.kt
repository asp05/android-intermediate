package com.sugara.submissionandroidintermediate.view.addStory

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.sugara.submissionandroidintermediate.R
import com.sugara.submissionandroidintermediate.databinding.ActivityAddStoryBinding
import com.sugara.submissionandroidintermediate.databinding.ActivityHomeBinding
import com.sugara.submissionandroidintermediate.di.ResultState
import com.sugara.submissionandroidintermediate.utils.getImageUri
import com.sugara.submissionandroidintermediate.view.ViewModelFactory
import com.sugara.submissionandroidintermediate.view.home.HomeActivity
import com.sugara.submissionandroidintermediate.view.home.HomeViewModel
import com.sugara.submissionandroidintermediate.view.signin.SigninActivity

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddStoryBinding
    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var loadingDialog: android.app.AlertDialog
    private var currentImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        addStoryViewModel = obtainViewModel(this)

        binding.btnSave.setOnClickListener {

            val desc = binding.etDesc.text.toString()
            if (currentImageUri == null) {
                Toast.makeText(this, "Gambar belum terpilih", Toast.LENGTH_SHORT).show()
            } else if (desc.isEmpty()) {
                Toast.makeText(this, "Deskripsi belum diisi", Toast.LENGTH_SHORT).show()
            } else {
                showLoadingDialog()
                // save to database
                addStoryViewModel.addStory(desc, currentImageUri!!, this).observe(this) { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            showLoadingDialog()
                        }
                        is ResultState.Success -> {
                            dismissLoadingDialog()
                            AlertDialog.Builder(this).apply {
                                setTitle("Yeah!")
                                setMessage("Story berhasil ditambahkan")
                                setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                    val intent = Intent(this@AddStoryActivity, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }.show()
                        }
                        is ResultState.Error -> {
                            dismissLoadingDialog()
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }



    }

    private fun obtainViewModel(activity: AppCompatActivity): AddStoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(AddStoryViewModel::class.java)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(this, "Gambar belum terpilih", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }


    private fun showImage() {
        currentImageUri?.let {
            binding.previewImage.setImageURI(it)
        }
    }

    private fun showLoadingDialog() {
        binding.btnSave.isEnabled = false
        binding.btnSave.text = "Loading..."
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_loading, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        loadingDialog = builder.create()
        loadingDialog.show()
    }

    private fun dismissLoadingDialog() {
        binding.btnSave.isEnabled = true
        binding.btnSave.text = "Simpan"
        if (::loadingDialog.isInitialized && loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }


}