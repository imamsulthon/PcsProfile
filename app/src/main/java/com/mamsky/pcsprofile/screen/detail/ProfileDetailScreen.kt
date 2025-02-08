package com.mamsky.pcsprofile.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mamsky.pcsprofile.databinding.DetailContentBinding
import com.mamsky.pcsprofile.domain.model.ProfileModel
import com.mamsky.pcsprofile.screen.list.sampleModel
import com.mamsky.pcsprofile.utils.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    navController: NavController,
    id: String,
    viewModel: ProfileDetailViewModel = hiltViewModel()
) {
    val data by viewModel.data.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getDetail(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = navController::popBackStack
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                title = { Text("Profile Detail") }
            )
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            Content(data)
        }
    }

}

@Composable
private fun Content(
    item: ProfileModel,
) {
    var showXML by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(10.dp)) {
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(6.dp)) {
                AsyncImage(
                    modifier = Modifier.size(140.dp)
                        .align(Alignment.CenterHorizontally),
                    model = item.avatar,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "${item.name} [${item.id}]", style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
            val first = item.name.substringBefore(" ")
            val last = item.name.substringAfter( " ")
            DetailRow("First Name: ", first)
            DetailRow("Last Name: ", last.ifBlank { first })
            DetailRow("Date: ", item.createdAt.formatDate())
            DetailRow("Street: ", item.street)
            DetailRow("Address No: ", item.addressNo)
            DetailRow("Zip Code: ", item.zipCode)
            DetailRow("County: ", item.county)
            DetailRow("City: ", item.city)
            DetailRow("Country: ", item.country)
            Spacer(modifier = Modifier.padding(16.dp))
            HorizontalDivider()
        }
        item {
            if (showXML) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Layout using XML & ViewBinding")
                Spacer(modifier = Modifier.padding(8.dp))
                DetailContentXML(item)
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { showXML = !showXML }
            ) {
                Text(if (showXML) "Hide XML Layout" else "Show XML Layout")
            }
            Spacer(modifier = Modifier.padding(8.dp))
            HorizontalDivider()
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.weight(.2f), text = label, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.padding(4.dp))
        Text(modifier = Modifier.weight(.3f), text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun DetailContentXML(
    item: ProfileModel
) {
    AndroidViewBinding(DetailContentBinding::inflate) {
        tvName.text = item.name
        tvStreet.text = item.street
        tvAddressNo.text = item.addressNo
        tvCounty.text = item.county
        tvCity.text = item.city
        tvCountry.text = item.country
        tvZip.text = item.zipCode
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    Content(sampleModel)
}