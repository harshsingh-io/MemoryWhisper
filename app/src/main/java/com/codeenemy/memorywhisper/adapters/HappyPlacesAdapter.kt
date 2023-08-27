package com.codeenemy.memorywhisper.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import com.codeenemy.memorywhisper.R
import com.codeenemy.memorywhisper.activities.AddHappyPlaceActivity
import com.codeenemy.memorywhisper.activities.MainActivity
import com.codeenemy.memorywhisper.database.DatabaseHandler
import com.codeenemy.memorywhisper.models.HappyPlaceModel
import kotlinx.coroutines.NonDisposableHandle.parent
import com.codeenemy.memorywhisper.databinding.ItemHappyPlaceBinding

open class HappyPlacesAdapter(
    private val context: Context, private var list: ArrayList<HappyPlaceModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null

    class ViewHolder(binding: ItemHappyPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivPlaceImage = binding.ivPlaceImage
        val tvTitle = binding.tvTitle
        val tvDescription = binding.tvDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemHappyPlaceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
//TODO can be do something wrong
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is ViewHolder) {
            holder.ivPlaceImage.setImageURI(Uri.parse(model.image))
            holder.tvTitle.text = model.title
            holder.tvDescription.text = model.description
            holder.itemView.setOnClickListener {
                if (onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface OnClickListener {
        fun onClick(position: Int, model: HappyPlaceModel)
    }
    fun removeAt(position: Int){
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteHappyPlace(list[position])
        if (isDeleted>0) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS,list[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//    }
//    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}