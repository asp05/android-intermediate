package com.sugara.submissionandroidintermediate.view.signin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.sugara.submissionandroidintermediate.R
import com.sugara.submissionandroidintermediate.data.model.UserModel
import com.sugara.submissionandroidintermediate.databinding.ActivitySigninBinding
import com.sugara.submissionandroidintermediate.view.MainActivity
import com.sugara.submissionandroidintermediate.view.ViewModelFactory
import com.sugara.submissionandroidintermediate.view.customView.CustomButton
import com.sugara.submissionandroidintermediate.view.customView.CustomInputPassword
import com.sugara.submissionandroidintermediate.view.home.HomeActivity
import com.sugara.submissionandroidintermediate.view.signup.SignupViewModel

class SigninActivity : AppCompatActivity() {

    private lateinit var signinViewModel: SigninViewModel
    private lateinit var binding: ActivitySigninBinding
    private lateinit var loadingDialog: android.app.AlertDialog
    private lateinit var login: UserModel
    private lateinit var txPasword: CustomInputPassword
    private lateinit var txEmail: EditText
    private lateinit var loginButton: CustomButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signinViewModel = obtainViewModel(this)

        setupView()
        setupAction()
        playAnimation()

        loginButton = findViewById(R.id.loginButton)
        loginButton.setButtonType(CustomButton.ButtonType.LOGIN)

        txEmail = binding.emailEditText
        txPasword = binding.passwordEditText

        txEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if(email.isEmpty()){
                binding.emailEditText.error = "Email tidak boleh kosong"
            } else if(password.isEmpty()){
                binding.passwordEditText.error = "Password tidak boleh kosong"
            } else {
                login = UserModel(
                    email = email,
                    password = password
                )
                signinViewModel.login(login)
            }

        }

        txPasword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        signinViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showLoadingDialog()
            } else {
                dismissLoadingDialog()
                //set text button register to register and enable button
            }
        }

        signinViewModel.response.observe(this) { response ->
            val builder = android.app.AlertDialog.Builder(this)
            builder.setMessage(response.message)
            builder.setCancelable(false)

            if (response.isSuccess) {
                builder.setTitle("Success")
                builder.setPositiveButton("OK") { dialog, _ ->
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            } else {
                builder.setTitle("Error")
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): SigninViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(SigninViewModel::class.java)
    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setMyButtonEnable() {
        val resultEmail = txEmail.text
        val resultPassword = txPasword.text
        loginButton.isEnabled = resultEmail != null && resultPassword != null && resultEmail.toString()
            .isNotEmpty() && resultPassword.toString().isNotEmpty() && resultPassword.toString().length > 7
    }


    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            AlertDialog.Builder(this).apply {
                setTitle("Yeah!")
                setMessage("Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
                setPositiveButton("Lanjut") { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }


    private fun showLoadingDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_loading, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        loadingDialog = builder.create()
        loadingDialog.show()
    }

    private fun dismissLoadingDialog() {
        if (::loadingDialog.isInitialized && loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

}