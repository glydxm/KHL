package com.glyfly.khl.app.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
import com.glyfly.khl.R
import com.glyfly.khl.app.util.ScreenUtil
import kotlinx.android.synthetic.main.dialog_theme.view.*

/**
 * Created by Administrator on 2017/5/19.
 */

class MDialog : Dialog {

    constructor(context: Context) : super(context)

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val params = window!!.attributes
        params.width = (ScreenUtil.getWidth() * 0.8).toInt()
        params.height = (params.width * 0.8).toInt()
    }

    class Builder {

        companion object {
            val instance = Builder()
        }
        private var title: String? = null
        private var message: String? = null
        private var cancelable = true
        private var positiveButtonText: String? = null
        private var negativeButtonText: String? = null
        private var positiveButtonClickListener: DialogInterface.OnClickListener? = null
        private var negativeButtonClickListener: DialogInterface.OnClickListener? = null

        fun setMessage(message: String): Builder {
            this.message = message
            return instance
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return instance
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return instance
        }

        fun setPositiveButton(positiveButtonText: String, listener: DialogInterface.OnClickListener): Builder {
            this.positiveButtonText = positiveButtonText
            this.positiveButtonClickListener = listener
            return instance
        }

        fun setNegativeButton(negativeButtonText: String, listener: DialogInterface.OnClickListener): Builder {
            this.negativeButtonText = negativeButtonText
            this.negativeButtonClickListener = listener
            return instance
        }

        fun create(context: Context): MDialog {
            val dialog = MDialog(context, R.style.Dialog)
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = inflater.inflate(R.layout.dialog_theme, null)
            dialog.addContentView(layout, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
            layout.title.text = title

            if (!TextUtils.isEmpty(positiveButtonText)) {
                layout.positiveButton.text = positiveButtonText
                layout.positiveButton.setOnClickListener {
                            positiveButtonClickListener!!.onClick(dialog, DialogInterface.BUTTON_POSITIVE)
                        }
            } else {
                layout.positiveButton.visibility = View.GONE
            }

            if (!TextUtils.isEmpty(negativeButtonText)) {
                layout.negativeButton.text = negativeButtonText
                layout.negativeButton.setOnClickListener {
                            negativeButtonClickListener!!.onClick(dialog, DialogInterface.BUTTON_NEGATIVE)
                        }
            } else {
                layout.negativeButton.visibility = View.GONE
            }

            if (!TextUtils.isEmpty(message)) {
                layout.message.text = message
            }
            dialog.setCancelable(cancelable)
            dialog.setContentView(layout)

            return dialog
        }
    }
}
