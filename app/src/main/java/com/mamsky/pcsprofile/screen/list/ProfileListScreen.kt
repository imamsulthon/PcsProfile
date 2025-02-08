package com.mamsky.pcsprofile.screen.list

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mamsky.pcsprofile.databinding.SomeLayoutBinding
import com.mamsky.pcsprofile.domain.model.ProfileModel
import com.mamsky.pcsprofile.utils.formatDate


@Composable
fun ProfileListScreen(
    navController: NavController,
    viewModel: ProfileListViewModel = hiltViewModel()
) {

    val state by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.test()
    }

    Scaffold { paddingValues ->
        when (state) {
            is ProfileScreenState.OnLoading -> {
                Box(modifier = Modifier.padding(paddingValues).fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${state.data}", style = MaterialTheme.typography.displaySmall)
                }
            }
            is ProfileScreenState.OnError -> {
                Box(modifier = Modifier.padding(paddingValues).fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("on Error ${state.data}",
                        style = MaterialTheme.typography.displaySmall)
                }
            }
            is ProfileScreenState.OnSuccess -> {
                Content(
                    modifier = Modifier.padding(paddingValues),
                    list = (state as ProfileScreenState.OnSuccess).list,
                    refresh = viewModel::test,
                    onClick = { item ->
                        navController.navigate("detailScreen/${item.id}")
                    }
                )
            }
        }
    }


}

@Composable
private fun Content(
    modifier: Modifier,
    list: List<ProfileModel>,
    refresh: () -> Unit,
    onClick: (ProfileModel) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        item {
            MyAndroidView("Profile Apps")
        }
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Number of Profile: ${list.size}")
                OutlinedButton(onClick = refresh::invoke) {
                    Text("Fetch again")
                }
            }
        }
        if (list.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("[Empty Data]", style = MaterialTheme.typography.titleLarge)
                }
            }
            return@LazyColumn
        }
        items(items = list) {
            ProfileItem(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                item = it,
                onClick = { onClick.invoke(it) }
            )
            Spacer(Modifier.height(10.dp))
        }
    }

}

@Composable
private fun ProfileItem(
    modifier: Modifier,
    item: ProfileModel,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .8f),
                shape = RoundedCornerShape(4.dp)
            ).clickable { onClick.invoke() }
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(10.dp)
        ) {
            val (imgRef, titleRef, otherRef, descRef) = createRefs()
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 10.dp)
                    .constrainAs(imgRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
                model = item.avatar,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(titleRef) {
                        top.linkTo(imgRef.top)
                        start.linkTo(imgRef.end)
                        end.linkTo(parent.end)
                        width = Dimension.preferredWrapContent
                    }, textAlign = TextAlign.Start,
                text = item.name, style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(otherRef) {
                        end.linkTo(parent.end)
                        top.linkTo(titleRef.bottom)
                        start.linkTo(imgRef.end)
                        width = Dimension.preferredWrapContent
                    }, textAlign = TextAlign.Start,
                text = "${item.street}, ${item.city}, ${item.country}", style = MaterialTheme.typography.bodySmall
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(descRef) {
                        end.linkTo(parent.end)
                        bottom.linkTo(imgRef.bottom)
                        start.linkTo(imgRef.end)
                        top.linkTo(otherRef.bottom)
                        width = Dimension.preferredWrapContent
                    }, textAlign = TextAlign.Start,
                text = item.createdAt.formatDate(), style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun MyAndroidView(title: String) {
    AndroidViewBinding(SomeLayoutBinding::inflate) {
        tvId.text = title
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {

    val list = listOf(
        sampleModel,
        sampleModel,
        sampleModel
    )

    Content(modifier = Modifier.fillMaxSize(), list, {}) {

    }
}

val sampleModel =
    ProfileModel(
        "1", "2024-08-25T11:45:50.334Z", "Zachary Hermann Jr",
        "https://loremflickr.com/640/480/people",
        "Luciehaven", "Slovenia", "Borders", "13882", "Moore Union", "57105"
    )