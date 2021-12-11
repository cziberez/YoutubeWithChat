package hu.unideb.youtube.database.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import hu.unideb.youtube.database.model.YoutubeModelEntity
import hu.unideb.youtube.database.viewmodel.YoutubeViewModel
import io.getstream.videochat.R
import io.getstream.videochat.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var mYoutubeViewModel: YoutubeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        mYoutubeViewModel = ViewModelProvider(this)[YoutubeViewModel::class.java]

        binding.addBtn.setOnClickListener {
            insertDataToDatabase()
        }

        return binding.root
    }

    private fun insertDataToDatabase() {
        val youtubeVideoId = binding.addYtVideoId.text.toString()

        if (inputCheck(youtubeVideoId)) {
            val youtubeModelEntity = YoutubeModelEntity(youtubeVideoId)
            mYoutubeViewModel.insertYoutubeData(youtubeModelEntity)
            Toast.makeText(requireContext(), R.string.videoIdAdded, Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.add_fragment_list_fragment)
        } else {
            Toast.makeText(requireContext(), R.string.pleaseInputId, Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun inputCheck(youtubeId: String): Boolean {
        return !(TextUtils.isEmpty(youtubeId))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}