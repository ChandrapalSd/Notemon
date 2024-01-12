package com.github.chandrapalsd.notemon

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.chandrapalsd.notemon.adapters.NotesAdapter
import com.github.chandrapalsd.notemon.databinding.ActivityMainBinding
import com.github.chandrapalsd.notemon.models.Note
import com.github.chandrapalsd.notemon.utils.getParcelableNoteExtra
import com.github.chandrapalsd.notemon.viewmodels.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: NoteViewModel by viewModels()

    lateinit var adapter: NotesAdapter

    private val createNoteLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == Activity.RESULT_OK) {
                val note = result.data?.getParcelableNoteExtra("note")
                note?.let {
                    viewModel.insertNote(note)
                    Snackbar.make(
                        binding.root, "Note Inserted", Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    private val updateNoteLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == Activity.RESULT_OK) {
                val note = result.data?.getParcelableNoteExtra("note")
                note?.let {
                    viewModel.updateNote(note)
                    Snackbar.make(
                        binding.root, "Note Updated", Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

        viewModel.allNotes.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }
        }
    }

    // Clear focus on touch outside SearchView
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun initUi() {

        adapter = NotesAdapter(object : NotesAdapter.NoteClickListener {
            override fun onClick(note: Note) {
                val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
                intent.putExtra("currentNote", note)
                updateNoteLauncher.launch(intent)
            }

            override fun onLongClick(note: Note, cardView: CardView) {
                popupDisplay(note, cardView)
            }

        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)


        binding.recyclerView.adapter = adapter

        binding.btnAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            createNoteLauncher.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    adapter.filterList(newText)
                }
                return true
            }

        })
    }


    private fun popupDisplay(selectedNote: Note, cardView: CardView) {
        val wrapper = ContextThemeWrapper(this, R.style.MyPopupMenuStyle)
        val popup = PopupMenu(wrapper, cardView)
        popup.setOnMenuItemClickListener { item ->
            if (item?.itemId == R.id.delete_note) {
                viewModel.deleteNote(selectedNote)
                Snackbar.make(binding.root, "Note Deleted", Snackbar.LENGTH_SHORT).show()
                true
            } else false
        }

        popup.inflate(R.menu.pop_up_delete_menu)
        popup.show()
    }
}