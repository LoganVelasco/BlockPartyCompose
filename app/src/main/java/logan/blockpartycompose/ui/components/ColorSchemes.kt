package logan.blockpartycompose.ui.components

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import logan.blockpartycompose.ui.theme.theme_1_dark_background
import logan.blockpartycompose.ui.theme.theme_1_dark_error
import logan.blockpartycompose.ui.theme.theme_1_dark_errorContainer
import logan.blockpartycompose.ui.theme.theme_1_dark_inverseOnSurface
import logan.blockpartycompose.ui.theme.theme_1_dark_inversePrimary
import logan.blockpartycompose.ui.theme.theme_1_dark_inverseSurface
import logan.blockpartycompose.ui.theme.theme_1_dark_onBackground
import logan.blockpartycompose.ui.theme.theme_1_dark_onError
import logan.blockpartycompose.ui.theme.theme_1_dark_onErrorContainer
import logan.blockpartycompose.ui.theme.theme_1_dark_onPrimary
import logan.blockpartycompose.ui.theme.theme_1_dark_onPrimaryContainer
import logan.blockpartycompose.ui.theme.theme_1_dark_onSecondary
import logan.blockpartycompose.ui.theme.theme_1_dark_onSecondaryContainer
import logan.blockpartycompose.ui.theme.theme_1_dark_onSurface
import logan.blockpartycompose.ui.theme.theme_1_dark_onSurfaceVariant
import logan.blockpartycompose.ui.theme.theme_1_dark_onTertiary
import logan.blockpartycompose.ui.theme.theme_1_dark_onTertiaryContainer
import logan.blockpartycompose.ui.theme.theme_1_dark_outline
import logan.blockpartycompose.ui.theme.theme_1_dark_outlineVariant
import logan.blockpartycompose.ui.theme.theme_1_dark_primary
import logan.blockpartycompose.ui.theme.theme_1_dark_primaryContainer
import logan.blockpartycompose.ui.theme.theme_1_dark_scrim
import logan.blockpartycompose.ui.theme.theme_1_dark_secondary
import logan.blockpartycompose.ui.theme.theme_1_dark_secondaryContainer
import logan.blockpartycompose.ui.theme.theme_1_dark_surface
import logan.blockpartycompose.ui.theme.theme_1_dark_surfaceTint
import logan.blockpartycompose.ui.theme.theme_1_dark_surfaceVariant
import logan.blockpartycompose.ui.theme.theme_1_dark_tertiary
import logan.blockpartycompose.ui.theme.theme_1_dark_tertiaryContainer
import logan.blockpartycompose.ui.theme.theme_1_light_background
import logan.blockpartycompose.ui.theme.theme_1_light_error
import logan.blockpartycompose.ui.theme.theme_1_light_errorContainer
import logan.blockpartycompose.ui.theme.theme_1_light_inverseOnSurface
import logan.blockpartycompose.ui.theme.theme_1_light_inversePrimary
import logan.blockpartycompose.ui.theme.theme_1_light_inverseSurface
import logan.blockpartycompose.ui.theme.theme_1_light_onBackground
import logan.blockpartycompose.ui.theme.theme_1_light_onError
import logan.blockpartycompose.ui.theme.theme_1_light_onErrorContainer
import logan.blockpartycompose.ui.theme.theme_1_light_onPrimary
import logan.blockpartycompose.ui.theme.theme_1_light_onPrimaryContainer
import logan.blockpartycompose.ui.theme.theme_1_light_onSecondary
import logan.blockpartycompose.ui.theme.theme_1_light_onSecondaryContainer
import logan.blockpartycompose.ui.theme.theme_1_light_onSurface
import logan.blockpartycompose.ui.theme.theme_1_light_onSurfaceVariant
import logan.blockpartycompose.ui.theme.theme_1_light_onTertiary
import logan.blockpartycompose.ui.theme.theme_1_light_onTertiaryContainer
import logan.blockpartycompose.ui.theme.theme_1_light_outline
import logan.blockpartycompose.ui.theme.theme_1_light_outlineVariant
import logan.blockpartycompose.ui.theme.theme_1_light_primary
import logan.blockpartycompose.ui.theme.theme_1_light_primaryContainer
import logan.blockpartycompose.ui.theme.theme_1_light_scrim
import logan.blockpartycompose.ui.theme.theme_1_light_secondary
import logan.blockpartycompose.ui.theme.theme_1_light_secondaryContainer
import logan.blockpartycompose.ui.theme.theme_1_light_surface
import logan.blockpartycompose.ui.theme.theme_1_light_surfaceTint
import logan.blockpartycompose.ui.theme.theme_1_light_surfaceVariant
import logan.blockpartycompose.ui.theme.theme_1_light_tertiary
import logan.blockpartycompose.ui.theme.theme_1_light_tertiaryContainer
import logan.blockpartycompose.ui.theme.theme_2_dark_background
import logan.blockpartycompose.ui.theme.theme_2_dark_error
import logan.blockpartycompose.ui.theme.theme_2_dark_errorContainer
import logan.blockpartycompose.ui.theme.theme_2_dark_inverseOnSurface
import logan.blockpartycompose.ui.theme.theme_2_dark_inversePrimary
import logan.blockpartycompose.ui.theme.theme_2_dark_inverseSurface
import logan.blockpartycompose.ui.theme.theme_2_dark_onBackground
import logan.blockpartycompose.ui.theme.theme_2_dark_onError
import logan.blockpartycompose.ui.theme.theme_2_dark_onErrorContainer
import logan.blockpartycompose.ui.theme.theme_2_dark_onPrimary
import logan.blockpartycompose.ui.theme.theme_2_dark_onPrimaryContainer
import logan.blockpartycompose.ui.theme.theme_2_dark_onSecondary
import logan.blockpartycompose.ui.theme.theme_2_dark_onSecondaryContainer
import logan.blockpartycompose.ui.theme.theme_2_dark_onSurface
import logan.blockpartycompose.ui.theme.theme_2_dark_onSurfaceVariant
import logan.blockpartycompose.ui.theme.theme_2_dark_onTertiary
import logan.blockpartycompose.ui.theme.theme_2_dark_onTertiaryContainer
import logan.blockpartycompose.ui.theme.theme_2_dark_outline
import logan.blockpartycompose.ui.theme.theme_2_dark_outlineVariant
import logan.blockpartycompose.ui.theme.theme_2_dark_primary
import logan.blockpartycompose.ui.theme.theme_2_dark_primaryContainer
import logan.blockpartycompose.ui.theme.theme_2_dark_scrim
import logan.blockpartycompose.ui.theme.theme_2_dark_secondary
import logan.blockpartycompose.ui.theme.theme_2_dark_secondaryContainer
import logan.blockpartycompose.ui.theme.theme_2_dark_surface
import logan.blockpartycompose.ui.theme.theme_2_dark_surfaceTint
import logan.blockpartycompose.ui.theme.theme_2_dark_surfaceVariant
import logan.blockpartycompose.ui.theme.theme_2_dark_tertiary
import logan.blockpartycompose.ui.theme.theme_2_dark_tertiaryContainer
import logan.blockpartycompose.ui.theme.theme_2_light_background
import logan.blockpartycompose.ui.theme.theme_2_light_error
import logan.blockpartycompose.ui.theme.theme_2_light_errorContainer
import logan.blockpartycompose.ui.theme.theme_2_light_inverseOnSurface
import logan.blockpartycompose.ui.theme.theme_2_light_inversePrimary
import logan.blockpartycompose.ui.theme.theme_2_light_inverseSurface
import logan.blockpartycompose.ui.theme.theme_2_light_onBackground
import logan.blockpartycompose.ui.theme.theme_2_light_onError
import logan.blockpartycompose.ui.theme.theme_2_light_onErrorContainer
import logan.blockpartycompose.ui.theme.theme_2_light_onPrimary
import logan.blockpartycompose.ui.theme.theme_2_light_onPrimaryContainer
import logan.blockpartycompose.ui.theme.theme_2_light_onSecondary
import logan.blockpartycompose.ui.theme.theme_2_light_onSecondaryContainer
import logan.blockpartycompose.ui.theme.theme_2_light_onSurface
import logan.blockpartycompose.ui.theme.theme_2_light_onSurfaceVariant
import logan.blockpartycompose.ui.theme.theme_2_light_onTertiary
import logan.blockpartycompose.ui.theme.theme_2_light_onTertiaryContainer
import logan.blockpartycompose.ui.theme.theme_2_light_outline
import logan.blockpartycompose.ui.theme.theme_2_light_outlineVariant
import logan.blockpartycompose.ui.theme.theme_2_light_primary
import logan.blockpartycompose.ui.theme.theme_2_light_primaryContainer
import logan.blockpartycompose.ui.theme.theme_2_light_scrim
import logan.blockpartycompose.ui.theme.theme_2_light_secondary
import logan.blockpartycompose.ui.theme.theme_2_light_secondaryContainer
import logan.blockpartycompose.ui.theme.theme_2_light_surface
import logan.blockpartycompose.ui.theme.theme_2_light_surfaceTint
import logan.blockpartycompose.ui.theme.theme_2_light_surfaceVariant
import logan.blockpartycompose.ui.theme.theme_2_light_tertiary
import logan.blockpartycompose.ui.theme.theme_2_light_tertiaryContainer
import logan.blockpartycompose.ui.theme.theme_3_dark_background
import logan.blockpartycompose.ui.theme.theme_3_dark_error
import logan.blockpartycompose.ui.theme.theme_3_dark_errorContainer
import logan.blockpartycompose.ui.theme.theme_3_dark_inverseOnSurface
import logan.blockpartycompose.ui.theme.theme_3_dark_inversePrimary
import logan.blockpartycompose.ui.theme.theme_3_dark_inverseSurface
import logan.blockpartycompose.ui.theme.theme_3_dark_onBackground
import logan.blockpartycompose.ui.theme.theme_3_dark_onError
import logan.blockpartycompose.ui.theme.theme_3_dark_onErrorContainer
import logan.blockpartycompose.ui.theme.theme_3_dark_onPrimary
import logan.blockpartycompose.ui.theme.theme_3_dark_onPrimaryContainer
import logan.blockpartycompose.ui.theme.theme_3_dark_onSecondary
import logan.blockpartycompose.ui.theme.theme_3_dark_onSecondaryContainer
import logan.blockpartycompose.ui.theme.theme_3_dark_onSurface
import logan.blockpartycompose.ui.theme.theme_3_dark_onSurfaceVariant
import logan.blockpartycompose.ui.theme.theme_3_dark_onTertiary
import logan.blockpartycompose.ui.theme.theme_3_dark_onTertiaryContainer
import logan.blockpartycompose.ui.theme.theme_3_dark_outline
import logan.blockpartycompose.ui.theme.theme_3_dark_outlineVariant
import logan.blockpartycompose.ui.theme.theme_3_dark_primary
import logan.blockpartycompose.ui.theme.theme_3_dark_primaryContainer
import logan.blockpartycompose.ui.theme.theme_3_dark_scrim
import logan.blockpartycompose.ui.theme.theme_3_dark_secondary
import logan.blockpartycompose.ui.theme.theme_3_dark_secondaryContainer
import logan.blockpartycompose.ui.theme.theme_3_dark_surface
import logan.blockpartycompose.ui.theme.theme_3_dark_surfaceTint
import logan.blockpartycompose.ui.theme.theme_3_dark_surfaceVariant
import logan.blockpartycompose.ui.theme.theme_3_dark_tertiary
import logan.blockpartycompose.ui.theme.theme_3_dark_tertiaryContainer
import logan.blockpartycompose.ui.theme.theme_3_light_background
import logan.blockpartycompose.ui.theme.theme_3_light_error
import logan.blockpartycompose.ui.theme.theme_3_light_errorContainer
import logan.blockpartycompose.ui.theme.theme_3_light_inverseOnSurface
import logan.blockpartycompose.ui.theme.theme_3_light_inversePrimary
import logan.blockpartycompose.ui.theme.theme_3_light_inverseSurface
import logan.blockpartycompose.ui.theme.theme_3_light_onBackground
import logan.blockpartycompose.ui.theme.theme_3_light_onError
import logan.blockpartycompose.ui.theme.theme_3_light_onErrorContainer
import logan.blockpartycompose.ui.theme.theme_3_light_onPrimary
import logan.blockpartycompose.ui.theme.theme_3_light_onPrimaryContainer
import logan.blockpartycompose.ui.theme.theme_3_light_onSecondary
import logan.blockpartycompose.ui.theme.theme_3_light_onSecondaryContainer
import logan.blockpartycompose.ui.theme.theme_3_light_onSurface
import logan.blockpartycompose.ui.theme.theme_3_light_onSurfaceVariant
import logan.blockpartycompose.ui.theme.theme_3_light_onTertiary
import logan.blockpartycompose.ui.theme.theme_3_light_onTertiaryContainer
import logan.blockpartycompose.ui.theme.theme_3_light_outline
import logan.blockpartycompose.ui.theme.theme_3_light_outlineVariant
import logan.blockpartycompose.ui.theme.theme_3_light_primary
import logan.blockpartycompose.ui.theme.theme_3_light_primaryContainer
import logan.blockpartycompose.ui.theme.theme_3_light_scrim
import logan.blockpartycompose.ui.theme.theme_3_light_secondary
import logan.blockpartycompose.ui.theme.theme_3_light_secondaryContainer
import logan.blockpartycompose.ui.theme.theme_3_light_surface
import logan.blockpartycompose.ui.theme.theme_3_light_surfaceTint
import logan.blockpartycompose.ui.theme.theme_3_light_surfaceVariant
import logan.blockpartycompose.ui.theme.theme_3_light_tertiary
import logan.blockpartycompose.ui.theme.theme_3_light_tertiaryContainer
import logan.blockpartycompose.ui.theme.theme_4_dark_background
import logan.blockpartycompose.ui.theme.theme_4_dark_error
import logan.blockpartycompose.ui.theme.theme_4_dark_errorContainer
import logan.blockpartycompose.ui.theme.theme_4_dark_inverseOnSurface
import logan.blockpartycompose.ui.theme.theme_4_dark_inversePrimary
import logan.blockpartycompose.ui.theme.theme_4_dark_inverseSurface
import logan.blockpartycompose.ui.theme.theme_4_dark_onBackground
import logan.blockpartycompose.ui.theme.theme_4_dark_onError
import logan.blockpartycompose.ui.theme.theme_4_dark_onErrorContainer
import logan.blockpartycompose.ui.theme.theme_4_dark_onPrimary
import logan.blockpartycompose.ui.theme.theme_4_dark_onPrimaryContainer
import logan.blockpartycompose.ui.theme.theme_4_dark_onSecondary
import logan.blockpartycompose.ui.theme.theme_4_dark_onSecondaryContainer
import logan.blockpartycompose.ui.theme.theme_4_dark_onSurface
import logan.blockpartycompose.ui.theme.theme_4_dark_onSurfaceVariant
import logan.blockpartycompose.ui.theme.theme_4_dark_onTertiary
import logan.blockpartycompose.ui.theme.theme_4_dark_onTertiaryContainer
import logan.blockpartycompose.ui.theme.theme_4_dark_outline
import logan.blockpartycompose.ui.theme.theme_4_dark_outlineVariant
import logan.blockpartycompose.ui.theme.theme_4_dark_primary
import logan.blockpartycompose.ui.theme.theme_4_dark_primaryContainer
import logan.blockpartycompose.ui.theme.theme_4_dark_scrim
import logan.blockpartycompose.ui.theme.theme_4_dark_secondary
import logan.blockpartycompose.ui.theme.theme_4_dark_secondaryContainer
import logan.blockpartycompose.ui.theme.theme_4_dark_surface
import logan.blockpartycompose.ui.theme.theme_4_dark_surfaceTint
import logan.blockpartycompose.ui.theme.theme_4_dark_surfaceVariant
import logan.blockpartycompose.ui.theme.theme_4_dark_tertiary
import logan.blockpartycompose.ui.theme.theme_4_dark_tertiaryContainer
import logan.blockpartycompose.ui.theme.theme_4_light_background
import logan.blockpartycompose.ui.theme.theme_4_light_error
import logan.blockpartycompose.ui.theme.theme_4_light_errorContainer
import logan.blockpartycompose.ui.theme.theme_4_light_inverseOnSurface
import logan.blockpartycompose.ui.theme.theme_4_light_inversePrimary
import logan.blockpartycompose.ui.theme.theme_4_light_inverseSurface
import logan.blockpartycompose.ui.theme.theme_4_light_onBackground
import logan.blockpartycompose.ui.theme.theme_4_light_onError
import logan.blockpartycompose.ui.theme.theme_4_light_onErrorContainer
import logan.blockpartycompose.ui.theme.theme_4_light_onPrimary
import logan.blockpartycompose.ui.theme.theme_4_light_onPrimaryContainer
import logan.blockpartycompose.ui.theme.theme_4_light_onSecondary
import logan.blockpartycompose.ui.theme.theme_4_light_onSecondaryContainer
import logan.blockpartycompose.ui.theme.theme_4_light_onSurface
import logan.blockpartycompose.ui.theme.theme_4_light_onSurfaceVariant
import logan.blockpartycompose.ui.theme.theme_4_light_onTertiary
import logan.blockpartycompose.ui.theme.theme_4_light_onTertiaryContainer
import logan.blockpartycompose.ui.theme.theme_4_light_outline
import logan.blockpartycompose.ui.theme.theme_4_light_outlineVariant
import logan.blockpartycompose.ui.theme.theme_4_light_primary
import logan.blockpartycompose.ui.theme.theme_4_light_primaryContainer
import logan.blockpartycompose.ui.theme.theme_4_light_scrim
import logan.blockpartycompose.ui.theme.theme_4_light_secondary
import logan.blockpartycompose.ui.theme.theme_4_light_secondaryContainer
import logan.blockpartycompose.ui.theme.theme_4_light_surface
import logan.blockpartycompose.ui.theme.theme_4_light_surfaceTint
import logan.blockpartycompose.ui.theme.theme_4_light_surfaceVariant
import logan.blockpartycompose.ui.theme.theme_4_light_tertiary
import logan.blockpartycompose.ui.theme.theme_4_light_tertiaryContainer


val LightColorsGreen = lightColorScheme(
    primary = theme_2_light_primary,
    onPrimary = theme_2_light_onPrimary,
    primaryContainer = theme_2_light_primaryContainer,
    onPrimaryContainer = theme_2_light_onPrimaryContainer,
    secondary = theme_2_light_secondary,
    onSecondary = theme_2_light_onSecondary,
    secondaryContainer = theme_2_light_secondaryContainer,
    onSecondaryContainer = theme_2_light_onSecondaryContainer,
    tertiary = theme_2_light_tertiary,
    onTertiary = theme_2_light_onTertiary,
    tertiaryContainer = theme_2_light_tertiaryContainer,
    onTertiaryContainer = theme_2_light_onTertiaryContainer,
    error = theme_2_light_error,
    errorContainer = theme_2_light_errorContainer,
    onError = theme_2_light_onError,
    onErrorContainer = theme_2_light_onErrorContainer,
    background = theme_2_light_background,
    onBackground = theme_2_light_onBackground,
    surface = theme_2_light_surface,
    onSurface = theme_2_light_onSurface,
    surfaceVariant = theme_2_light_surfaceVariant,
    onSurfaceVariant = theme_2_light_onSurfaceVariant,
    outline = theme_2_light_outline,
    inverseOnSurface = theme_2_light_inverseOnSurface,
    inverseSurface = theme_2_light_inverseSurface,
    inversePrimary = theme_2_light_inversePrimary,
    surfaceTint = theme_2_light_surfaceTint,
    outlineVariant = theme_2_light_outlineVariant,
    scrim = theme_2_light_scrim,
)


val DarkColorsGreen = darkColorScheme(
    primary = theme_2_dark_primary,
    onPrimary = theme_2_dark_onPrimary,
    primaryContainer = theme_2_dark_primaryContainer,
    onPrimaryContainer = theme_2_dark_onPrimaryContainer,
    secondary = theme_2_dark_secondary,
    onSecondary = theme_2_dark_onSecondary,
    secondaryContainer = theme_2_dark_secondaryContainer,
    onSecondaryContainer = theme_2_dark_onSecondaryContainer,
    tertiary = theme_2_dark_tertiary,
    onTertiary = theme_2_dark_onTertiary,
    tertiaryContainer = theme_2_dark_tertiaryContainer,
    onTertiaryContainer = theme_2_dark_onTertiaryContainer,
    error = theme_2_dark_error,
    errorContainer = theme_2_dark_errorContainer,
    onError = theme_2_dark_onError,
    onErrorContainer = theme_2_dark_onErrorContainer,
    background = theme_2_dark_background,
    onBackground = theme_2_dark_onBackground,
    surface = theme_2_dark_surface,
    onSurface = theme_2_dark_onSurface,
    surfaceVariant = theme_2_dark_surfaceVariant,
    onSurfaceVariant = theme_2_dark_onSurfaceVariant,
    outline = theme_2_dark_outline,
    inverseOnSurface = theme_2_dark_inverseOnSurface,
    inverseSurface = theme_2_dark_inverseSurface,
    inversePrimary = theme_2_dark_inversePrimary,
    surfaceTint = theme_2_dark_surfaceTint,
    outlineVariant = theme_2_dark_outlineVariant,
    scrim = theme_2_dark_scrim,
)

val LightColorsDefault = lightColorScheme(
    primary = theme_1_light_primary,
    onPrimary = theme_1_light_onPrimary,
    primaryContainer = theme_1_light_primaryContainer,
    onPrimaryContainer = theme_1_light_onPrimaryContainer,
    secondary = theme_1_light_secondary,
    onSecondary = theme_1_light_onSecondary,
    secondaryContainer = theme_1_light_secondaryContainer,
    onSecondaryContainer = theme_1_light_onSecondaryContainer,
    tertiary = theme_1_light_tertiary,
    onTertiary = theme_1_light_onTertiary,
    tertiaryContainer = theme_1_light_tertiaryContainer,
    onTertiaryContainer = theme_1_light_onTertiaryContainer,
    error = theme_1_light_error,
    errorContainer = theme_1_light_errorContainer,
    onError = theme_1_light_onError,
    onErrorContainer = theme_1_light_onErrorContainer,
    background = theme_1_light_background,
    onBackground = theme_1_light_onBackground,
    surface = theme_1_light_surface,
    onSurface = theme_1_light_onSurface,
    surfaceVariant = theme_1_light_surfaceVariant,
    onSurfaceVariant = theme_1_light_onSurfaceVariant,
    outline = theme_1_light_outline,
    inverseOnSurface = theme_1_light_inverseOnSurface,
    inverseSurface = theme_1_light_inverseSurface,
    inversePrimary = theme_1_light_inversePrimary,
    surfaceTint = theme_1_light_surfaceTint,
    outlineVariant = theme_1_light_outlineVariant,
    scrim = theme_1_light_scrim,
)


val DarkColorsDefault = darkColorScheme(
    primary = theme_1_dark_primary,
    onPrimary = theme_1_dark_onPrimary,
    primaryContainer = theme_1_dark_primaryContainer,
    onPrimaryContainer = theme_1_dark_onPrimaryContainer,
    secondary = theme_1_dark_secondary,
    onSecondary = theme_1_dark_onSecondary,
    secondaryContainer = theme_1_dark_secondaryContainer,
    onSecondaryContainer = theme_1_dark_onSecondaryContainer,
    tertiary = theme_1_dark_tertiary,
    onTertiary = theme_1_dark_onTertiary,
    tertiaryContainer = theme_1_dark_tertiaryContainer,
    onTertiaryContainer = theme_1_dark_onTertiaryContainer,
    error = theme_1_dark_error,
    errorContainer = theme_1_dark_errorContainer,
    onError = theme_1_dark_onError,
    onErrorContainer = theme_1_dark_onErrorContainer,
    background = theme_1_dark_background,
    onBackground = theme_1_dark_onBackground,
    surface = theme_1_dark_surface,
    onSurface = theme_1_dark_onSurface,
    surfaceVariant = theme_1_dark_surfaceVariant,
    onSurfaceVariant = theme_1_dark_onSurfaceVariant,
    outline = theme_1_dark_outline,
    inverseOnSurface = theme_1_dark_inverseOnSurface,
    inverseSurface = theme_1_dark_inverseSurface,
    inversePrimary = theme_1_dark_inversePrimary,
    surfaceTint = theme_1_dark_surfaceTint,
    outlineVariant = theme_1_dark_outlineVariant,
    scrim = theme_1_dark_scrim,
)

val LightColorsPurple = lightColorScheme(
    primary = theme_3_light_primary,
    onPrimary = theme_3_light_onPrimary,
    primaryContainer = theme_3_light_primaryContainer,
    onPrimaryContainer = theme_3_light_onPrimaryContainer,
    secondary = theme_3_light_secondary,
    onSecondary = theme_3_light_onSecondary,
    secondaryContainer = theme_3_light_secondaryContainer,
    onSecondaryContainer = theme_3_light_onSecondaryContainer,
    tertiary = theme_3_light_tertiary,
    onTertiary = theme_3_light_onTertiary,
    tertiaryContainer = theme_3_light_tertiaryContainer,
    onTertiaryContainer = theme_3_light_onTertiaryContainer,
    error = theme_3_light_error,
    errorContainer = theme_3_light_errorContainer,
    onError = theme_3_light_onError,
    onErrorContainer = theme_3_light_onErrorContainer,
    background = theme_3_light_background,
    onBackground = theme_3_light_onBackground,
    surface = theme_3_light_surface,
    onSurface = theme_3_light_onSurface,
    surfaceVariant = theme_3_light_surfaceVariant,
    onSurfaceVariant = theme_3_light_onSurfaceVariant,
    outline = theme_3_light_outline,
    inverseOnSurface = theme_3_light_inverseOnSurface,
    inverseSurface = theme_3_light_inverseSurface,
    inversePrimary = theme_3_light_inversePrimary,
    surfaceTint = theme_3_light_surfaceTint,
    outlineVariant = theme_3_light_outlineVariant,
    scrim = theme_3_light_scrim,
)


val DarkColorsPurple = darkColorScheme(
    primary = theme_3_dark_primary,
    onPrimary = theme_3_dark_onPrimary,
    primaryContainer = theme_3_dark_primaryContainer,
    onPrimaryContainer = theme_3_dark_onPrimaryContainer,
    secondary = theme_3_dark_secondary,
    onSecondary = theme_3_dark_onSecondary,
    secondaryContainer = theme_3_dark_secondaryContainer,
    onSecondaryContainer = theme_3_dark_onSecondaryContainer,
    tertiary = theme_3_dark_tertiary,
    onTertiary = theme_3_dark_onTertiary,
    tertiaryContainer = theme_3_dark_tertiaryContainer,
    onTertiaryContainer = theme_3_dark_onTertiaryContainer,
    error = theme_3_dark_error,
    errorContainer = theme_3_dark_errorContainer,
    onError = theme_3_dark_onError,
    onErrorContainer = theme_3_dark_onErrorContainer,
    background = theme_3_dark_background,
    onBackground = theme_3_dark_onBackground,
    surface = theme_3_dark_surface,
    onSurface = theme_3_dark_onSurface,
    surfaceVariant = theme_3_dark_surfaceVariant,
    onSurfaceVariant = theme_3_dark_onSurfaceVariant,
    outline = theme_3_dark_outline,
    inverseOnSurface = theme_3_dark_inverseOnSurface,
    inverseSurface = theme_3_dark_inverseSurface,
    inversePrimary = theme_3_dark_inversePrimary,
    surfaceTint = theme_3_dark_surfaceTint,
    outlineVariant = theme_3_dark_outlineVariant,
    scrim = theme_3_dark_scrim,
)

val LightColorsGrey = lightColorScheme(
    primary = theme_4_light_primary,
    onPrimary = theme_4_light_onPrimary,
    primaryContainer = theme_4_light_primaryContainer,
    onPrimaryContainer = theme_4_light_onPrimaryContainer,
    secondary = theme_4_light_secondary,
    onSecondary = theme_4_light_onSecondary,
    secondaryContainer = theme_4_light_secondaryContainer,
    onSecondaryContainer = theme_4_light_onSecondaryContainer,
    tertiary = theme_4_light_tertiary,
    onTertiary = theme_4_light_onTertiary,
    tertiaryContainer = theme_4_light_tertiaryContainer,
    onTertiaryContainer = theme_4_light_onTertiaryContainer,
    error = theme_4_light_error,
    errorContainer = theme_4_light_errorContainer,
    onError = theme_4_light_onError,
    onErrorContainer = theme_4_light_onErrorContainer,
    background = theme_4_light_background,
    onBackground = theme_4_light_onBackground,
    surface = theme_4_light_surface,
    onSurface = theme_4_light_onSurface,
    surfaceVariant = theme_4_light_surfaceVariant,
    onSurfaceVariant = theme_4_light_onSurfaceVariant,
    outline = theme_4_light_outline,
    inverseOnSurface = theme_4_light_inverseOnSurface,
    inverseSurface = theme_4_light_inverseSurface,
    inversePrimary = theme_4_light_inversePrimary,
    surfaceTint = theme_4_light_surfaceTint,
    outlineVariant = theme_4_light_outlineVariant,
    scrim = theme_4_light_scrim,
)


val DarkColorsGrey = darkColorScheme(
    primary = theme_4_dark_primary,
    onPrimary = theme_4_dark_onPrimary,
    primaryContainer = theme_4_dark_primaryContainer,
    onPrimaryContainer = theme_4_dark_onPrimaryContainer,
    secondary = theme_4_dark_secondary,
    onSecondary = theme_4_dark_onSecondary,
    secondaryContainer = theme_4_dark_secondaryContainer,
    onSecondaryContainer = theme_4_dark_onSecondaryContainer,
    tertiary = theme_4_dark_tertiary,
    onTertiary = theme_4_dark_onTertiary,
    tertiaryContainer = theme_4_dark_tertiaryContainer,
    onTertiaryContainer = theme_4_dark_onTertiaryContainer,
    error = theme_4_dark_error,
    errorContainer = theme_4_dark_errorContainer,
    onError = theme_4_dark_onError,
    onErrorContainer = theme_4_dark_onErrorContainer,
    background = theme_4_dark_background,
    onBackground = theme_4_dark_onBackground,
    surface = theme_4_dark_surface,
    onSurface = theme_4_dark_onSurface,
    surfaceVariant = theme_4_dark_surfaceVariant,
    onSurfaceVariant = theme_4_dark_onSurfaceVariant,
    outline = theme_4_dark_outline,
    inverseOnSurface = theme_4_dark_inverseOnSurface,
    inverseSurface = theme_4_dark_inverseSurface,
    inversePrimary = theme_4_dark_inversePrimary,
    surfaceTint = theme_4_dark_surfaceTint,
    outlineVariant = theme_4_dark_outlineVariant,
    scrim = theme_4_dark_scrim,
)

