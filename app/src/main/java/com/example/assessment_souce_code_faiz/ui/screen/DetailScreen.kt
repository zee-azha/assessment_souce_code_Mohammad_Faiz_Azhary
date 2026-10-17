package com.example.assessment_souce_code_faiz.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.assessment_souce_code_faiz.data.UsersDetails
import com.example.assessment_souce_code_faiz.domain.model.Repository
import com.example.assessment_souce_code_faiz.viewmodel.DetailsViewModels
import com.example.assessment_souce_code_faiz.viewmodel.FollowsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    username: String,
    detailsViewModel: DetailsViewModels,
    followsViewModel: FollowsViewModel,
    onBackClick: () -> Unit,
    onUserClick: (String) -> Unit
) {
    LaunchedEffect(username) {
        detailsViewModel.setDetails(username)
        detailsViewModel.setRepos(username)
        followsViewModel.setListFollowers(username)
        followsViewModel.setListFollowing(username)
    }

    val userDetails by detailsViewModel.getOnDetails().observeAsState()
    val isLoadingDetails by detailsViewModel.isLoading.observeAsState(initial = false)

    val reposList by detailsViewModel.reposList.observeAsState(initial = emptyList())
    val isLoadingRepos by detailsViewModel.isLoadingRepos.collectAsState()

    val followersList by followsViewModel.getListFollowers().observeAsState(initial = emptyList())
    val isLoadingFollowers by followsViewModel.isLoadingFollowers.collectAsState()

    val followingList by followsViewModel.getListFollowing().observeAsState(initial = emptyList())
    val isLoadingFollowing by followsViewModel.isLoadingFollowing.collectAsState()

    val tabs = listOf("Repositories", "Followers", "Following")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = username) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoadingDetails) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    userDetails?.let { details ->
                        ProfileHeader(user = details)
                    }

                    TabRow(selectedTabIndex = pagerState.currentPage) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = { Text(text = title) }
                            )
                        }
                    }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f)
                    ) { page ->
                        when (page) {
                            0 -> RepoListContent(
                                reposList = reposList,
                                isLoading = isLoadingRepos
                            )
                            1 -> FollowListContent(
                                userList = followersList,
                                isLoading = isLoadingFollowers,
                                onUserClick = onUserClick
                            )
                            2 -> FollowListContent(
                                userList = followingList,
                                isLoading = isLoadingFollowing,
                                onUserClick = onUserClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(user: UsersDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = user.avatar_url,
            contentDescription = user.login,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(12.dp))

        user.name?.let {
            if (it.isNotBlank()) {
                Text(text = it, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }

        Text(text = "@${user.login}", fontSize = 15.sp, color = MaterialTheme.colorScheme.secondary)

        user.bio?.let {
            if (it.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = it, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        user.company?.let {
            if (it.isNotBlank()) {
                Text(text = "Company: $it", fontSize = 13.sp)
            }
        }

        user.location?.let {
            if (it.isNotBlank()) {
                Text(text = "Location: $it", fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(count = user.public_repos, label = "Repositories")
            StatItem(count = user.followers, label = "Followers")
            StatItem(count = user.following, label = "Following")
        }
    }
}

@Composable
fun StatItem(count: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
fun RepoListContent(
    reposList: List<Repository>,
    isLoading: Boolean
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (reposList.isEmpty()) {
            Text(
                text = "No public repositories found",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(reposList) { repo ->
                    RepoItemCard(repo = repo)
                }
            }
        }
    }
}

@Composable
fun RepoItemCard(repo: Repository) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = repo.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            repo.description?.let {
                if (it.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                repo.language?.let {
                    if (it.isNotBlank()) {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Stars",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = repo.starCount.toString(), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun FollowListContent(
    userList: List<UsersDetails>,
    isLoading: Boolean,
    onUserClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (userList.isEmpty()) {
            Text(
                text = "No users found",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(userList) { user ->
                    UserItem(user = user, onClick = { onUserClick(user.login) })
                }
            }
        }
    }
}
