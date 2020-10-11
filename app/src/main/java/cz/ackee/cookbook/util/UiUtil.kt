package cz.ackee.cookbook.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import cz.ackee.cookbook.R

object UiUtil {

    fun showAlertDialog(resIdTitle: Int, resIdDescription: Int, context: Context?) {
        context?.let {
            AlertDialog.Builder(context)
                    .setTitle(resIdTitle)
                    .setMessage(resIdDescription)
                    .setNeutralButton(R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setCancelable(false)
                    .show()
        }
    }

    fun hideKeyboard(activity: Activity?) {
        if (activity != null) {
            val view = activity.currentFocus
            if (view != null) {
                val imm =
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}