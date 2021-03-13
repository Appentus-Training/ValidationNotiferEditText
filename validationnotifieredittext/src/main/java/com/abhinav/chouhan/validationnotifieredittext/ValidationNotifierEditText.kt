package com.abhinav.chouhan.validationnotifieredittext

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.abhinav.chouhan.validationnotifieredittext.R
import java.lang.IllegalArgumentException

import java.util.*

/**
 * @author Abhinav Chouhan
 * @author abhinavchouhan97@gmail.com
 * @since 13-march-2021
 *
 * A subclass which notifies observers about when the text inside this editText
 * is valid or invalid the validity is check against the regex that client provide if no regex is provided
 * no listeners will be called
 * moreover client can choose to draw a border with desired color when the text inside this editText becomes valid
 * or becomes invalid
 *
 * <b> for example </b> make border red when user enters number instead of alphabet to indicate that something is wrong
 *
 * a default border color can also be provided
 *
 * <b> note </b> borders will only work if vne_giveBorder attribute is set to true which is by default false
 */
class ValidationNotifierEditText(context: Context, attributeSet: AttributeSet? = null) :
    androidx.appcompat.widget.AppCompatEditText(context, attributeSet) {

    /** without this variable set to true no border will be drawn **/
    var hasBorder: Boolean = false
    /** default border color for the editText**/
    var borderColor:Int? = null
        set(value) {
            field = value
            /** f a null value is passed for the color then just set
             *  transparent color because user is probably trying to remove the color
             */
            paint?.color = value?:Color.TRANSPARENT
            invalidate() // invalidate to redraw with new color
        }
    /** valid this editText enters a valid state the border will be drawn with this color **/
    var validBorderColor:Int? = null
    /** valid this editText enters a invalid state the border will be drawn with this color **/
    var invalidBorderColor:Int? = null
    private var cornerRadius: Float? = null
    private var borderWidth: Float? = null
    private var paint: Paint? = null
    private val validatorRegex: String? // check the validity of text inside this editText against this regex
    var isValid = false
        private set

    /** contains a list of all the listener who was to be notified when this editText
     * enter a valid or invalid state
     */
    private var validationChangeListenerList: MutableList<ValidationChangeListener> = LinkedList()
    private var previouslyMatched = false

    /**
     * interface to notify all the listeners of this editText about
     * its valid or invalid state
     */
    interface ValidationChangeListener {
        /** called when this ediText enters a valid state
         * @param validationNotifierEditText the editText which entered in valid state
         */
        fun onBecomeValid(validationNotifierEditText: ValidationNotifierEditText)
        /** called when this ediText enters a invalid state
         * @param validationNotifierEditText the editText which entered in invalid state
         */
        fun onBecomeInvalid(validationNotifierEditText: ValidationNotifierEditText)
    }

    /**
     * adds a ValidationChangeListener to get notified about valid or invalid state of this editText
     */
    fun addValidationChangeListener(validationChangeListener: ValidationChangeListener) {
        validationChangeListenerList.add(validationChangeListener)
    }


    /**
     * remove a ValidationChangeListener
     */
    fun removeValidationChangeListener(validationChangeListener: ValidationChangeListener): Boolean {
        return validationChangeListenerList.remove(validationChangeListener)
    }


    init {
        val ta = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.ValidationNotifierEditText,
            0,
            0
        )
        // get the validator regex
        validatorRegex = ta.getString(R.styleable.ValidationNotifierEditText_vne_validatorRegex)

        /**
         * if we have a validator regex then only we are interested in drawing border or notifying listeners
         */
        if (validatorRegex != null) {

            // retrive all the attributes
            hasBorder = ta.getBoolean(R.styleable.ValidationNotifierEditText_vne_giveBorder, false)
            if (hasBorder) {
                borderColor =
                    ta.getColor(R.styleable.ValidationNotifierEditText_vne_borderColor, Color.BLACK)
                validBorderColor = ta.getColor(R.styleable.ValidationNotifierEditText_vne_validBorderColor,Color.TRANSPARENT)
                invalidBorderColor = ta.getColor(R.styleable.ValidationNotifierEditText_vne_invalidBorderColor,Color.TRANSPARENT)
                cornerRadius =
                    ta.getDimension(R.styleable.ValidationNotifierEditText_vne_cornerRadius, 0f)
                borderWidth =
                    ta.getDimension(R.styleable.ValidationNotifierEditText_vne_borderWidth, 5f)
                paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = borderColor?:Color.TRANSPARENT
                    strokeWidth = borderWidth as Float
                    style = Paint.Style.STROKE
                }
            }
            ta.recycle()
            //when the text is changed
            doOnTextChanged { text, start, before, count ->
                super.onTextChanged(text, start, before, count)
                if (text != null) {
                    //if the text matches our regex and was not matched when the last time a character was enter
                    // we are maintaining a previouslyMatched variable so that we do not redraw and notify our listeners
                     // if use is continuously entering valid or invalid characters
                    if (text.matches(validatorRegex.toRegex()) && !previouslyMatched) {
                        isValid = true
                        previouslyMatched = true
                        notifyValidity()
                    } else if (previouslyMatched) {
                        isValid = false
                        previouslyMatched = false
                        notifyInvalidity()
                    }
                }
            }
        } else {
            isValid = true // if we are not provided a regex to match agains the this edittext is always valid
        }
    }

    private fun notifyValidity() {
        validationChangeListenerList.forEach { it.onBecomeValid(this) }
        if(hasBorder) borderColor = validBorderColor
    }

    private fun notifyInvalidity() {
        validationChangeListenerList.forEach { it.onBecomeInvalid(this) }
        if(hasBorder) borderColor = invalidBorderColor
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (hasBorder) {
            canvas?.drawRoundRect(
                0f + borderWidth!!,
                0f + borderWidth!!,
                width.toFloat() - borderWidth!!,
                height.toFloat() - borderWidth!!,
                cornerRadius!!,
                cornerRadius!!,
                paint!!
            )
        }
    }
}