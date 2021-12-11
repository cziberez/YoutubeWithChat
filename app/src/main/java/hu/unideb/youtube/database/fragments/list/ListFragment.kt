package hu.unideb.youtube.database.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.unideb.youtube.database.viewmodel.YoutubeViewModel
import io.getstream.videochat.R
import io.getstream.videochat.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var mUserYoutubeViewModel: YoutubeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        val adapter = ListAdapter()
        val recyclerView = binding.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mUserYoutubeViewModel = ViewModelProvider(this).get(YoutubeViewModel::class.java)
        mUserYoutubeViewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->
            adapter.setData(user)
        })

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.list_fragment_add_fragment)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteAllYoutubeVideoId()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllYoutubeVideoId() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.yes) { _, _ ->
            mUserYoutubeViewModel.deleteAllYoutubeData()
            Toast.makeText(
                requireContext(),
                R.string.deleted,
                Toast.LENGTH_SHORT
            )
                .show()
        }
        builder.setNegativeButton(R.string.no) { _, _ -> }
        builder.setTitle(R.string.deleteAllQuestion)
        builder.setMessage(R.string.areyousuredeleteAll)
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}