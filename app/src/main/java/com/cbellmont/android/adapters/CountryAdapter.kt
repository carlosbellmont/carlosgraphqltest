package com.cbellmont.android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cbellmont.android.graphqltest.R
import com.cbellmont.android.storage.Country
import kotlinx.android.synthetic.main.item_country.view.*


class CountryAdapter(
    private var items: Array<Country>,
    private val mListener: OnItemClickListener? = null) : RecyclerView.Adapter<CountryAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Country?, position: Int)
    }

    fun setData(forecastList: Array<Country>) {
        items = forecastList
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.setValue(item)
        holder.view.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                mListener?.onItemClick(item, position)
            }
        }
    }

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        private var mTextName: TextView = view.tw_title

        fun setValue(item: Country) {
            mTextName.text = item.name
        }
    }
}