package me.ikvarxt.binderdemo

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.ikvarxt.binderdemo.databinding.FragmentFirstBinding
import kotlin.random.Random

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookService: IBookManager

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            bookService = IBookManager.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonFirst.setOnClickListener {
                if (::bookService.isInitialized) {
                    val random = Random.nextInt()
                    val book = Book(random, "#$random-book")
                    bookService.addBook(book)
                    textviewFirst.text = bookService.getBookList()?.joinToString("\n")
                }
            }
            buttonGetIndex.setOnClickListener {
                val list = bookService.getBookList() ?: emptyList()
                if (list.size >= 2) {
                    val random = Random.nextInt(list.lastIndex)
                    textviewFirst.text = bookService.getBookAt(random).toString()
                } else {
                    val text = "empty list"
                    textviewFirst.text = text
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        with(requireContext()) {
            Intent(this, BookService::class.java).also {
                bindService(it, connection, Activity.BIND_AUTO_CREATE)
            }
        }
    }

    override fun onStop() {
        super.onStop()

        requireContext().unbindService(connection)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "FirstFragment"
    }
}