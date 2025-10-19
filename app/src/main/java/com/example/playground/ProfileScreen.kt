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

// --- Data Models: Blueprints for the data objects ---

// 'data class' is a simple way to create a class that only holds data (a blueprint for a Profile).
data class Profile(
    // A profile must have a name (String), defaulting to "Aldo Kipyegon" if not provided.
    val name: String = "Aldo Kipyegon",
    // Title is fixed (val) and is a String.
    val title: String = "Mobile Developer",
    val location: String = "Meru Njiru, Meru",
    val bio: String = "Passionate about creating beautiful and functional Android apps. Love Kotlin and Jetpack Compose!",
    val profileImageUrl: String = "",
    val rating: Float = 4.5f,
    // A list of strings for skills.
    val skills: List<String> = listOf("Kotlin", "Jetpack Compose", "MVVM", "Firebase"),
    // A list of other data objects (SocialLink).
    val socialLinks: List<SocialLink> = listOf()
)

// Blueprint for a single social media link.
data class SocialLink(
    // The name of the platform.
    val platform: String,
    // The actual URL.
    val url: String,
    // The key used to select the correct icon.
    val icon: String
)

// --- ViewModel: The data manager and logic holder ---

// This class handles the data for the screen. It extends 'ViewModel' so it survives screen rotations.
class ProfileViewModel : ViewModel() {
    // This is a private, mutable (changeable) data stream. The 'false' is the starting value.
    private val _bioExpanded = MutableStateFlow(false)
    // This is the public, read-only stream. The UI uses this to watch for changes.
    val bioExpanded: StateFlow<Boolean> = _bioExpanded

    // Same pattern for skills expansion state.
    private val _skillsExpanded = MutableStateFlow(false)
    val skillsExpanded: StateFlow<Boolean> = _skillsExpanded

    // The core Profile data, also wrapped in a data stream.
    private val _profile = MutableStateFlow(
        Profile( // Initialize the Profile with the starting social links.
            socialLinks = listOf(
                SocialLink("Twitter", "https://x.com/AldoKipyegon", "twitter"),
                SocialLink("LinkedIn", "https://www.linkedin.com/in/aldo-korir-kipyegon/", "linkedin"),
                SocialLink("GitHub", "https://github.com/DevKorrir", "github"),
                SocialLink("Instagram", "https://instagram.com", "instagram")
            )
        )
    )
    val profile: StateFlow<Profile> = _profile

    // This holds the URI (location) of a locally chosen profile image.
    private val _profileImageUri = MutableStateFlow<Uri?>(null) // Uri? means it can be null (no image selected yet).
    val profileImageUri: StateFlow<Uri?> = _profileImageUri

    // Function (logic) to flip the bio state when the user clicks the card.
    fun toggleBioExpanded() {
        // '!' means NOT. It flips the current value to the opposite (true to false, false to true).
        _bioExpanded.value = !_bioExpanded.value
    }

    // Function to flip the skills state.
    fun toggleSkillsExpanded() {
        _skillsExpanded.value = !_skillsExpanded.value
    }

    // Function to replace the entire profile data with new data.
    fun updateProfile(newProfile: Profile) {
        _profile.value = newProfile
    }

    // Function to update the profile image location.
    fun setProfileImage(uri: Uri?) {
        _profileImageUri.value = uri
    }
}

// --- Main Screen Composable ---

// '@Composable' means this function describes a part of the user interface.
@Composable
fun ProfileScreen(
    // Takes the ViewModel as a parameter (input). '= viewModel()' creates or finds the ViewModel automatically.
    viewModel: ProfileViewModel = viewModel()
) {
    // 'by' and 'collectAsState()' connect the UI to the ViewModel's data streams.
    // Whenever the data in the ViewModel changes, this entire function knows it needs to redraw.
    val profile by viewModel.profile.collectAsState()
    val bioExpanded by viewModel.bioExpanded.collectAsState()
    val skillsExpanded by viewModel.skillsExpanded.collectAsState()
    val profileImageUri by viewModel.profileImageUri.collectAsState()
    // Gets the Android Context, which is needed for system interactions (like opening links).
    val context = LocalContext.current

    // Sets up the "launcher" that asks the Android system to open the image picker.
    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) { // If the user actually picked an image (uri is not null).
            viewModel.setProfileImage(uri) // Tell the ViewModel to save the image location.
        }
    }
    // LazyColumn: The main container. It's efficient for scrolling long lists (only draws visible items).
    LazyColumn(
        // 'modifier' is how you customize the look, size, and behavior of a Composable.
        modifier = Modifier
            .fillMaxSize() // Make it take up the whole screen.
            .background(Color(0xFFF8F9FA)) // Set a light grey background color.
            .padding(16.dp), // Add padding around the edges.
        horizontalAlignment = Alignment.CenterHorizontally // Center all the items inside the column horizontally.
    ) {
        // 'item {}' adds a single, non-scrolling element to the LazyColumn.
        item {
            // Call the component function for the header section.
            ProfileHeaderSection(
                profile, profileImageUri,
                // The 'onImageClick' parameter is given the action to perform:
                onImageClick = { pickImage.launch("image/*") } // Launch the system's image picker, looking for any image file.
            )
        }
        item { Spacer(modifier = Modifier.height(24.dp)) } // A vertical gap of 24 density-independent pixels.
        item { ProfileInfoCard(profile) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { RatingSection(profile.rating) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { SocialMediaSection(profile.socialLinks) }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            // Component for the expandable bio section.
            ExpandableBioCard(
                bio = profile.bio, // Pass the bio text.
                isExpanded = bioExpanded, // Pass the current state (expanded or not).
                // Pass the function (logic) to run when clicked.
                onToggle = { viewModel.toggleBioExpanded() }
            )
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            // Component for the expandable skills section.
            ExpandableSkillsCard(
                skills = profile.skills,
                isExpanded = skillsExpanded,
                onToggle = { viewModel.toggleSkillsExpanded() }
            )
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

// --- Composable Components (The building blocks of the UI) ---

@Composable
fun ProfileHeaderSection(
    // Input parameters needed for this component.
    profile: Profile, profileImageUri: Uri?,
    // The action (function) to run when the image is clicked.
    onImageClick: () -> Unit
) {
    // Box is a layout container that stacks elements and helps with alignment.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp), // Pushes the content down from the top.
        contentAlignment = Alignment.Center // Center everything inside this box.
    ) {
        // The inner box holds the actual profile image/placeholder.
        Box(
            modifier = Modifier
                .size(120.dp) // Set a fixed size.
                .clip(CircleShape) // Cut the box into a perfect circle.
                .background(Color.White)
                //.shadow(8.dp, CircleShape) // (This line is commented out, meaning it's ignored right now).
                .clickable { onImageClick() }, // Make the circle clickable, running the passed 'onImageClick' function.
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUri != null) { // Check if we have a local image URI.
                AsyncImage( // Loads an image asynchronously (in the background).
                    model = profileImageUri, // The image URI.
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop // Crop the image to fill the circular space.
                )
            } else { // If profileImageUri is null (no image selected).
                // Column stacks things vertically.
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Display the "Add Photo" icon.
                    Icon(
                        imageVector = Icons.Rounded.AddAPhoto, // The specific camera icon.
                        contentDescription = "Add Photo",
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFF667EEA) // Set the icon color.
                    )
                    // Display the "Add Photo" text.
                    Text(
                        text = "Add Photo",
                        fontSize = 10.sp, // Small font size.
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
    // Input is the Profile data object.
    profile: Profile
) {
    // Card provides a nice elevated background, often used for content blocks.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)), // Adds a shadow with a rounded shape.
        shape = RoundedCornerShape(16.dp), // Defines the corner radius.
        colors = CardDefaults.cardColors(containerColor = Color.White) // Sets the card's background color.
    ) {
        // Column stacks the name, title, and location vertically.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the user's name.
            Text(
                text = profile.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Display the user's title.
            Text(
                text = profile.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF667EEA)
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Row is for arranging things horizontally (icon and location text).
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
    // Input is the floating-point number for the rating.
    rating: Float
) {
    // ... Card setup (similar to the ProfileInfoCard) ...
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
                repeat(5) { index -> // This loop runs 5 times (for 5 stars).
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        // Logic: If the current star's 'index' is less than the whole number part of the 'rating', color it gold, otherwise grey.
                        tint = if (index < rating.toInt()) Color(0xFFFFD700) else Color(0xFFE0E0E0),
                        modifier = Modifier.size(24.dp)
                    )
                    if (index < 4) Spacer(modifier = Modifier.width(4.dp)) // Add space between stars, but not after the last one.
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                // Display the rating number, e.g., "4.5 / 5.0".
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
    // Input is the list of SocialLink data objects.
    socialLinks: List<SocialLink>
) {
    // ... Card setup ...
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
                horizontalArrangement = Arrangement.SpaceEvenly // Spreads the items evenly across the width.
            ) {
                socialLinks.forEach { social -> // Loops through every SocialLink in the list.
                    SocialButton(social) // Calls the composable function for each link, creating one button per link.
                }
            }
        }
    }
}

@Composable
fun SocialButton(
    // Input is a single SocialLink data object.
    social: SocialLink
) {
    val context = LocalContext.current // Get the tool needed to launch a web browser.
    // 'when' is like a powerful 'if/else if/else' block. It checks the 'icon' string.
    val (backgroundColor, drawableResId, applyTint) = when (social.icon) {
        // If "twitter", define the color, the image resource (R.drawable.twitter), and whether to tint it white.
        "twitter" -> Triple(Color(0xFF1DA1F2), R.drawable.twitter, true)
        "linkedin" -> Triple(Color(0xFF0A66C2), R.drawable.linkedin, true)
        "github" -> Triple(Color(0xFF333333), R.drawable.github, false) // Note: GitHub icon is often black/grey, so we don't force a white tint.
        "instagram" -> Triple(Color(0xFFF1017B), R.drawable.instagram, false)
        else -> Triple(Color(0xFF667EEA), R.drawable.ic_launcher_background, true) // Default case.
    }

    Button(
        onClick = { // The code to run when the button is pressed.
            // Create an 'Intent' (a request) to view a URI (the social link's URL).
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(social.url))
            context.startActivity(intent) // Execute the request, opening the browser/app.
        },
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        contentPadding = PaddingValues(0.dp), // Remove default button padding.
        shape = CircleShape
    ) {
        val painter = painterResource(id = drawableResId) // Loads the icon image from the project resources.
        // Determines if the icon should be colored white (for visibility on the colored background).
        val colorFilter = if (applyTint) ColorFilter.tint(Color.White) else null

        Image(
            painter = painter,
            contentDescription = social.platform,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            colorFilter = colorFilter // Apply the color filter if needed.
        )
    }
}

@Composable
fun ExpandableBioCard(
    // Inputs: bio text, current expansion state, and the toggle function.
    bio: String, isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable { onToggle() }, // Clicking runs the 'onToggle' function from the ViewModel.
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
                horizontalArrangement = Arrangement.SpaceBetween, // Puts elements on far left and far right.
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "About",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF666666)
                )
                // The expand icon.
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF667EEA)
                )
            }
            if (isExpanded) { // **KEY LOGIC:** Only draw the full bio if the state is TRUE.
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = bio,
                    fontSize = 14.sp,
                    color = Color(0xFF555555),
                    lineHeight = 20.sp // Set spacing between lines for readability.
                )
            }
        }
    }
}

// ... ExpandableSkillsCard works exactly like ExpandableBioCard ...

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
            if (isExpanded) { // Only draw the skill list if expanded is TRUE.
                Spacer(modifier = Modifier.height(12.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    skills.forEach { skill -> // Loop through all the skills in the list.
                        SkillProgressBar(skill = skill) // Draw a progress bar for each skill.
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
    // A list of progress values (percentages) to choose from.
    val progress = listOf(0.95f, 0.85f, 0.90f, 0.80f)
    // Pick one randomly for demonstration purposes.
    val randomProgress = progress.random()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = skill,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1A1A1A)
        )
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator( // The visual bar showing progress.
            progress = { randomProgress }, // Set the progress amount (0.0f to 1.0f).
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)), // Make the bar rounded.
            color = Color(0xFF667EEA), // Color of the filled part.
            trackColor = Color(0xFFE0E0E0), // Color of the empty background part.
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap, // Makes the ends of the bar rounded.
        )
    }
}