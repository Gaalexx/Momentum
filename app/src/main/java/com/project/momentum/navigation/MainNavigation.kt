package com.project.momentum.navigation

import android.net.Uri
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.unveilIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import com.project.momentum.features.account.ui.AccountRoot
import com.project.momentum.features.auth.ui.AuthorizationAccountRoot
import com.project.momentum.features.auth.ui.AuthorizationPasswordRoot
import com.project.momentum.features.auth.ui.CreateAccountRoot
import com.project.momentum.features.auth.ui.CreatePasswordRoot
import com.project.momentum.features.auth.ui.InsertCodeRoot
import com.project.momentum.features.auth.ui.PasswordRecoveryRoot
import com.project.momentum.features.cameracontentpager.ui.CameraContentPager
import com.project.momentum.features.contentcreation.models.ContentCreationMode
import com.project.momentum.features.contentcreation.ui.MediaCreationScreen
import com.project.momentum.features.contentcreation.ui.SendContentScreen
import com.project.momentum.features.editingAccount.ui.EditingAccountRoot
import com.project.momentum.features.editingAccount.viewmodel.AccountInfo
import com.project.momentum.features.friends.ui.FriendAccountRoot
import com.project.momentum.features.friends.ui.FriendsScreenRoute
import com.project.momentum.features.offline.ui.NoInternetScreen
import com.project.momentum.features.posts.ui.GalleryScreen
import com.project.momentum.features.posts.ui.WatchPhotoScreenRoute
import com.project.momentum.features.settings.ui.DeleteAccountCheckCodeScreen
import com.project.momentum.features.settings.ui.DeleteAccountCheckPasswordScreen
import com.project.momentum.features.settings.ui.DeleteAccountConfirmationScreen
import com.project.momentum.features.settings.ui.SettingsMainScreen
import com.project.momentum.features.settings.ui.SettingsPremiumScreen
import com.project.momentum.navigation.viewmodel.AppStartState
import com.project.momentum.navigation.viewmodel.AppStartViewModel
import com.project.momentum.ui.common.LoadingOverlay


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {

    SharedTransitionLayout {
        val appStartViewModel: AppStartViewModel = hiltViewModel()
        val state = appStartViewModel.state
        val sharedTransitionScope = this

        LaunchedEffect(Unit) {
            appStartViewModel.restoreSession()
        }

        val startRoute = when (state) {
            AppStartState.Authorized -> NavRoutes.Camera
            AppStartState.Unauthorized -> NavRoutes.RegistrationLogin
            AppStartState.Loading -> null
            AppStartState.NoInternetConnection -> NavRoutes.NoInternetConnection
        }



        if (startRoute == null) {
            LoadingOverlay()
        } else {
            val backStack: NavBackStack<NavKey> = rememberNavBackStack(startRoute)

            fun setBase(route: NavRoutes) {
                if (backStack.isEmpty()) {
                    backStack.add(route)
                    return
                }
                backStack[0] = route
            }

            fun openOverlay(route: NavRoutes) {
                backStack.add(route)
            }

            fun closeOverlay() {
                val last = backStack.lastOrNull()
                if (last != null && last.isOverlayRoute() && backStack.size > 1) {
                    backStack.removeAt(backStack.lastIndex)
                }
            }

            fun closeGallery() {
                val last = backStack.lastOrNull()
                if (last is NavRoutes.Gallery && backStack.size > 1) {
                    backStack.removeAt(backStack.lastIndex)
                } else {
                    setBase(NavRoutes.Camera)
                }
            }

            fun currentBackTo(): String {
                val lastBase = backStack.lastOrNull { !it.isOverlayRoute() }
                return when (lastBase) {
                    is NavRoutes.Gallery -> "gallery"
                    is NavRoutes.Recorder -> "recorder"
                    else -> "camera"
                }
            }


            val noAnimation: () -> ContentTransform = {
                ContentTransform(EnterTransition.None, ExitTransition.None)
            }

            val saveableStateHolder = rememberSaveableStateHolder()
            val entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator<NavKey>(saveableStateHolder),
//        rememberViewModelStoreNavEntryDecorator<NavKey>()
            )

            val navEntryProvider = entryProvider<NavKey> {
                entry<NavRoutes.NoInternetConnection> {
                    NoInternetScreen(
                        onRetryClick = {
                            appStartViewModel.retrySession()
                        },
                        onOpenSettingsClick = {}
                    )
                }

                // Экраны регистрации
                entry<NavRoutes.RegistrationLogin> {
                    CreateAccountRoot(
                        onBackClick = {
                            closeOverlay()
                        },
                        onContinueClick = {
                            openOverlay(NavRoutes.RegistrationCode)
                        },
                        onSubButtonClick = {
                            openOverlay(NavRoutes.AuthorizationLogin)
                        }
                    )
                }

                entry<NavRoutes.RegistrationCode> {
                    InsertCodeRoot(
                        onBackClick = {
                            closeOverlay()
                        },
                        onContinueClick = {
                            openOverlay(NavRoutes.RegistrationPassword)
                        }
                    )
                }

                entry<NavRoutes.RegistrationPassword> {
                    CreatePasswordRoot(
                        onBackClick = {
                            closeOverlay()
                        },
                        onContinueClick = {
                            openOverlay(NavRoutes.Camera)
                        }
                    )
                }

                // Экраны входа
                entry<NavRoutes.AuthorizationLogin> {
                    AuthorizationAccountRoot(
                        onBackClick = {
                            closeOverlay()
                        },
                        onContinueClick = {
                            openOverlay(NavRoutes.AuthorizationPassword)
                        }
                    )
                }

                entry<NavRoutes.AuthorizationPassword> {
                    AuthorizationPasswordRoot(
                        onBackClick = {
                            closeOverlay()
                        },
                        onContinueClick = {
                            openOverlay(NavRoutes.Camera)
                        },
                        onPasswordRecoveryClick = {
                            openOverlay(NavRoutes.AuthorizationCode)
                        }
                    )
                }

                entry<NavRoutes.AuthorizationCode> {
                    PasswordRecoveryRoot(
                        onBackClick = {
                            closeOverlay()
                        },
                        onContinueClick = {
                            openOverlay(NavRoutes.Camera)
                        }
                    )
                }

                // Экраны создания контента

                entry<NavRoutes.Camera> {
                    CameraContentPager(
                        initialMode = ContentCreationMode.Camera,
                        onGoToPreview = { uri, mediaType ->
                            openOverlay(NavRoutes.SendPhoto(uri.toString(), mediaType))
                        },
                        onProfileClick = {
                            openOverlay(NavRoutes.Account("camera"))
                        },
                        onGoToSettings = {
                            openOverlay(NavRoutes.Settings("camera"))
                        },
                        onGoToFriends = {
                            openOverlay(NavRoutes.Friends)
                        },
                        onGoToGallery = {
                            openOverlay(NavRoutes.Gallery)
                        },
                        onGoToTakePhoto = {},
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current
                    )
                }



                entry<NavRoutes.Recorder> {
                    CameraContentPager(
                        initialMode = ContentCreationMode.Audio,
                        onGoToPreview = { uri, mediaType ->
                            openOverlay(NavRoutes.SendPhoto(uri.toString(), mediaType))
                        },
                        onProfileClick = {
                            openOverlay(NavRoutes.Account("camera"))
                        },
                        onGoToSettings = {
                            openOverlay(NavRoutes.Settings("camera"))
                        },
                        onGoToFriends = {
                            openOverlay(NavRoutes.Friends)
                        },
                        onGoToGallery = {
                            openOverlay(NavRoutes.Gallery)
                        },
                        onGoToTakePhoto = {},
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current
                    )
                }

                entry<NavRoutes.Gallery>(
                    metadata = NavDisplay.transitionSpec {
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(1000)
                        ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                    }
                ) {
                    GalleryScreen(
                        onPostClick = { post ->
                            openOverlay(NavRoutes.PreviewPhoto(post = post))
                        },
                        onProfileClick = {
                            openOverlay(NavRoutes.Account("gallery"))
                        },
                        onBackClick = {
                            closeGallery()
                        },
                        onGoToSettings = {
                            openOverlay(NavRoutes.Settings("gallery"))
                        },
                        onGoToFriends = {
                            openOverlay(NavRoutes.Friends)
                        },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    )
                }

                entry<NavRoutes.Settings> {
                    SettingsMainScreen(
                        onBackClick = {
                            closeOverlay()
                        },
                        onPremiumClick = {
                            openOverlay(NavRoutes.Premium)
                        },
                        onLogoutClick = {
                            closeOverlay()
                        },
                        onDeleteAccountClick = {
                            openOverlay(NavRoutes.DeleteAccountCheckPassword)
                        }
                    )
                }

                entry<NavRoutes.Account>(
                    metadata = NavDisplay.transitionSpec {
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(500)
                        ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                    }
                ) {
                    AccountRoot(
                        onPostClick = { post, userId ->
                            openOverlay(
                                NavRoutes.PreviewPhoto(
                                    post = post,
                                    userId = userId
                                )
                            )
                        },
                        onEditClick = { uiInfoState ->
                            openOverlay(
                                NavRoutes.EditAccount(
                                    currentUserInfo = AccountInfo(
                                        username = uiInfoState.name,
                                        email = uiInfoState.email,
                                        phone = uiInfoState.phone,
                                        profilePhotoURL = uiInfoState.profilePhotoURL
                                    )
                                )
                            )
                        },
                        onBackClick = {
                            closeOverlay()
                        },
                        onAddPostClick = { openOverlay(NavRoutes.Camera) },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    )
                }

                entry<NavRoutes.Friends> {
                    FriendsScreenRoute(
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        onBackClick = {
                            closeOverlay()
                        },
                        onFriendClick = { friend ->
                            openOverlay(NavRoutes.FriendAccount(friend))
                        }
                    )
                }

                entry<NavRoutes.FriendAccount> { route ->
                    FriendAccountRoot(
                        friend = route.friend,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                        onPostClick = { post, userId ->
                            openOverlay(
                                NavRoutes.PreviewPhoto(
                                    post = post,
                                    userId = userId
                                )
                            )
                        },
                        onBackClick = {
                            closeOverlay()
                        }
                    )
                }

                entry<NavRoutes.EditAccount> { route ->
                    EditingAccountRoot(
                        currentUserInfo = route.currentUserInfo,
                        onBackClick = { closeOverlay() },
                        onContinueClick = { closeOverlay() }
                    )
                }



                entry<NavRoutes.SendPhoto> { route ->
                    val uri = Uri.parse(route.uri)
                    val mediaType = route.mediaTypeToSend
                    SendContentScreen(
                        uri = uri,
                        mediaType = mediaType,
                        onGoToTakePhoto = {
                            closeOverlay()
                        },
                        onGoToSettings = {
                            openOverlay(NavRoutes.Settings(currentBackTo()))
                        },
                        onProfileClick = {
                            openOverlay(NavRoutes.Account(currentBackTo()))
                        },
                        onGoToFriends = {
                            openOverlay(NavRoutes.Friends)
                        },
                        onError = {
                            openOverlay(NavRoutes.NoInternetConnection)
                        }
                    )
                }

                entry<NavRoutes.PreviewPhoto> { route ->

                    WatchPhotoScreenRoute(
                        onGoToTakePhoto = {
                            openOverlay(NavRoutes.Camera)
                        },
                        onGoToGallery = {
                            closeOverlay()
                        },
                        onGoToSettings = {
                            openOverlay(NavRoutes.Settings(currentBackTo()))
                        },
                        onProfileClick = {
                            openOverlay(NavRoutes.Account(currentBackTo()))
                        },
                        onGoToFriends = {
                            openOverlay(NavRoutes.Friends)
                        },
                        postIndex = route.post,
                        userId = route.userId,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    )

                }

                entry<NavRoutes.DeleteAccountCheckPassword> {
                    DeleteAccountCheckPasswordScreen(
                        onBackClick = {
                            closeOverlay()
                        },
                        onContinueClick = {
                            openOverlay(NavRoutes.DeleteAccountCheckCode)
                        }
                    )
                }

                entry<NavRoutes.DeleteAccountCheckCode> {
                    DeleteAccountCheckCodeScreen(
                        onBackClick = {
                            closeOverlay()
                        },
                        onContinueClick = {
                            openOverlay(NavRoutes.DeleteAccountConfirmation)
                        }
                    )
                }
                entry<NavRoutes.DeleteAccountConfirmation> {
                    DeleteAccountConfirmationScreen(
                        onCancel = {
                            closeOverlay()
                        },
                        onConfirm = {
                            openOverlay(NavRoutes.RegistrationLogin)
                        }
                    )
                }

                entry<NavRoutes.Premium> {
                    SettingsPremiumScreen(
                        onBackClick = {
                            closeOverlay()
                        },
                        onBuyClick = {

                        }
                    )
                }
            }

            val entries = rememberDecoratedNavEntries(
                backStack = backStack,
                entryDecorators = entryDecorators,
                entryProvider = navEntryProvider
            )

            NavDisplay(
                entries = entries,
                transitionSpec = { fadeIn(animationSpec = tween(750)) togetherWith ExitTransition.KeepUntilTransitionsFinished },
                popTransitionSpec = {
                    EnterTransition.None togetherWith
                            fadeOut(animationSpec = tween(750))
                },
                predictivePopTransitionSpec = { _ ->
                    EnterTransition.None togetherWith
                            fadeOut(animationSpec = tween(750))
                },
                onBack = {
                    if (backStack.size > 1) {
                        backStack.removeAt(backStack.lastIndex)
                    }
                }
            )
        }
    }

}

private fun NavKey.isOverlayRoute(): Boolean {
    return when (this) {
        is NavRoutes.NoInternetConnection,
        is NavRoutes.Camera,
        is NavRoutes.Recorder,
        is NavRoutes.Gallery -> false

        else -> true
    }
}
