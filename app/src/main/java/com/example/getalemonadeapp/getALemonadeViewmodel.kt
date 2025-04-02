package com.example.getalemonadeapp

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class LemonadeStates(
    @DrawableRes val imageResource: Int,
    @StringRes val descriptionResource: Int,
    @StringRes val textResource: Int
) {
    TREE(
        imageResource = R.drawable.lemon_tree,
        descriptionResource = R.string.lemon_tree,
        textResource = R.string.tree_image_text
    ),
    LEMON(
        imageResource = R.drawable.lemon_squeeze,
        descriptionResource = R.string.lemon,
        textResource = R.string.lemon_image_text
    ),
    LEMONADE(
        imageResource = R.drawable.lemon_drink,
        descriptionResource = R.string.glass_of_lemonade,
        textResource = R.string.lemonade_image_text
    ),
    GLASS(
        imageResource = R.drawable.lemon_restart,
        descriptionResource = R.string.empty_glass,
        textResource = R.string.empty_glass_image_text
    )
}