package com.github.chandrapalsd.notemon.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.chandrapalsd.notemon.database.NoteRepository
import com.github.chandrapalsd.notemon.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val repository:NoteRepository): ViewModel() {

    val allNotes:LiveData<List<Note>> = repository.allNotes

    fun insertNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(note)
        }
    }
    fun updateNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(note)
        }
    }
    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(note)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository:NoteRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NoteViewModel(repository) as T
        }
    }
}