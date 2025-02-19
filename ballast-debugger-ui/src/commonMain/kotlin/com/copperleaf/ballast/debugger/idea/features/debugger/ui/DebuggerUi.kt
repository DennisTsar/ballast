package com.copperleaf.ballast.debugger.idea.features.debugger.ui

import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.copperleaf.ballast.debugger.idea.features.debugger.injector.DebuggerToolWindowInjector
import com.copperleaf.ballast.debugger.idea.features.debugger.router.DebuggerRoute
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.DebuggerPrimaryToolbar
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.DebuggerScaffold
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.EventDetails
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.EventDetailsToolbar
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.EventsList
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.EventsListToolbar
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.InputDetails
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.InputDetailsToolbar
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.InputsList
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.InputsListToolbar
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.InterceptorDetails
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.InterceptorDetailsToolbar
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.InterceptorsList
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.InterceptorsListToolbar
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.LogsList
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.LogsListToolbar
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.ProvideTime
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.SideJobDetails
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.SideJobDetailsToolbar
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.SideJobsList
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.SideJobsListToolbar
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.SpecialRouterToolbar
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.SpecialViewModelState
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.StateDetails
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.StateDetailsToolbar
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.StatesList
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.StatesListToolbar
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.ViewModelTabStrip
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberConnectionCurrentDestination
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberConnectionsList
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberLatestViewModelStateSnapshot
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberSelectedConnection
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberSelectedViewModel
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberSelectedViewModelEvent
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberSelectedViewModelInput
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberSelectedViewModelInterceptor
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberSelectedViewModelSideJob
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberSelectedViewModelStateSnapshot
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberViewModelEventsList
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberViewModelInputsList
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberViewModelInterceptorList
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberViewModelList
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberViewModelLogsList
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberViewModelSideJobsList
import com.copperleaf.ballast.debugger.idea.features.debugger.ui.widgets.rememberViewModelStatesList
import com.copperleaf.ballast.debugger.idea.features.debugger.vm.DebuggerUiContract
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination

public object DebuggerUi {

    @Composable
    public fun Content(injector: DebuggerToolWindowInjector) {
        val debuggerUiViewModel = remember(injector) { injector.debuggerUiViewModel }
        val debuggerUiState by debuggerUiViewModel.observeStates().collectAsState()

        ProvideTime {
            Content(
                debuggerUiState,
                debuggerUiViewModel::trySend,
            )
        }
    }

    @Composable
    public fun Content(
        uiState: DebuggerUiContract.State,
        postInput: (DebuggerUiContract.Inputs) -> Unit,
    ) {
        uiState.backstack.renderCurrentDestination(
            route = { currentRoute ->
                RouteContent(currentRoute, uiState, postInput)
            },
            notFound = { },
        )
    }

    @Composable
    public fun Destination.Match<DebuggerRoute>.RouteContent(
        currentRoute: DebuggerRoute,
        uiState: DebuggerUiContract.State,
        postInput: (DebuggerUiContract.Inputs) -> Unit,
    ) {
        when (currentRoute) {
            DebuggerRoute.Home -> {
                val connectionsList by rememberConnectionsList(uiState.serverState)

                DebuggerScaffold(
                    primaryToolbar = {
                        DebuggerPrimaryToolbar(
                            currentRoute,
                            connectionsList,
                            null,
                            emptyList(),
                            null,
                            uiState.searchText,
                            postInput,
                        )
                    },
                )
            }

            DebuggerRoute.Connection -> {
                val connectionsList by rememberConnectionsList(uiState.serverState)
                val connection by rememberSelectedConnection(connectionsList)
                val viewModelList by rememberViewModelList(connection)
                val currentAppDestination by rememberConnectionCurrentDestination(connection, uiState.cachedDebuggerUiSettings)

                DebuggerScaffold(
                    primaryToolbar = {
                        DebuggerPrimaryToolbar(
                            currentRoute,
                            connectionsList,
                            connection,
                            viewModelList,
                            null,
                            uiState.searchText,
                            postInput,
                        )
                    },
                    secondaryContent = { SpecialRouterToolbar(currentAppDestination, uiState.cachedDebuggerUiSettings, postInput) },
                )
            }

            DebuggerRoute.ViewModelStates -> {
                val connectionsList by rememberConnectionsList(uiState.serverState)
                val connection by rememberSelectedConnection(connectionsList)
                val viewModelList by rememberViewModelList(connection)
                val viewModel by rememberSelectedViewModel(connection)
                val currentAppDestination by rememberConnectionCurrentDestination(connection, uiState.cachedDebuggerUiSettings)
                val statesList by rememberViewModelStatesList(viewModel, uiState.searchText)
                val latestState by rememberLatestViewModelStateSnapshot(viewModel)

                DebuggerScaffold(
                    primaryToolbar = {
                        DebuggerPrimaryToolbar(
                            currentRoute,
                            connectionsList,
                            connection,
                            viewModelList,
                            viewModel,
                            uiState.searchText,
                            postInput,
                        )
                    },
                    tabs = { ViewModelTabStrip(connection, viewModel, postInput) },
                    mainContentLeft = { StatesList(connection, viewModel, statesList, null, postInput) },
                    contentLeftToolbar = { StatesListToolbar(connection, viewModel, statesList, postInput) },
                    secondaryContent = { SpecialRouterToolbar(currentAppDestination, uiState.cachedDebuggerUiSettings, postInput) },
                    stickyContent = { SpecialViewModelState(latestState, uiState.cachedDebuggerUiSettings, postInput) }
                )
            }

            DebuggerRoute.ViewModelStateDetails -> {
                val connectionsList by rememberConnectionsList(uiState.serverState)
                val connection by rememberSelectedConnection(connectionsList)
                val viewModelList by rememberViewModelList(connection)
                val viewModel by rememberSelectedViewModel(connection)
                val currentAppDestination by rememberConnectionCurrentDestination(connection, uiState.cachedDebuggerUiSettings)
                val statesList by rememberViewModelStatesList(viewModel, uiState.searchText)
                val selectedState by rememberSelectedViewModelStateSnapshot(viewModel)
                val latestState by rememberLatestViewModelStateSnapshot(viewModel)

                DebuggerScaffold(
                    primaryToolbar = {
                        DebuggerPrimaryToolbar(
                            currentRoute,
                            connectionsList,
                            connection,
                            viewModelList,
                            viewModel,
                            uiState.searchText,
                            postInput,
                        )
                    },
                    tabs = { ViewModelTabStrip(connection, viewModel, postInput) },
                    mainContentLeft = { StatesList(connection, viewModel, statesList, selectedState, postInput) },
                    contentLeftToolbar = { StatesListToolbar(connection, viewModel, statesList, postInput) },
                    mainContentRight = {
                        Text("Focused State")
                        Divider()
                        StateDetails(selectedState, postInput)
                    },
                    contentRightToolbar = { StateDetailsToolbar(connection, viewModel, selectedState, postInput) },
                    secondaryContent = { SpecialRouterToolbar(currentAppDestination, uiState.cachedDebuggerUiSettings, postInput) },
                    stickyContent = { SpecialViewModelState(latestState, uiState.cachedDebuggerUiSettings, postInput) }
                )
            }

            DebuggerRoute.ViewModelInputs -> {
                val connectionsList by rememberConnectionsList(uiState.serverState)
                val connection by rememberSelectedConnection(connectionsList)
                val viewModelList by rememberViewModelList(connection)
                val viewModel by rememberSelectedViewModel(connection)
                val currentAppDestination by rememberConnectionCurrentDestination(connection, uiState.cachedDebuggerUiSettings)
                val inputsList by rememberViewModelInputsList(viewModel, uiState.searchText)
                val latestState by rememberLatestViewModelStateSnapshot(viewModel)

                DebuggerScaffold(
                    primaryToolbar = {
                        DebuggerPrimaryToolbar(
                            currentRoute,
                            connectionsList,
                            connection,
                            viewModelList,
                            viewModel,
                            uiState.searchText,
                            postInput,
                        )
                    },
                    tabs = { ViewModelTabStrip(connection, viewModel, postInput) },
                    mainContentLeft = { InputsList(connection, viewModel, inputsList, null, postInput) },
                    contentLeftToolbar = { InputsListToolbar(connection, viewModel, inputsList, postInput) },
                    secondaryContent = { SpecialRouterToolbar(currentAppDestination, uiState.cachedDebuggerUiSettings, postInput) },
                    stickyContent = { SpecialViewModelState(latestState, uiState.cachedDebuggerUiSettings, postInput) }
                )
            }

            DebuggerRoute.ViewModelInputDetails -> {
                val connectionsList by rememberConnectionsList(uiState.serverState)
                val connection by rememberSelectedConnection(connectionsList)
                val viewModelList by rememberViewModelList(connection)
                val viewModel by rememberSelectedViewModel(connection)
                val currentAppDestination by rememberConnectionCurrentDestination(connection, uiState.cachedDebuggerUiSettings)
                val inputsList by rememberViewModelInputsList(viewModel, uiState.searchText)
                val selectedInput by rememberSelectedViewModelInput(viewModel)
                val latestState by rememberLatestViewModelStateSnapshot(viewModel)

                DebuggerScaffold(
                    primaryToolbar = {
                        DebuggerPrimaryToolbar(
                            currentRoute,
                            connectionsList,
                            connection,
                            viewModelList,
                            viewModel,
                            uiState.searchText,
                            postInput,
                        )
                    },
                    tabs = { ViewModelTabStrip(connection, viewModel, postInput) },
                    mainContentLeft = { InputsList(connection, viewModel, inputsList, selectedInput, postInput) },
                    contentLeftToolbar = { InputsListToolbar(connection, viewModel, inputsList, postInput) },
                    mainContentRight = {
                        Text("Focused Input")
                        Divider()
                        InputDetails(selectedInput, postInput)
                    },
                    contentRightToolbar = { InputDetailsToolbar(connection, viewModel, selectedInput, postInput) },
                    secondaryContent = { SpecialRouterToolbar(currentAppDestination, uiState.cachedDebuggerUiSettings, postInput) },
                    stickyContent = { SpecialViewModelState(latestState, uiState.cachedDebuggerUiSettings, postInput) }
                )
            }

            DebuggerRoute.ViewModelEvents -> {
                val connectionsList by rememberConnectionsList(uiState.serverState)
                val connection by rememberSelectedConnection(connectionsList)
                val viewModelList by rememberViewModelList(connection)
                val viewModel by rememberSelectedViewModel(connection)
                val currentAppDestination by rememberConnectionCurrentDestination(connection, uiState.cachedDebuggerUiSettings)
                val eventsList by rememberViewModelEventsList(viewModel, uiState.searchText)
                val latestState by rememberLatestViewModelStateSnapshot(viewModel)

                DebuggerScaffold(
                    primaryToolbar = {
                        DebuggerPrimaryToolbar(
                            currentRoute,
                            connectionsList,
                            connection,
                            viewModelList,
                            viewModel,
                            uiState.searchText,
                            postInput,
                        )
                    },
                    tabs = { ViewModelTabStrip(connection, viewModel, postInput) },
                    mainContentLeft = { EventsList(connection, viewModel, eventsList, null, postInput) },
                    contentLeftToolbar = { EventsListToolbar(connection, viewModel, eventsList, postInput) },
                    secondaryContent = { SpecialRouterToolbar(currentAppDestination, uiState.cachedDebuggerUiSettings, postInput) },
                    stickyContent = { SpecialViewModelState(latestState, uiState.cachedDebuggerUiSettings, postInput) }
                )
            }

            DebuggerRoute.ViewModelEventDetails -> {
                val connectionsList by rememberConnectionsList(uiState.serverState)
                val connection by rememberSelectedConnection(connectionsList)
                val viewModelList by rememberViewModelList(connection)
                val viewModel by rememberSelectedViewModel(connection)
                val currentAppDestination by rememberConnectionCurrentDestination(connection, uiState.cachedDebuggerUiSettings)
                val eventsList by rememberViewModelEventsList(viewModel, uiState.searchText)
                val selectedEvent by rememberSelectedViewModelEvent(viewModel)
                val latestState by rememberLatestViewModelStateSnapshot(viewModel)

                DebuggerScaffold(
                    primaryToolbar = {
                        DebuggerPrimaryToolbar(
                            currentRoute,
                            connectionsList,
                            connection,
                            viewModelList,
                            viewModel,
                            uiState.searchText,
                            postInput,
                        )
                    },
                    tabs = { ViewModelTabStrip(connection, viewModel, postInput) },
                    mainContentLeft = { EventsList(connection, viewModel, eventsList, selectedEvent, postInput) },
                    contentLeftToolbar = { EventsListToolbar(connection, viewModel, eventsList, postInput) },
                    mainContentRight = {
                        Text("Focused Event")
                        Divider()
                        EventDetails(selectedEvent, postInput)
                    },
                    contentRightToolbar = { EventDetailsToolbar(connection, viewModel, selectedEvent, postInput) },
                    secondaryContent = { SpecialRouterToolbar(currentAppDestination, uiState.cachedDebuggerUiSettings, postInput) },
                    stickyContent = { SpecialViewModelState(latestState, uiState.cachedDebuggerUiSettings, postInput) }
                )
            }

            DebuggerRoute.ViewModelSideJobs -> {
                val connectionsList by rememberConnectionsList(uiState.serverState)
                val connection by rememberSelectedConnection(connectionsList)
                val viewModelList by rememberViewModelList(connection)
                val viewModel by rememberSelectedViewModel(connection)
                val currentAppDestination by rememberConnectionCurrentDestination(connection, uiState.cachedDebuggerUiSettings)
                val sideJobsList by rememberViewModelSideJobsList(viewModel, uiState.searchText)
                val latestState by rememberLatestViewModelStateSnapshot(viewModel)

                DebuggerScaffold(
                    primaryToolbar = {
                        DebuggerPrimaryToolbar(
                            currentRoute,
                            connectionsList,
                            connection,
                            viewModelList,
                            viewModel,
                            uiState.searchText,
                            postInput,
                        )
                    },
                    tabs = { ViewModelTabStrip(connection, viewModel, postInput) },
                    mainContentLeft = { SideJobsList(connection, viewModel, sideJobsList, null, postInput) },
                    contentLeftToolbar = { SideJobsListToolbar(connection, viewModel, sideJobsList, postInput) },
                    secondaryContent = { SpecialRouterToolbar(currentAppDestination, uiState.cachedDebuggerUiSettings, postInput) },
                    stickyContent = { SpecialViewModelState(latestState, uiState.cachedDebuggerUiSettings, postInput) }
                )
            }

            DebuggerRoute.ViewModelSideJobDetails -> {
                val connectionsList by rememberConnectionsList(uiState.serverState)
                val connection by rememberSelectedConnection(connectionsList)
                val viewModelList by rememberViewModelList(connection)
                val viewModel by rememberSelectedViewModel(connection)
                val currentAppDestination by rememberConnectionCurrentDestination(connection, uiState.cachedDebuggerUiSettings)
                val sideJobsList by rememberViewModelSideJobsList(viewModel, uiState.searchText)
                val selectedSideJob by rememberSelectedViewModelSideJob(viewModel)
                val latestState by rememberLatestViewModelStateSnapshot(viewModel)

                DebuggerScaffold(
                    primaryToolbar = {
                        DebuggerPrimaryToolbar(
                            currentRoute,
                            connectionsList,
                            connection,
                            viewModelList,
                            viewModel,
                            uiState.searchText,
                            postInput,
                        )
                    },
                    tabs = { ViewModelTabStrip(connection, viewModel, postInput) },
                    mainContentLeft = { SideJobsList(connection, viewModel, sideJobsList, selectedSideJob, postInput) },
                    contentLeftToolbar = { SideJobsListToolbar(connection, viewModel, sideJobsList, postInput) },
                    mainContentRight = {
                        Text("Focused Side Job")
                        Divider()
                        SideJobDetails(selectedSideJob, postInput)
                    },
                    contentRightToolbar = { SideJobDetailsToolbar(connection, viewModel, selectedSideJob, postInput) },
                    secondaryContent = { SpecialRouterToolbar(currentAppDestination, uiState.cachedDebuggerUiSettings, postInput) },
                    stickyContent = { SpecialViewModelState(latestState, uiState.cachedDebuggerUiSettings, postInput) }
                )
            }

            DebuggerRoute.ViewModelInterceptors -> {
                val connectionsList by rememberConnectionsList(uiState.serverState)
                val connection by rememberSelectedConnection(connectionsList)
                val viewModelList by rememberViewModelList(connection)
                val viewModel by rememberSelectedViewModel(connection)
                val currentAppDestination by rememberConnectionCurrentDestination(connection, uiState.cachedDebuggerUiSettings)
                val interceptorList by rememberViewModelInterceptorList(viewModel, uiState.searchText)
                val latestState by rememberLatestViewModelStateSnapshot(viewModel)

                DebuggerScaffold(
                    primaryToolbar = {
                        DebuggerPrimaryToolbar(
                            currentRoute,
                            connectionsList,
                            connection,
                            viewModelList,
                            viewModel,
                            uiState.searchText,
                            postInput,
                        )
                    },
                    tabs = { ViewModelTabStrip(connection, viewModel, postInput) },
                    mainContentLeft = { InterceptorsList(connection, viewModel, interceptorList, null, postInput) },
                    contentLeftToolbar = { InterceptorsListToolbar(connection, viewModel, interceptorList, postInput) },
                    secondaryContent = { SpecialRouterToolbar(currentAppDestination, uiState.cachedDebuggerUiSettings, postInput) },
                    stickyContent = { SpecialViewModelState(latestState, uiState.cachedDebuggerUiSettings, postInput) }
                )
            }

            DebuggerRoute.ViewModelInterceptorDetails -> {
                val connectionsList by rememberConnectionsList(uiState.serverState)
                val connection by rememberSelectedConnection(connectionsList)
                val viewModelList by rememberViewModelList(connection)
                val viewModel by rememberSelectedViewModel(connection)
                val currentAppDestination by rememberConnectionCurrentDestination(connection, uiState.cachedDebuggerUiSettings)
                val interceptorList by rememberViewModelInterceptorList(viewModel, uiState.searchText)
                val selectedInterceptor by rememberSelectedViewModelInterceptor(viewModel)
                val latestState by rememberLatestViewModelStateSnapshot(viewModel)

                DebuggerScaffold(
                    primaryToolbar = {
                        DebuggerPrimaryToolbar(
                            currentRoute,
                            connectionsList,
                            connection,
                            viewModelList,
                            viewModel,
                            uiState.searchText,
                            postInput,
                        )
                    },
                    tabs = { ViewModelTabStrip(connection, viewModel, postInput) },
                    mainContentLeft = { InterceptorsList(connection, viewModel, interceptorList, selectedInterceptor, postInput) },
                    contentLeftToolbar = { InterceptorsListToolbar(connection, viewModel, interceptorList, postInput) },
                    mainContentRight = {
                        Text("Focused Interceptor")
                        Divider()
                        InterceptorDetails(selectedInterceptor, postInput)
                    },
                    contentRightToolbar = { InterceptorDetailsToolbar(connection, viewModel, selectedInterceptor, postInput) },
                    secondaryContent = { SpecialRouterToolbar(currentAppDestination, uiState.cachedDebuggerUiSettings, postInput) },
                    stickyContent = { SpecialViewModelState(latestState, uiState.cachedDebuggerUiSettings, postInput) }
                )
            }

            DebuggerRoute.ViewModelLogs -> {
                val connectionsList by rememberConnectionsList(uiState.serverState)
                val connection by rememberSelectedConnection(connectionsList)
                val viewModelList by rememberViewModelList(connection)
                val viewModel by rememberSelectedViewModel(connection)
                val currentAppDestination by rememberConnectionCurrentDestination(connection, uiState.cachedDebuggerUiSettings)
                val fullHistory by rememberViewModelLogsList(viewModel, uiState.searchText)
                val latestState by rememberLatestViewModelStateSnapshot(viewModel)

                DebuggerScaffold(
                    primaryToolbar = {
                        DebuggerPrimaryToolbar(
                            currentRoute,
                            connectionsList,
                            connection,
                            viewModelList,
                            viewModel,
                            uiState.searchText,
                            postInput,
                        )
                    },
                    tabs = { ViewModelTabStrip(connection, viewModel, postInput) },
                    mainContentLeft = { LogsList(connection, viewModel, fullHistory, postInput) },
                    contentLeftToolbar = { LogsListToolbar(connection, viewModel, fullHistory, postInput) },
                    secondaryContent = { SpecialRouterToolbar(currentAppDestination, uiState.cachedDebuggerUiSettings, postInput) },
                    stickyContent = { SpecialViewModelState(latestState, uiState.cachedDebuggerUiSettings, postInput) }
                )
            }

//            DebuggerRoute.ViewModelTimeline -> {
//                val connectionsList by rememberConnectionsList(uiState.serverState)
//                val connection by rememberSelectedConnection(connectionsList)
//                val viewModelList by rememberViewModelList(connection)
//                val viewModel by rememberSelectedViewModel(connection)
//                val currentAppDestination by rememberConnectionCurrentDestination(connection, uiState.cachedDebuggerUiSettings)
//                val latestState by rememberLatestViewModelStateSnapshot(viewModel)
//
//                DebuggerScaffold(
//                    primaryToolbar = {
//                        DebuggerPrimaryToolbar(
//                            currentRoute,
//                            connectionsList,
//                            connection,
//                            viewModelList,
//                            viewModel,
//                            uiState.searchText,
//                            postInput,
//                        )
//                    },
//                    tabs = { ViewModelTabStrip(connection, viewModel, postInput) },
//                    secondaryContent = { SpecialRouterToolbar(currentAppDestination, uiState.cachedDebuggerUiSettings, postInput) },
//                    stickyContent = { SpecialViewModelState(latestState, uiState.cachedDebuggerUiSettings, postInput) }
//                )
//            }
        }
    }
}
