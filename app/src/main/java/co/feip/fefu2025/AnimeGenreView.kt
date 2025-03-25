package co.feip.fefu2025

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import co.feip.fefu2025.databinding.ViewAnimeGenreBinding
import android.graphics.drawable.GradientDrawable

class AnimeGenreView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewAnimeGenreBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewAnimeGenreBinding.inflate(inflater, this, true)
    }

    fun setGenreName(name: String) {
        binding.genreName.text = name
    }

    override fun setBackgroundColor(color: Int) {
        val background = GradientDrawable().apply {
            setColor(color)
            this.cornerRadius = 92f
        }
        binding.genreBackground.background = background
    }
}