package com.github.chandrapalsd.notemon

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.chandrapalsd.notemon.databinding.ActivityAddNoteBinding
import com.github.chandrapalsd.notemon.databinding.ActivityMainBinding
import com.github.chandrapalsd.notemon.models.Note
import com.github.chandrapalsd.notemon.utils.getParcelableNoteExtra
import java.util.Date

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding

    private var note: Note = Note(0, "", "", Date())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getParcelableNoteExtra("currentNote")?.let {
            note = it
            binding.etTitle.setText(it.title)
            binding.etNote.setText(it.note)
        }

        binding.btnBack.setOnClickListener{
            finish()
        }

        binding.btnCheck.setOnClickListener{
            note.title = binding.etTitle.text.toString()
            note.note = binding.etNote.text.toString()
            note.date = Date()

            val intent = Intent()
            intent.putExtra("note", note)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}