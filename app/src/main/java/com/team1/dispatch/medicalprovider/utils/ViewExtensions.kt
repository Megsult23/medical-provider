package com.team1.dispatch.medicalprovider.utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.team1.dispatch.medicalprovider.R
import com.team1.dispatch.medicalprovider.databinding.MainToolbarBinding
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

private const val TAG = "ViewExtensions"

fun AppCompatActivity.setUpToolBar(
    toolbarBinding: MainToolbarBinding,
    title: String? = "",
    isHome: Boolean = false,
    @DrawableRes navigationIcon: Int? = null
) = toolbarBinding.execute {
    if (supportActionBar == null) {
        setSupportActionBar(toolbarBinding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(!isHome)
        if (!isHome) {
            toolbarBinding.tvTitle.show()
            toolbarBinding.llUserInfo.hide()
            supportActionBar?.setHomeAsUpIndicator(
                ContextCompat.getDrawable(
                    this@setUpToolBar,
                    navigationIcon.orDefault(R.drawable.ic_back)
                )
            )
        }

        if (title.isNullOrEmpty()) {
            toolbarBinding.tvTitle.visibility = View.GONE
        } else {
            toolbarBinding.tvTitle.visibility = View.VISIBLE
            toolbarBinding.tvTitle.text = title
        }
    }


    toolbarBinding.root.setNavigationOnClickListener {
//        if (!isHome) {
        onBackPressedDispatcher.onBackPressed()
//        }
    }
}

fun Activity.getDrawableExt(@DrawableRes id: Int): Drawable? = ContextCompat.getDrawable(this, id)

fun Activity.getColorStateListExt(@ColorRes id: Int): ColorStateList? =
    ContextCompat.getColorStateList(this, id)

fun Fragment.getDrawable(@DrawableRes id: Int): Drawable? =
    ContextCompat.getDrawable(requireContext(), id)

fun Context.getDrawableExt(@DrawableRes id: Int): Drawable? = ContextCompat.getDrawable(this, id)

fun Activity.getColorExt(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)
fun Fragment.getColor(@ColorRes id: Int): Int = ContextCompat.getColor(requireContext(), id)
fun Context.getColorExt(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

fun Activity.displayToast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.displayInfoDialogWithActionButton(
    message: String?,
    actionText: String?,
    callback: MaterialDialogActionsCallback? = null,
    showActionCancelButton: Boolean? = false
) {
    MaterialDialog(this)
        .show {
            title(R.string.text_info)
            message(
                text = if (message?.contains("http") == true) HtmlCompat.fromHtml(
                    message,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                ) else message
            )
            cornerRadius(12f)
            positiveButton(text = actionText) {
                callback?.proceed()
            }
            if (showActionCancelButton == true)
                negativeButton(text = getString(R.string.text_cancel)) {
                    callback?.cancel()
                }
            icon(R.mipmap.ic_launcher)
            setTheme(R.style.MaterialDialog)
        }
}

@OptIn(ExperimentalContracts::class)
fun <T> T.execute(block: T.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block()
}

fun Context.displayToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun Fragment.hideSoftKeyboard() {
    if (requireActivity().currentFocus != null) {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
    }
}

fun Activity.displayToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.singleChoiceStringDialog(
    title: String?,
    list: List<CharSequence>,
    pos: Int,
    callback: SingleChoiceCallback,
    negativeCallback: NegativeMultiChoiceCallback? = null
): MaterialDialog {
//    var selectedIndex: Int = 0                checkedColor = resources.getColor(R.color.purple_700),
    return MaterialDialog(this)
        .show {
            val result = listItemsSingleChoice(
                items = list,
                initialSelection = pos
            ) { dialog, index, text ->
                // Invoked when the user selects an item
//                selectedIndex = index
                callback.proceed(index)
            }
            Log.d(TAG, "singleChoiceStringDialog: title $title")
            title(text = title)
            titleColor
            actionBar
            cornerRadius(12f)
            positiveButton(R.string.text_ok)
            negativeButton(R.string.text_cancel) {
                negativeCallback?.proceedNegative()
            }
            icon(R.mipmap.ic_launcher)
            setTheme(R.style.MaterialDialog)

        }
}

fun Activity.multipleChoiceDialog(
    title: String?,
    items: List<CharSequence>,
    initialSelection: IntArray,
    callback: MultiChoiceCallback,
    negativeCallback: NegativeMultiChoiceCallback? = null
): MaterialDialog {
//    var selectedIndex: Int = 0
    return MaterialDialog(this)
        .show {
            val result = listItemsMultiChoice(
                items = items,
                initialSelection = initialSelection
            ) { dialog, indices, items ->
                callback.proceed(indices, items)

            }

            title(text = title)
            cornerRadius(12f)
            positiveButton(R.string.text_ok)
            negativeButton(R.string.text_cancel) {
                negativeCallback?.proceedNegative()
            }
            icon(R.mipmap.ic_launcher)
            setTheme(R.style.MaterialDialog)
        }
}

fun Context.areYouSureDialog(
    message: String?,
    callback: MaterialDialogActionsCallback,
    onDismissAction: (() -> Unit)? = null
) {
    MaterialDialog(this)
        .show {
            cornerRadius(12f)
            positiveButton(R.string.text_yes) {
                callback.proceed()
            }
            negativeButton(R.string.text_cancel) {
                callback.cancel()
            }
            setOnDismissListener {
                if (onDismissAction != null) {
                    onDismissAction()
                }
            }
            title(R.string.are_you_sure)
            message(text = message)
            icon(R.drawable.ic_launcher_foreground)
            setTheme(R.style.MaterialDialog)
        }
}

fun Fragment.displayToast(@StringRes message: Int) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.displayToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Activity.displaySuccessDialog(message: String?) {
    MaterialDialog(this)
        .show {
            title(R.string.text_success)
            message(text = message)
            positiveButton(R.string.text_ok)
        }
}

fun Activity.displaySuccessDialog(message: Int?, onDismiss: () -> Unit = {}) {
    MaterialDialog(this)
        .show {
            title(R.string.text_success)
            message(res = message)
            positiveButton(R.string.text_ok)
            icon(R.drawable.ic_launcher_foreground)
            onDismiss { onDismiss() }
        }
}

fun Activity.displayErrorDialog(message: String?) {
    Log.d(TAG, "displayErrorDialog: $message")
    MaterialDialog(this)
        .show {
            title(R.string.text_error)
            message(text = message)
            cornerRadius(12f)
            positiveButton(R.string.text_ok)
            icon(R.mipmap.ic_launcher)
            setTheme(R.style.Theme_DispatchMedicalProvider)
        }
}

fun Activity.displayErrorDialog(message: Int?) {
    MaterialDialog(this)
        .show {
            title(R.string.text_error)
            message(res = message)
            cornerRadius(12f)
            positiveButton(R.string.text_ok)
            icon(R.mipmap.ic_launcher)
            setTheme(R.style.Theme_DispatchMedicalProvider)
        }
}

fun <T> List<T>?.orEmpty(): List<T> = this ?: emptyList()

fun <T, R> List<T>.ifNotEmpty(block: (List<T>) -> R): R {
    if (this.isNotEmpty()) return block(this)
    else throw IllegalStateException("List is empty")
}

fun <T> ArrayList<T>?.orEmpty(): ArrayList<T> = this ?: arrayListOf()

fun String?.orDefault(default: String = ""): String = this ?: default

fun Int?.orDefault(default: Int = 0): Int = this ?: default

fun Long?.orDefault(default: Long = 0L): Long = this ?: default

fun Float?.orDefault(default: Float = 0f): Float = this ?: default

fun Double?.orDefault(default: Double = 0.0): Double = this ?: default

fun Boolean?.orDefault(default: Boolean = false): Boolean = this ?: default

fun <T> T?.orDefault(default: T): T = this ?: default

interface SingleChoiceCallback {
    fun proceed(pos: Int)
}

interface NegativeMultiChoiceCallback {
    fun proceedNegative()
}

interface MaterialDialogActionsCallback {
    fun proceed()
    fun cancel()
}

interface MultiChoiceCallback {
    fun proceed(indices: IntArray, items: List<CharSequence>)
}