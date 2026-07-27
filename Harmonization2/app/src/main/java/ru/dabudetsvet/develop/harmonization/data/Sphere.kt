package ru.dabudetsvet.develop.harmonization.data

/**
 * The 8 spheres of the "wheel of harmony" life-balance exercise.
 */
enum class Sphere(val title: String) {
    HEALTH("Здоровье"),
    FAMILY("Семья"),
    SOCIETY("Социум"),
    BUSINESS("Бизнес и работа"),
    MONEY("Деньги"),
    LEARNING("Обучение"),
    SPIRITUALITY("Духовность"),
    LIFE_BRIGHTNESS("Яркость жизни");

    companion object {
        val ORDERED: List<Sphere> = values().toList()
    }
}
