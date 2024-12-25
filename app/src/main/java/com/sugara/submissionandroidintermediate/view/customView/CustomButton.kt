package com.sugara.submissionandroidintermediate.view.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.sugara.submissionandroidintermediate.R

class CustomButton : AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var txtColor: Int = 0
    private var enabledBackground: Drawable
    private var disabledBackground: Drawable
    private var buttonType: ButtonType = ButtonType.LOGIN
    init {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.background_button) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.background_button_disabled) as Drawable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if(isEnabled) enabledBackground else disabledBackground
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
        text = if (isEnabled) {
            when (buttonType) {
                ButtonType.LOGIN -> "Login"
                ButtonType.REGISTER -> "Register"
            }
        } else {
            "Isi semua field dengan benar"
        }
    }


    fun setButtonType(type: ButtonType) {
        buttonType = type
        invalidate()
    }

    enum class ButtonType {
        LOGIN, REGISTER
    }
}