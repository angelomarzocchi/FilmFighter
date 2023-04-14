package com.example.filmfighter.model



data class Question(val question: String, val answers: List<String>, val rightAnswer: Int)


object QuestionsList{

         val value: List<Question> = listOf(
        Question("Come si chiama il protagonista di Titanic ?",listOf("Jack","Mario","Raphael","Christian"),0),
        Question("Chi é il regista di The Prestige ?",listOf("Burton","Nolan","Tarantino","Vanzina"),1),
        Question("Chi é il custode delle chiavi in Harry Potter ?",listOf("Hagrid","Hermione","Gazza","Silente"),0),
        Question("In quale film é presente Jack Sparrow?",listOf("Titanic","Batman","Pirati dei caraibi","Io Robot"),2),
        Question("Quale film ha come protagonista un cyborg?",listOf("Terminator","Matrix","Blade Runner","Inception"),0),
        Question("In quale film Tom Hanks interpreta un naufrago?",listOf("Il Miglio Verde","Forrest Gump","Il codice Da vinci","Cast Away"),3),
        Question("Quale film ha come protagonista un ex poliziotto che cerca di salvare sua figlia rapita?",listOf("Taken","John Wick","l'esorcista","Lo squalo"),0)

        )




}

