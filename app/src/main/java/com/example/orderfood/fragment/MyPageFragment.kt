package com.example.orderfood.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orderfood.R
import com.example.orderfood.Utils.Utils
import com.example.orderfood.activity.ChangePasswordActivity
import com.example.orderfood.activity.FavoriteActivity
import com.example.orderfood.activity.LoginActivity
import com.example.orderfood.adapter.RvItemClick
import com.example.orderfood.adapter.RvListActivityMyPage
import com.example.orderfood.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("DEPRECATION", "UNUSED_EXPRESSION")
class MyPageFragment : Fragment(R.layout.fragment_my_page) {
    private lateinit var imgProfile: CircleImageView
    private lateinit var txtEmail: TextView
    private lateinit var recyclerViewListActivityMyPage: RecyclerView
    private lateinit var btnLogOut: Button
    private lateinit var mAuth: FirebaseAuth

    private lateinit var progressBar: ProgressBar
    private val listActivity: MutableList<String> =
        mutableListOf("Change Password", "My Favorite", "Contact")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        mAuth = FirebaseAuth.getInstance()
        btnLogOutClickEvent()

        val adapter = RvListActivityMyPage(listActivity,object : RvItemClick {
            override fun onItemClick(position: Int) {
                when(position){
                    //example
                    0 -> startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
                    1 -> startActivity(Intent(requireContext(),FavoriteActivity::class.java))
                    //2 -> startActivity(Intent(requireContext(),LoginActivity::class.java))
                    else -> true
                }
            }
        })
        recyclerViewListActivityMyPage.adapter = adapter
    }

    private fun btnLogOutClickEvent() {
        btnLogOut.setOnClickListener {
            mAuth.signOut()

            //refesh Utils current_User , listCart , messageList
            progressBar.visibility = View.VISIBLE
            Utils.current_User = UserModel("", "", "")
            Utils.listCart.clear()
            Utils.messageList.clear()
            Handler().postDelayed({
                progressBar.visibility = View.GONE
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }, 1000)


        }
    }

    private fun initView(view: View) {
        imgProfile = view.findViewById(R.id.imgProfile)
        progressBar = view.findViewById(R.id.progress_bar)
        txtEmail = view.findViewById(R.id.txtEmail)
        txtEmail.text = Utils.current_User.email
        recyclerViewListActivityMyPage = view.findViewById(R.id.recyclerViewListActivityMyPage)
        recyclerViewListActivityMyPage.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        btnLogOut = view.findViewById(R.id.btnLogOut)

    }
}