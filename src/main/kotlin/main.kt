class Note(
    val noteId: Int,
    val title: String,
    val text: String,
    val privacy: Boolean,
    val commentPrivacy: Boolean,
    var comment: MutableList<Comment> = mutableListOf()
) {
    override fun toString(): String {
        return "noteId: $noteId, title: $title, text: $text, privacy: $privacy, commentPrivacy: $commentPrivacy, comment: $comment\n"
    }
}

class Comment(
    val commentId: Int,
    var message: String,
    var delete: Boolean = false
) {
    override fun toString(): String {
        return "$message, "
    }
}

class YouCantCommentThisNote() : RuntimeException()
class AccessToNoteDenied() : RuntimeException()

object NoteService{
    val notes : MutableList<Note> = mutableListOf()

    fun add(note: Note): Int {
        notes.add(note)
        return note.noteId
    }

    fun createComment(comment: Comment, noteId: Int): Int{
        for ((index, note) in notes.withIndex()) {
            if (note.noteId == noteId) {
                if (note.privacy == true) {
                    if (note.commentPrivacy) {
                        notes[index].comment.add(comment)
                        break
                    } else {
                        throw YouCantCommentThisNote()
                    }
                } else {
                    throw AccessToNoteDenied()
                }
            }
        }
        return comment.commentId
    }

    fun delete(noteId: Int): Int{
        for((index, note) in notes.withIndex()){
            if (note.noteId == noteId){
                if(note.privacy) {
                    notes.remove(notes[index])
                    break
                } else { throw AccessToNoteDenied() }
            }
        }
        return 1
    }

    fun deleteComment(idComment: Int, noteId: Int): Int {
        for ((index, note) in notes.withIndex()) {
            if (note.noteId == noteId) {
                if (note.privacy) {
                    for ((indexComment, comment) in note.comment.withIndex()){
                        if (comment.commentId == idComment && !comment.delete){
                            notes[index].comment[indexComment].delete = true
                        }
                    }
                } else { throw AccessToNoteDenied() }
                break
            }
        }
        return 1
    }

    fun edit(note: Note, noteId: Int): Int {
        for ((index, noteID) in notes.withIndex()) {
            if (noteID.noteId == noteId) {
                if (noteID.privacy) {
                    notes[index] = note
                } else {
                    throw AccessToNoteDenied()
                }
            }
        }
        return 1
    }

    fun editComment(message: String, idComment: Int, noteId: Int): Int {
        for ((index, note) in notes.withIndex()) {
            if (note.noteId == noteId) {
                if (note.privacy) {
                    for ((indexComment, comment) in note.comment.withIndex()){
                        if (comment.commentId == idComment && !comment.delete){
                            notes[index].comment[indexComment].message = message
                        }
                    }
                } else { throw AccessToNoteDenied() }
                break
            }
        }
        return 1
    }

    fun get(): MutableList<Note> {
        var noteList: MutableList<Note> = mutableListOf()
        var comment: MutableList<Comment> = mutableListOf()
        var note : Note
        for (noteID in notes) {
            //comment.clear()
            for (commentId in noteID.comment) {
                if (!commentId.delete) {
                    comment.add(commentId)
                }
            }
            note = Note(noteID.noteId, noteID.title, noteID.text, noteID.privacy, noteID.commentPrivacy, comment)
            noteList.add(note)
            comment.clear() // можете подсказать, как реализовать выведение списка без удаленных комментариев
        }
        return noteList
    }

    fun getById(noteId: Int): Note {
        var note = Note(0, "", "", true, true)
        var comment: MutableList<Comment> = mutableListOf()
        for (noteID in notes){
            if (noteID.noteId == noteId){
                for (commentId in noteID.comment) {
                    if (!commentId.delete) {
                        comment.add(commentId)
                    }
                }
                note = Note(noteID.noteId, noteID.title, noteID.text, noteID.privacy, noteID.commentPrivacy, comment)
            }
        }
        return note
    }

    fun getComments(noteId: Int): MutableList<Comment> {
        var comment: MutableList<Comment> = mutableListOf()
        for ((index, note) in notes.withIndex()) {
            if (note.noteId == noteId) {
                for (commentId in notes[index].comment) {
                    if (!commentId.delete) {
                        comment.add(commentId)
                    }
                }
            }
        }
        return comment
    }


    fun restoreComment(noteId: Int, commentId: Int): Int {
        for ((index, note) in notes.withIndex()) {
            if (note.noteId == noteId) {
                if (note.privacy) {
                    for ((indexComment, comment) in note.comment.withIndex()){
                        if (comment.commentId == commentId && comment.delete){
                            notes[index].comment[indexComment].delete = false
                        }
                    }
                } else { throw AccessToNoteDenied() }
            }
        }
        return 1
    }

    fun clear(){
        notes.clear()
    }
}


fun main(){
    val note1 = Note(0, "HomeWork", "Hello", true, true)
    val note2 = Note(1, "HomeWork", "Hello", true, true)
    NoteService.add(note1)
    NoteService.add(note2)
    val comment1 = Comment(0, "Like")
    val comment2 = Comment(1, "Hello")
    val comment3 = Comment(2, "WOW")
    NoteService.createComment(comment1, 0)
    NoteService.createComment(comment2, 0)
    NoteService.createComment(comment3, 1)
    NoteService.deleteComment(0, 0)
    println(NoteService.get())
}