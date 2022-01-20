package com.example.authflow.presentation.loggedin_page

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.authflow.databinding.FragmentLoggedinBinding
import com.example.authflow.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log


@AndroidEntryPoint
class LoggedinFragment : Fragment() {

    val PICK_IMAGE = 1
    private var uri: Uri? = null

    private lateinit var loggedinBinding: FragmentLoggedinBinding
    val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        loggedinBinding = FragmentLoggedinBinding.inflate(inflater, container, false)
        val view = loggedinBinding.root

        loggedinBinding.loggedOut.setOnClickListener {
            mainViewModel.signOut()
        }


        loggedinBinding.uploadImgBt.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
        }



        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            uri = data?.data
            if (uri != null) {
                // loggedinBinding.pickImageImg.setImageURI(null)
                //Glide.with(requireView()).load(uri).into(loggedinBinding.pickImageImg)
                 mainViewModel.uploadImage(uri!!)

                val picture =
                    MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri)
                loggedinBinding.pickImageImg.setImageBitmap(picture)

            }

            }


        }

}