package com.jc.promise_keeper.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jc.promise_keeper.R
import com.jc.promise_keeper.data.model.datas.User

class MyFriendRecyclerAdapter(
    val mContext: Context,
    val mList: List<User>
): RecyclerView.Adapter<MyFriendRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        private val txtNickname = view.findViewById<TextView>(R.id.txtNickname)
        private val imgSocialLoginLogo = view.findViewById<ImageView>(R.id.imgSocialLoginLogo)
        private val txtEmail = view.findViewById<TextView>(R.id.txtEmail)

        fun bind(data: User) {

            Glide.with(mContext)
                .load(data.profileImg)
                .into(imgProfile)

            txtNickname.text = data.nickName

            when (data.provider) {
                "default" -> {
                    imgSocialLoginLogo.visibility = View.GONE
                    txtEmail.text = data.email
                }
                "kakao" -> {
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    txtEmail.text = "카카오 로그인"
                    setSocialGlide(R.drawable.kakao, imgSocialLoginLogo)

                }
                "facebook" -> {
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    txtEmail.text = "페이스북 로그인"
                    setSocialGlide(R.drawable.facebook, imgSocialLoginLogo)
                }
                "naver" -> {
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    txtEmail.text = "네이버 로그인"
                    setSocialGlide(R.drawable.naver, imgSocialLoginLogo)
                }
                else -> {
                    setSocialGlide(R.drawable.no_entry, imgSocialLoginLogo)
                }
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.my_friend_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = mList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = mList.size


    private fun setSocialGlide(drawableId: Int, view: ImageView) {

        Glide.with(mContext)
            .load(drawableId)
            .into(view)
    }

}