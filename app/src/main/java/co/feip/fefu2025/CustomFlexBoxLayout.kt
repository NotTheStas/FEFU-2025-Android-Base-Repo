package co.feip.fefu2025

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class CustomFlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val hGap = 8
    private val vGap = 8

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var lineWidth = 0
        var totalHeight = 0
        var currentLineHeight = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)

            if (lineWidth + child.measuredWidth > widthSize) {
                totalHeight += currentLineHeight + vGap
                lineWidth = 0
                currentLineHeight = 0
            }

            lineWidth += child.measuredWidth + hGap
            currentLineHeight = maxOf(currentLineHeight, child.measuredHeight)
        }

        totalHeight += currentLineHeight
        setMeasuredDimension(widthSize, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var x = 0
        var y = 0
        var currentLineHeight = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            if (x + childWidth > measuredWidth) {
                x = 0
                y += currentLineHeight + vGap
                currentLineHeight = 0
            }

            child.layout(x, y, x + childWidth, y + childHeight)
            x += childWidth + hGap
            currentLineHeight = maxOf(currentLineHeight, childHeight)
        }
    }
}