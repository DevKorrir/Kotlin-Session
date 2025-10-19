package com.example.playground

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.playground.navigation.Screen
import kotlinx.coroutines.launch


data class BottomNavItem(val title: String, val route: String, val icon: ImageVector)
data class DrawerItem(val title: String, val route: String, val icon: ImageVector)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    modifier: Modifier = Modifier,
    topLevelNavController: NavHostController
) {


    val bottomLevelNavController = rememberNavController()

    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val bottomNavItems = listOf(
        BottomNavItem("Home", Screen.BottomHome.route, Icons.Default.Home),
        BottomNavItem("Profile", Screen.BottomProfile.route, Icons.Default.Person)
    )

    val drawerItems = listOf(
        DrawerItem("Settings", Screen.Settings.route, Icons.Default.Settings),
        DrawerItem ("Logout", "logout", Icons.Default.Delete)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("App Menu", modifier = Modifier.padding(16.dp))


                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.title) },
                        icon = { Icon(item.icon, contentDescription = null) },
                        selected = false,
                        onClick = {
                            if (item.title == "Logout") {
                                scope.launch { drawerState.close() }
                            }
                            topLevelNavController.navigate(Screen.Login.route) {
                                // pop all my previous screen from my stack inclusive main
                                popUpTo(Screen.Main.route) { inclusive = true }
                            }

                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

        }
    ) {

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Main") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
                 },
        bottomBar = {
            NavigationBar {

                val currentDestination = bottomLevelNavController.currentBackStackEntryAsState().value?.destination
                bottomNavItems.forEach { item ->

                    val selected = currentDestination?.route == item.route

                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selected,
                        onClick = {
                            bottomLevelNavController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                            }
                        },
                    )
                }


            }
                          },
        content = { innerPadding ->
            NavHost(
                navController  =  bottomLevelNavController,
                startDestination = Screen.BottomHome.route,
                modifier = Modifier.padding(innerPadding)
            ) {

                composable(Screen.BottomHome.route) {
                    HomeScreen()
                }

                composable(Screen.BottomProfile.route) {
                    ProfileScreen()
                }

            }
        }

    )
    }

}