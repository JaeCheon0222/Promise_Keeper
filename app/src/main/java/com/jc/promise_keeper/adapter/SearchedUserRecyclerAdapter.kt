package com.jc.promise_keeper.adapter

import android.content.Context
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
import retrofit2.*

class SearchedUserRecyclerAdapter(
    val mContext: Context,
    val mList: List<User>
) : RecyclerView.Adapter<SearchedUserRecyclerAdapter.MyViewHolder>() {

    private val scope = MainScope()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val txtNickname = view.findViewById<TextView>(R.id.txtNickname)
        val imgSocialLoginLogo = view.findViewById<ImageView>(R.id.imgSocialLoginLogo)
        val txtEmail = view.findViewById<TextView>(R.id.txtEmail)
        val btnAddFriend = view.findViewById<Button>(R.id.btnAddFriend)

        // 실제 데이터 반영 기능이 있는 함수
        fun bind(data: User) {
            txtNickname.text = data.nickName

            Glide.with(mContext)
                .load(data.profileImg)
                .into(imgProfile)

            when (data.provider) {
                "default" -> {
                    imgSocialLoginLogo.visibility = View.GONE
                    txtEmail.text = data.email
                }
                "kakao" -> {
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    imgSocialLoginLogo.setImageResource(R.drawable.kakao)
                    txtEmail.text = "카카오 로그인"
                }
                "facebook" -> {
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    imgSocialLoginLogo.setImageResource(R.drawable.facebook)
                    txtEmail.text = "페이스북 로그인"
                }
                "naver" -> {
                    imgSocialLoginLogo.visibility = View.VISIBLE
                    imgSocialLoginLogo.setImageResource(R.drawable.naver)
                    txtEmail.text = "네이버 로그인"
                }
            }

            // 친구 추가 버튼이 눌리면 할 일 => 친구 추가 요청 API 호출
            // 어댑터에서 => API 호출 => 레트로핏 객체 직접 생성해서 호출
            btnAddFriend.setOnClickListener {
                postRequestAddFriend(data.id!!)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        // xml 을 inflate 해와서 => 이를 가지고, MyViewHolder 객체로 성생. 리턴
        // 재사용성을 알아서 보존해준다.
        val row =
            LayoutInflater.from(mContext).inflate(R.layout.searched_user_list_item, parent, false)
        return MyViewHolder(row)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // 실제 데이터 반영 함수
        val data = mList[position]

        // 이 함수에서 직접 코딩하면 => holder.UI 변수로 매번 holder 단어를 사용해야한다.
        // holder 변수의 멤버 변수들을 사용할 수 있는 것처럼, 함수도 사용할 수 있다.
        holder.bind(data)
    }

    // 몇 개의 아이템을 보여줄 것인지 => 데이터 목록의 갯 수 만큼 보여준다.
    override fun getItemCount(): Int = mList.size


    private fun postRequestAddFriend(userId: Int) = scope.launch {

        val result = FriendRepository.postRequestAddFriend(userId)

        if (result.isSuccessful) {
            Toast.makeText(mContext, "친구 요청을 보냈습니다.", Toast.LENGTH_SHORT).show()
        }


    }

}