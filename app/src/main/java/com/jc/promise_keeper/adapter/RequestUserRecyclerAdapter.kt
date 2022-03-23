package com.jc.promise_keeper.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jc.promise_keeper.R
import com.jc.promise_keeper.common.api.repository.FriendRepository
import com.jc.promise_keeper.data.model.datas.User
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class RequestUserRecyclerAdapter(
    val mContext: Context,
    val mList: List<User>
): RecyclerView.Adapter<RequestUserRecyclerAdapter.MyViewHolder>() {

    private val scope = MainScope()

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val txtNickname = view.findViewById<TextView>(R.id.txtNickname)
        val imgSocialLoginLogo = view.findViewById<ImageView>(R.id.imgSocialLoginLogo)
        val txtEmail = view.findViewById<TextView>(R.id.txtEmail)
        val btnAccept = view.findViewById<Button>(R.id.btnAccept)
        val btnDeny = view.findViewById<Button>(R.id.btnDeny)

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


            // btnAccept, btnDeny 모두 같은 API 를 호출. (하는 행동이 같다.)
            //  => type 파라미터에 첨부하는 값만 다르다. ("수락"/ "거절")

            // 두 개의 버튼이 눌리면, 할 일을 하나의 변수에 담아두자 (같은 일을 하게 만든다.)
            // 할 일 : Interface => 정석 - object : 인터페이스 종류 { }
            // 축약 문법 (lambda) => 인터페이스 종류 { }
            val ocl = View.OnClickListener {
                // 서버에 수락 / 거절 의사 전달.
                // 수락 버튼 : 수락, 거절 버튼 : 거절 => 어느 버튼이 눌렸는지 파악 가능해야, 파라미터도 다르게 전달 할 수 있다.
                // it 변수 : 클릭된 버튼을 담고 있는 역할.
                // tag 속성 : 아무 말이나 적어도 되는 일종의 메모. 수락 / 거절 등 보내야할 값을 메모해두자

                val tagStr = it.tag.toString()
                Log.d("보낼파라미터값", tagStr)

                putRequestAcceptOrDenyFriend(data.id!!, tagStr)

            }

            btnAccept.setOnClickListener(ocl)
            btnDeny.setOnClickListener(ocl)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.requested_user_list_item, parent, false)
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

    private fun putRequestAcceptOrDenyFriend(userId: Int, type: String) = scope.launch {

        val result = FriendRepository.putRequestAcceptOrDenyFriend(userId, type)

        if (result.isSuccessful) {
            Toast.makeText(mContext, "성공", Toast.LENGTH_SHORT).show()
        }

    }


}