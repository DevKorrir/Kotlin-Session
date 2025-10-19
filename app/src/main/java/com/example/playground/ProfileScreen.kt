package com.example.playground

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Profile(
    val name: String = "Aldo Kipyegon",
    val title: String = "Mobile Developer",
    val location: String = "Meru Njiru, Meru",
    val bio: String = "Passionate about creating beautiful and functional Android apps. Love Kotlin and Jetpack Compose!",
    val profileImageUrl: String = "",
    val rating: Float = 4.5f,
    val skills: List<String> = listOf("Kotlin", "Jetpack Compose", "MVVM", "Firebase"),
    val socialLinks: List<SocialLink> = listOf()
)

data class SocialLink(
    val platform: String,
    val url: String,
    val icon: String
)

class ProfileViewModel : ViewModel() {
    private val _bioExpanded = MutableStateFlow(false)
    val bioExpanded: StateFlow<Boolean> = _bioExpanded

    private val _skillsExpanded = MutableStateFlow(false)
    val skillsExpanded: StateFlow<Boolean> = _skillsExpanded

    private val _profile = MutableStateFlow(
        Profile(
            socialLinks = listOf(
                SocialLink("Twitter", "https://x.com/AldoKipyegon", "twitter"),
                SocialLink("LinkedIn", "https://www.linkedin.com/in/aldo-korir-kipyegon/", "linkedin"),
                SocialLink("GitHub", "https://github.com/DevKorrir", "github"),
                SocialLink("Instagram", "https://instagram.com", "instagram")
            )
        )
    )
    val profile: StateFlow<Profile> = _profile

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri

    fun toggleBioExpanded() {
        _bioExpanded.value = !_bioExpanded.value
    }

    fun toggleSkillsExpanded() {
        _skillsExpanded.value = !_skillsExpanded.value
    }

    fun updateProfile(newProfile: Profile) {
        _profile.value = newProfile
    }

    fun setProfileImage(uri: Uri?) {
        _profileImageUri.value = uri
    }
}


@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel()
) {
    val profile by viewModel.profile.collectAsState()
    val bioExpanded by viewModel.bioExpanded.collectAsState()
    val skillsExpanded by viewModel.skillsExpanded.collectAsState()
    val profileImageUri by viewModel.profileImageUri.collectAsState()
    val context = LocalContext.current

    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            viewModel.setProfileImage(uri)
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { ProfileHeaderSection(profile, profileImageUri, onImageClick = { pickImage.launch("image/*") }) }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item { ProfileInfoCard(profile) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { RatingSection(profile.rating) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { SocialMediaSection(profile.socialLinks) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            ExpandableBioCard(
                bio = profile.bio,
                isExpanded = bioExpanded,
                onToggle = { viewModel.toggleBioExpanded() }
            )
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            ExpandableSkillsCard(
                skills = profile.skills,
                isExpanded = skillsExpanded,
                onToggle = { viewModel.toggleSkillsExpanded() }
            )
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
fun ProfileHeaderSection(
    profile: Profile, profileImageUri: Uri?,
    onImageClick: () -> Unit
) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(140.dp)
//            .clip(RoundedCornerShape(20.dp))
//            .background(
//                brush = Brush.linearGradient(
//                    colors = listOf(
//                        Color(0xFF667EEA),
//                        Color(0xFF764BA2),
//                        Color(0xFFF093FB)
//                    )
//                )
//            )
//    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White)
                //.shadow(8.dp, CircleShape)
                .clickable { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUri != null) {
                AsyncImage(
                    model = profileImageUri,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AddAPhoto,
                        contentDescription = "Add Photo",
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFF667EEA)
                    )
                    Text(
                        text = "Add Photo",
                        fontSize = 10.sp,
                        color = Color(0xFF667EEA),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileInfoCard(
    profile: Profile
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = profile.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = profile.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF667EEA)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color(0xFF764BA2),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = profile.location,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
fun RatingSection(
    rating: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Rating",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF666666)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(5) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = if (index < rating.toInt()) Color(0xFFFFD700) else Color(0xFFE0E0E0),
                        modifier = Modifier.size(24.dp)
                    )
                    if (index < 4) Spacer(modifier = Modifier.width(4.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$rating / 5.0",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
        }
    }
}

@Composable
fun SocialMediaSection(
    socialLinks: List<SocialLink>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Connect",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF666666),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                socialLinks.forEach { social ->
                    SocialButton(social)
                }
            }
        }
    }
}

@Composable
fun SocialButton(
    social: SocialLink
) {
    val context = LocalContext.current
    val (backgroundColor, drawableResId, applyTint) = when (social.icon) {
        "twitter" -> Triple(Color(0xFF1DA1F2), R.drawable.twitter, true)
        "linkedin" -> Triple(Color(0xFF0A66C2), R.drawable.linkedin, true)
        "github" -> Triple(Color(0xFF333333), R.drawable.github, false) // github logo often full-color/mono
        "instagram" -> Triple(Color(0xFFF1017B), R.drawable.instagram, false)
        else -> Triple(Color(0xFF667EEA), R.drawable.ic_launcher_background, true)
    }

    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(social.url))
            context.startActivity(intent)
        },
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(0.dp),
        shape = CircleShape
    ) {
        val painter = painterResource(id = drawableResId)
        val colorFilter = if (applyTint) ColorFilter.tint(Color.White) else null

        Image(
            painter = painter,
            contentDescription = social.platform,
            modifier = Modifier
                .fillMaxSize() // fill the button area
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            colorFilter = colorFilter
        )
    }
}

@Composable
fun SocialImageButton3(social: SocialLink) {
    val context = LocalContext.current
    val (backgroundColor, drawableResId, applyTint) = when (social.icon) {
        "twitter" -> Triple(Color(0xFF1DA1F2), R.drawable.twitter, true)
        "linkedin" -> Triple(Color(0xFF0A66C2), R.drawable.linkedin, true)
        "github" -> Triple(Color(0xFF333333), R.drawable.github, false)
        "instagram" -> Triple(Color(0xFFF1017B), R.drawable.instagram, false)
        else -> Triple(Color(0xFF667EEA), R.drawable.ic_launcher_background, true)
    }

    val painter = painterResource(id = drawableResId)
    val colorFilter = if (applyTint) ColorFilter.tint(Color.White) else null

    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(social.url))
                context.startActivity(intent)
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = social.platform,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            colorFilter = colorFilter
        )
    }
}


@Composable
fun ExpandableBioCard(
    bio: String, isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable { onToggle() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "About",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF666666)
                )
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF667EEA)
                )
            }
            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = bio,
                    fontSize = 14.sp,
                    color = Color(0xFF555555),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun ExpandableSkillsCard(
    skills: List<String>,
    isExpanded: Boolean, onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable { onToggle() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Skills",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF666666)
                )
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF667EEA)
                )
            }
            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    skills.forEach { skill ->
                        SkillProgressBar(skill = skill)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SkillProgressBar(
    skill: String
) {
    val progress = listOf(0.95f, 0.85f, 0.90f, 0.80f)
    val randomProgress = progress.random()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = skill,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1A1A1A)
        )
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { randomProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = Color(0xFF667EEA),
            trackColor = Color(0xFFE0E0E0),
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }
}