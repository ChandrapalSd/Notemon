package com.github.chandrapalsd.notemon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.github.chandrapalsd.notemon.databinding.NoteListItemBinding
import com.github.chandrapalsd.notemon.models.Note
import java.text.SimpleDateFormat
import java.util.Locale

class NotesAdapter(private val clickListener: NoteClickListener) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val notesToShow = ArrayList<Note>()
    private val allNotes = ArrayList<Note>()

    fun updateList(newList: List<Note>) {
        allNotes.clear()
        allNotes.addAll(newList)

        notesToShow.clear()
        notesToShow.addAll(allNotes)

        notifyDataSetChanged()
    }

    fun filterList(search: String) {
        notesToShow.clear()

        notesToShow.addAll( allNotes.filter { it.contains(search) } )
        notifyDataSetChanged()
    }

    private fun Note.contains(search: String): Boolean {
        return title.lowercase().contains(search.lowercase()) || note.lowercase().contains(search.lowercase())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding =
            NoteListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notesToShow.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notesToShow[position])

    }

    inner class NoteViewHolder(private val binding: NoteListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.tvTitle.text = note.title
            binding.tvNote.text = note.note
            binding.tvDate.text = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(note.date)

            binding.root.setOnClickListener {
                clickListener.onClick(note)
            }

            binding.root.setOnLongClickListener {
                clickListener.onLongClick(note, binding.root)
                true
            }
        }
    }


    interface NoteClickListener {
        fun onClick(note: Note)
        fun onLongClick(note: Note, cardView: CardView)
    }
}

