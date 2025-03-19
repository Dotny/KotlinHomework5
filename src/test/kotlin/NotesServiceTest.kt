import org.junit.Assert.*
import org.junit.Before
import kotlin.test.Test

class NotesServiceTest{
    @Before
    fun clearBeforeTest() {
        NoteService.clear()
    }

    @Test
     fun add(){
         val note1 = Note(0, "HomeWork", "Hello", true, true)
         assertEquals(NoteService.add(note1), 0)
     }

     @Test
     fun createCommentNotError(){
         val note1 = Note(0, "HomeWork", "Hello", true, true)
         NoteService.add(note1)
         val comment1 = Comment(0, "Like")
         assertEquals(NoteService.createComment(comment1, 0), 0)
     }

    @Test(expected = AccessToNoteDenied::class)
    fun createCommentAccessToNoteDenided(){
        val note1 = Note(0, "HomeWork", "Hello", false, true)
        NoteService.add(note1)
        val comment1 = Comment(0, "Like")
        NoteService.createComment(comment1, 0)
    }

    @Test(expected = YouCantCommentThisNote::class)
    fun createCommentYouCantCommentThisNote(){
        val note1 = Note(0, "HomeWork", "Hello", true, false)
        NoteService.add(note1)
        val comment1 = Comment(0, "Like")
        NoteService.createComment(comment1, 0)
    }

    @Test
    fun deleteNotError(){
        val note1 = Note(0, "HomeWork", "Hello", true, false)
        NoteService.add(note1)
        assertEquals(NoteService.delete(0), 1)
    }

    @Test(expected = AccessToNoteDenied::class)
    fun deleteAccessToNoteDenided(){
        val note1 = Note(0, "HomeWork", "Hello", false, true)
        NoteService.add(note1)
        NoteService.delete(0)
    }

    @Test
    fun deleteCommentNotError(){
        val note1 = Note(0, "HomeWork", "Hello", true, true)
        NoteService.add(note1)
        val comment1 = Comment(0, "Like")
        assertEquals(NoteService.deleteComment(0, 0), 1)
    }

    @Test(expected = AccessToNoteDenied::class)
    fun deleteCommentAccessToNoteDenided(){
        val comment = Comment(123, "Kotlin")
        val note1 = Note(0, "HomeWork", "Hello", false, true)
        NoteService.add(note1)
        NoteService.createComment(comment, 0)
        NoteService.deleteComment(123, 0)
    }

    @Test
    fun editNotError() {
        val note1 = Note(0, "HomeWork", "Hello", true, false)
        val note2 = Note(1, "HomeWork", "Hello", false, false)
        NoteService.add(note1)
        assertEquals(NoteService.edit(note2, 0), 1)
    }

    @Test(expected = AccessToNoteDenied::class)
    fun editAccessToNoteDenided(){
        val note1 = Note(0, "HomeWork", "Hello", false, false)
        val note2 = Note(1, "HomeWork", "Hello", false, false)
        NoteService.add(note1)
        NoteService.edit(note2, 0)
    }

    @Test
    fun editCommentNotError(){
        val note1 = Note(0, "HomeWork", "Hello", true, true)
        NoteService.add(note1)
        val comment1 = Comment(0, "Like")
        NoteService.createComment(comment1, 0)
        assertEquals(NoteService.editComment("Hello", 0, 0), 1)
    }

    @Test(expected = AccessToNoteDenied::class)
    fun editCommentAccessToNoteDenied(){
        val note1 = Note(0, "HomeWork", "Hello", false, true)
        NoteService.add(note1)
        val comment1 = Comment(0, "Like")
        NoteService.createComment(comment1, 0)
        NoteService.editComment("Hello", 0, 0)
    }

    @Test
    fun restoreCommentNotError(){
        val note1 = Note(0, "HomeWork", "Hello", true, true)
        NoteService.add(note1)
        val comment1 = Comment(0, "Like")
        NoteService.deleteComment(0, 0)
        assertEquals(NoteService.restoreComment(0, 0), 1)
    }

    @Test(expected = AccessToNoteDenied::class)
    fun restoreCommentAccessToNoteDenied(){
        val note1 = Note(0, "HomeWork", "Hello", false, true)
        NoteService.add(note1)
        val comment1 = Comment(0, "Like")
        NoteService.deleteComment(0, 0)
        NoteService.restoreComment(0, 0)
    }
}