package com.nicoapps.cooktime.debug

import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.model.Instruction
import com.nicoapps.cooktime.model.Recipe

/**
 * Recipes for manual testing. Between them they cover a starred and an unstarred recipe, a
 * long name, fractional quantities, ingredients without a unit and an instruction list long
 * enough to scroll.
 */
object SampleRecipes {
    val all = listOf(
        Recipe(
            name = "Arroz doce",
            isStarred = true,
            ingredients = listOf(
                ingredient("Arroz", 1f, "xícara"),
                ingredient("Leite", 1f, "litro"),
                ingredient("Leite condensado", 1f, "lata"),
                ingredient("Canela em pó", 0.5f, "colher de chá")
            ),
            instructions = listOf(
                instruction("Cozinhe o arroz em 2 xícaras de água até secar."),
                instruction("Junte o leite e cozinhe em fogo baixo, mexendo sempre, por 20 minutos."),
                instruction("Acrescente o leite condensado e cozinhe até engrossar."),
                instruction("Sirva polvilhado com canela.")
            )
        ),
        Recipe(
            name = "Bolo de cenoura com cobertura de chocolate",
            isStarred = false,
            ingredients = listOf(
                ingredient("Cenoura", 3f, null),
                ingredient("Ovo", 4f, null),
                ingredient("Óleo", 1f, "xícara"),
                ingredient("Açúcar", 2f, "xícaras"),
                ingredient("Farinha de trigo", 2.5f, "xícaras"),
                ingredient("Fermento em pó", 1f, "colher de sopa"),
                ingredient("Chocolate em pó", 0.75f, "xícara"),
                ingredient("Manteiga", 1f, "colher de sopa")
            ),
            instructions = listOf(
                instruction("Preaqueça o forno a 180 °C e unte uma forma."),
                instruction("Bata no liquidificador a cenoura, os ovos e o óleo."),
                instruction("Despeje numa tigela e misture o açúcar."),
                instruction("Acrescente a farinha aos poucos, mexendo até ficar homogêneo."),
                instruction("Misture o fermento delicadamente."),
                instruction("Asse por cerca de 40 minutos, até o palito sair limpo."),
                instruction("Para a cobertura, leve ao fogo o chocolate, a manteiga e 3 colheres de açúcar."),
                instruction("Mexa até ferver e engrossar levemente."),
                instruction("Espalhe a cobertura sobre o bolo ainda quente.")
            )
        ),
        Recipe(
            name = "Omelete",
            isStarred = false,
            ingredients = listOf(
                ingredient("Ovo", 2f, null),
                ingredient("Sal", 1f, "pitada"),
                ingredient("Queijo ralado", 2f, "colheres de sopa")
            ),
            instructions = listOf(
                instruction("Bata os ovos com o sal."),
                instruction("Despeje numa frigideira untada em fogo médio."),
                instruction("Espalhe o queijo, dobre ao meio e sirva.")
            )
        )
    )

    private fun ingredient(name: String, quantity: Float, unit: String?) =
        Ingredient(name = name, quantity = quantity, measurementUnit = unit)

    private fun instruction(text: String) = Instruction(text = text)
}
