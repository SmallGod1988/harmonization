package ru.dabudetsvet.develop.harmonization

import org.junit.Assert.assertTrue
import org.junit.Test
import ru.dabudetsvet.develop.harmonization.data.GoalTemplates
import ru.dabudetsvet.develop.harmonization.data.Sphere

class GoalTemplatesTest {

    @Test
    fun everySphereHasGoalTemplates() {
        Sphere.values().forEach { sphere ->
            assertTrue(
                "Sphere ${sphere.name} should have goal templates",
                GoalTemplates.templatesFor(sphere).isNotEmpty()
            )
        }
    }

    @Test
    fun eachSphereIncludesTheReflectionTemplate() {
        Sphere.values().forEach { sphere ->
            val templates = GoalTemplates.templatesFor(sphere)
            assertTrue(
                "Sphere ${sphere.name} should include a '3 steps' reflection goal",
                templates.any { it.title.contains(sphere.title) }
            )
        }
    }
}
