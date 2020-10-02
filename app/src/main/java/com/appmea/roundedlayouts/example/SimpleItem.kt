package com.appmea.roundedlayouts.example

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class SimpleItem(private val text: String) : AbstractFlexibleItem<SimpleItem.SimpleVH>() {

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun getLayoutRes(): Int {
        return R.layout.item_simple
    }

    override fun createViewHolder(view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?): SimpleVH {
        return SimpleVH(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?, holder: SimpleVH?, position: Int, payloads: MutableList<Any>?) {
        holder?.update(text)
    }


    class SimpleVH(view: View?, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) : FlexibleViewHolder(view, adapter) {
        var tvText: TextView? = view?.findViewById(R.id.tv_text)

        fun update(string: String) {
            tvText?.text = string
        }
    }
}