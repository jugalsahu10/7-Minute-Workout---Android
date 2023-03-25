package com.android.a7minuteworkout

object Constants {

    private val exerciseList: ArrayList<Pair<String, Int>> = arrayListOf(
        Pair("Jumping Jacks", R.drawable.ic_jumping_jacks),
        Pair("High Knees Running in Place", R.drawable.ic_high_knees_running_in_place),
        Pair("Abdominal Crunch", R.drawable.ic_abdominal_crunch),
        Pair("Lunge", R.drawable.ic_lunge),
        Pair("Plank", R.drawable.ic_plank),
        Pair("Push Up", R.drawable.ic_push_up),
        Pair("Push Up and Rotation", R.drawable.ic_push_up_and_rotation),
        Pair("Side Plank", R.drawable.ic_side_plank),
        Pair("Squat", R.drawable.ic_squat),
        Pair("Jumping Jacks", R.drawable.ic_step_up_onto_chair),
        Pair("Triceps Dip on Chair", R.drawable.ic_triceps_dip_on_chair),
    )

    fun defaultExerciseList(): List<ExerciseModel> {
        var id = 1
        val exerciseModelList = exerciseList.map { exercise ->
            ExerciseModel(
                id++,
                exercise.first,
                exercise.second,
                false,
                false
            )
        }.toList()
        return exerciseModelList.subList(0, exerciseModelList.size)
    }
}