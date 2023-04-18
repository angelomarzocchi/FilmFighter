package com.example.filmfighter.model



data class Question(val question: String, val answers: List<String>, val rightAnswer: Int)


object QuestionsList{

         val value: List<Question> = listOf(
        Question("What is the name of the main character in Titanic ?",listOf("Jack","Mario","Raphael","Christian"),0),
        Question("Who is the director of The Prestige ?",listOf("Burton","Nolan","Tarantino","Vanzina"),1),
        Question("Who is the keeper of the keys in Harry Potter ?",listOf("Hagrid","Hermione","Filch","Silente"),0),
        Question("In which movie is Jack Sparrow featured?",listOf("Titanic","Batman","Pirates of the Caribbean","I, Robot"),2),
        Question("Which movie features a cyborg as the main character?",listOf("Terminator","Matrix","Blade Runner","Inception"),0),
        Question("In which movie does Tom Hanks play a castaway?",listOf("The Green Mile","Forrest Gump","The Da Vinci Code","Cast Away"),3),
        Question("Which movie stars a former policeman who tries to rescue his kidnapped daughter?",listOf("Taken","John Wick","The Exorcist","Jaws"),0)

        )




}

