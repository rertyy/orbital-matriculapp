package com.example.frontend.ui.screens.secondscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun CalendarScreen(
    navController: NavHostController,
) {
    InfoList()
}


@Composable
fun InfoList(infoList: List<LinkDetails> = linkList) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .size(10.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),

            ) {
            Text(
                "List of Useful Academic Resources",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .size(30.dp)
        )
        infoList.forEach {
            LinkWithDetails(linkDetails = it)
        }

    }
}

@Composable
fun LinkWithDetails(linkDetails: LinkDetails) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HyperlinkText(
            text = linkDetails.text,
            link = linkDetails.link,
            modifier = Modifier.width(200.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = linkDetails.desc,
            fontSize = 16.sp
        )
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .size(10.dp)
    )
}


data class LinkDetails(
    val text: String,
    val link: String? = null,
    val desc: String = "No details found"
)

val link1 = LinkDetails(
    text = "NUS Academic Calendar AY23/24",
    link = "https://www.nus.edu.sg/nusbulletin/ay202324/general-information/academic-calendar/",
    desc = "Details including recess week, exam dates, etc."
)
val link2 = LinkDetails(
    text = "TeleNUS",
    link = "https://telenus.nusmods.com/",
    desc = "Unofficial Telegram chat groups for variable courses"
)
val link3 = LinkDetails(
    text = "CourseReg Schedule",
    link = "https://www.nus.edu.sg/CourseReg/schedule-and-timeline.html",
    desc = "Details on when to register for courses"
)
val linkList = listOf(link1, link2, link3)


@Preview(showBackground = true)
@Composable
fun InfoListPreview() {
    InfoList()
}