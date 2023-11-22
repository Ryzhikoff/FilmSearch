package evgeniy.ryzhikov.filmsearch.view.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import evgeniy.ryzhikov.filmsearch.databinding.MergePromoBinding
import evgeniy.ryzhikov.remote_module.entity.ApiConstants
import evgeniy.ryzhikov.remote_module.entity.ApiConstants.SIZE_IMAGE_PROMO_POSTER

class PromoView(context: Context, attributeSet: AttributeSet? = null) : FrameLayout(context, attributeSet) {

    val binding = MergePromoBinding.inflate(LayoutInflater.from(context), this)
    val watchButton = binding.watchButton

    fun setLinkForPoster(link: String) {
        Glide.with(binding.root)
            .load(ApiConstants.IMAGE_URL + SIZE_IMAGE_PROMO_POSTER + link)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(ROUNDED_CORNERS)))
            .into(binding.poster)
    }

    companion object {
        const val ROUNDED_CORNERS = 55
    }
}