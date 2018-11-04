/*
 * Created by Tamás Szincsák on 2018-11-04.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.ui.detail

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import androidx.appcompat.widget.AppCompatImageButton

class CheckableImageButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatImageButton(context, attrs), Checkable {
    private var isChecked: Boolean = false

    init {
        context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.checked)).run {
            isChecked = getBoolean(0, false)
            recycle()
        }
    }

    override fun toggle() {
        setChecked(!isChecked)
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    /**
     * Changes the checked state of this button.
     *
     * @param isChecked
     * true to check the button, false to uncheck it
     */
    override fun setChecked(isChecked: Boolean) {
        if (this.isChecked == isChecked)
            return

        this.isChecked = isChecked

        refreshDrawableState()
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked()) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()

        invalidate()
    }

    internal class SavedState : View.BaseSavedState {
        var checked: Boolean = false

        constructor(superState: Parcelable) : super(superState)

        private constructor(`in`: Parcel) : super(`in`) {
            checked = `in`.readByte().toInt() != 0
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeByte((if (checked) 1 else 0).toByte())
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.checked = isChecked()
        return ss
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState

        super.onRestoreInstanceState(ss.getSuperState())
        setChecked(ss.checked)
        requestLayout()
    }

    companion object {
        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }
}
