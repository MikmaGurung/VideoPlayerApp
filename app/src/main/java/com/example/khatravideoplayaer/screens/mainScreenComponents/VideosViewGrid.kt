package com.codingwithumair.app.vidcompose.ui.screens.mainScreenComponents

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.khatravideoplayaer.Data.VideoItem
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun VideoItemGridLayout(
	videoList: List<VideoItem>,
	onVideoItemClick: (VideoItem) -> Unit,
	contentPadding: PaddingValues,
	modifier: Modifier = Modifier
){
	 LazyColumn(
		modifier = modifier,
		contentPadding = contentPadding,
	){
		   items(videoList, key = {it.name}){videoItem ->
			VideoGridItem(
				videoItem = videoItem,
				onItemClick = onVideoItemClick,
				modifier = Modifier.padding(6.dp)
			)
		}

	}
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun VideoGridItem(
	videoItem: VideoItem,
	onItemClick: (VideoItem) -> Unit,
	modifier: Modifier = Modifier
){
	OutlinedCard(
		modifier = modifier,
		onClick = {
			onItemClick(videoItem)
		},
		shape = RoundedCornerShape(15.dp)
	){

		Box(
			modifier = Modifier
				.height(100.dp)
				.fillMaxWidth()
		){
			GlideImage(
				imageModel = { videoItem.uri },
				imageOptions = ImageOptions(
					contentScale = ContentScale.Crop,
					alignment = Alignment.Center
				),
				loading = {
					CircularProgressIndicator(modifier = Modifier
						.matchParentSize()
						.align(Alignment.Center))
				}
			)
		}

		Spacer(modifier = Modifier.size(12.dp))

		Text(
			text = videoItem.name,
			style = MaterialTheme.typography.bodyMedium,
			textAlign = TextAlign.Start,
			maxLines = 3,
			overflow = TextOverflow.Ellipsis,
			modifier = Modifier.padding(10.dp),
			color = MaterialTheme.colorScheme.primary
		)

		FlowRow(modifier = Modifier.padding(6.dp)){
			FlowRowItem(text = "${videoItem.size/1000000} MB")
			FlowRowItem(text = videoItem.duration.toHhMmSs())
			FlowRowItem(text = "${videoItem.height} x ${videoItem.width}")
		}


	}
}

@Composable
private fun FlowRowItem(
	text: String,
	modifier: Modifier = Modifier
){
	ElevatedCard(
		modifier = modifier.padding(4.dp),
		shape = CutCornerShape(2.dp),
		colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
	){
		Text(text, modifier = Modifier.padding(2.dp))
	}
}
fun Long.toHhMmSs(): String {
	val seconds = (this / 1000).toInt()
	val hours = seconds / 3600
	val minutes = (seconds % 3600) / 60
	val remainingSeconds = seconds % 60
	return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
}

@Preview
@Composable
fun VideoGridItemPreview(){
	VideoGridItem(videoItem = VideoItem(
		absolutePath = "",
		size = 0L,
		dateModified = 0L,
		uri = Uri.EMPTY,
		id = 0L,
		name = "hawda bridge",
		duration = 400L,
		height = 1,
		width = 1
	), onItemClick = {})
}