package co.feip.fefu2025.presentation.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RatingDistributionChart(
    ratingData: Map<Int, Int>,
    modifier: Modifier = Modifier
) {
    val maxVotes = ratingData.values.maxOrNull() ?: 1
    val maxHeight = 160.dp
    val columnWidth = 24.dp

    fun getBarColor(rating: Int): Color {
        val redColor = Color(0xFFFF0000)
        val yellowColor = Color(0xFFFFF700)
        val greenColor = Color(0xFF00FF04)

        return if (rating <= 5) {
            lerp(redColor, yellowColor, (rating - 1) / 4f)
        } else {
            lerp(yellowColor, greenColor, (rating - 6) / 4f)
        }
    }

    @Composable
    fun RatingColumn(rating: Int, voteCount: Int, heightRatio: Float) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(
                text = voteCount.toString(),
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 12.sp
            )

            Box(
                modifier = Modifier
                    .width(columnWidth)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(getBarColor(rating))
                    .height((maxHeight * heightRatio).coerceAtLeast(10.dp))
            )

            Text(
                text = rating.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        for (rating in 1..10) {
            val voteCount = ratingData[rating] ?: 0
            val heightRatio = voteCount / maxVotes.toFloat()

            RatingColumn(
                rating = rating,
                voteCount = voteCount,
                heightRatio = heightRatio
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingChartPreview() {
    val sampleRatings = mapOf(
        1 to 120,
        2 to 50,
        3 to 30,
        4 to 40,
        5 to 80,
        6 to 150,
        7 to 250,
        8 to 300,
        9 to 350,
        10 to 500
    )

    RatingDistributionChart(ratingData = sampleRatings)
}